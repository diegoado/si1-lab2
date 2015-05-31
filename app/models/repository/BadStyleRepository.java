package models.repository;

import models.entity.BadStyle;

public class BadStyleRepository extends GenericRepository<BadStyle> {

	private static BadStyleRepository instance;

	private BadStyleRepository() {
		super(BadStyle.class);
	}
	
	public static BadStyleRepository getInstance() {
		if(instance == null) {
			instance = new BadStyleRepository();
		}
		return instance;
	}
}
