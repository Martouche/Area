import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mobile/Page/Login/webview.dart';
import 'package:mobile/global.dart';
import 'package:http/http.dart' as http;

class ActionBloc extends StatefulWidget {
  final int index;
  final List<String> actionList;
  final List<String> reactionList;
  String actionVal = "";
  String reactionVal = "";
  ActionBloc({this.index, this.actionList, this.reactionList});
  @override
  _ActionBlocState createState() => _ActionBlocState();
}

class _ActionBlocState extends State<ActionBloc> {
  bool isSub = false;

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(vertical: 20.0),
      color: Colors.white,
      child: Column(
        children: <Widget>[
          Text("Action"),
          action(widget.index),
          Text("Reaction"),
          reaction(widget.index),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: <Widget>[
              Text("Subscribe"),
              IconButton(
                icon: Icon(Icons.check),
                onPressed: () {setState(() {
                  print(widget.reactionVal);
                  Navigator.push(context, MaterialPageRoute(builder: (context) => WebView('http://localhost:8080/postActionReactionForUser?userid=${user.id}&actionName=${post[widget.index].action}&actionValue=${widget.reactionVal}&reactionName=${post[widget.index].reaction}&reactionValue=${widget.reactionVal}', 'Add service', '')));
                });},
              )
            ],
          )
        ],
      ),
    );
  }

  Widget action(int index) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      children: <Widget>[
        DropdownButton(
          value: post[index].action,
          items: widget.actionList.map((String value) {
            return new DropdownMenuItem<String>(
              value: value,
              child: Text(value,),
            );
          }).toList(),
          onChanged: (value) {
            setState(() {
              post[index].action = value;
            });
          },
        ),
        Expanded(
          child: TextField(
            textAlign: TextAlign.center,
            onChanged: (value) {
              setState(() {
                widget.actionVal = value;
              });
            },
          ),
        )
      ],
    );
  }

  Widget reaction(int index) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      children: <Widget>[
        DropdownButton(
          value: post[index].reaction,
          items: widget.reactionList.map((String value) {
            return new DropdownMenuItem<String>(
              value: value,
              child: Text(value,),
            );
          }).toList(),
          onChanged: (value) {
            setState(() {
              post[index].reaction = value;
            });
          },
        ),
        Expanded(
          child: TextField(
            textAlign: TextAlign.center,
            onChanged: (value) {
              setState(() {
                print(value);
                widget.reactionVal = value;
                print(widget.reactionVal);
              });
              },
          ),
        )
      ],
    );
  }
}

bool checkService() {
  if (google == false &&
      github == false &&
      spotify == false &&
      linkedin == false &&
      twitter == false &&
      facebook == false &&
      twitch == false &&
      reddit == false &&
      discord == false)
    return false;
  else
    return true;
}