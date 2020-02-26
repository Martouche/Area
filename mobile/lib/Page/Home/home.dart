import 'package:flutter/material.dart';
import 'package:mobile/Container/login_button.dart';
import 'package:mobile/Container/logout.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  double _height;
  bool isOpen = false;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    _height = MediaQuery
        .of(context)
        .size
        .height;
    return Scaffold(
      body: Container(
        color: Colors.black87,
        child: Stack(
          children: <Widget>[
            AnimatedPositioned(
              // Use the properties stored in the State class.
              top: (isOpen) ? (_height - (_height * .6)) : _height,
              bottom: 0,
              left: 0,
              right: 0,
              duration: Duration(seconds: 1),
              curve: Curves.fastOutSlowIn,
              child: Container(
                color: Colors.blue,
                child: SingleChildScrollView(
                  child: signIn(),
                ),
              )
            ),
            Column(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                LogOut(),
                FloatingActionButton.extended(
                  label: (!isOpen) ? Text("Get more") : Text("Close"),
                  onPressed: () {
                    setState(() {
                      isOpen = !isOpen;
                    });
                  },
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
  Widget signIn() {
    return Column(
      children: <Widget>[
        SignIn(root: '/github',
          text: 'Sign in with Github',
          logo: 'assets/logo-github.png',),
        SignIn(root: '/spotify',
          text: 'Sign in with Spotify',
          logo: 'assets/logo-spotify.png',
          textColor: Colors.green,),
        SignIn(root: '/linkedin',
          text: 'Sign in with Linkedin',
          logo: 'assets/logo-linkedin.png',
          textColor: Colors.blue[600],),
        SignIn(root: '/twitter',
          text: 'Sign in with Twitter',
          logo: 'assets/logo-twitter.png',
          textColor: Colors.lightBlue,),
        SignIn(root: '/facebook',
          text: 'Sign in with Facebook',
          logo: 'assets/logo-facebook.png',
          textColor: Colors.blue[900],),
      ],
    );
  }
}

