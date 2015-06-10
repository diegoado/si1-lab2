package global;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import models.constant.InstrumentType;
import models.constant.StyleType;
import models.entity.Instrument;
import models.entity.Poster;
import models.entity.Style;
import models.entity.User;
import models.exception.NewAdException;
import models.repository.InstrumentRepository;
import models.repository.PosterRepository;
import models.repository.StyleRepository;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;

public class Global extends GlobalSettings {
		
	@Override
	public void onStart(Application app) {
		super.onStart(app);
		
		Logger.info("Application has started");
		
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				try {
					StyleRepository styleRepository = StyleRepository.getInstance();
					List<Style> styles = styleRepository.findAll();
					if(styles.size() == 0) {
						for(StyleType style : StyleType.values()) {
							styleRepository.persist(new Style(style.getDescription()));
							
						}
						styleRepository.flush();
					}
					
					InstrumentRepository instrumentRepository = InstrumentRepository.getInstance();
					List<Instrument> instruments = instrumentRepository.findAll();
					if(instruments.size() == 0) {
						for(InstrumentType instrument : InstrumentType.values()) {
							instrumentRepository.persist(new Instrument(instrument.getDescription()));
						}
						instrumentRepository.flush();
					}
					
					List<Poster> adverts;
					PosterRepository posterRepository = PosterRepository.getInstance();
					adverts = posterRepository.findAllNotFinalized();
					if(adverts.size() == 0) {
						for(Poster poster : createExampleAd()) {
							posterRepository.persist(poster);
						}
						posterRepository.flush();
					}
					
					int count = 0;
					Random random = new Random();
					adverts = posterRepository.findAllNotFinalized();
					while(count < 15) {
						Poster poster = adverts.get(random.nextInt(adverts.size() - 1));
						if(!poster.isFinalized()) {
							count++;
							poster.setFinalized(true);
							poster.setPartnerFound(true);
							posterRepository.merge(poster);
						}
					}
					posterRepository.flush();
				} catch (Exception e) {
					Logger.debug(e.getMessage());
				}
			}
		});
	}
	
	@Override
	public void onStop(Application app) {	
		super.onStop(app);
		
		StyleRepository styleRepository = StyleRepository.getInstance();
		PosterRepository posterRepository = PosterRepository.getInstance();
		InstrumentRepository instrumentRepository = InstrumentRepository.getInstance();
		
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				Logger.info("Application shutdown");
				try {
					List<Poster> adverts = posterRepository.findAll();
					for(Poster poster : adverts) {
						posterRepository.removeById(poster.getId());
					}
					
					List<Style> styles = styleRepository.findAll();
					for(Style style : styles) {
						styleRepository.removeById(style.getId());
					}
					
					List<Instrument> instruments = instrumentRepository.findAll();
					for(Instrument instrument : instruments) {
						instrumentRepository.removeById(instrument.getId());
					}
				} catch (Exception e) {
					Logger.debug("Problem in finalizing: " + e.getMessage());
				}
			}
		});
	}
	
	private List<Poster> createExampleAd() {
		Random random = new Random();
		Calendar date = Calendar.getInstance();
		
		String title = "";
		String description = "Teste básico para um POST de um anúncio!";
		String email = "";
		String city = "Campina Grande";
		String neighborhood = "José Pinheiro";
		String profile = "";
		String interest = "";
		
		List<Poster> adverts = new ArrayList<>();
		List<Style> styles = StyleRepository.getInstance().findAll();
		List<Instrument> instruments = InstrumentRepository.getInstance().findAll();
				
		for(int i=1; i <= 40; i++) {
			int index = 0;
			int randomLevel = 0;
			double release = Math.random();
			title = "Anúncio Teste " + String.valueOf(i);
			
			if(release < 0.35) {
				email = "diegoado@gmail.com";
			} else if(release > 0.35 &&  release < 0.65) {
				email = "diegoado@gmail.com";
				profile = "https://www.facebook.com/diego.adolfo.332";
			} else {
				profile = "https://www.facebook.com/diego.adolfo.332";
			}
			
			if(release <= 0.5) {
				interest = "Formar uma Banda"; 
			} else {
				interest = "Tocar Ocasionalmente";
			}
			
			List<Instrument> myInstruments = new ArrayList<>();
			while((Math.random() <= 0.5 && randomLevel <= 3) || myInstruments.isEmpty()) {
				randomLevel += 1;
				index = random.nextInt(instruments.size() - 1);
				while(myInstruments.contains(instruments.get(index))) {
					index = random.nextInt(instruments.size() - 1);
				}
				myInstruments.add(instruments.get(index));
			}
			
			index = 0;
			randomLevel = 0;
			List<Style> goodStyle = new ArrayList<>();
			while(Math.random() <= 0.5 && randomLevel <= 3) {
				randomLevel += 1;
				index = random.nextInt(styles.size() - 1);
				while(goodStyle.contains(styles.get(index))) {
					index = random.nextInt(styles.size() - 1);
				}
				goodStyle.add(styles.get(index));
			}
			
			index = 0;
			randomLevel = 0;
			List<Style> badStyle = new ArrayList<>();
			while(Math.random() <= 0.5 && randomLevel <= 3) {
				randomLevel += 1;
				index = random.nextInt(styles.size() - 1);
				while(badStyle.contains(styles.get(index)) || goodStyle.contains(styles.get(index))) {
					index = random.nextInt(styles.size() - 1);
				}
				badStyle.add(styles.get(index));
			}
		
			try {
				User user = new User(email, profile, city, neighborhood, myInstruments, badStyle, goodStyle);
				Poster poster = new Poster(title, description, interest, user);
				date.add(Calendar.DAY_OF_MONTH, -i);
				poster.setCreatedOn(date.getTime());
				adverts.add(poster);
			} catch (NewAdException e) {
				Logger.debug("Erro: " + e.getMessage());
			} finally {
				title = email = profile = interest = "";
			}
		}
		return adverts;
	}
}
