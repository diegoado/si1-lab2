

import java.util.List;

import models.repository.InstrumentRepository;
import models.constant.InstrumentType;
import models.constant.StyleType;
import models.entity.Instrument;
import models.entity.Style;
import models.repository.StyleRepository;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;

public class Global extends GlobalSettings {
	
	private List<Style> styles;
	private List<Instrument> instruments;
	private static StyleRepository styleRepository = StyleRepository.getInstance();
	private static InstrumentRepository instrumentRepository = InstrumentRepository.getInstance();
	
	@Override
	public void onStart(Application app) {
		super.onStart(app);
		Logger.info("Application has started");
		
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				try {
					styles = styleRepository.findAll();
					if(styles.size() == 0) {
						for(StyleType style : StyleType.values()) {
							styleRepository.persist(new Style(style.getDescription()));
						}
						styleRepository.flush();
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
					styles = styleRepository.findAll();
					for(Style style : styles) {
						styleRepository.removeById(style.getId());
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
