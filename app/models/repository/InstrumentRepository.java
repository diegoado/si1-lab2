package models.repository;

import models.entity.Instrument;
import models.repository.GenericRepository;

public class InstrumentRepository extends GenericRepository<Instrument> {
	
	private static InstrumentRepository instance;

	private InstrumentRepository() {
		super(Instrument.class);
	}
	
	public static InstrumentRepository getInstance() {
		if(instance == null) {
			instance = new InstrumentRepository();
		}
		return instance;
	}

}
