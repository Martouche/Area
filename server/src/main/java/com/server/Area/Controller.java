package com.server.Area;

import java.util.concurrent.atomic.AtomicLong;
import java.sql.*;
import java.io.*;
import java.lang.*;
import java.security.Principal;
import com.server.Area.User;

import java.util.Random;


import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


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

	Twitter twitter = TwitterFactory.getSingleton();
	AccessToken accessTokenTwitter = null;
	RequestToken requestToken = null;
	BufferedReader br = null;


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
					"INSERT INTO services (id, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 'Linkdedin' WHERE NOT EXISTS (SELECT * FROM services where name='Linkdedin');" +
					"INSERT INTO services (id, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 'Discord' WHERE NOT EXISTS (SELECT * FROM services where name='Discord');" +
					"INSERT INTO services (id, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 'Facebook' WHERE NOT EXISTS (SELECT * FROM services where name='Facebook');" +
					"INSERT INTO services (id, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 'Reddit' WHERE NOT EXISTS (SELECT * FROM services where name='Reddit');" +
					"INSERT INTO services (id, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 'Twitter' WHERE NOT EXISTS (SELECT * FROM services where name='Twitter');");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Table User token Service
		try {
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS user_service_token (id_user VARCHAR(250), " +
					"google_token VARCHAR(250), github_token VARCHAR(250), linkedin_token VARCHAR(250), " +
					"spotify_token VARCHAR(250), discord_token VARCHAR(250), facebook_token VARCHAR(250), " +
					"reddit_token VARCHAR(250), twitter_token VARCHAR(250));");
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

	@RequestMapping(value = "/oauth2/callback/discord", method = RequestMethod.GET)
	public RedirectView getTokenDiscord(@RequestParam(value = "code") String code) {
		System.out.println("mon code Discord = " + code);
		DiscordController mine = new DiscordController(code, c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}

	@RequestMapping(value = "/oauth2/callback/reddit", method = RequestMethod.GET)
	public RedirectView getTokenReddit(@RequestParam(value = "code") String code) {
		System.out.println("mon code reddit = " + code);
		RedditController mine = new RedditController(code, c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}


	@RequestMapping(value = "/oauth2/callback/facebook", method = RequestMethod.GET)
	public RedirectView getTokenFacebook(@RequestParam(value = "code") String code) {
		System.out.println("mon code Facebook = " + code);
		FacebookController mine = new FacebookController(code, c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}

	@RequestMapping(value = "/oauth2/callback/twitter", method = RequestMethod.GET)
	public RedirectView getTokenTwitter(@RequestParam(value = "oauth_token") String oauth_token, @RequestParam(value = "oauth_verifier") String oauth_verifier) {
		try{
			if(oauth_token.length() > 0){
				this.accessTokenTwitter = this.twitter.getOAuthAccessToken(requestToken, oauth_verifier);
			}else{
				this.accessTokenTwitter = this.twitter.getOAuthAccessToken();
			}
		} catch (TwitterException te) {
			if(401 == te.getStatusCode()){
				System.out.println("Unable to get the access token.");
			}else{
				te.printStackTrace();
			}
		}
		System.out.println("acces token twitter " + this.accessTokenTwitter);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}

	@RequestMapping(value = "/oauth2/autorize/twitter", method = RequestMethod.GET)
	public RedirectView getTokenTwitter() throws Exception {
		BufferedReader br = null;
		String clientId = "RyDqv5K1O7VcivZjVUY7oppsS";
		String clientSecret = "kEJUgA7vzCmtpydZ13bO2WgY2FcBnAwqMl27E0jo1edBiMIHHZ";
		twitter.setOAuthConsumer(clientId, clientSecret);
		requestToken = twitter.getOAuthRequestToken();
		br = new BufferedReader(new InputStreamReader(System.in));
		RedirectView redirectView = new RedirectView(requestToken.getAuthorizationURL());
		return redirectView;
	}

	@RequestMapping(value = "/oauth2/callback/linkedin", method = RequestMethod.GET)
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