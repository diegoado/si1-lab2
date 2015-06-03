package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.GlobalSettings;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import play.test.Helpers;
import scala.Option;


public class ApplicationTest {
	
	private EntityManager entityManager;

	@Before
	public void setUp() throws Exception {
    	FakeApplication app = Helpers.fakeApplication(new GlobalSettings());
    	Helpers.start(app);
        Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(JPAPlugin.class);
        entityManager = jpaPlugin.get().em("default");
        JPA.bindForCurrentThread(entityManager);
        entityManager.getTransaction().begin();
	}
	
	@Test
	public void mustRenderIndex() {
		Result result = Application.index();
		assertThat(status(result)).isEqualTo(OK);
	}
	
	@Test
	public void mustRenderPublique() {
		Result result = Application.publique();
		assertThat(status(result)).isEqualTo(OK);
	}
	
	@Test
	public void mustCreateAd() {
		Map<String, String> requestMap = new HashMap<>();
		requestMap.put("titulo", "Anúnicio Teste");
		requestMap.put("descricao", "Teste para um POST de um anúncio");
		requestMap.put("email", "diegoado@gmail.com");
		requestMap.put("city", "Campina Grande");
		requestMap.put("bairro", "José Pinheiro");
		requestMap.put("myInst[0]", "1");
		requestMap.put("myInst[1]", "2");
		
		FakeRequest fakeRequest = new FakeRequest().withFormUrlEncodedBody(requestMap);
		Result resultPost = callAction(controllers.routes.ref.Application.createAd(), fakeRequest);
		assertThat(status(resultPost)).isEqualTo(SEE_OTHER);
		
		
	}
	
	@Test
	public void mustRenderAbout() {
		Result result = Application.sobre();
		assertThat(status(result)).isEqualTo(OK);
	}

	@After
	public void tearDown() throws Exception {
        entityManager.getTransaction().commit();
        JPA.bindForCurrentThread(null);
        entityManager.close();
	}
}
