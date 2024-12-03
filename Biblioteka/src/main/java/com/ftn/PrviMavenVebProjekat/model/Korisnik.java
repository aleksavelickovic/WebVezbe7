package com.ftn.PrviMavenVebProjekat.model;

public class Korisnik {
	
	private Long id;
	private String ime;
	private String prezime;
	private String email;
	private String lozinka;
	
	public Korisnik() {}

	public Korisnik(String ime, String prezime, String email, String lozinka) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.lozinka = lozinka;
	}
	
	public Korisnik(Long id, String ime, String prezime, String email, String lozinka) {
		super();
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.lozinka = lozinka;
	}
	
	public Long getId() { return id; }

	public void setId(Long id) { this.id = id; }

	public String getIme() { return ime; }

	public void setIme(String ime) { this.ime = ime; }

	public String getPrezime() { return prezime; }

	public void setPrezime(String prezime) { this.prezime = prezime; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }
	

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	@Override
	public String toString() {
		return this.ime + " " + this.prezime + " (" + this.email + ")";
	}
	
	
}
