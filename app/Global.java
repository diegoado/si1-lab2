

import java.util.List;

import models.repository.BadStyleRepository;
import models.repository.GoodStyleRepository;
import models.repository.InstrumentRepository;
import models.constant.InstrumentType;
import models.constant.StyleType;
import models.entity.BadStyle;
import models.entity.GoodStyle;
import models.entity.Instrument;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;

public class Global extends GlobalSettings {
	
	private List<BadStyle> badStyles;
	private List<GoodStyle> goodStyles;
	private List<Instrument> instruments;
	private static BadStyleRepository badStyleRepository = BadStyleRepository.getInstance();
	private static GoodStyleRepository goodStyleRepository = GoodStyleRepository.getInstance();
	private static InstrumentRepository instrumentRepository = InstrumentRepository.getInstance();
	
	@Override
	public void onStart(Application app) {
		super.onStart(app);
		Logger.info("Application has started");
		
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				try {
					goodStyles = goodStyleRepository.findAll();
					if(goodStyles.size() == 0) {
						for(StyleType style : StyleType.values()) {
							goodStyleRepository.persist(new GoodStyle(style.getDescription()));
							
						}
						goodStyleRepository.flush();
					}
					
					badStyles = badStyleRepository.findAll();
					if(badStyles.size() == 0) {
						for(StyleType style : StyleType.values()) {
							badStyleRepository.persist(new BadStyle(style.getDescription()));
							
						}
						badStyleRepository.flush();
					}
					
					instruments = instrumentRepository.findAll();
					if(instruments.size() == 0) {
						for(InstrumentType instrument : InstrumentType.values()) {
							instrumentRepository.persist(new Instrument(instrument.getDescription()));
						}
						instrumentRepository.flush();
					}
				} catch (Exception e) {
					Logger.debug(e.getMessage());
				}
			}
		});
	}
	
	@Override
	public void onStop(Application app) {	
		super.onStop(app);
		
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				Logger.info("Application shutdown");
				try {
					goodStyles = goodStyleRepository.findAll();
					for(GoodStyle style : goodStyles) {
						goodStyleRepository.removeById(style.getId());
					}
					
					badStyles = badStyleRepository.findAll();
					for(BadStyle style : badStyles) {
						badStyleRepository.removeById(style.getId());
					}
					
					instruments = instrumentRepository.findAll();
					for(Instrument instrument : instruments) {
						instrumentRepository.removeById(instrument.getId());
					}
				} catch (Exception e) {
					Logger.debug("Problem in finalizing: " + e.getMessage());
				}
			}
		});
	}
}
