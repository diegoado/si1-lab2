package models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


@Entity(name = "user")
public class User implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "nick_name", nullable = false, unique = true)
	private String nickName;
	
	@Column(name = "passworld", nullable = false)
	private String passworld;
		
	@OneToOne(mappedBy = "user")
	private Address address;
	
	@OneToOne(mappedBy = "user")
	private Contact contact;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Instrument> instruments;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Style> goodStyles;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Style> badStyles;
	
	@Transient
	private static final long serialVersionUID = 1L;
	
	public User() {
		instruments = new ArrayList<Instrument>();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassworld() {
		return passworld;
	}

	public void setPassworld(String passworld) {
		this.passworld = passworld;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public List<Instrument> getInstruments() {
		return instruments;
	}

	public void setInstruments(List<Instrument> instruments) {
		this.instruments = instruments;
	}

	public List<Style> getGoodStyles() {
		return goodStyles;
	}

	public void setGoodStyles(List<Style> goodStyles) {
		this.goodStyles = goodStyles;
	}

	public List<Style> getBadStyles() {
		return badStyles;
	}

	public void setBadStyles(List<Style> badStyles) {
		this.badStyles = badStyles;
	}
}
