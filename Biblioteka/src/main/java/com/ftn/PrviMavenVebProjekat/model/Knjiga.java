package com.ftn.PrviMavenVebProjekat.model;

public class Knjiga {

	private Long id;
	private String naziv;
	private String registarskiBrojPrimerka; //
	private String jezik;
	private int brojStranica;
	private boolean izdata;

	public Knjiga() {
	}

	public Knjiga(Long id, String naziv, String registarskiBrojPrimerka, String jezik, int brojStranica, boolean izdata) {
		super();
		this.id = id;
		this.naziv = naziv;
		this.registarskiBrojPrimerka = registarskiBrojPrimerka;
		this.jezik = jezik;
		this.brojStranica = brojStranica;
		this.izdata = izdata;
	}

	public Knjiga(String naziv, String registarskiBrojPrimerka, String jezik, int brojStranica) {
		super();
		this.naziv = naziv;
		this.registarskiBrojPrimerka = registarskiBrojPrimerka;
		this.jezik = jezik;
		this.brojStranica = brojStranica;
		this.izdata = false;
	}
	
	

	public Knjiga(Long id, String naziv, String registarskiBrojPrimerka, String jezik, int brojStranica) {
		super();
		this.id = id;
		this.naziv = naziv;
		this.registarskiBrojPrimerka = registarskiBrojPrimerka;
		this.jezik = jezik;
		this.brojStranica = brojStranica;
		this.izdata = false;
	}

	@Override
	public String toString() {
		return this.id + ";" + this.naziv + ";" + this.registarskiBrojPrimerka + ";" + this.jezik + ";"
				+ this.brojStranica + ";" + this.izdata;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getRegistarskiBrojPrimerka() {
		return registarskiBrojPrimerka;
	}

	public void setRegistarskiBrojPrimerka(String registarskiBrojPrimerka) {
		this.registarskiBrojPrimerka = registarskiBrojPrimerka;
	}

	public String getJezik() {
		return jezik;
	}

	public void setJezik(String jezik) {
		this.jezik = jezik;
	}

	public int getBrojStranica() {
		return brojStranica;
	}

	public void setBrojStranica(int brojStranica) {
		this.brojStranica = brojStranica;
	}

	public boolean isIzdata() {
		return izdata;
	}

	public void setIzdata(boolean izdata) {
		this.izdata = izdata;
	}

}
