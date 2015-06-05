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
        return ok(index.render());
    }
	
	@Transactional
	public static Result anuncios(int page, int pageSize) {
		postRepository = PosterRepository.getInstance();
		
		page = page >= FIRST_PAGE ? page : FIRST_PAGE;
		pageSize = pageSize >= FIRST_PAGE ? pageSize : PosterRepository.DEFAULT_RESULTS;
		
		Long posterNumber = postRepository.countAll();
		if(page > (posterNumber / pageSize)) {
			page = (int) 
				(Math.ceil(posterNumber / Float.parseFloat(String.valueOf(pageSize))));
		}
		
		session("actualPage", String.valueOf(page));
		List<Poster> posterList = postRepository.findAll(page, pageSize);
		
		Collections.sort(posterList);
		return ok(anuncios.render(posterList));
	}
	
	@Transactional
	public static Result searchAd() {
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
				
		postRepository = PosterRepository.getInstance();
		List<Poster> posterList = postRepository.findAll();
		
		if(search != null && !search.trim().toLowerCase().isEmpty()) {
			if(titulo != null) {
				posterList = posterList.stream().filter(p -> 
				search.trim().toLowerCase().contains(p.getTitle().trim().toLowerCase())).
				collect(Collectors.toList());
			}
			if(cidade != null) {
				posterList = posterList.stream().filter(p -> 
				search.trim().toLowerCase().toLowerCase().contains(p.getUser().
				getCity().trim().toLowerCase())).collect(Collectors.toList());
			}
			if(date != null) {
				posterList = posterList.stream().filter(p -> 
				search.trim().toLowerCase().contains(p.getDateFormat())).
				collect(Collectors.toList());
			}
			if(instrumento != null) {
				List<Poster> posterListTemp = new ArrayList<>();
				for(Poster poster : posterList) {
					for(Instrument instrument : poster.getUser().getInstruments()) {
						if(search.trim().toLowerCase().contains(
								instrument.getNome().trim().toLowerCase())) {
							
							posterListTemp.add(poster);
							break;
						}
					}
				}
				posterList.clear();
				posterList.addAll(posterListTemp);
				
			}
			if(estilo != null) {
				List<Poster> posterListTemp = new ArrayList<>();
				for(Poster poster : posterList) {
					for(Style style : poster.getUser().getGoodStyles()) {
						if(search.trim().toLowerCase().contains(
								style.getNome().trim().toLowerCase())) {
							
							posterListTemp.add(poster);
							break;
						}
					}
				}
				posterList.clear();
				posterList.addAll(posterListTemp);
			}
		}
		if(formarBanda != null && tocarOcasionalmente == null) {
			posterList = posterList.stream().filter(p -> 
			formarBanda.trim().contains(p.getSearchFor().trim()))
			.collect(Collectors.toList());
		}
		if(formarBanda == null && tocarOcasionalmente != null) {
			posterList = posterList.stream().filter(p -> 
			tocarOcasionalmente.trim().contains(p.getSearchFor().trim()))
			.collect(Collectors.toList());
		}
		
		if(posterList.isEmpty()) {
			flash("notFound", "Nenhum anúncio encontrado com os parâmetros informados");
			return redirect("anuncios");
		}
		
		return ok(anuncios.render(posterList));
	}
	
	@Transactional
	public static Result sobre() {
		return ok(sobre.render());
	}
}
