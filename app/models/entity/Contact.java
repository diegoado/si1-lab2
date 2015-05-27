package models.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.persistence.Transient;

@Entity(name = "contact")
public class Contact implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "phofile", unique = true)
	private String phofile;
	
	@Column(name = "telephone")
	private String telephone;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private User user;
	
	@Transient
	private static final long serialVersionUID = 1L;
	
	public Contact() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhofile() {
		return phofile;
	}

	public void setPhofile(String phofile) {
		this.phofile = phofile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
