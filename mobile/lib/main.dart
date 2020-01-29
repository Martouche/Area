import 'package:mobile/Page/Home/navigation_bar.dart';
import 'package:mobile/Page/Login/login.dart';
import 'package:mobile/Page/Home/home.dart';
import 'package:flutter/material.dart';
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
        '/home': (context) => Home(),
        '/home/notification' : (context) => NotificationPage(),
      },
      home: LoginPage(),
    );
  }
}
