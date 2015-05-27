package models.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity(name = "address")
public class Address implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "city", nullable = false)
	private String city;
	
	@Column(name = "district", nullable = false)
	private String district;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private User user;
	
	@Transient
	private static final long serialVersionUID = 1L;
	
	public Address() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
