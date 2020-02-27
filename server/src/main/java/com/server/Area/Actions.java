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
import org.apache.http.entity.StringEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.collect.ImmutableMap;

public class Actions {
    static String apiKeyOpenWhether = "f22fcf91b5ea6b50c3f3510082393fbd";
    static String twitchClientId = "riddoiwsiud1uyk92zkzwrdgipurqp";
    static String ApiKeyGoogle = "AIzaSyBnWft8Xhjk1T5Y4oyf2A5RhgbnSKzHv18";


    public static String getAccesTokenById(int userId, String type, Connection c, PreparedStatement stmt) {
        String accesToken = null;
        try {
            stmt = c.prepareStatement("SELECT " + type + "_token FROM  user_service_token WHERE id_user = '" + userId + "'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                accesToken = rs.getString(1);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accesToken;
    }


    public static String getGmailCurrentEmailUser(String accessToken) {
        String data = null;
        JSONObject datauser = null;
        try {
            data = get(new StringBuilder("https://www.googleapis.com/oauth2/v1/userinfo?access_token=").append(accessToken).toString());

            // get the json
            try {
                datauser = new JSONObject(data);
            } catch (JSONException e) {
                throw new RuntimeException("Unable to parse json " + data);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return (String) datauser.get("email");
    }

    public static void getGmailCurrentValueNumberMail(int userId, Connection c, PreparedStatement stmt) {
        try {
            String accessToken = getAccesTokenById(userId, "google", c, stmt);
            HttpGet url = new HttpGet("https://www.googleapis.com/gmail/v1/users/"+ getGmailCurrentEmailUser(accessToken) +"/profile?key=" + ApiKeyGoogle + "");
            url.addHeader("Authorization",  "Bearer " + accessToken);
            url.addHeader("Accept", "application/json");
            String reponse = execute(url);
            System.out.println("reponse " + reponse);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    //// Return True si le nombre de mail a augmenté
    public static boolean gmailNewMail(int userId, String valueTotalMessage, Connection c, PreparedStatement stmt) {
        JSONObject datauser = null;
        int total = 0;
        try {
            String accessToken = getAccesTokenById(userId, "google", c, stmt);
            HttpGet url = new HttpGet("https://www.googleapis.com/gmail/v1/users/"+ getGmailCurrentEmailUser(accessToken) +"/profile?key=AIzaSyBnWft8Xhjk1T5Y4oyf2A5RhgbnSKzHv18");
            url.addHeader("Authorization",  "Bearer " + accessToken);
            url.addHeader("Accept", "application/json");
            String reponse = execute(url);
            System.out.println("reponse " + reponse);
            try {
                datauser = new JSONObject(reponse);
                total = datauser.getInt("messagesTotal");
            } catch (JSONException e) {
                throw new RuntimeException("Unable to parse json " + data);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        if (total != Integer.valueOf(valueTotalMessage)) {
            // a tester
            try {
                int id_action = getActionIdbyName("gmailNewMail");
                stmt = c.prepareStatement("UPDATE user_actions_reactions SET value_service_action = '" + total + "' WHERE id_user = "+ userId + "AND id_service_action = " + gmailNewMail + "");
                stmt.execute();
            }catch (Exception e) {
                System.out.println(e);
            }
            return true;
        }
        return false;
    }

    //// Return True si la temperature depase la valeur fixé sinon false
    public static boolean wetherTemperatureMax(int userId, String valueDegre, Connection c, PreparedStatement stmt) {
        double temp = 0;
        try {
            String reponse = get("http://api.openweathermap.org/data/2.5/weather?q=Nice,fr&APPID=" + apiKeyOpenWhether + "");
            System.out.println("ma reponse : " + reponse);
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) new JSONObject(reponse);
            } catch (JSONException e) {
                throw new RuntimeException("Unable to parse json " + reponse);
            }
            JSONObject test = jsonObject.getJSONObject("main");
            temp = test.getInt("temp") - 273.15;
            System.out.println(temp) ;
        }  catch (IOException e) {
            System.out.println(e);
        }
        if (Integer.valueOf(valueDegre) < temp)
            return true;
        return false;
    }
    //// Return True si la temperature est en dessous la valeur fixé sinon false
    public static boolean wetherTemperatureMin(int userId, String valueDegre, Connection c, PreparedStatement stmt) {
        double temp = 0;
        try {
            String reponse = get("http://api.openweathermap.org/data/2.5/weather?q=Nice,fr&APPID=" + apiKeyOpenWhether + "");
            System.out.println("ma reponse : " + reponse);
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) new JSONObject(reponse);
            } catch (JSONException e) {
                throw new RuntimeException("Unable to parse json " + reponse);
            }
            JSONObject test = jsonObject.getJSONObject("main");
            temp = test.getInt("temp") - 273.15;
            System.out.println(temp) ;
        }  catch (IOException e) {
            System.out.println(e);
        }
        if (Integer.valueOf(valueDegre) > temp)
            return true;
        return false;
    }

    //// Return True si l humidite est en dessous la valeur fixé sinon false
    public static boolean wetherHumidityMin(int userId, String valueHumidity, Connection c, PreparedStatement stmt) {
        int humidity = 0;
        try {
            String reponse = get("http://api.openweathermap.org/data/2.5/weather?q=Nice,fr&APPID=" + apiKeyOpenWhether + "");
            System.out.println("ma reponse : " + reponse);
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) new JSONObject(reponse);
            } catch (JSONException e) {
                throw new RuntimeException("Unable to parse json " + reponse);
            }
            System.out.println("mon object json " + jsonObject);
            JSONObject test = jsonObject.getJSONObject("main");
            System.out.println("mon object json apres test  " + test);
            humidity = test.getInt("humidity");
            System.out.println(humidity) ;
        }  catch (IOException e) {
            System.out.println(e);
        }
        if (Integer.valueOf(valueHumidity) > humidity)
            return true;
        return false;
    }

    //// Return True si l humidite est au dessus la valeur fixé sinon false
    public static boolean wetherHumidityMax(int userId, String valueHumidity, Connection c, PreparedStatement stmt) {
        int humidity = 0;
        try {
            String reponse = get("http://api.openweathermap.org/data/2.5/weather?q=Nice,fr&APPID=" + apiKeyOpenWhether + "");
            System.out.println("ma reponse : " + reponse);
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) new JSONObject(reponse);
            } catch (JSONException e) {
                throw new RuntimeException("Unable to parse json " + reponse);
            }
            System.out.println("mon object json " + jsonObject);
            JSONObject test = jsonObject.getJSONObject("main");
            System.out.println("mon object json apres test  " + test);
            humidity = test.getInt("humidity");
            System.out.println(humidity) ;
        }  catch (IOException e) {
            System.out.println(e);
        }
        if (Integer.valueOf(valueHumidity) < humidity)
            return true;
        return false;
    }

