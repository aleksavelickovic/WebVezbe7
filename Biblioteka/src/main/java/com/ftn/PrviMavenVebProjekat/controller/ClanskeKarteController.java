package com.ftn.PrviMavenVebProjekat.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftn.PrviMavenVebProjekat.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.PrviMavenVebProjekat.model.ClanskaKarta;
import com.ftn.PrviMavenVebProjekat.model.Knjiga;
import com.ftn.PrviMavenVebProjekat.model.Knjige;
import com.ftn.PrviMavenVebProjekat.model.Korisnici;
import com.ftn.PrviMavenVebProjekat.model.Korisnik;
import org.springframework.web.context.ServletContextAware;

@Controller
@RequestMapping(value="/clanskeKarte")
public class ClanskeKarteController implements ServletContextAware {
	
	public static final String CLANSKE_KARTE_KEY = "clanske_karte";
	public static final String KORISNICI_KEY = "korisnici";
	public static final String KNJIGE_ZA_IZNAJMLJIVANJE = "knjige_za_iznajmljivanje";
	public static final String CLANSKA_KARTA = "clanska_karta";
	
	@Autowired
	private ServletContext servletContext;
	private  String bURL;
	
	@Autowired
	private ApplicationMemory memorijaAplikacije;

	/** pristup ApplicationContext */
	@PostConstruct
	public void init() {
		bURL = servletContext.getContextPath()+"/";
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	// GET: clanskeKarte
	@GetMapping
	@ResponseBody
	public void index(HttpServletResponse response) throws IOException {
		//preuzimanje vrednosti iz konteksta
		Map<String, ClanskaKarta> clanskeKarte = (Map<String, ClanskaKarta>) memorijaAplikacije.get(ClanskeKarteController.CLANSKE_KARTE_KEY);

		PrintWriter out;
		out = response.getWriter();
		File htmlFile = new ClassPathResource("static/template.html").getFile();
		Document doc = Jsoup.parse(htmlFile, "UTF-8");

		Element body = doc.select("body").first();

		Element ulTag = new Element(Tag.valueOf("ul"), "");
		Element liTagKnjige = new Element(Tag.valueOf("li"), "");
		Element liTagClanskeKarte = new Element(Tag.valueOf("li"), "");
		Element aTagKnjige = new Element(Tag.valueOf("a"), "").attr("href", "/PrviMavenVebProjekat/knjige").text("Knjige");
		Element aTagClanskeKarte = new Element(Tag.valueOf("a"), "").attr("href", "/PrviMavenVebProjekat/clanskeKarte").text("Clanske karte");
		liTagKnjige.appendChild(aTagKnjige);
		liTagClanskeKarte.appendChild(aTagClanskeKarte);
		ulTag.appendChild(liTagKnjige);
		ulTag.appendChild(liTagClanskeKarte);

		Element table = new Element(Tag.valueOf("table"), "");
		Element caption = new Element(Tag.valueOf("caption"), "").text("Knjige");
		Element trZaglavlje = new Element(Tag.valueOf("tr"), "");
		Element thRedniBroj = new Element(Tag.valueOf("th"), "").text("R. br.");
		Element thRegistarskiBroj = new Element(Tag.valueOf("th"), "").text("Registarski broj");
		Element thVlasnik = new Element(Tag.valueOf("th"), "").text("Vlasnik");
		Element thDetails = new Element(Tag.valueOf("th"), "");

		trZaglavlje.appendChild(thRedniBroj);
		trZaglavlje.appendChild(thRegistarskiBroj);
		trZaglavlje.appendChild(thVlasnik);
		trZaglavlje.appendChild(thDetails);

		table.appendChild(caption);
		table.appendChild(trZaglavlje);


		ArrayList<ClanskaKarta> listaClanskihKarata = new ArrayList<ClanskaKarta>(clanskeKarte.values());
		for (int i=0; i < listaClanskihKarata.size(); i++) {
			Element trClanskihKarata = new Element(Tag.valueOf("tr"), "");

			Element tdRedniBroj = new Element(Tag.valueOf("td"), "").text(String.valueOf(i + 1));
			Element tdRegistarskiBroj = new Element(Tag.valueOf("td"), "").text(listaClanskihKarata.get(i).getRegistarskiBroj());
			Element tdVlasnik = new Element(Tag.valueOf("td"), "").text(listaClanskihKarata.get(i).getKorisnik().toString());
			Element tdDetails = new Element(Tag.valueOf("td"), "");
			Element aDetails = new Element(Tag.valueOf("a"), "").attr("href","clanskeKarte/details?registarskiBroj="+listaClanskihKarata.get(i).getRegistarskiBroj()).text("Vidi detalje");

			tdDetails.appendChild(aDetails);
			trClanskihKarata.appendChild(tdRedniBroj);
			trClanskihKarata.appendChild(tdRegistarskiBroj);
			trClanskihKarata.appendChild(tdVlasnik);
			trClanskihKarata.appendChild(tdDetails);

			table.appendChild(trClanskihKarata);
		}

		Element ulTagDodajClanskuKartu = new Element(Tag.valueOf("ul"), "");
		Element liTagDodajClanskuKartu = new Element(Tag.valueOf("li"), "");
		Element aTagDodajClanskuKartu = new Element(Tag.valueOf("a"), "").attr("href", "clanskeKarte/add").text("Dodaj novu clansku kartu");
		liTagDodajClanskuKartu.appendChild(aTagDodajClanskuKartu);
		ulTagDodajClanskuKartu.appendChild(liTagDodajClanskuKartu);

		body.appendChild(ulTag);
		body.appendChild(table);
		body.appendChild(ulTagDodajClanskuKartu);

		out.write(doc.html());
		return;
	}

	// GET: clanskeKarte/dodaj
	@GetMapping(value="/add")
	@ResponseBody
	public void create(HttpServletResponse response) throws IOException {
		Korisnici korisnici = (Korisnici) memorijaAplikacije.get(ClanskeKarteController.KORISNICI_KEY);

		PrintWriter out;
		out = response.getWriter();
		File htmlFile = new ClassPathResource("static/template.html").getFile();
		Document doc = Jsoup.parse(htmlFile, "UTF-8");

		Element body = doc.select("body").first();

		Element ulTag = new Element(Tag.valueOf("ul"), "");
		Element liTagKnjige = new Element(Tag.valueOf("li"), "");
		Element liTagClanskeKarte = new Element(Tag.valueOf("li"), "");
		Element aTagKnjige = new Element(Tag.valueOf("a"), "").attr("href", "/PrviMavenVebProjekat/knjige").text("Knjige");
		Element aTagClanskeKarte = new Element(Tag.valueOf("a"), "").attr("href", "/PrviMavenVebProjekat/clanskeKarte").text("Clanske karte");
		liTagKnjige.appendChild(aTagKnjige);
		liTagClanskeKarte.appendChild(aTagClanskeKarte);
		ulTag.appendChild(liTagKnjige);
		ulTag.appendChild(liTagClanskeKarte);

		Element form = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "add");
		Element table = new Element(Tag.valueOf("table"), "");
		Element caption = new Element(Tag.valueOf("caption"), "").text("Clanska karta");

		Element trRegistarskiBroj = new Element(Tag.valueOf("tr"), "");
		Element thRegistarskiBroj = new Element(Tag.valueOf("th"), "").text("Registarski broj:");
		Element tdRegistarskiBroj = new Element(Tag.valueOf("td"), "");
		Element inputRegistarskiBroj = new Element(Tag.valueOf("input"), "").attr("type", "text").attr("name", "registarskiBroj");

		tdRegistarskiBroj.appendChild(inputRegistarskiBroj);
		trRegistarskiBroj.appendChild(thRegistarskiBroj);
		trRegistarskiBroj.appendChild(tdRegistarskiBroj);

		Element trKorisnik = new Element(Tag.valueOf("tr"), "");
		Element thKorisnik = new Element(Tag.valueOf("th"), "").text("Korisnik:");
		Element tdKorisnik = new Element(Tag.valueOf("td"), "");
		Element selectKorisnik = new Element(Tag.valueOf("select"), "").attr("name", "idKorisnika");

		StringBuilder retVal = new StringBuilder();
		for (Korisnik k : korisnici.findAll()) {
			Element optionKorisnik = new Element(Tag.valueOf("option"), "").attr("value", String.valueOf(k.getId())).text(k.getIme() + " " + k.getPrezime() + '(' + k.getEmail() + ')');
			selectKorisnik.appendChild(optionKorisnik);
		}

		tdKorisnik.appendChild(selectKorisnik);
		trKorisnik.appendChild(thKorisnik);
		trKorisnik.appendChild(tdKorisnik);

		Element trSubmit = new Element(Tag.valueOf("tr"), "");
		Element thSubmit = new Element(Tag.valueOf("th"), "");
		Element tdSubmit = new Element(Tag.valueOf("td"), "");
		Element inputSubmit = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Dodaj");

		tdSubmit.appendChild(inputSubmit);
		trSubmit.appendChild(thSubmit);
		trSubmit.appendChild(tdSubmit);

		table.appendChild(caption);
		table.appendChild(trRegistarskiBroj);
		table.appendChild(trKorisnik);
		table.appendChild(trSubmit);
		form.appendChild(table);

		body.appendChild(ulTag);
		body.appendChild(form);
		out.write(doc.html());
		return;
	}

