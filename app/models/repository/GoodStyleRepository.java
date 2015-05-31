package models.repository;

import models.entity.GoodStyle;

public class GoodStyleRepository extends GenericRepository<GoodStyle> {

	private static GoodStyleRepository instance;

	private GoodStyleRepository() {
		super(GoodStyle.class);
	}
	
	public static GoodStyleRepository getInstance() {
		if(instance == null) {
			instance = new GoodStyleRepository();
		}
		return instance;
	}
}
