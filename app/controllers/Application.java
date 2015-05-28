package controllers;

import models.repository.InstrumentRepository;
import models.repository.StyleRepository;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {
	
	@Transactional
    public static Result index() {
        return ok(index.render());
    }
	
	@Transactional
	public static Result publique() {
		StyleRepository styles = StyleRepository.getInstance();
		InstrumentRepository instruments = InstrumentRepository.getInstance();
		return ok(publique.render(styles.findAll(), instruments.findAll()));
	}
			
	@Transactional
	public static Result createAd() {
		return redirect("/");
	}
	
	@Transactional
	public static Result sobre() {
		return ok(sobre.render());
	}
}