    //// Return true ou false si un streamer est en ligne
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

    //// Return nombre d'amis qu'on a sur Youtube
    public static int youtubeGetNumberFriends(int userId, Connection c, PreparedStatement stmt) {
        String access_token = "Bearer "+ getAccesTokenById(userId, "google", c, stmt);
        int friends = 0;
        try {
            HttpGet url = new HttpGet("https://www.googleapis.com/youtube/v3/subscriptions?part=subscriberSnippet&mySubscribers=true");
            url.addHeader("Authorization", access_token);
            JSONObject reponse = new JSONObject(execute(url));
            friends = reponse.getJSONObject("pageInfo").getInt("totalResults");
        }  catch (IOException e) {
            System.out.println(e);
        }
        return friends;
    }

    //// Return nombre de Videos qu'on a Like
    public static int youtubeGetVideosLike(int userId, Connection c, PreparedStatement stmt) {
        String access_token = "Bearer "+ getAccesTokenById(userId, "google", c, stmt);
        int like = 0;
        try {
            HttpGet url = new HttpGet("https://www.googleapis.com/youtube/v3/videos?part=snippet&myRating=like&maxResults=50");
            url.addHeader("Authorization", access_token);
            JSONObject reponse = new JSONObject(execute(url));
            like = reponse.getJSONObject("pageInfo").getInt("totalResults");
        }  catch (IOException e) {
            System.out.println(e);
        }
        System.out.println(like);
        return like;
    }

    //// Return nombre de Videos qu'on a Dislike
    public static int youtubeGetVideosDislike(int userId, Connection c, PreparedStatement stmt) {
        String access_token = "Bearer "+ getAccesTokenById(userId, "google", c, stmt);
        int dislike = 0;
        try {
            HttpGet url = new HttpGet("https://www.googleapis.com/youtube/v3/videos?part=snippet&myRating=dislike&maxResults=50");
            url.addHeader("Authorization", access_token);
            JSONObject reponse = new JSONObject(execute(url));
            dislike = reponse.getJSONObject("pageInfo").getInt("totalResults");
        }  catch (IOException e) {
            System.out.println(e);
        }
        System.out.println(dislike);
        return dislike;
    }

    //// Return le nombre de Repo qu'on a
    public static int githubGetRepo(int userId, Connection c, PreparedStatement stmt) {
        String access_token = "token "+ getAccesTokenById(userId, "github", c, stmt);
        int count = 0;
        try {
            HttpGet url = new HttpGet("https://api.github.com/user/repos");
            url.addHeader("Authorization", access_token);
            JSONArray reponse = new JSONArray(execute(url));
            count = reponse.length();
        }  catch (IOException e) {
            System.out.println(e);
        }
        return count;
    }

    //// Return le nombre de commits d'un Repo
    public static int githubCommitsRepo(int userId, String username, String repoName, Connection c, PreparedStatement stmt) {
        String access_token = "token "+ getAccesTokenById(userId, "github", c, stmt);
        int count = 0;
        try {
            HttpGet url = new HttpGet("https://api.github.com/repos/"+ username + "/" + repoName + "/commits");
            url.addHeader("Authorization", access_token);
            JSONArray reponse = new JSONArray(execute(url));
            count = reponse.length();
        }  catch (IOException e) {
            System.out.println(e);
        }
        return count;
    }

