package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import models.entity.*;
import models.exception.*;
import models.repository.*;


public class Application extends Controller {
	
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

			Logger.debug(Arrays.toString(badStyles.toArray()));
			Logger.debug(Arrays.toString(goodStyles.toArray()));
			Logger.debug(Arrays.toString(myInstruments.toArray()));
			
			User user = new User(email, perfil, cidade, bairro, myInstruments, badStyles, goodStyles);
			Poster poster = new Poster(titulo, descrição, interesse, user);
			postRepository.persist(poster);
			postRepository.flush();
			
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
	public static Result anuncios() {
		postRepository = PosterRepository.getInstance();
		List<Poster> posterList = postRepository.findAll();
		
		Collections.sort(posterList);
		return ok(anuncios.render(posterList));
	}
	
	@Transactional
	public static Result sobre() {
		return ok(sobre.render());
	}
}
