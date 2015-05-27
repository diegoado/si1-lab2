package models.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity(name = "poster")
public class Poster implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Size(min = 15)
	@Column(name = "subject", nullable = false)
	private String subject;
	
	@Column(name = "create_on")
	@Temporal(value = TemporalType.DATE)
	private Calendar createdOn;
	
	@Column(name = "search_for", nullable = false)
	private String searchFor;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Transient
	private static final long serialVersionUID = 1L;
	
	public Poster() {
		createdOn = Calendar.getInstance();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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
}
