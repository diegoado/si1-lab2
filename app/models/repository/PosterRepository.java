package models.repository;

import java.util.List;

import models.entity.Poster;

public class PosterRepository extends GenericRepository<Poster> {
	
	private static PosterRepository instance;

	private PosterRepository() {
		super(Poster.class);
	}
	
	public static PosterRepository getInstance() {
		if(instance == null) {
			instance = new PosterRepository();
		}
		return instance;
	}
	
	public List<Poster> findAllNotFinalized() {
		return findByAttributeName("isFinalized", "false");
	}
	
	public long countAllPartnerFound() {
		return findByAttributeName("partnerFound", "true").size();
	}
}