	/** obrada podataka forme za unos novog entiteta, post zahtev */
	// POST: clanskeKarte/add
	@PostMapping(value="/add")
	public void create(@RequestParam String registarskiBroj, @RequestParam Long idKorisnika, HttpServletResponse response) throws IOException {
		Map<String, ClanskaKarta> clanskeKarte = (Map<String, ClanskaKarta>) memorijaAplikacije.get(ClanskeKarteController.CLANSKE_KARTE_KEY);
		Korisnici korisnici = (Korisnici) memorijaAplikacije.get(ClanskeKarteController.KORISNICI_KEY);

		Korisnik korisnik = korisnici.findOne(idKorisnika);
		ClanskaKarta clanskaKarta = new ClanskaKarta(registarskiBroj, korisnik);
		ClanskaKarta saved = clanskeKarte.put(clanskaKarta.getRegistarskiBroj(), clanskaKarta);
		response.sendRedirect(bURL+"clanskeKarte");
	}

	// POST: clanskeKarte/izdajKnjigu -> izdajKnjige sve sa sesije
	@PostMapping(value="/izdajKnjige")
	public void izdajKnjigu(@RequestParam String registarskiBroj, HttpSession session, HttpServletResponse response) throws IOException {
		Map<String, ClanskaKarta> clanskeKarte = (Map<String, ClanskaKarta>) memorijaAplikacije.get(ClanskeKarteController.CLANSKE_KARTE_KEY);
		ClanskaKarta ck = clanskeKarte.get(registarskiBroj);

		List<Knjiga> zaIznajmljivanje = (List<Knjiga>) session.getAttribute(ClanskeKarteController.KNJIGE_ZA_IZNAJMLJIVANJE);

		Knjige knjige = (Knjige) memorijaAplikacije.get(KnjigeController.KNJIGE_KEY);

		for (Knjiga k : zaIznajmljivanje) {
			if (ck != null) {
				ck.getIznajmljenjeKnjige().add(k);
				k.setIzdata(true);
				knjige.save(k);
			}
		}

		session.setAttribute(ClanskeKarteController.KNJIGE_ZA_IZNAJMLJIVANJE, new ArrayList<Knjiga>());

		response.sendRedirect(bURL+"knjige");
	}

