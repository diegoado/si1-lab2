package models.repository;

import models.entity.Style;

public class StyleRepository extends GenericRepository<Style> {

	private static StyleRepository instance;

	private StyleRepository() {
		super(Style.class);
	}
	
	public static StyleRepository getInstance() {
		if(instance == null) {
			instance = new StyleRepository();
		}
		return instance;
	}
}
