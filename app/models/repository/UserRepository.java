package models.repository;

import models.entity.User;

public class UserRepository extends GenericRepository<User> {
	
	private static UserRepository instance;

	private UserRepository() {
		super(User.class);
	}
	
	public static UserRepository getInstance() {
		if(instance == null) {
			instance = new UserRepository();
		}
		return instance;
	}
}
