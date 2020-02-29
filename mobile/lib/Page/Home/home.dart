import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:mobile/Container/action_bloc.dart';
import 'package:mobile/Container/login_button.dart';
import 'package:mobile/Container/logout.dart';
import 'package:mobile/global.dart';
import 'package:http/http.dart' as http;

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  double _height;
  bool isOpen = false;

  @override
  void initState() {
    print("IDDDDDDD ${user.id}");
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
            Column(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                LogOut(),
                Container(
                  height: MediaQuery.of(context).size.height * .8,
                  child: service(),
                ),
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
          ],
        ),
      ),
    );
  }
  Widget signIn() {
    return Column(
      children: <Widget>[
        Center(child: IconButton(
          icon: Icon(Icons.keyboard_arrow_down),
          onPressed: () {setState(() {isOpen = !isOpen;});},
          color: Colors.white,
        )),
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
  Widget service() {
    post.add(new Post());
    return ListView.builder(
      itemCount: count,
      itemBuilder: (context, index) {
        if (count == index + 1) {
          return Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: <Widget>[
              IconButton(
                icon: Icon(Icons.add),
                color: Colors.white,
                onPressed: () {
                  setState(() {
                    count += 1;
                  });
                },
              ),
              (count > 1) ? IconButton(
                icon: Icon(Icons.delete),
                color: Colors.white,
                onPressed: () {
                  setState(() {
                    count -= 1;
                  });
                },
              ) : Container()
            ],
          );
        } else {
          return ActionBloc(
            index: index,
            actionList: actionList,
            reactionList: reactionList,
          );
        }
      }
    );
  }
}

Future<dynamic> fetchAction() async {
  final response = await http.get('http://localhost:8080/getActionForUser?userid=' + user.id);

  if (response.statusCode == 200) {
    // If the call to the server was successful, parse the JSON.
    return json.decode(response.body);
  } else {
    // If that call was not successful, throw an error.
    throw Exception('Failed to load post');
  }
}

Future<dynamic> fetchReaction() async {
  final response = await http.get('http://localhost:8080/getReactionForUser?userid=' + user.id);

  if (response.statusCode == 200) {
    // If the call to the server was successful, parse the JSON.
    return json.decode(response.body);
  } else {
    // If that call was not successful, throw an error.
    throw Exception('Failed to load post');
  }
}

Future<dynamic> fetchService() async {
  final response = await http.get('http://localhost:8080/getServiceForUser?userid=' + user.id);

  if (response.statusCode == 200) {
    // If the call to the server was successful, parse the JSON.
    return json.decode(response.body);
  } else {
    // If that call was not successful, throw an error.
    throw Exception('Failed to load post');
  }
}
