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

    @ApiModelProperty(notes = "Google's Token")
    private String code;

    public GoogleController(String code, Connection c, PreparedStatement stmt) {
        System.out.println("JE suis sur la bonne route _____________---------------");
        System.out.println(code);

    }

    public String getCode() { return code; }
}