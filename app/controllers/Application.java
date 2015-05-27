package controllers;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {
	
	@Transactional
    public static Result index() {
        return ok(index.render("Find Your Band: Home"));
    }
	
	@Transactional
	public static Result cadastro() {
		return ok(cadastro.render("Find Your Band: Cadastro"));
	}

}
