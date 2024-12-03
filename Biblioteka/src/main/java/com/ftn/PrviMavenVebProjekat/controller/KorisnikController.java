package com.ftn.PrviMavenVebProjekat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftn.PrviMavenVebProjekat.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.PrviMavenVebProjekat.model.Korisnici;
import com.ftn.PrviMavenVebProjekat.model.Korisnik;

@Controller
@RequestMapping(value = "/korisnici")
public class KorisnikController {
	
	public static final String KORISNIK_KEY = "korisnik";
	
	@Autowired
	private ApplicationMemory memorijaAplikacije;
	
	@Autowired
	private ServletContext servletContext;
	
	private  String bURL; 
	
	/** inicijalizacija podataka za kontroler */
	@PostConstruct
	public void init() {	
		//Specify the base URL for all relative URLs in a document
		bURL = servletContext.getContextPath()+"/";			
	}

	@GetMapping(value = "/login")
	public void getLogin(@RequestParam(required = false) String email, @RequestParam(required = false) String sifra,
			HttpSession session, HttpServletResponse response) throws IOException {
		postLogin(email, sifra, session, response);
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/login")
	@ResponseBody
	public void postLogin(@RequestParam(required = false) String email, @RequestParam(required = false) String sifra,
			HttpSession session, HttpServletResponse response) throws IOException {

		Korisnici korisnici = (Korisnici) memorijaAplikacije.get(ClanskeKarteController.KORISNICI_KEY);
		if (korisnici != null) {
			if (korisnici.findAll().size() == 0) {
				korisnici = new Korisnici();
				memorijaAplikacije.put(ClanskeKarteController.KORISNICI_KEY, korisnici);
			}
		}
		
		
		Korisnik korisnik = korisnici.findOneByEmail(email);
		String greska = "";
		if (korisnik == null)
			greska = "neispravno korisničko ime<br/>";
		else if (!korisnik.getLozinka().equals(sifra))
			greska = "neispravna korisnika šifra<br/>";

		if (!greska.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();

			StringBuilder retVal = new StringBuilder();
			retVal.append("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\"/PrviMavenVebProjekat/\">	\r\n" + "	<title>Prijava korisnika</title>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviForma.css\"/>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "</head>\r\n" + "<body>\r\n" + "	<ul>\r\n"
					+ "		<li><a href=\"registracija.html\">Registruj se</a></li>\r\n" + "	</ul>\r\n");
			if (!greska.equals(""))
				retVal.append("	<div>" + greska + "</div>\r\n");
			retVal.append("	<form method=\"post\" action=\"korisnici/login\">\r\n" + "		<table>\r\n"
					+ "			<caption>Prijava korisnika na sistem</caption>\r\n"
					+ "			<tr><th>Email:</th><td><input type=\"text\" value=\"\" name=\"email\" required/></td></tr>\r\n"
					+ "			<tr><th>Šifra:</th><td><input type=\"password\" value=\"\" name=\"sifra\" required/></td></tr>\r\n"
					+ "			<tr><th></th><td><input type=\"submit\" value=\"Prijavi se\" /></td>\r\n"
					+ "		</table>\r\n" + "	</form>\r\n" + "	<br/>\r\n" + "</body>\r\n" + "</html>");

			out.write(retVal.toString());
			return;
		}

		if (session.getAttribute(KORISNIK_KEY) != null)
			greska = "korisnik je već prijavljen na sistem morate se prethodno odjaviti<br/>";

		if (!greska.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();

			StringBuilder retVal = new StringBuilder();
			retVal.append("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\"/PrviiMavenVebProjekat/\">	\r\n" + "	<title>Prijava korisnika</title>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviForma.css\"/>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "</head>\r\n" + "<body>\r\n" + "	<ul>\r\n"
					+ "		<li><a href=\"registracija.html\">Registruj se</a></li>\r\n" + "	</ul>\r\n");
			if (!greska.equals(""))
				retVal.append("	<div>" + greska + "</div>\r\n");
			retVal.append("	<a href=\"index.html\">Povratak</a>\r\n" + "	<br/>\r\n" + "</body>\r\n" + "</html>");

			out.write(retVal.toString());
			return;
		}

		session.setAttribute(KORISNIK_KEY, korisnik);

		response.sendRedirect(bURL + "knjige");
	}
	
	@GetMapping(value="/logout")
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {	

		Korisnik korisnik = (Korisnik) request.getSession().getAttribute(KORISNIK_KEY);
		String greska = "";
		if(korisnik==null)
			greska="korisnik nije prijavljen<br/>";
		
		if(!greska.equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out;	
			out = response.getWriter();
			
			StringBuilder retVal = new StringBuilder();
			retVal.append(
					"<!DOCTYPE html>\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" +
					"	<meta charset=\"UTF-8\">\r\n" + 
					"	<base href=\"/PrviMavenVebProjekat/\">	\r\n" + 
					"	<title>Prijava korisnika</title>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviForma.css\"/>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n" + 
					"</head>\r\n" + 
					"<body>\r\n" + 
					"	<ul>\r\n" + 
					"		<li><a href=\"registracija.html\">Registruj se</a></li>\r\n" + 
					"	</ul>\r\n");
			if(!greska.equals(""))
				retVal.append(
					"	<div>"+greska+"</div>\r\n");
			retVal.append(
					"	<form method=\"post\" action=\"PrijavaOdjava/Login\">\r\n" + 
					"		<table>\r\n" + 
					"			<caption>Prijava korisnika na sistem</caption>\r\n" + 
					"			<tr><th>Email:</th><td><input type=\"text\" value=\"\" name=\"email\" required/></td></tr>\r\n" + 
					"			<tr><th>Šifra:</th><td><input type=\"password\" value=\"\" name=\"sifra\" required/></td></tr>\r\n" + 
					"			<tr><th></th><td><input type=\"submit\" value=\"Prijavi se\" /></td>\r\n" + 
					"		</table>\r\n" + 
					"	</form>\r\n" + 
					"	<br/>\r\n" + 
					"	<ul>\r\n" + 
					"		<li><a href=\"korisnici/logout\">Odjavi se</a></li>\r\n" + 
					"	</ul>" +
					"</body>\r\n" + 
					"</html>");
			
			out.write(retVal.toString());
			return;
		}
		
		
		request.getSession().removeAttribute(KORISNIK_KEY);
		request.getSession().invalidate();
		response.sendRedirect(bURL+"korisnici/login");
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/registracija")
	public void registracija(@RequestParam(required = true) String email, @RequestParam(required = true) String sifra,
			@RequestParam(required = true) String ime, @RequestParam(required = true) String prezime,
			HttpSession session, HttpServletResponse response) throws IOException {

		Korisnici korisnici = (Korisnici) memorijaAplikacije.get(ClanskeKarteController.KORISNICI_KEY);
		if (korisnici != null) {
			if (korisnici.findAll().size() == 0) {
				korisnici = new Korisnici();
				memorijaAplikacije.put(ClanskeKarteController.KORISNICI_KEY, korisnici);
			}
		}
		
		Korisnik korisnik = new Korisnik(ime, prezime, email, sifra);
		korisnici.add(korisnik);
		
		response.sendRedirect(bURL + "korisnici");
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping
	@ResponseBody
	public String getKorisnici(HttpSession session, HttpServletResponse response) throws IOException {
		Korisnici korisnici = (Korisnici) memorijaAplikacije.get(ClanskeKarteController.KORISNICI_KEY);
				
		StringBuilder retVal = new StringBuilder();

		List<Korisnik> korisniciLista = korisnici.findAll();
		if(korisniciLista.size() == 0) {
			// nema korisnika TODO doraditi za domaci neku poruku i redirekt
		} else {
			retVal.append(
					"<!DOCTYPE html>\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" + 
					"	<meta charset=\"UTF-8\">\r\n" + 
		    		"	<base href=\""+bURL+"\">" + 
					"	<title>Clanske karte</title>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" + 
					"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"+
					"</head>\r\n" + 
					"<body> "+
					"	<ul>\r\n" + 
					"		<li><a href=\"knjige\">Knjige</a></li>\r\n" + 
					"		<li><a href=\"clanskeKarte\">Clanske karte</a></li>\r\n" + 
					"		<li><a href=\"korisnici\">Korisnici</a></li>\r\n" + 
					"	</ul>\r\n" + 
					"		<table>\r\n" + 
					"			<caption>Korisnici</caption>\r\n" + 
					"			<tr>\r\n" + 
					"				<th>Ime</th>\r\n" + 
					"				<th>Prezime</th>\r\n" + 
					"				<th>Email</th>\r\n" +
					"				<th></th>\r\n" +
					"			</tr>\r\n");
			
			for (Korisnik k: korisniciLista) {
				retVal.append(
					"			<tr>\r\n" + 
					"				<td>"+ k.getIme() +"</td>\r\n" + 
					"				<td>"+ k.getPrezime() +"</td>\r\n" +
					"				<td>"+ k.getEmail() +"</td>\r\n" +
					"				<td>" + 
									"	<form method=\"post\" action=\"korisnici/obrisi\">\r\n" + 
									"		<input type=\"hidden\" name=\"id\" value=\""+k.getId()+"\">\r\n" + 
									"		<input type=\"submit\" value=\"Obrisi\">\r\n" + 
									"	</form>\r\n" +
					"				</td>\r\n" +
					"			</tr>\r\n");
			}
			retVal.append("</table>\r\n");
			
			retVal.append(
					"</body>\r\n"+
					"</html>\r\n");		
		}
		
		return retVal.toString();
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value="/obrisi")
	public void obrisiKorisnika(@RequestParam Long id, HttpServletResponse response) throws IOException {		
		Korisnici korisnici = (Korisnici) memorijaAplikacije.get(ClanskeKarteController.KORISNICI_KEY);

		if (korisnici != null) {
			if (korisnici.findAll().size() == 0) {
				korisnici = new Korisnici();
				memorijaAplikacije.put(ClanskeKarteController.KORISNICI_KEY, korisnici);
			}
		}
		
		korisnici.delete(id);

		response.sendRedirect(bURL+"korisnici");
	}

}
