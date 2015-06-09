package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import models.entity.*;
import models.exception.*;
import models.repository.*;


public class Application extends Controller {
	
	private static final int FIRST_PAGE = 1;
	private static List<Poster> adverts;
	
	private static StyleRepository styles;
	private static PosterRepository postRepository;
	private static InstrumentRepository instruments;
	
	@Transactional
    public static Result index() {
        return ok(index.render());
    }
	
	@Transactional
	public static Result publique() {
		styles = StyleRepository.getInstance();
		instruments = InstrumentRepository.getInstance();
		return ok(publique.render(styles.findAll(), instruments.findAll()));
	}
			
	@Transactional
	public static Result createAd() {
		postRepository = PosterRepository.getInstance();
		Map<String, String> data = Form.form().bindFromRequest().data();
		
		// Informações do Anúncio
		String titulo = data.get("titulo");
		String descrição = data.get("descricao");
		
		//Informamões do Anunciante
		String email = data.get("email");
		String cidade = data.get("city");
		String bairro = data.get("bairro");
		String perfil = data.get("perfil");
		String interesse = data.get("interesse");
		
		//Multi selected Option about advertiser
		List<Style> badStyles = getStyleSelectedData("badSty[]");
		List<Style> goodStyles = getStyleSelectedData("goodSty[]");
		List<Instrument> myInstruments = getInstrumentSelectedData(); 
		
		try {			
			User user = new User(email, perfil, cidade, bairro, myInstruments, badStyles, goodStyles);
			Poster poster = new Poster(titulo, descrição, interesse, user);
			postRepository.persist(poster);
			postRepository.flush();
			
			flash("success", String.valueOf(poster.getCode()));
		} catch(NewAdException e) {
			flash("erro", "Anúncio não publicado, pois: " + e.getMessage());
			return redirect("publique");
		} 
		
		return redirect("anuncios");
	}
			
	@Transactional
	private static List<Style> getStyleSelectedData(String key) {
		styles = StyleRepository.getInstance();
		Map<String, String[]> multipleData = request().body().asFormUrlEncoded();
		
		List<Style> styleList = new ArrayList<Style>();
		String[] requestStyleArray = multipleData.get(key);
		
		if(requestStyleArray != null) {
			for(int i = 0; i < requestStyleArray.length; i++) {
				long id = Long.parseLong(requestStyleArray[i]);
				Style style = styles.findByEntityId(id);
				if(!styleList.contains(style)) {
					styleList.add(style);
				}
			}
		}
		return styleList;
	}
	
	@Transactional
	private static List<Instrument> getInstrumentSelectedData() {
		instruments = InstrumentRepository.getInstance();
		Map<String, String[]> multipleData = request().body().asFormUrlEncoded();
		
		List<Instrument> instrumentList = new ArrayList<>();
		String[] requestInstrumentArray = multipleData.get("myInst[]");
		
		if(requestInstrumentArray != null) {
			for(int i = 0; i < requestInstrumentArray.length; i++) {
				long id = Long.parseLong(requestInstrumentArray[i]);
				Instrument instrument = instruments.findByEntityId(id);
				if(!instrumentList.contains(instrument)) {
					instrumentList.add(instrument);
				}
			}
		}
		return instrumentList;
	}
	
	@Transactional
    public static Result anuncio(Long id) {
		postRepository = PosterRepository.getInstance();
		
		Poster poster = postRepository.findByEntityId(id);
        return ok(anuncio.render(poster));
    }
	
	@Transactional
	public static Result anuncios(int page, int pageSize, boolean check) {
		if(check) {
			postRepository = PosterRepository.getInstance();
			adverts = postRepository.findAllNotFinalized();
		}
		
		page = page >= FIRST_PAGE ? page : FIRST_PAGE;
		pageSize = pageSize >= FIRST_PAGE ? pageSize : PosterRepository.DEFAULT_RESULTS;
				
		long posterNumber = adverts.size();
		if(page > (posterNumber / pageSize)) {
			page = (int) 
				(Math.ceil(posterNumber / Float.parseFloat(String.valueOf(pageSize))));
		}
		
		Collections.sort(adverts);
		session("actualPage", String.valueOf(page));
		
		int fromIndex = (page - 1) * pageSize;
		int toIndex = 
			fromIndex + pageSize < adverts.size() ? fromIndex + pageSize : adverts.size();
				
		return ok(anuncios.render(adverts.subList(fromIndex, toIndex), postRepository.countAllPartnerFound()));
	}
	
