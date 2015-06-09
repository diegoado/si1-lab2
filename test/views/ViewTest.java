package views;

import static org.fest.assertions.Assertions.assertThat;
//import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.when;
import static play.test.Helpers.*;

import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;

import play.GlobalSettings;
import play.mvc.*;
import views.html.*;
import play.twirl.api.Content;
import play.test.FakeApplication;
import play.test.WithApplication;

class EmptyGlobal extends GlobalSettings {
	
}

public class ViewTest extends WithApplication {
		
	@Override
	protected FakeApplication provideFakeApplication() {
		return fakeApplication(inMemoryDatabase(), new EmptyGlobal());
	}
	
	@Before
    public void setUp() throws Exception {
        // Setup HTTP Context
        Http.Context context = Mockito.mock(Http.Context.class);
        
        // Mocking flash session, request, etc... as required
        Http.Flash flash = Mockito.mock(Http.Flash.class);
        when(context.flash()).thenReturn(flash);
        Http.Context.current.set(context);
    }

	@Test
	public void mustRenderIndex() {
		Content html = index.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Bem vindo");
	}
}
