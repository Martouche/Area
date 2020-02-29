import 'package:mobile/Page/Home/home.dart';
import 'package:mobile/Page/Home/navigation_bar.dart';
import 'package:mobile/Page/Login/login.dart';
import 'package:flutter/material.dart';
import 'package:mobile/Page/Login/webview.dart';
import 'package:mobile/Page/Notification/notification.dart';

import 'Page/Login/webview.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Demo',
      theme: ThemeData(
        primaryColor: Colors.blue,
        accentColor: Colors.white,
        hintColor: Colors.white,
      ),
      initialRoute: '/',
      routes: {
        '/login': (context) => LoginPage(),
        '/google': (context) => WebView('http://localhost:8080/oauth2/autorize/google', 'Connection GOOGLE', 'google'),
        '/github': (context) => WebView('http://localhost:8080/oauth2/autorize/github', 'Connection GITHUB', 'github'),
        '/spotify': (context) => WebView('http://localhost:8080/oauth2/autorize/spotify', 'Connection SPOTIFY', 'spotify'),
        '/linkedin': (context) => WebView('http://localhost:8080/oauth2/autorize/linkedin', 'Connection LINKEDIN', 'linkedin'),
        '/twitter': (context) => WebView('http://localhost:8080/oauth2/autorize/twitter', 'Connection TWITTER', 'twitter'),
        '/facebook': (context) => WebView('http://localhost:8080/oauth2/autorize/facebook', 'Connection FACEBOOK', 'facebook'),
        '/twitch': (context) => WebView('http://localhost:8080/oauth2/autorize/twitch', 'Connection TWITCH', 'twitch'),
        '/navbar': (context) => BottomBarPage(),
        '/home/signin': (context) => HomePage(),
        '/home/notification' : (context) => NotificationPage(),
      },
      home: LoginPage(),
    );
  }
}
