package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;
import global.Global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;

import models.entity.Instrument;
import models.repository.InstrumentRepository;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import play.test.Helpers;
import scala.Option;

public class ApplicationTest {
	
	private final int PAGE_NUMBER = 1;
	private final int PAGE_SIZE = 15;
	
	private static FakeApplication app;
	private EntityManager entityManager;
	
	@BeforeClass
	public static void startApp() {
		app = Helpers.fakeApplication(new Global());
		Helpers.start(app);
	}

	@Before
	public void setUp() {
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
		Result result = callAction(controllers.routes.ref.Application.publique(), new FakeRequest());
		assertThat(status(result)).isEqualTo(OK);
	}
	
	@Test
	public void mustCreateAd() {
		Random random = new Random();
		List<Instrument> instrument = InstrumentRepository.getInstance().findAll();
		
		String instrumentId = String.valueOf(
					instrument.get(random.nextInt(instrument.size() - 1)).getId());
		
		Map<String, String> requestMap = new HashMap<>();
		requestMap.put("titulo", "Anúncio Teste");
		requestMap.put("descricao", "Teste básico para um POST de um anúncio!");
		requestMap.put("email", "diegoado@gmail.com");
		requestMap.put("city", "Campina Grande");
		requestMap.put("bairro", "José Pinheiro");
		requestMap.put("myInst[]", instrumentId);
		requestMap.put("interesse", "Tocar Ocasionalmente");
		
		FakeRequest fakeRequest = new FakeRequest().withFormUrlEncodedBody(requestMap);
		Result resultPost = callAction(controllers.routes.ref.Application.createAd(), fakeRequest);
		assertThat(status(resultPost)).isEqualTo(SEE_OTHER);
		
		Result resultGet = callAction(controllers.routes.ref.Application.anuncios(PAGE_NUMBER, PAGE_SIZE, true), new FakeRequest());
		assertThat(status(resultGet)).isEqualTo(OK);
		assertThat(contentType(resultGet)).isEqualTo("text/html");
		assertThat(contentAsString(resultGet)).contains("Anúncio Teste");
		
	}
	
	@Test
	public void mustNotCreateAd() {
		Random random = new Random();
		List<Instrument> instrument = InstrumentRepository.getInstance().findAll();
		
		String instrumentId = String.valueOf(
					instrument.get(random.nextInt(instrument.size() - 1)).getId());
		
		Map<String, String> requestMap = new HashMap<>();
		requestMap.put("titulo", "Anúncio Teste");
		requestMap.put("descricao", "Teste básico para um POST de um anúncio!");
		requestMap.put("email", "diegoado@gmail.com");
		requestMap.put("city", "Campina Grande");
		requestMap.put("bairro", "José Pinheiro");
		requestMap.put("myInst[]", instrumentId);
		
		FakeRequest fakeRequest = new FakeRequest().withFormUrlEncodedBody(requestMap);
		Result resultPost = callAction(controllers.routes.ref.Application.createAd(), fakeRequest);
		assertThat(status(resultPost)).isEqualTo(SEE_OTHER);
		
	}
	
	@Test
	public void mustRenderAnuncios() {
		Result result = callAction(controllers.routes.ref.Application.anuncios(PAGE_NUMBER, PAGE_SIZE, true), new FakeRequest());
		assertThat(status(result)).isEqualTo(OK);
	}
	
	@Test
	public void mustRenderAbout() {
		Result result = Application.sobre();
		assertThat(status(result)).isEqualTo(OK);
	}

	@After
	public void tearDown() {
        entityManager.getTransaction().commit();
        JPA.bindForCurrentThread(null);
        entityManager.close();
	}
	
	@AfterClass
	public static void shopApp() {
		Helpers.stop(app);
	}
}
