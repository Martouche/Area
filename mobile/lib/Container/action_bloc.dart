import 'package:flutter/material.dart';

class ActionBloc extends StatelessWidget {
  final String service;
  final Color colorBackground;
  ActionBloc({this.service, this.colorBackground});
  @override
  Widget build(BuildContext context) {
    return Container(
      color: colorBackground,
      child: Column(
        children: <Widget>[
          Text(service),
        ],
      ),
    );
  }
}
