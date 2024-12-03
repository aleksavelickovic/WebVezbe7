package com.ftn.PrviMavenVebProjekat.service;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.Knjiga;

public interface KnjigaService {

	Knjiga findOne(Long id); 
	List<Knjiga> findAll(); 
	Knjiga save(Knjiga knjiga); 
	Knjiga update(Knjiga knjiga); 
	Knjiga delete(Long id); 
}
