package models.entity;

import java.io.Serializable;

import javax.persistence.*;


@Entity(name = "instrument")
public class Instrument implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "name", nullable = false)
	private String nome;

	@Transient
	private static final long serialVersionUID = 1L;
	
	protected Instrument() {
	} 

	public Instrument(String nome) {
		this.nome = nome;
	}
	
	// Getters and Setters

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}
}
