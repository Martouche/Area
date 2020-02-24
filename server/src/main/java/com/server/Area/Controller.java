package com.server.Area;

import java.util.concurrent.atomic.AtomicLong;
import java.sql.*;
import java.io.*;
import java.lang.*;
import java.security.Principal;
import com.server.Area.User;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



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
		Random rand = new Random();
		String GoogleId = Integer.toString(rand.nextInt(1000));
		String SpotifyId = Integer.toString(rand.nextInt(1000));
		String GithubId = Integer.toString(rand.nextInt(1000));
		String LinkedinId = Integer.toString(rand.nextInt(1000));
		String DiscordId = Integer.toString(rand.nextInt(1000));
		String FacebookId = Integer.toString(rand.nextInt(1000));
		String RedditId = Integer.toString(rand.nextInt(1000));
		String TwitterId = Integer.toString(rand.nextInt(1000));
		// Table Service
		try {
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS services (id INT NOT NULL, name VARCHAR(250) NOT NULL);" +
					"INSERT INTO services (id, name) SELECT " + GoogleId + ", 'google' WHERE NOT EXISTS (SELECT * FROM services where name='google');" +
					"INSERT INTO services (id, name) SELECT " + SpotifyId + ", 'spotify' WHERE NOT EXISTS (SELECT * FROM services where name='spotify');" +
					"INSERT INTO services (id, name) SELECT " + GithubId + ", 'github' WHERE NOT EXISTS (SELECT * FROM services where name='github');" +
					"INSERT INTO services (id, name) SELECT " + LinkedinId + ", 'linkdedin' WHERE NOT EXISTS (SELECT * FROM services where name='linkdedin');" +
					"INSERT INTO services (id, name) SELECT " + DiscordId + ", 'discord' WHERE NOT EXISTS (SELECT * FROM services where name='discord');" +
					"INSERT INTO services (id, name) SELECT " + FacebookId + ", 'facebook' WHERE NOT EXISTS (SELECT * FROM services where name='facebook');" +
					"INSERT INTO services (id, name) SELECT " + RedditId + ", 'reddit' WHERE NOT EXISTS (SELECT * FROM services where name='reddit');" +
					"INSERT INTO services (id, name) SELECT " + TwitterId + ", 'twitter' WHERE NOT EXISTS (SELECT * FROM services where name='twitter');");
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
		// Table Service Action
		try {
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS services_actions (id INT NOT NULL, id_service INT NOT NULL, name VARCHAR(250) NOT NULL);" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'exempleActionGoogle3' WHERE NOT EXISTS (SELECT * FROM services_actions where name='exempleActionGoogle3');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'exempleActionGoogle2' WHERE NOT EXISTS (SELECT * FROM services_actions where name='exempleActionGoogle2');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'exempleActionGoogle' WHERE NOT EXISTS (SELECT * FROM services_actions where name='exempleActionGoogle');");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Table Service Reaction
		try {
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS services_reactions (id INT NOT NULL, id_service INT NOT NULL, name VARCHAR(250) NOT NULL);" +
					"INSERT INTO services_reactions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'exempleReactionGoogle' WHERE NOT EXISTS (SELECT * FROM services_reactions where name='exempleReactionGoogle');" +
					"INSERT INTO services_reactions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'exempleReactionGoogle2' WHERE NOT EXISTS (SELECT * FROM services_reactions where name='exempleReactionGoogle2');" +
					"INSERT INTO services_reactions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'exempleReactionGoogle3' WHERE NOT EXISTS (SELECT * FROM services_reactions where name='exempleReactionGoogle3');");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Table User Actions Reaction
		try {
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS user_actions_reactions (id_user INT NOT NULL, id_service_action INT NOT NULL, value_service_action VARCHAR(250) NOT NULL, id_service_reaction INT NOT NULL, value_service_reaction VARCHAR(250) NOT NULL);");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public RedirectView logoutUser() {
		System.out.println("Je suis logout dans le serveur");
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

	// Discord Routes
	@RequestMapping(value = "/oauth2/autorize/discord", method = RequestMethod.GET)
	public RedirectView getUrlAutorizeDiscord() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://discordapp.com/api/oauth2/authorize?client_id=679280369891147807&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Foauth2%2Fcallback%2Fdiscord&response_type=code&scope=identify%20email");
		return redirectView;
	}
	@RequestMapping(value = "/oauth2/callback/discord", method = RequestMethod.GET)
	public RedirectView getTokenDiscord(@RequestParam(value = "code") String code) {
		System.out.println("mon code Discord = " + code);
		DiscordController mine = new DiscordController(id, code, c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}


	// Reddit Routes
	@RequestMapping(value = "/oauth2/autorize/reddit", method = RequestMethod.GET)
	public RedirectView getUrlAutorizeReddit() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://www.reddit.com/api/v1/authorize?client_id=O8RWcER1WbCJpg&response_type=code&state=adeidhiahidlhde&redirect_uri=http://localhost:8080/oauth2/callback/reddit&duration=permanent&scope=*");
		return redirectView;
	}
	@RequestMapping(value = "/oauth2/callback/reddit", method = RequestMethod.GET)
	public RedirectView getTokenReddit(@RequestParam(value = "code") String code) {
		System.out.println("mon code reddit = " + code);
		RedditController mine = new RedditController(id, code, c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}


	// Facebook Routes
	@RequestMapping(value = "/oauth2/autorize/facebook", method = RequestMethod.GET)
	public RedirectView getUrlAutorizeFacebook() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://www.facebook.com/v6.0/dialog/oauth?client_id=208135047001196&redirect_uri=http://localhost:8080/oauth2/callback/facebook&state=st=state123abc,ds=123456789&scope=email");
		return redirectView;
	}
	@RequestMapping(value = "/oauth2/callback/facebook", method = RequestMethod.GET)
	public RedirectView getTokenFacebook(@RequestParam(value = "code") String code) {
		System.out.println("mon code Facebook = " + code);
		FacebookController mine = new FacebookController(id, code, c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}


	// Twitter Routes
	@RequestMapping(value = "/oauth2/autorize/twitter", method = RequestMethod.GET)
	public RedirectView getUrlAutorizeTwitter() throws Exception {
		BufferedReader br = null;
		String clientId = "RyDqv5K1O7VcivZjVUY7oppsS";
		String clientSecret = "kEJUgA7vzCmtpydZ13bO2WgY2FcBnAwqMl27E0jo1edBiMIHHZ";
		twitter.setOAuthConsumer(clientId, clientSecret);
		requestToken = twitter.getOAuthRequestToken();
		br = new BufferedReader(new InputStreamReader(System.in));
		RedirectView redirectView = new RedirectView(requestToken.getAuthorizationURL());
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
		TwitterController mine = new TwitterController(id, (String) this.accessTokenTwitter.toString(),  c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}


	// Linkedin Routes
	@RequestMapping(value = "/oauth2/autorize/linkedin", method = RequestMethod.GET)
	public RedirectView getUrlAutorizeLinkedin() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=86yu19zq37j60p&redirect_uri=http://localhost:8080/oauth2/callback/linkedin?&scope=r_liteprofile%20r_emailaddress%20w_member_social");
		return redirectView;
	}
	@RequestMapping(value = "/oauth2/callback/linkedin", method = RequestMethod.GET)
	public RedirectView getTokenLinkedin(@RequestParam(value = "code") String code) {
		System.out.println("mon code linkedin = " + code);
		LinkedinController mine = new LinkedinController(id, code, c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}


	// Spotify Routes
	@RequestMapping(value = "/oauth2/autorize/spotify", method = RequestMethod.GET)
	public RedirectView getUrlAutorizeSpotify() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://accounts.spotify.com/authorize?client_id=b348a012872f4fe78567e7cea9e20c7c&response_type=code&redirect_uri=http://localhost:8080/oauth2/callback/spotify&scope=user-read-private");
		return redirectView;
	}
	@RequestMapping(value = "/oauth2/callback/spotify", method = RequestMethod.GET)
	public RedirectView getTokenSpotify(@RequestParam(value = "code") String code) {
		SpotifyController mine = new SpotifyController(id, code,  c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}

	// Github Routes
	@RequestMapping(value = "/oauth2/autorize/github", method = RequestMethod.GET)
	public RedirectView getUrlAutorizeGithub() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://github.com/login/oauth/authorize?scope=user:email,repo&client_id=1b8ddffb28f26996c08f");
		return redirectView;
	}
	@RequestMapping(value = "/oauth2/callback/github", method = RequestMethod.GET)
	public RedirectView getTokenGitHub(@RequestParam(value = "code") String code) {
		GitHubController mine = new GitHubController(id, code, c, stmt);
		if (!isLogged) {
			id = mine.id;
			isLogged = true;
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}


	// Google Routes
	@RequestMapping(value = "/oauth2/autorize/google", method = RequestMethod.GET)
	public RedirectView getUrlAutorizeGoogle() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://accounts.google.com/o/oauth2/v2/auth?access_type=offline&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&response_type=code&client_id=377968007025-013sa07vehs51n1rau6qfmplp7esq964.apps.googleusercontent.com&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Foauth2%2Fcallback%2Fgoogle");
		return redirectView;
	}
	@RequestMapping(value = "/oauth2/callback/google", method = RequestMethod.GET)
	public RedirectView getTokenGoogle(@RequestParam(value = "code") String code) {
		GoogleController mine = new GoogleController(id, code, c, stmt);
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

	public List<String> getServiceIdByName(String[] nameService) {
		List<String> data = new ArrayList<String>();
		int size = nameService.length;
		for (int i=0; i<size; i++)  {
			try {
				stmt = c.prepareStatement("SELECT id FROM services where name='" + nameService[i] + "'");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					data.add(Integer.toString(rs.getInt(1)));
				}
				rs.close();
			}catch (Exception e) {
				System.out.println(e);
			}
		}
		return data;
	}

	public List<String> getNameReactionByServiceId(List<String> idServices) {
		List<String> data = new ArrayList<String>();
		for(String idservice : idServices) {
			System.out.println(idservice);
			try {
				stmt = c.prepareStatement("SELECT name FROM services_reactions WHERE id_service = '" + idservice + "'");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					data.add(rs.getString("name"));
				}
				rs.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		System.out.println("mes reactions dispo");
		for (String namereaction : data)
			System.out.println("-> " + namereaction);
		return data;
	}

	public List<String> getNameActionByServiceId(List<String> idServices) {
		List<String> data = new ArrayList<String>();
		for(String idservice : idServices) {
			System.out.println(idservice);
			try {
				stmt = c.prepareStatement("SELECT name FROM services_actions WHERE id_service = '" + idservice + "'");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					data.add(rs.getString("name"));
				}
				rs.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		System.out.println("mes actions dispo");
		for (String nameaction : data)
			System.out.println("-> " + nameaction);
		return data;
	}

	public String[] getServiceByUser(String userId) {
		String[] data = new String[8];
		try {
			stmt = c.prepareStatement("SELECT * FROM user_service_token WHERE id_user = '" + userId + "'");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				data[0] = (rs.getString(2) == null) ? null : "google";
				data[1] = (rs.getString(3) == null) ? null : "github";
				data[2] = (rs.getString(4) == null) ? null : "linkedin";
				data[3] = (rs.getString(5) == null) ? null : "spotify";
				data[4] = (rs.getString(6) == null) ? null : "discord";
				data[5] = (rs.getString(7) == null) ? null : "facebook";
				data[6] = (rs.getString(8) == null) ? null : "reddit";
				data[7] = (rs.getString(9) == null) ? null : "twitter";
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		int size = data.length;
		for (int i=0; i<size; i++) {

			System.out.println(data[i]);
		}
		System.out.println("mes column");
		return data;
	}

	@RequestMapping(value = "/getActionForUser", method = RequestMethod.GET)
	public String GetAction(@RequestParam(value = "userid") String userId) {
		System.out.println("monuid user dans ma req " + userId);
		String[] serviceName = getServiceByUser(userId);
		List<String> serviceId = getServiceIdByName(serviceName);
		List<String> actionName = getNameActionByServiceId(serviceId);
		String json = new Gson().toJson(actionName);
		System.out.println("mon JSON : " + json);
		return json;
	}

	@RequestMapping(value = "/getReactionForUser", method = RequestMethod.GET)
	public String GetReaction(@RequestParam(value = "userid") String userId) {
		System.out.println("monuid user dans ma req " + userId);
		String[] serviceName = getServiceByUser(userId);
		List<String> serviceId = getServiceIdByName(serviceName);
		List<String> actionName = getNameReactionByServiceId(serviceId);
		String json = new Gson().toJson(actionName);
		System.out.println("mon JSON : " + json);
		return json;
	}

	@RequestMapping(value = "/getServiceForUser", method = RequestMethod.GET)
	public String GetService(@RequestParam(value = "userid") String userId) {
		System.out.println("monuid user dans ma req " + userId);
		List<String> newservicename = new ArrayList<String>();
		String[] serviceName = getServiceByUser(userId);
		int size = serviceName.length;
		for (int i=0; i<size; i++) {
			if (serviceName[i] != null)
				newservicename.add(serviceName[i]);
			System.out.println(serviceName[i]);
		}
		String json = new Gson().toJson(newservicename);
		System.out.println("mon JSON : " + json);
		return json;
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