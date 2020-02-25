import 'dart:async';
import 'package:flutter/material.dart';
import 'package:mobile/Container/Scroll/scrollview_social.dart';
import 'package:mobile/Container/login_button.dart';
import 'package:mobile/Page/Home/fetch.dart';
import 'package:mobile/Container/logout.dart';
import 'package:mobile/Container/name.dart';
import 'package:mobile/Container/Scroll/scrollview.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  Future<Post> post;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        color: Colors.black87,
        child: Column(
                children: <Widget>[
                  Expanded(
                    flex: 1,
                    child: LogOut(),
                  ),
                  Expanded(
                    flex: 1,
                    child: NameContainer(),
                  ),
                  Expanded(
                    flex: 8,
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        SignIn(root: '/github', text: 'Sign in with Github', logo: 'assets/logo-github.png',),
                        SignIn(root: '/spotify', text: 'Sign in with Spotify', logo: 'assets/logo-spotify.png', textColor: Colors.green,),
                        SignIn(root: '/linkedin', text: 'Sign in with Linkedin', logo: 'assets/logo-linkedin.png', textColor: Colors.blue[600],),
                        SignIn(root: '/twitter', text: 'Sign in with Twitter', logo: 'assets/logo-twitter.png', textColor: Colors.lightBlue,),
                        SignIn(root: '/facebook', text: 'Sign in with Facebook', logo: 'assets/logo-facebook.png', textColor: Colors.blue[900],),
                      ],
                    )
                  )
                ],
        ),
      ),
    );
  }
}

