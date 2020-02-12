import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:verbal_expressions/verbal_expressions.dart';

class WebView extends StatefulWidget {
  @override
  WebViewState createState() {
    return new WebViewState();
  }
}

class WebViewState extends State<WebView> {
  final flutterWebviewPlugin = new FlutterWebviewPlugin();
  GlobalKey<ScaffoldState> scaffoldKey = GlobalKey<ScaffoldState>();

  StreamSubscription<String> _onUrlChanged;

  @override
  void initState() {
    super.initState();
    int count = 0;

    _onUrlChanged = flutterWebviewPlugin.onUrlChanged.listen((String url) {
      count++;
      if (mounted && count == 2) {
        flutterWebviewPlugin.close();
        Navigator.pushNamed(context, '/home');
      }
    });
  }


  String getAccessToken(String url) {
    var expression = VerbalExpression()
      ..startOfLine()
      ..then("https://app.getpostman.com/oauth2/callback#access_token")
      ..then('=')
      ..beginCapture()
      ..anythingBut('&')
      ..endCapture()
      ..anything()
      ..endOfLine();

    return expression.toRegExp().firstMatch(url).group(1);
  }

  @override
  void dispose() {
    _onUrlChanged.cancel();
    flutterWebviewPlugin.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return WebviewScaffold(
      key: scaffoldKey,
      url: 'https://api.imgur.com/oauth2/authorize?client_id=784880b5d8f3965&response_type=token',
      hidden: true,
      clearCache: true,
      clearCookies: true,
//      appBar: AppBar(title: Text("Current Url")),
    );
  }
}