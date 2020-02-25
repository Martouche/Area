import 'package:mobile/Page/Home/navigation_bar.dart';
import 'package:mobile/Page/Login/login.dart';
import 'package:flutter/material.dart';
import 'package:mobile/Page/Login/sign_in.dart';
import 'package:mobile/Page/Notification/notification.dart';

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
        '/google': (context) => WebView('http://10.0.2.2.xip.io:8080/oauth2/autorize/google'),
        '/github': (context) => WebView('http://10.0.2.2.xip.io:8080/oauth2/autorize/github'),
        '/spotify': (context) => WebView('http://10.0.2.2.xip.io:8080/oauth2/autorize/spotify'),
        '/linkedin': (context) => WebView('http://10.0.2.2.xip.io:8080/oauth2/autorize/linkedin'),
        '/twitter': (context) => WebView('http://10.0.2.2.xip.io:8080/oauth2/autorize/twitter'),
        '/facebook': (context) => WebView('http://10.0.2.2.xip.io:8080/oauth2/autorize/facebook'),
        '/home': (context) => Home(),
        '/home/notification' : (context) => NotificationPage(),
      },
      home: LoginPage(),
    );
  }
}
