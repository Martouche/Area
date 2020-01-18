package com.server.Area;

import java.util.concurrent.atomic.AtomicLong;
import java.sql.*;
import java.io.*;
import java.lang.*;
import com.server.Area.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
			stmt = c.prepareStatement("CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(250) NOT NULL, password VARCHAR(250) NOT NULL);");
			stmt.executeUpdate();
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
}