	// GET: clanskeKarte/details?id=1
	@GetMapping(value="/details")
	@ResponseBody
	public void details(@RequestParam String registarskiBroj, HttpServletResponse response) throws IOException {
		Map<String, ClanskaKarta> clanskeKarte = (Map<String, ClanskaKarta>) memorijaAplikacije.get(ClanskeKarteController.CLANSKE_KARTE_KEY);
		ClanskaKarta ck = clanskeKarte.get(registarskiBroj);

		PrintWriter out;
		out = response.getWriter();
		File htmlFile = new ClassPathResource("static/template.html").getFile();
		Document doc = Jsoup.parse(htmlFile, "UTF-8");

		Element body = doc.select("body").first();

		Element ulTag = new Element(Tag.valueOf("ul"), "");
		Element liTagKnjige = new Element(Tag.valueOf("li"), "");
		Element liTagClanskeKarte = new Element(Tag.valueOf("li"), "");
		Element aTagKnjige = new Element(Tag.valueOf("a"), "").attr("href", "/PrviMavenVebProjekat/knjige").text("Knjige");
		Element aTagClanskeKarte = new Element(Tag.valueOf("a"), "").attr("href", "/PrviMavenVebProjekat/clanskeKarte").text("Clanske karte");
		liTagKnjige.appendChild(aTagKnjige);
		liTagClanskeKarte.appendChild(aTagClanskeKarte);
		ulTag.appendChild(liTagKnjige);
		ulTag.appendChild(liTagClanskeKarte);

		Element table = new Element(Tag.valueOf("table"), "");
		Element caption = new Element(Tag.valueOf("caption"), "").text("Clanska karta");

		Element trRegistarskiBroj = new Element(Tag.valueOf("tr"), "");
		Element thRegistarskiBroj = new Element(Tag.valueOf("th"), "").text("Registarski broj:");
		Element tdRegistarskiBroj = new Element(Tag.valueOf("td"), "").text(ck.getRegistarskiBroj());

		trRegistarskiBroj.appendChild(thRegistarskiBroj);
		trRegistarskiBroj.appendChild(tdRegistarskiBroj);

		Element trVlasnik = new Element(Tag.valueOf("tr"), "");
		Element thVlasnik = new Element(Tag.valueOf("th"), "").text("Vlasnik:");
		Element tdVlasnik = new Element(Tag.valueOf("td"), "").text(ck.getKorisnik().getIme() + " " + ck.getKorisnik().getPrezime());

		trVlasnik.appendChild(thVlasnik);
		trVlasnik.appendChild(tdVlasnik);

		table.appendChild(caption);
		table.appendChild(trRegistarskiBroj);
		table.appendChild(trVlasnik);

		Element tableKnjige = new Element(Tag.valueOf("table"), "");
		Element captionKnjige = new Element(Tag.valueOf("caption"), "").text("Iznajmljene knjige");
		Element trZaglavlje = new Element(Tag.valueOf("tr"), "");
		Element thNaziv = new Element(Tag.valueOf("th"), "").text("Naziv");
		Element thRBPrimerka = new Element(Tag.valueOf("th"), "").text("Registarski broj primerka");
		Element thJezik = new Element(Tag.valueOf("th"), "").text("Jezik");
		Element thBrStranica = new Element(Tag.valueOf("th"), "").text("Broj stranica");
		Element thForm = new Element(Tag.valueOf("th"), "").text("");

		trZaglavlje.appendChild(thNaziv);
		trZaglavlje.appendChild(thRBPrimerka);
		trZaglavlje.appendChild(thJezik);
		trZaglavlje.appendChild(thBrStranica);
		trZaglavlje.appendChild(thForm);

		tableKnjige.appendChild(captionKnjige);
		tableKnjige.appendChild(trZaglavlje);

		for (Knjiga k : ck.getIznajmljenjeKnjige()) {
			Element trKnjiga = new Element(Tag.valueOf("tr"), "");
			Element tdNaziv = new Element(Tag.valueOf("td"), "").text(k.getNaziv());
			Element tdRBPrimerka = new Element(Tag.valueOf("td"), "").text(k.getRegistarskiBrojPrimerka());
			Element tdJezik = new Element(Tag.valueOf("td"), "").text(k.getJezik());
			Element tdBrStranica = new Element(Tag.valueOf("td"), "").text(String.valueOf(k.getBrojStranica()));

			trKnjiga.appendChild(tdNaziv);
			trKnjiga.appendChild(tdRBPrimerka);
			trKnjiga.appendChild(tdJezik);
			trKnjiga.appendChild(tdBrStranica);

			if (k.isIzdata()) {
				Element tdForm = new Element(Tag.valueOf("td"), "");
				Element form = new Element(Tag.valueOf("form"), "").attr("method", "post").attr("action", "vratiKnjigu");
				Element inputHiddenId = new Element(Tag.valueOf("input"), "").attr("type", "hidden").attr("name", "idKnjige").attr("value", String.valueOf(k.getId()));
				Element inputHiddenRegistarskiBroj = new Element(Tag.valueOf("input"), "").attr("type", "hidden").attr("name", "registarskiBroj").attr("value", String.valueOf(ck.getRegistarskiBroj()));
				Element inputSubmit = new Element(Tag.valueOf("input"), "").attr("type", "submit").attr("value", "Vrati knjigu");
				form.appendChild(inputHiddenId);
				form.appendChild(inputHiddenRegistarskiBroj);
				form.appendChild(inputSubmit);
				tdForm.appendChild(form);
				trKnjiga.appendChild(tdForm);
			}
			tableKnjige.appendChild(trKnjiga);
		}

		body.appendChild(table);
		body.appendChild(tableKnjige);

		out.write(doc.html());
		return;
	}

	// POST: clanskeKarte/vratiKnjigu
	@PostMapping(value="/vratiKnjigu")
	public void vratiKnjigu(@RequestParam Long idKnjige, @RequestParam String registarskiBroj, HttpServletResponse response) throws IOException {
		Map<String, ClanskaKarta> clanskeKarte = (Map<String, ClanskaKarta>) memorijaAplikacije.get(ClanskeKarteController.CLANSKE_KARTE_KEY);
		ClanskaKarta ck = clanskeKarte.get(registarskiBroj);

		Knjige knjige = (Knjige) memorijaAplikacije.get(KnjigeController.KNJIGE_KEY);

		if (ck != null) {
			Knjiga knjiga = knjige.findOne(idKnjige);
			ck.getIznajmljenjeKnjige().remove(knjiga);
			knjiga.setIzdata(false);
			knjige.save(knjiga);
		}

		response.sendRedirect(bURL+"knjige");
	}

}