    //// Return le nombre de commentaires d'un Repo
    public static int githubCommentsRepo(int userId, String username, String repoName, Connection c, PreparedStatement stmt) {
        String access_token = "token "+ getAccesTokenById(userId, "github", c, stmt);
        int count = 0;
        try {
            HttpGet url = new HttpGet("https://api.github.com/repos/"+ username + "/" + repoName + "/comments");
            url.addHeader("Authorization", access_token);
            JSONArray reponse = new JSONArray(execute(url));
            count = reponse.length();
        }  catch (IOException e) {
            System.out.println(e);
        }
        return count;
    }

    /// REACTION add comment to a commit
    public static void githubPostComment(int userId, String username, String repoName, String sha, Connection c, PreparedStatement stmt) {
        String access_token = "token "+ getAccesTokenById(userId, "github", c, stmt);
        try {
            HttpPost url = new HttpPost("https://api.github.com/repos/" + username + "/"+ repoName + "/commits/" + sha + "/comments");
            url.addHeader("Authorization", access_token);
            JSONObject countryObj = new JSONObject();
            countryObj.put("body", "Bon travail :)");
            StringEntity entity = new StringEntity(countryObj.toString());
            url.setEntity(entity);
            JSONObject reponse = new JSONObject(execute(url));
        }  catch (IOException e) {
            System.out.println(e);
        }
    }

    /// REACTION create Repo
    public static void githubCreateRepo(int userId, String repoName,  Connection c, PreparedStatement stmt) {
        String access_token = "token "+ getAccesTokenById(userId, "github", c, stmt);
        try {
            HttpPost url = new HttpPost("https://api.github.com/user/repos");
            url.addHeader("Authorization", access_token);
            JSONObject countryObj = new JSONObject();
            countryObj.put("name", repoName);
            StringEntity entity = new StringEntity(countryObj.toString());
            url.setEntity(entity);
            JSONObject reponse = new JSONObject(execute(url));
        }  catch (IOException e) {
            System.out.println(e);
        }
    }

    /// REACTION add heart Emoji in comment
    public static void githubReactionComments(int userId, String userName, String repoName, String commentIds, Connection c, PreparedStatement stmt) {
        String access_token = "token "+ getAccesTokenById(userId, "github", c, stmt);
        try {
            HttpPost url = new HttpPost("https://api.github.com/repos/" + userName + "/" +  repoName + "/comments/" + commentIds + "/reactions");
            url.addHeader("Authorization", access_token);
            url.addHeader("Accept", "application/vnd.github.squirrel-girl-preview+json");
            JSONObject countryObj = new JSONObject();
            countryObj.put("content", "heart");
            StringEntity entity = new StringEntity(countryObj.toString());
            url.setEntity(entity);
            JSONObject reponse = new JSONObject(execute(url));
        }  catch (IOException e) {
            System.out.println(e);
        }
    }

    /// REACTION add friend
    public static void youtubeReactionNewFriend(int userId, String channelId, Connection c, PreparedStatement stmt) {
        String access_token = "Bearer "+ getAccesTokenById(userId, "google", c, stmt);
        try {
            HttpPost url = new HttpPost("https://www.googleapis.com/youtube/v3/subscriptions?part=snippet");
            url.addHeader("Authorization", access_token);
            url.addHeader("Content-Type", "application/json");
            String json = "{\"snippet\": {\"resourceId\": {\"kind\": \"youtube#channel\",\"channelId\": \"" + channelId +"\"}}}";
            StringEntity entity = new StringEntity(json);
            url.setEntity(entity);
            JSONObject reponse = new JSONObject(execute(url));
        }  catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void gmailSendMail(int userid, String message, Connection c, PreparedStatement stmt) {
        String accessToken = getAccesTokenById(userId, "google", c, stmt);
        String userEmail = getGmailCurrentEmailUser(accessToken);
        try {
            HttpPost url = new HttpPost("https://www.googleapis.com/gmail/v1/users/"+ userEmail +"/messages/send?key=" + ApiKeyGoogle +"");
            url.addHeader("Authorization", "Bearer " + access_token);
            url.addHeader("Accept", "application/json");
            url.addHeader("Content-Type", "application/json");
            String json = "{\"raw\": \"mon message\"}";
            StringEntity entity = new StringEntity(json);
            url.setEntity(entity);
        }  catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void putValue(int userId, int value, int idAction, Connection c, PreparedStatement stmt) {
        String accesToken = null;
        try {
            stmt = c.prepareStatement("INSERT INTO user_actions_reactions VALUES ('"+ userId + "','" + idAction + "','" + value + "','1','1')");
            ResultSet rs = stmt.executeQuery();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        if (response.getStatusLine().getStatusCode() != 200 || response.getStatusLine().getStatusCode() != 201) {
            throw new RuntimeException("Expected 200 or 201 but got " + response.getStatusLine().getStatusCode() + ", with body " + body);
        }

        return body;
    }

}
