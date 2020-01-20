import 'package:flutter/material.dart';

class NotificationContainer extends StatefulWidget {
  IconData logo;
  String msg;

  NotificationContainer(this.logo, this.msg);

  @override
  _NotificationContainerState createState() => _NotificationContainerState();
}

class _NotificationContainerState extends State<NotificationContainer> {
  TextStyle style = TextStyle(fontSize: 20.0, color: Colors.black);

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.all(5.0),
      decoration: BoxDecoration(
        color: Colors.blue[200],
        borderRadius: BorderRadius.circular(12.0),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Icon(widget.logo),
          Text(widget.msg, style: style),
        ],
      ),
    );
  }
}
