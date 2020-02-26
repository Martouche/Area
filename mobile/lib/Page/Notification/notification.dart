import 'dart:async';
import 'package:flutter/material.dart';
import 'package:mobile/Container/custom_name.dart';
import 'package:mobile/Page/Home/fetch.dart';
import 'package:mobile/Container/Scroll/scrollview.dart';

class NotificationPage extends StatefulWidget {
  @override
  _NotificationPageState createState() => _NotificationPageState();
}

class _NotificationPageState extends State<NotificationPage> {
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
          color: Colors.black87,
          child: Column(
            children: <Widget>[
              Expanded(
                flex: 1,
                child: CustomNameContainer("Notifications"),
              ),
              Expanded(
                flex: 8,
                child: ScrollContainer(),
              ),
            ],
          ),
      ),
    );
  }
}

