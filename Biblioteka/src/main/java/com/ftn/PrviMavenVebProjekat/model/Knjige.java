package com.ftn.PrviMavenVebProjekat.model;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Knjige {

	private Map<Long, Knjiga> knjige = new HashMap<>();
	private long nextId = 1L;

	/** Cita knjige iz datoteke i smesta ih u asocijativnu listu knjiga. */
	public Knjige() {

		try {
			Path path = Paths.get(getClass().getClassLoader().getResource("knjige.txt").toURI());
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			for (String line : lines) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				String[] tokens = line.split(";");
				Long id = Long.parseLong(tokens[0]);
				String naziv = tokens[1];
				String registarskiBrojPrimerka = tokens[2];
				String jezik = tokens[3];
				int brojStranica  = Integer.parseInt(tokens[4]);

				knjige.put(Long.parseLong(tokens[0]), new Knjiga(id, naziv, registarskiBrojPrimerka, jezik, brojStranica));
				if(nextId<id)
					nextId=id;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** vraca knjigu u odnosu na zadati id */
	public Knjiga findOne(Long id) {
		return knjige.get(id);
	}

	/** vraca sve knjige */
	public List<Knjiga> findAll() {
		return new ArrayList<Knjiga>(knjige.values());
	}

	/** cuva podatke o knjizi */
	public Knjiga save(Knjiga knjiga) {
		if (knjiga.getId() == null) {
			knjiga.setId(++nextId);
		}
		knjige.put(knjiga.getId(), knjiga);
		return knjiga;
	}

	/** cuva podatke o svim knjigama */
	public List<Knjiga> save(List<Knjiga> knjige) {
		List<Knjiga> ret = new ArrayList<>();

		for (Knjiga k : knjige) {
			// za svaku prosleđenu knjigu pozivamo save
			// metodu koju smo već napravili i testirali -
			// ona će sepobrinuti i za dodelu ID-eva
			// ako je to potrebno
			Knjiga saved = save(k);

			// uspešno snimljene dodajemo u listu za vraćanje
			if (saved != null) {
				ret.add(saved);
			}
		}
		return ret;
	}

	/** brise knjigu u odnosu na zadati id */
	public Knjiga delete(Long id) {
		if (!knjige.containsKey(id)) {
			throw new IllegalArgumentException("tried to remove non existing book");
		}
		Knjiga knjiga = knjige.get(id);
		if (knjiga != null) {
			knjige.remove(id);
		}
		return knjiga;
	}

	/** brise knjige u odnosu na zadate ids */
	public void delete(List<Long> ids) {
		for (Long id : ids) {
			// pozivamo postojeću metodu za svaki
			delete(id);
		}
	}

	/** vraca sve knjige ciji naziv zapocinje sa tekstom*/
	public List<Knjiga> findByNaziv(String naziv) {
		List<Knjiga> ret = new ArrayList<>();

		for (Knjiga k : knjige.values()) {
			if (naziv.startsWith(k.getNaziv())) {
				ret.add(k);
			}
		}

		return ret;
	}
}
