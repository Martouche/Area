import 'package:flutter/material.dart';

class NotificationContainer extends StatefulWidget {
  IconData logo;
  String msg;

  NotificationContainer(this.logo, this.msg);

  @override
  _NotificationContainerState createState() => _NotificationContainerState();
}

class _NotificationContainerState extends State<NotificationContainer> {
  TextStyle style = TextStyle(fontSize: 20.0, color: Colors.white);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Container(
//        width: MediaQuery.of(context).size.width*0.4,
        margin: EdgeInsets.only(left: 5.0, top: 15.0, bottom: 20.0, right: 5.0),
        decoration: BoxDecoration(
          color: Color(0x9900cc66),
          borderRadius: BorderRadius.circular(8.0),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Icon(widget.logo, color: Colors.black),
            Text(widget.msg, style: style),
          ],
        ),
      ),
    );
  }
}

class NotificationRight extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.lightBlue,
      child: Align(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: <Widget>[
            Icon(
              Icons.check,
              color: Colors.white,
            ),
            Text(
              " Like",
              style: TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.w700,
              ),
              textAlign: TextAlign.right,
            ),
            SizedBox(
              width: 20,
            ),
          ],
        ),
        alignment: Alignment.centerLeft,
      ),
    );
  }
}

class NotificationLeft extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.red,
      child: Align(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.start,
          children: <Widget>[
            SizedBox(
              width: 20,
            ),
            Icon(
              Icons.delete,
              color: Colors.white,
            ),
            Text(
              " Delete",
              style: TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.w700,
              ),
              textAlign: TextAlign.left,
            ),
          ],
        ),
        alignment: Alignment.centerRight,
      ),
    );
  }
}
