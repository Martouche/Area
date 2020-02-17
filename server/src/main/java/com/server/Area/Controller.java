package com.server.Area;

import java.util.concurrent.atomic.AtomicLong;
import java.sql.*;
import java.io.*;
import java.lang.*;
import java.security.Principal;
import com.server.Area.User;

import java.util.Random;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.servlet.view.RedirectView;
import io.swagger.annotations.Api;


@RestController
@Api(value="Authentification", description="Routes for login & register")
public class Controller {
	Connection c = null;
	PreparedStatement stmt = null;
	boolean isLogged = false;
	int id = 0;

	public Controller() {
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager
					.getConnection("jdbc:postgresql://db:5432/" + System.getenv("POSTGRES_DB"),
							System.getenv("POSTGRES_USER"), System.getenv("POSTGRES_PASSWORD"));
			CreateTableDataBase(c, stmt);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}

	public void CreateTableDataBase(Connection c, PreparedStatement stmt) {
		// Table users
		try {
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(250) NOT NULL, password VARCHAR(250) NULL, type VARCHAR(250) NOT NULL);");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Table Service
		Random rand = new Random();
		try {
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS services (id INT NOT NULL, name VARCHAR(250) NOT NULL);" +
					"INSERT INTO services (id, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 'Google' WHERE NOT EXISTS (SELECT * FROM services where name='Google');" +
					"INSERT INTO services (id, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 'Spotify' WHERE NOT EXISTS (SELECT * FROM services where name='Spotify');" +
					"INSERT INTO services (id, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 'Github' WHERE NOT EXISTS (SELECT * FROM services where name='Github');" +
					"INSERT INTO services (id, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 'Linkdedin' WHERE NOT EXISTS (SELECT * FROM services where name='Linkdedin');");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Table User token Service
		try {
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS user_service_token (id_user VARCHAR(250), google_token VARCHAR(250), github_token VARCHAR(250), linkedin_token VARCHAR(250), spotify_token VARCHAR(250));");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public RedirectView logoutUser() {
		isLogged = false;
		id = 0;
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/login");
		return redirectView;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public RedirectView registerPost(@RequestParam(value = "name") String name, @RequestParam(value = "pwd") String pwd) {
		RegisterController mine = new RegisterController(name, pwd, c, stmt);
		if (!isLogged) {
			id = mine.id;
			isLogged = true;
		}
		RedirectView redirectView = new RedirectView();
		if (mine.state == 1)
			redirectView.setUrl("http://localhost:9090/signup?value=error1");
		else
			redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public RedirectView loginPost(@RequestParam(value = "name") String name, @RequestParam(value = "pwd") String pwd) {
		LoginController mine = new LoginController(name, pwd, c, stmt);
		if (!isLogged) {
			id = mine.id;
			isLogged = true;
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}

	@RequestMapping(value = "/oauth2/linkedin", method = RequestMethod.GET)
	public RedirectView getTokenLinkedin(@RequestParam(value = "code") String code) {
		System.out.println("mon code linkedin = " + code);
		LinkedinController mine = new LinkedinController(code, c, stmt);
		if (!isLogged) {
			id = mine.id;
			isLogged = true;
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}

	@RequestMapping(value = "/oauth2/spotify", method = RequestMethod.GET)
	public RedirectView getTokenSpotify(@RequestParam(value = "code") String code) {
		SpotifyController mine = new SpotifyController(code, id, c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}

	@RequestMapping(value = "/oauth2/github", method = RequestMethod.GET)
	public RedirectView getTokenGitHub(@RequestParam(value = "code") String code) {
		GitHubController mine = new GitHubController(code, c, stmt);
		if (!isLogged) {
			id = mine.id;
			isLogged = true;
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}

	@RequestMapping(value = "/oauth2/google", method = RequestMethod.GET)
	public RedirectView getTokenGoogle(@RequestParam(value = "code") String code) {
		GoogleController mine = new GoogleController(code, c, stmt);
		if (!isLogged) {
			id = mine.id;
			isLogged = true;
		}
		System.out.println("mon putain d'id = " + mine.getId());
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}

	@RequestMapping(value = "/getId", method = RequestMethod.GET)
	public String GetId(@RequestParam(value = "email") String email) {
		String id = null;
		try {
			stmt = c.prepareStatement("SELECT id FROM users WHERE email = '" + email + "' ;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				id = rs.getString("text");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return id;
	}

	@RequestMapping(value = "/getEmail", method = RequestMethod.GET)
	public String GetEmail(@RequestParam(value = "id") String id) {
		String email = null;
		try {
			stmt = c.prepareStatement("SELECT email FROM users WHERE id = '" + id + "' ;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				email = rs.getString("text");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return email;
	}
}