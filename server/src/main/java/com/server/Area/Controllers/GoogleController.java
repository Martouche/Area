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

public class GoogleController {

    @ApiModelProperty(notes = "Name of the user")
    private String name;
    @ApiModelProperty(notes = "Google's Token")
    private String token;

    public GoogleController(String name, String token, Connection c, PreparedStatement stmt) {
        if (name != null && token != null) {
            this.name = name;
            this.token = token;
        }
    }

    public String getUserName() {
        return name;
    }
    public String getToken() { return token; }
}