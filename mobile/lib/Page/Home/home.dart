import 'package:flutter/material.dart';
import 'package:mobile/Page/Home/logout.dart';
import 'package:mobile/Page/Home/name.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        color: Colors.blue,
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
              child: Container(),
            )
          ],
        ),
      ),
    );
  }
}
