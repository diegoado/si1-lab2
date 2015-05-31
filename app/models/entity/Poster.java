package models.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.*;

import models.exception.NewAdException;

@Entity(name = "poster")
public class Poster implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "code", unique = true, nullable = false)
	private long code;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "search_for", nullable = false)
	private String searchFor;
	
	@Column(name = "create_on")
	@Temporal(value = TemporalType.DATE)
	private Calendar createdOn;
		
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Transient
	private static final long serialVersionUID = 1L;
	
	protected Poster() {
	}
	
	public Poster(String title, String description, String searchFor, User user) throws NewAdException {
		checkRequiredInfo(title, description, searchFor);
		
		this.title = title;
		this.description = description;
		createdOn = Calendar.getInstance();
		code = title.hashCode() + user.hashCode();
		this.searchFor = searchFor;
		this.user = user;
		
	}
	
	// Getters and Setters

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Calendar createdOn) {
		this.createdOn = createdOn;
	}
	
	public String getSearchFor() {
		return searchFor;
	}

	public void setSearchFor(String searchFor) {
		this.searchFor = searchFor;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	private void checkRequiredInfo(String title, String description, 
			String searchFor) throws NewAdException {
		
		if(title == null || description == null || searchFor == null ||
				title.isEmpty() || description.isEmpty() || searchFor.isEmpty()) {
			throw new NewAdException("os campos de titulo, descrição e a "
						+ "motivação do anúncio são obrigatórios");
		}
		
		if(description.length() < 14) {
			throw new NewAdException("sua descrição dever ter no mínimo 15 caracteres, "
					+ "por favor verifique!");
		}
	}
}
