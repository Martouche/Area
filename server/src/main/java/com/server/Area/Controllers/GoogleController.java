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

import org.json.JSONObject;
import org.json.JSONException;


public class GoogleController {

    @ApiModelProperty(notes = "Google's Token")
    private String code;

    public GoogleController(String code, Connection c, PreparedStatement stmt) {
        System.out.println("JE suis sur la bonne route _____________---------------");
        System.out.println(code);
        String url = "https://www.googleapis.com/oauth2/v4/token?code="+ code +"&client_id=377968007025-013sa07vehs51n1rau6qfmplp7esq964.apps.googleusercontent.com&client_secret=dXw6n2fh3lNh6URBVW_0P6xO&grant_type=authorization_code&redirect_uri=http://localhost:9090";
        try {
            JSONObject json = JsonReader.readJsonFromUrl(url);
            System.out.println(json.toString());
        }catch(IOException e) {
            e.printStackTrace();
        }

    }

    public String getCode() { return code; }
}