import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mobile/global.dart';

class ActionBloc extends StatefulWidget {
  final int index;
  final List<String> actionList;
  final List<String> reactionList;
  ActionBloc({this.index, this.actionList, this.reactionList});
  @override
  _ActionBlocState createState() => _ActionBlocState();
}

class _ActionBlocState extends State<ActionBloc> {

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(vertical: 20.0),
      color: Colors.white,
      child: Column(
        children: <Widget>[
          Text("Action"),
          service(widget.index),
          Text("Reaction"),
          area(widget.index),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              IconButton(
                icon: Icon(Icons.check),
                onPressed: () {setState(() {count = count;});},
              ),
            ],
          )
        ],
      ),
    );
  }

  Widget service(int index) {
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
              post[index].actionValue = value;
            },
          ),
        )
      ],
    );
  }

  Widget area(int index) {
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
              post[index].reactionValue = value;
            },
          ),
        )
      ],
    );
  }
}
