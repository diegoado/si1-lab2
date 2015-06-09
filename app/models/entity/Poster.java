package models.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

import models.exception.NewAdException;

@Entity(name = "poster")
public class Poster implements Serializable, Comparable<Poster> {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "description", nullable = false)
	private String description;
		
	@Column(name = "search_for", nullable = false)
	private String searchFor;
		
	@Column(name = "create_on")
	@Temporal(value = TemporalType.DATE)
	private Date createdOn;

	@Column(name = "finalized", nullable = false)
	private boolean isFinalized;
	
	@Column(name = "partner_found", nullable = false)
	private boolean partnerFound;
		
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Transient
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	@Transient
	private static final long serialVersionUID = 1L;
	
	protected Poster() {
	}
	
	public Poster(String title, String description, String searchFor, User user) throws NewAdException {
		checkRequiredInfo(title, description, searchFor);
		
		this.title = title;
		this.description = description;
		createdOn = new Date();
		this.searchFor = searchFor;
		isFinalized = false;
		partnerFound = false;
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
		return user.getId() + this.getId();
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

	public String getResumeDesc() {
		return description.substring(0, 39) + "...";
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public String getSearchFor() {
		return searchFor;
	}

	public void setSearchFor(String searchFor) {
		this.searchFor = searchFor;
	}
	
	public String getDateFormat() {
		return dateFormat.format(createdOn);
	}
	
	public boolean isFinalized() {
		return isFinalized;
	}

	public void setFinalized(boolean isFinalized) {
		this.isFinalized = isFinalized;
	}

	public boolean isPartnerFound() {
		return partnerFound;
	}

	public void setPartnerFound(boolean partnerFound) {
		this.partnerFound = partnerFound;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Anúnicio [Titulo= ").append(title).append(", criado em= ")
				.append(getDateFormat()).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Poster other) {
		return this.createdOn.compareTo(other.getCreatedOn()) * (-1);
	}
	
	private void checkRequiredInfo(String title, String description, 
			String searchFor) throws NewAdException {
		
		if(title == null || description == null || searchFor == null || 
				title.trim().isEmpty() || description.trim().isEmpty() || searchFor.trim().isEmpty()) {
			throw new NewAdException("os campos de titulo, descrição e a "
						+ "motivação do anúncio são obrigatórios");
		}
		
		if(description.length() < 39) {
			throw new NewAdException("sua descrição dever ter no mínimo 40 caracteres, "
					+ "por favor verifique!");
		}
		
		if(title.length() > 100) {
			throw new NewAdException("o titulo do anúncio deve ter no máximo 100 caracteres "
					+ "por favor verifique!");
		}
	}
}
