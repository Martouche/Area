package com.server.Area;


import java.sql.*;
import java.util.Map;

import java.io.*;
import java.lang.*;

import com.server.Area.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import io.swagger.annotations.ApiModelProperty;

import java.net.URL;
import java.nio.charset.Charset;

//import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Base64Utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.collect.ImmutableMap;

public class Actions {
    static String apiKeyOpenWhether = "f22fcf91b5ea6b50c3f3510082393fbd";
    static String twitchClientId = "riddoiwsiud1uyk92zkzwrdgipurqp";


    public static String getAccesTokenById(int userId, String type, Connection c, PreparedStatement stmt) {
        String accesToken = null;
        try {
            stmt = c.prepareStatement("SELECT " + type + "_token FROM  user_service_token WHERE id_user = '" + userId + "'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                accesToken = rs.getString("name");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accesToken;
    }

    // Faire les actions ici
    public static int wetherTemperatureMax(int userId, String valueDegre, Connection c, PreparedStatement stmt) {
        try {
            String reponse = get("http://api.openweathermap.org/data/2.5/weather?q=Nice,fr&APPID=" + apiKeyOpenWhether + "");
            System.out.println("ma reponse : " + reponse);
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) new JSONParser().parse(reponse);
            } catch (ParseException e) {
                throw new RuntimeException("Unable to parse json " + reponse);
            }
            System.out.println(jsonObject);
            //String accessToken = (String) jsonObject.get("main");
            //System.out.println(accessToken);
        }  catch (IOException e) {
            System.out.println(e);
        }
        return 0;
    }

    public static boolean twitchStreamerIsOnline(int userId, String channel, Connection c, PreparedStatement stmt) {
        String data = null;
        try {
            HttpGet url = new HttpGet("https://api.twitch.tv/helix/streams?user_login=" + channel);
            url.addHeader("Client-ID", twitchClientId );
            url.addHeader("Accept", "application/vnd.twitchtv.v5+json" );
            String reponse = execute(url);
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) new JSONParser().parse(reponse);
            } catch (ParseException e) {
                throw new RuntimeException("Unable to parse json " + reponse);
            }
            data = jsonObject.get("data").toString();
            System.out.println(data.indexOf("title") != -1 ? true : false);
        }  catch (IOException e) {
            System.out.println(e);
        }
        return data.indexOf("title") != -1 ? true : false;
    }

    // makes a GET request to url and returns body as a string
    public static String get(String url) throws ClientProtocolException, IOException {
        return execute(new HttpGet(url));
    }
    // makes request and checks response code for 200
    private static String execute(HttpRequestBase request) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);

        System.out.println("MA REQUETE : " + request);

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Expected 200 but got " + response.getStatusLine().getStatusCode() + ", with body " + body);
        }

        return body;
    }

}
