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
        SignIn(root: 'google',
          serviceName: 'Google',
          logo: 'assets/logo-google.png',
          textColor: Colors.grey,),
        SignIn(root: 'github',
          serviceName: 'Github',
          logo: 'assets/logo-github.png',
          textColor: Colors.black,),
        SignIn(root: 'spotify',
          serviceName: 'Spotify',
          logo: 'assets/logo-spotify.png',
          textColor: Color(0xFF1DB954),),
        SignIn(root: 'linkedin',
          serviceName: 'Linkedin',
          logo: 'assets/logo-linkedin.png',
          textColor: Color(0xFF0072b1),),
        SignIn(root: 'twitter',
          serviceName: 'Twitter',
          logo: 'assets/logo-twitter.png',
          textColor: Color(0xFF00acee),),
        SignIn(root: 'facebook',
          serviceName: 'Facebook',
          logo: 'assets/logo-facebook.png',
          textColor: Color(0xFF3b5998),),
        SignIn(root: 'twitch',
          serviceName: 'Twitch',
          logo: 'assets/logo-twitch.png',
          textColor: Color(0xFF6441A4),),
        SignIn(root: 'reddit',
          serviceName: 'Reddit',
          logo: 'assets/logo-reddit.png',
          textColor: Color(0xFFFF5700),),
        SizedBox(height: 50,),
      ],
    );
  }
  Widget action() {
    return ListView(
      children: <Widget>[

      ],
    );
  }
}

