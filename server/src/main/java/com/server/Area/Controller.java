package com.server.Area;

import com.server.Area.Actions;

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

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.web.servlet.view.RedirectView;
import io.swagger.annotations.Api;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@CrossOrigin(maxAge = 3600)
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
		//Actions.twitchStreamerIsOnline(1, "wisethug", c, stmt);
		//Actions.youtubeGetNumberFriends(1, c, stmt);
		//Actions.youtubeGetVideosLike(1, c, stmt);
		//Actions.youtubeGetVideosDislike(1, c, stmt);
		//Actions.githubGetRepo(34,c , stmt);
		//Actions.githubPostComment(34,"Martouche", "BSQ","6f9d387ca6e1220fe9488180469d05084c72ca35", c , stmt);
		//Actions.githubReactionComments(34, "Martouche", "BSQ", "37507663", c , stmt );
		//Actions.youtubeReactionNewFriend(1,"xMrClyde", c, stmt );
		//Reactions.gmailSendMail(1, c, stmt );
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
		String TwitchId = Integer.toString(rand.nextInt(1000));
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
					"INSERT INTO services (id, name) SELECT " + TwitchId + ", 'twitch' WHERE NOT EXISTS (SELECT * FROM services where name='twitch');" +
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
					"reddit_token VARCHAR(250), twitter_token VARCHAR(250), twitch_token VARCHAR(250));");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Table Service Action
		try {
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS services_actions (id INT NOT NULL, id_service INT NOT NULL, name VARCHAR(250) NOT NULL);" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'gmailNewMail' WHERE NOT EXISTS (SELECT * FROM services_actions where name='gmailNewMail');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 0, 'wetherTemperatureMax' WHERE NOT EXISTS (SELECT * FROM services_actions where name='wetherTemperatureMax');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 0, 'wetherTemperatureMin' WHERE NOT EXISTS (SELECT * FROM services_actions where name='wetherTemperatureMin');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 0, 'wetherHumidityMin' WHERE NOT EXISTS (SELECT * FROM services_actions where name='wetherHumidityMin');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", 0, 'wetherHumidityMax' WHERE NOT EXISTS (SELECT * FROM services_actions where name='wetherHumidityMax');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + TwitchId + ", 'twitchStreamerIsOnline' WHERE NOT EXISTS (SELECT * FROM services_actions where name='twitchStreamerIsOnline');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'youtubeGetNumberFriends' WHERE NOT EXISTS (SELECT * FROM services_actions where name='youtubeGetNumberFriends');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'youtubeGetVideosLike' WHERE NOT EXISTS (SELECT * FROM services_actions where name='youtubeGetVideosLike');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'youtubeGetVideosDislike' WHERE NOT EXISTS (SELECT * FROM services_actions where name='youtubeGetVideosDislike');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GithubId + ", 'githubGetRepo' WHERE NOT EXISTS (SELECT * FROM services_actions where name='githubGetRepo');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GithubId + ", 'githubCommitsRepo' WHERE NOT EXISTS (SELECT * FROM services_actions where name='githubCommitsRepo');" +
					"INSERT INTO services_actions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GithubId + ", 'githubCommentsRepo' WHERE NOT EXISTS (SELECT * FROM services_actions where name='githubCommentsRepo');");
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Table Service Reaction
		try {
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS services_reactions (id INT NOT NULL, id_service INT NOT NULL, name VARCHAR(250) NOT NULL);" +
					"INSERT INTO services_reactions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GithubId + ", 'githubPostComment' WHERE NOT EXISTS (SELECT * FROM services_reactions where name='githubPostComment');" +
					"INSERT INTO services_reactions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GithubId + ", 'githubCreateRepo' WHERE NOT EXISTS (SELECT * FROM services_reactions where name='githubCreateRepo');" +
					"INSERT INTO services_reactions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GithubId + ", 'githubReactionComments' WHERE NOT EXISTS (SELECT * FROM services_reactions where name='githubReactionComments');" +
					"INSERT INTO services_reactions (id, id_service, name) SELECT " + Integer.toString(rand.nextInt(1000)) + ", " + GoogleId + ", 'youtubeReactionNewFriend' WHERE NOT EXISTS (SELECT * FROM services_reactions where name='youtubeReactionNewFriend');");
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
	@RequestMapping(value = "/oauth2/logout/discord", method = RequestMethod.GET)
	public RedirectView logoutDiscord(@RequestParam(value = "userid") String userId) {
		try {
			stmt = c.prepareStatement("UPDATE user_service_token SET discord_token = NULL WHERE id_user = '" + userId + "';");
			stmt.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + userId + "");
		return redirectView;
	}

		// Twitch Routes
	@RequestMapping(value = "/oauth2/autorize/twitch", method = RequestMethod.GET)
	public RedirectView getUrlAutorizeTwitch() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=riddoiwsiud1uyk92zkzwrdgipurqp&redirect_uri=http://localhost:8080/oauth2/callback/twitch&scope=viewing_activity_read&state=c3ab8aa609ea11e793aa92361f002672");
		return redirectView;
	}
	@RequestMapping(value = "/oauth2/callback/twitch", method = RequestMethod.GET)
	public RedirectView getTokenTwitch(@RequestParam(value = "code") String code) {
		System.out.println("mon code twitch = " + code);
		TwitchController mine = new TwitchController(id, code, c, stmt);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + id + "");
		return redirectView;
	}
	@RequestMapping(value = "/oauth2/logout/twitch", method = RequestMethod.GET)
	public RedirectView logoutTwitch(@RequestParam(value = "userid") String userId) {
		try {
			stmt = c.prepareStatement("UPDATE user_service_token SET twitch_token = NULL WHERE id_user = '" + userId + "';");
			stmt.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + userId + "");
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
	@RequestMapping(value = "/oauth2/logout/reddit", method = RequestMethod.GET)
	public RedirectView logoutReddit(@RequestParam(value = "userid") String userId) {
		try {
			stmt = c.prepareStatement("UPDATE user_service_token SET reddit_token = NULL WHERE id_user = '" + userId + "';");
			stmt.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + userId + "");
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
	@RequestMapping(value = "/oauth2/logout/facebook", method = RequestMethod.GET)
	public RedirectView logoutFacebook(@RequestParam(value = "userid") String userId) {
		try {
			stmt = c.prepareStatement("UPDATE user_service_token SET facebook_token = NULL WHERE id_user = '" + userId + "';");
			stmt.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + userId + "");
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
	@RequestMapping(value = "/oauth2/logout/twitter", method = RequestMethod.GET)
	public RedirectView logoutTwitter(@RequestParam(value = "userid") String userId) {
		twitter = TwitterFactory.getSingleton();
		try {
			stmt = c.prepareStatement("UPDATE user_service_token SET twitter_token = NULL WHERE id_user = '" + userId + "';");
			stmt.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + userId + "");
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
	@RequestMapping(value = "/oauth2/logout/linkedin", method = RequestMethod.GET)
	public RedirectView logoutLinkedin(@RequestParam(value = "userid") String userId) {
		try {
			stmt = c.prepareStatement("UPDATE user_service_token SET linkedin_token = NULL WHERE id_user = '" + userId + "';");
			stmt.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + userId + "");
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
	@RequestMapping(value = "/oauth2/logout/spotify", method = RequestMethod.GET)
	public RedirectView logoutSpotify(@RequestParam(value = "userid") String userId) {
		try {
			stmt = c.prepareStatement("UPDATE user_service_token SET spotify_token = NULL WHERE id_user = '" + userId + "';");
			stmt.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + userId + "");
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
	@RequestMapping(value = "/oauth2/logout/github", method = RequestMethod.GET)
	public RedirectView logoutGithub(@RequestParam(value = "userid") String userId) {
		try {
			stmt = c.prepareStatement("UPDATE user_service_token SET github_token = NULL WHERE id_user = '" + userId + "';");
			stmt.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + userId + "");
		return redirectView;
	}


	// Google Routes
	@RequestMapping(value = "/oauth2/autorize/google", method = RequestMethod.GET)
	public RedirectView getUrlAutorizeGoogle() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://accounts.google.com/o/oauth2/v2/auth?access_type=offline&scope=https%3A%2F%2Fmail.google.com+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fgmail.send+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fyoutube&response_type=code&client_id=377968007025-013sa07vehs51n1rau6qfmplp7esq964.apps.googleusercontent.com&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Foauth2%2Fcallback%2Fgoogle");
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
	@RequestMapping(value = "/oauth2/logout/google", method = RequestMethod.GET)
	public RedirectView logoutGoogle(@RequestParam(value = "userid") String userId) {
		try {
			stmt = c.prepareStatement("UPDATE user_service_token SET google_token = NULL WHERE id_user = '" + userId + "';");
			stmt.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:9090/home?id=" + userId + "");
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

	@CrossOrigin
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

	@CrossOrigin
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

    @CrossOrigin
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

	public String getReactionNamebyId(int idAction) {
		try {
			stmt = c.prepareStatement("SELECT name FROM services_actions WHERE id = " + idAction + ";");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				return rs.getString(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}


	public String getActionNamebyId(int idAction) {
		try {
			stmt = c.prepareStatement("SELECT name FROM services_actions WHERE id = " + idAction + ";");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				return rs.getString(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public int getActionIdbyName(String nameAction) {
		try {
			stmt = c.prepareStatement("SELECT id FROM services_actions WHERE name = '" + nameAction + "';");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	public int getReactionIdbyName(String nameReaction) {
		try {
			stmt = c.prepareStatement("SELECT id FROM services_reactions WHERE name = '" + nameReaction + "';");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	@RequestMapping(value = "/postActionReactionForUser", method = RequestMethod.GET)
	public String PostActionReaction(@RequestParam(value = "userid") String userId,
									 @RequestParam(value = "actionName") String actionName,
									 @RequestParam(value = "actionValue") String actionValue,
									 @RequestParam(value = "reactionName") String reactionName,
									 @RequestParam(value = "reactionValue") String reactionValue) {
		int id_service_action = getActionIdbyName(actionName);
		int id_service_reaction = getReactionIdbyName(reactionName);
		int int_user_id = Integer.parseInt(userId);
		if (actionName == "gmailNewMail")
			actionValue = String.valueOf(Actions.getGmailCurrentValueNumberMail(int_user_id, c, stmt));
		if (actionName == "youtubeNewFriend")
			actionValue = String.valueOf(Actions.youtubeGetNumberFriends(int_user_id, c, stmt));
		if (actionName == "youtubeLikingVideo")
			actionValue = String.valueOf(Actions.youtubeGetVideosLike(int_user_id, c, stmt));
		if (actionName == "youtubeDislikingVideo")
			actionValue = String.valueOf(Actions.youtubeGetVideosDislike(int_user_id, c, stmt));
		if (actionName == "githubNewRepo")
			actionValue = String.valueOf(Actions.githubGetRepo(int_user_id, c, stmt));
		if (actionName == "githubNewCommitsRepo")
			actionValue = actionValue + ":" + String.valueOf(Actions.githubGetCommitsRepo(int_user_id, actionValue, c, stmt));
		if (actionName == "githubNewCommentsRepo")
			actionValue = actionValue + ":" + String.valueOf(Actions.githubGetCommentsRepo(int_user_id, actionValue, c, stmt));
		try {
			stmt = c.prepareStatement("INSERT INTO user_actions_reactions " +
					"(id_user, id_service_action, value_service_action, id_service_reaction, value_service_reaction) " +
					"SELECT " + userId + ", " + id_service_action + ", '" + actionValue + "'," + id_service_reaction + ", '" + reactionValue + "';");
			stmt.execute();
			return "work";
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Scheduled(cron = "*/10 * * * * *")
	public void updateDataBase() {
		try {
			stmt = c.prepareStatement("SELECT * FROM user_actions_reactions");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int user_id = rs.getInt("id_user");
				int action_id = rs.getInt("id_service_action");
				String action_value = rs.getString("value_service_action");
				int reaction_id = rs.getInt("id_service_reaction");
				String reaction_value = rs.getString("value_service_reaction");

				boolean resultaction = false;
				String nameAction = getActionNamebyId(action_id);

				if (nameAction == "gmailNewMail")
					resultaction = Actions.gmailNewMail(user_id, action_value, c, stmt);
				if (nameAction == "youtubeNewFriend")
					resultaction = Actions.youtubeNewFriend(user_id, action_value, c, stmt);
				if (nameAction == "youtubeLikingVideo")
					resultaction = Actions.youtubeLikingVideo(user_id, action_value, c, stmt);
				if (nameAction == "youtubeDislikingVideo")
					resultaction = Actions.youtubeDislikingVideo(user_id, action_value, c, stmt);
				if (nameAction == "githubNewRepo")
					resultaction = Actions.githubNewRepo(user_id, action_value, c, stmt);
				if (nameAction == "githubNewCommitsRepo")
					resultaction = Actions.githubNewCommitsRepo(user_id, action_value, c, stmt);
				if (nameAction == "githubNewCommentsRepo")
					resultaction = Actions.githubNewCommentsRepo(user_id, action_value, c, stmt);
				if (nameAction == "wetherTemperatureMax")
					resultaction = Actions.wetherTemperatureMax(user_id, action_value, c, stmt);
				if (nameAction == "wetherTemperatureMin")
					resultaction = Actions.wetherTemperatureMin(user_id, action_value, c, stmt);
				if (nameAction == "wetherHumidityMax")
					resultaction = Actions.wetherHumidityMax(user_id, action_value, c, stmt);
				if (nameAction == "wetherHumidityMin")
					resultaction = Actions.wetherHumidityMin(user_id, action_value, c, stmt);
				if (nameAction == "twitchStreamerIsOnline")
					resultaction = Actions.twitchStreamerIsOnline(user_id, action_value, c, stmt);
				/*
				if (resultaction) {
					String nameReaction = getReactionNamebyId(action_id);
					if (nameReaction == "githubPostComment")
							Reactions.githubPostComment();
					if (nameReaction == "githubCreateRepo")
						Reactions.githubCreateRepo();
					if (nameReaction == "githubReactionComments")
						Reactions.githubReactionComments();
					if (nameReaction == "youtubeReactionNewFriend")
						Reactions.youtubeReactionNewFriend();
					if (nameReaction == "gmailSendMail")
						Reactions.gmailSendMail();
				}*/
				System.out.println(user_id);
				System.out.println(action_id);
				System.out.println(action_value);
				System.out.println(reaction_id);
				System.out.println(reaction_value);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("je suis alallalalalla");
	}
}
