import 'dart:async';
import 'package:flutter/material.dart';
import 'package:mobile/Page/Home/Container/dashboardview.dart';
import 'package:mobile/Page/Home/fetch.dart';
import 'package:mobile/Page/Home/Container/logout.dart';
import 'package:mobile/Page/Home/Container/name.dart';
import 'package:mobile/Page/Home/Container/Scroll/scrollview.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  Future<Post> post;

  @override
  void initState() {
    post = fetchPost();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        color: Colors.white,
        child: FutureBuilder<Post>(
          future: post,
          builder: (context, snapshot) {
            if (snapshot.hasData) {
/*              Timer.periodic(Duration(seconds: 5), (Timer t) =>
                  setState(() {
                    HomePage();
                  })
              );*/
              initalize_value(snapshot.data);
              return Column(
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
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        Expanded(
                          child: ScrollContainer(),
                          flex: 5,
                        ),
                        Expanded(
                          child: DashboardContainer(),
                          flex: 5,
                        ),
                      ],
                    )
                  )
                ],
              );
            } else if (snapshot.hasError) {
              return Text("${snapshot.error}");
            }
            return CircularProgressIndicator();
          },
        ),
      ),
    );
  }
}

