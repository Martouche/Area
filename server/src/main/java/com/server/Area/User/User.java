package com.server.Area;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Map;

import java.io.*;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

public class User {

    private String name;
    private String email;
    private String token_google;

    private static String toHexString(byte[] hash)
    {
        BigInteger number = new BigInteger(1, hash);

        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    public static void addUserGoogle(String email, String accesToken, Connection c,  PreparedStatement stmt) {
        try {
            stmt = c.prepareStatement("SELECT name, name FROM users WHERE name = '" + email + "' AND type = 'google';");
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(accesToken.getBytes(StandardCharsets.UTF_8));

                    stmt = c.prepareStatement("INSERT INTO users (name, password, type) VALUES (?, ?, ?);");
                    stmt.setString(1, email);
                    stmt.setString(2, toHexString(hash));
                    stmt.setString(3, "google");
                    stmt.execute();
                    System.out.println("Utilisateur: " + email + " from  google vient de s'inscrire");
                } catch (Exception e) {
                    System.out.println(e.getClass().getName()+": " + e.getLocalizedMessage() );
                }
            } else {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(accesToken.getBytes(StandardCharsets.UTF_8));

                // change the acces token
            }
        }catch (Exception e) {
            System.out.println(e.getClass().getName()+": " + e.getLocalizedMessage() );
        }

    }

    public static void addUser(String name, String password, Connection c, PreparedStatement stmt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            stmt = c.prepareStatement("INSERT INTO users (name, password, type) VALUES (?, ?, ?);");
            stmt.setString(1, name);
            stmt.setString(2, toHexString(hash));
            stmt.setString(3, "basic");
            stmt.execute();
            System.out.println("Utilisateur: " + name + " vient de s'inscrire");
        } catch (Exception e) {System.out.println(e.getClass().getName()+": " + e.getLocalizedMessage() );}
    }

    public static int logUser(String name, String password, Connection c, PreparedStatement stmt) {

        String check_name = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            stmt = c.prepareStatement("SELECT name, name FROM users WHERE name = '" + name + "' AND password = '" + toHexString(hash) + "';");
            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
                return 0;
            return 1;
        } catch (Exception e) {System.out.println(e.getClass().getName()+": " + e.getLocalizedMessage() );}
        return 1;
    }
}