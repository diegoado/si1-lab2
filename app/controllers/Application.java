package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import models.entity.BadStyle;
import models.entity.GoodStyle;
import models.entity.Instrument;
import models.entity.Poster;
import models.entity.User;
import models.exception.NewAdException;
import models.repository.BadStyleRepository;
import models.repository.GoodStyleRepository;
import models.repository.InstrumentRepository;
import models.repository.PosterRepository;
import models.repository.UserRepository;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {
	
	private static BadStyleRepository badStyles;
	private static GoodStyleRepository goodStyles;
	private static PosterRepository postRepository;
	private static InstrumentRepository instruments;
	
	@Transactional
    public static Result index() {
        return ok(index.render());
    }
	
	@Transactional
	public static Result publique() {
		badStyles = BadStyleRepository.getInstance();
		goodStyles = GoodStyleRepository.getInstance();
		instruments = InstrumentRepository.getInstance();
		return ok(publique.render(badStyles.findAll(), goodStyles.findAll(), instruments.findAll()));
	}
			
	@SuppressWarnings("unchecked")
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
		List<BadStyle> badStyles = getBadStyleSelectedData();
		List<GoodStyle> goodStyles = getGoodStyleSelectedData();
		List<Instrument> myInstruments = getInstrumentSelectedData(); 
		
		try {
			Logger.debug(titulo);
			Logger.debug(descrição);
			
			Logger.debug(email);
			Logger.debug(cidade);
			Logger.debug(bairro);
			Logger.debug(perfil);
			Logger.debug(interesse);
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
	private static List<BadStyle> getBadStyleSelectedData() {
		badStyles = BadStyleRepository.getInstance();
		Map<String, String[]> multipleData = request().body().asFormUrlEncoded();
		
		List<BadStyle> styleList = new ArrayList<>();
		String[] requestStyleArray = multipleData.get("badSty[]");
		
		if(requestStyleArray != null) {
			for(int i = 0; i < requestStyleArray.length; i++) {
				long id = Long.parseLong(requestStyleArray[i]);
				BadStyle style = badStyles.findByEntityId(id);
				if(!styleList.contains(style)) {
					styleList.add(style);
				}
			}
		}
		return styleList;
	}
	
	@Transactional
	private static List<GoodStyle> getGoodStyleSelectedData() {
		goodStyles = GoodStyleRepository.getInstance();
		Map<String, String[]> multipleData = request().body().asFormUrlEncoded();
		
		List<GoodStyle> styleList = new ArrayList<>();
		String[] requestStyleArray = multipleData.get("goodSty[]");
		
		if(requestStyleArray != null) {
			for(int i = 0; i < requestStyleArray.length; i++) {
				long id = Long.parseLong(requestStyleArray[i]);
				GoodStyle style = goodStyles.findByEntityId(id);
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
	
	public static Result anuncios() {
		return ok(anuncios.render());
	}
	
	@Transactional
	public static Result sobre() {
		return ok(sobre.render());
	}
}
