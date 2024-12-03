package com.ftn.PrviMavenVebProjekat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PrviMavenVebProjekatApplication extends SpringBootServletInitializer {
	  
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PrviMavenVebProjekatApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(PrviMavenVebProjekatApplication.class, args);
	}
}


