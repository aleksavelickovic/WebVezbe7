package com.ftn.PrviMavenVebProjekat.model;

import java.util.ArrayList;
import java.util.List;

public class ClanskaKarta {
	
	private String registarskiBroj;
	private Korisnik korisnik;
	private List<Knjiga> iznajmljenjeKnjige;
	
	public ClanskaKarta() {
		this.iznajmljenjeKnjige = new ArrayList<Knjiga>();
	}

	public ClanskaKarta(String registarskiBroj, List<Knjiga> iznajmljenjeKnjige) {
		super();
		this.registarskiBroj = registarskiBroj;
		this.iznajmljenjeKnjige = iznajmljenjeKnjige;
	}
	
	public ClanskaKarta(String registarskiBroj, Korisnik korisnik) {
		super();
		this.registarskiBroj = registarskiBroj;
		this.korisnik = korisnik;
		this.iznajmljenjeKnjige = new ArrayList<Knjiga>();
	}
	
	public ClanskaKarta(String registarskiBroj) {
		super();
		this.registarskiBroj = registarskiBroj;
		this.iznajmljenjeKnjige = new ArrayList<Knjiga>();
	}

	public String getRegistarskiBroj() { return registarskiBroj; }

	public Korisnik getKorisnik() { return korisnik; }

	public void setKorisnik(Korisnik korisnik) { this.korisnik = korisnik; }

	public void setRegistarskiBroj(String registarskiBroj) { this.registarskiBroj = registarskiBroj; }

	public List<Knjiga> getIznajmljenjeKnjige() { return iznajmljenjeKnjige; }

	public void setIznajmljenjeKnjige(List<Knjiga> iznajmljenjeKnjige) { this.iznajmljenjeKnjige = iznajmljenjeKnjige; }
}