	@Transactional
	public static Result searchAd() {
		postRepository = PosterRepository.getInstance();
		Map<String, String> data = Form.form().bindFromRequest().data();
		
		String search = data.get("search");
		String titulo = data.get("titulo");
		String cidade = data.get("cidade");
		String date = data.get("data");
		String instrumento = data.get("instrumento");
		String estilo = data.get("estilo");
		String formarBanda = data.get("formar uma banda");
		String tocarOcasionalmente = data.get("tocar ocasionalmente");
		
		if((search == null || search.trim().isEmpty()) && 
				(formarBanda == null && tocarOcasionalmente == null)) {
			flash("erro", " Pesquisa inválida, tente novamente.");
			return redirect("anuncios");
		}
		
		adverts = postRepository.findAll();
		if(search != null && !search.trim().toLowerCase().isEmpty()) {
			if(titulo != null) {
				adverts = adverts.stream().filter(p -> 
				search.trim().toLowerCase().contains(p.getTitle().trim().toLowerCase())).
				collect(Collectors.toList());
			}
			if(cidade != null) {
				adverts = adverts.stream().filter(p -> 
				search.trim().toLowerCase().toLowerCase().contains(p.getUser().
				getCity().trim().toLowerCase())).collect(Collectors.toList());
			}
			if(date != null) {
				adverts = adverts.stream().filter(p -> 
				search.trim().toLowerCase().contains(p.getDateFormat())).
				collect(Collectors.toList());
			}
			if(instrumento != null) {
				List<Poster> posterList = new ArrayList<>();
				for(Poster poster : adverts) {
					for(Instrument instrument : poster.getUser().getInstruments()) {
						if(search.trim().toLowerCase().contains(
								instrument.getNome().trim().toLowerCase())) {
							
							posterList.add(poster);
							break;
						}
					}
				}
				adverts.clear();
				adverts.addAll(posterList);
				
			}
			if(estilo != null) {
				List<Poster> posterList = new ArrayList<>();
				for(Poster poster : adverts) {
					for(Style style : poster.getUser().getGoodStyles()) {
						if(search.trim().toLowerCase().contains(
								style.getNome().trim().toLowerCase())) {
							
							posterList.add(poster);
							break;
						}
					}
				}
				adverts.clear();
				adverts.addAll(posterList);
			}
		}
		if(formarBanda != null && tocarOcasionalmente == null) {
			adverts = adverts.stream().filter(p -> 
			formarBanda.trim().contains(p.getSearchFor().trim()))
			.collect(Collectors.toList());
		}
		if(formarBanda == null && tocarOcasionalmente != null) {
			adverts = adverts.stream().filter(p -> 
			tocarOcasionalmente.trim().contains(p.getSearchFor().trim()))
			.collect(Collectors.toList());
		}
		
		if(adverts.isEmpty()) {
			flash("notFound", "Nenhum anúncio encontrado com os parâmetros informados");
			return redirect("anuncios");
		}
		
		return ok(anuncios.render(adverts, postRepository.countAllPartnerFound()));
	}
	
	@Transactional
	public static Result finallyAd(Long id) {
		postRepository = PosterRepository.getInstance();
		Map<String, String> data = Form.form().bindFromRequest().data();
		
		String code = data.get("code");
		String found = data.get("found");
		
		
		Poster poster = postRepository.findByEntityId(id);
		if(found == null  || code.isEmpty()) {
			flash("incompleteInfo", "Informação sobre o código do anúncio e/ou "
					+ "os seus resultados estão incompletas, por favor verifique!");
			return redirect(controllers.routes.Application.anuncio(id));
		}
		
		if(!String.valueOf(poster.getCode()).equals(code)) {
			flash("codeErro", "Código de finalização do anúncio incorreto, por favor verifique!");
			return redirect(controllers.routes.Application.anuncio(id));
		} else if(found.equals("1")) {
			poster.setFinalized(true);
			poster.setPartnerFound(true);
		} else {
			poster.setFinalized(true);
		}
		
		postRepository.merge(poster);
		postRepository.flush();
		
		flash("adFinally", "Anúncio "
				+ poster.getId() + " finalizado com sucesso!");
		return redirect(controllers.routes.Application.anuncios(1, 15, true));
	}
	
	@Transactional
	public static Result sobre() {
		return ok(sobre.render());
	}
}
