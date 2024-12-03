package com.ftn.PrviMavenVebProjekat.service.impl;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ftn.PrviMavenVebProjekat.model.Knjiga;
import com.ftn.PrviMavenVebProjekat.service.KnjigaService;

@Service
@Qualifier("fajloviKnjiga")
public class KnjigaServiceImpl implements KnjigaService {

	@Value("${knjige.pathToFile}")
	private String pathToFile;

	private Map<Long, Knjiga> readFromFile() {

		Map<Long, Knjiga> knjige = new HashMap<>();
		Long nextId = 1L;

		try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			for (String line : lines) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;

				String[] tokens = line.split(";");
				Long id = Long.parseLong(tokens[0]);
				// todo
				String naziv = tokens[1];
				String registarskiBroj = tokens[2];
				String jezik = tokens[3];
				int brojStranica = Integer.parseInt(tokens[4]);
				boolean izdata = false;
				if (tokens.length > 5) {
					izdata = Boolean.valueOf(tokens[5]);
				}

				knjige.put(id, new Knjiga(id, naziv, registarskiBroj, jezik, brojStranica, izdata));

				if (nextId < id)
					nextId = id;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return knjige;
	}

	private Map<Long, Knjiga> saveToFile(Map<Long, Knjiga> knjige) {

		Map<Long, Knjiga> ret = new HashMap<>();

		try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = new ArrayList<>();

			for (Knjiga knjiga : knjige.values()) {
				String line = knjiga.toString(); // todo upis linije u fajl
				lines.add(line);
				ret.put(knjiga.getId(), knjiga);
			}
			// pisanje svih redova za filmove
			Files.write(path, lines, Charset.forName("UTF-8"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public Knjiga findOne(Long id) {
		// TODO Auto-generated method stub
		Map<Long, Knjiga> knjige = readFromFile();
		return knjige.get(id);
	}

	@Override
	public List<Knjiga> findAll() {
		// TODO Auto-generated method stub
		Map<Long, Knjiga> knjige = readFromFile();
		return new ArrayList<>(knjige.values());
	}

	@Override
	public Knjiga save(Knjiga knjiga) {
		// TODO Auto-generated method stub
		Map<Long, Knjiga> knjige = readFromFile();
		Long nextId = nextId(knjige);

		if (knjiga.getId() == null) {
			knjiga.setId(nextId + 1);
		}

		knjige.put(knjiga.getId(), knjiga);
		saveToFile(knjige);
		return null;
	}

	@Override
	public Knjiga update(Knjiga knjiga) {
		// TODO Auto-generated method stub
		Map<Long, Knjiga> knjige = readFromFile();
//		Knjiga knjigaEdited = findOne(knjiga.getId());
//		Knjiga knjiga = knjigaService.findOne(knjigaEdited.getId());
		if(knjiga != null) {
			if(knjiga.getNaziv() != null && !knjiga.getNaziv().trim().equals(""))
				knjiga.setNaziv(knjiga.getNaziv());
			if(knjiga.getJezik() != null && !knjiga.getJezik().trim().equals(""))
				knjiga.setJezik(knjiga.getJezik());
			if(knjiga.getBrojStranica() > 0)
				knjiga.setBrojStranica(knjiga.getBrojStranica());
		}
		knjige.replace(knjiga.getId(), knjiga);
//		save(knjiga);
		saveToFile(knjige);
		return knjiga;
	}

	@Override
	public Knjiga delete(Long id) {
		// TODO Auto-generated method stub
		Map<Long, Knjiga> knjige = readFromFile();
		knjige.remove(id);
		saveToFile(knjige);
		return null;
	}

	private Long nextId(Map<Long, Knjiga> map) {
		Long nextId = 0L;

		for (Long id : map.keySet()) {
			if (id > nextId) {
				nextId = id;
			}
		}

		return nextId;
	}

}
