package com.example.restservice;

import java.sql.*;
import java.util.Map;

import java.io.*;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.restservice.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
/*
public class LoginController {

    public static String login(Map<String, String> formData, Connection c, PreparedStatement stmt) {
        int log;

        if (formData != null) {
            String name = formData.get("name");
            String password = formData.get("password");

            if (name != null && password != null) {
                log = User.logUser(formData, c, stmt);
                if (log == 1)
                    return "index";
            }
        }
        return "login";
    }
}*/