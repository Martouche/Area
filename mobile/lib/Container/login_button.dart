import 'package:flutter/material.dart';

class SignIn extends StatelessWidget {
  final root;
  final text;
  final logo;
  final backgroundColor;
  final textColor;

  SignIn({this.root, this.text, this.logo, this.backgroundColor = Colors.white, this.textColor = Colors.black});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery
          .of(context)
          .size
          .width,
      margin: const EdgeInsets.only(left: 40.0, right: 40.0, top: 10.0),
      alignment: Alignment.center,
      child: Container(
        width: MediaQuery
            .of(context)
            .size
            .width*.8,
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(12.0),
          color: backgroundColor,
        ),
        child: OutlineButton(
          onPressed: () => Navigator.of(context).pushNamed(root),
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12)
          ),
          borderSide: BorderSide(color: Colors.white),
          child: Padding(
            padding: const EdgeInsets.fromLTRB(0, 10, 0, 10),
            child: Row(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Image(
                    image: AssetImage(logo), height: 35.0),
                Padding(
                  padding: const EdgeInsets.only(left: 10),
                  child: Text(
                    text,
                    style: TextStyle(
                      fontSize: 20,
                      color: textColor,
                    ),
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
