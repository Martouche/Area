package com.server.Area;

import java.util.concurrent.atomic.AtomicLong;
import java.sql.*;
import java.io.*;
import java.lang.*;
import java.security.Principal;
import com.server.Area.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.servlet.view.RedirectView;
import io.swagger.annotations.Api;


String apikeyTrello = "c050bd57b4f0d1ddec3231d30de0c2b2";

@RestController
@Api(value="Authentification", description="Routes for login & register")
public class Controller {
	Connection c = null;
	PreparedStatement stmt = null;
	boolean isLogged = false;

	public Controller() {
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager
					.getConnection("jdbc:postgresql://db:5432/" + System.getenv("POSTGRES_DB"),
							System.getenv("POSTGRES_USER"), System.getenv("POSTGRES_PASSWORD"));
			stmt = c.prepareStatement("DROP TABLE users;CREATE TABLE users (id SERIAL PRIMARY KEY, name VARCHAR(250) NOT NULL, password VARCHAR(250) NOT NULL, type VARCHAR(250) NOT NULL);");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public RegisterController registerPost(@RequestParam(value = "name") String name, @RequestParam(value = "pwd") String pwd) {
		return new RegisterController(name, pwd, c, stmt);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public LoginController loginPost(@RequestParam(value = "name") String name, @RequestParam(value = "pwd") String pwd) {
		return new LoginController(name, pwd, c, stmt);
	}

	@RequestMapping(value = "/oauth2/github", method = RequestMethod.GET)
	public RedirectView getTokenGitHub(@RequestParam(value = "code") String code) {
		GitHubController mine = new GitHubController(code, c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=mabite");
		return redirectView;
	}

	@RequestMapping(value = "/oauth2/google", method = RequestMethod.GET)
	public RedirectView getTokenGoogle(@RequestParam(value = "code") String code) {
		GoogleController mine = new GoogleController(code, c, stmt);
		System.out.println("mon putain d'id = " + mine.getId());
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=mabite");
		return redirectView;
	}

	@RequestMapping(value = "/getId", method = RequestMethod.GET)
	public String GetName(@RequestParam(value = "email") String email) {
		stmt = c.prepareStatement("SELECT id FROM users WHERE email = '" + email + "' ;");
		ResultSet rs = stmt.executeQuery();
		String id = null;
		while (rs.next()) {
			id = rs.getString("text");
		}
		rs.close();
		return id;
	}

	@RequestMapping(value = "/getEmail", method = RequestMethod.GET)
	public String GetName(@RequestParam(value = "id") String id) {
		stmt = c.prepareStatement("SELECT email FROM users WHERE id = '" + id +"' ;");
		ResultSet rs = stmt.executeQuery();
		String name = null;
		while (rs.next() ) {
			name = rs.getString("text");
		}
		rs.close();
		return name;
	}
}