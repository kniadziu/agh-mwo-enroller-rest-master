package com.company.enroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {   		
//    	
//    	Uruchomienie aplikacji na innym porcie niż 8080:
//
//    		App.java/main
//    		 SpringApplication app = new SpringApplication(App.class);      app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
//    		app.run(args); 

        SpringApplication.run(App.class, args);
    }
}