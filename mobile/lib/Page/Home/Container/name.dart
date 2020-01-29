import 'package:flutter/material.dart';
import 'package:mobile/global.dart' as global;

class NameContainer extends StatefulWidget {
  @override
  _NameContainerState createState() => _NameContainerState();
}

class _NameContainerState extends State<NameContainer> {
  TextStyle style = TextStyle(fontSize: 20.0, color: Colors.white);

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.all(5.0),
      decoration: BoxDecoration(
        color: Color(0x9900cc66),
        border: Border.all(
          color: Colors.white,
          width: 1,
        ),
        borderRadius: BorderRadius.circular(12.0)
      ),
      child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text("Hello " + global.name, style: style),
            ],
          )
      ),
    );
  }
}
