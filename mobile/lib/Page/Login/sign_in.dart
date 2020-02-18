import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:verbal_expressions/verbal_expressions.dart';
import 'package:flutter_user_agent/flutter_user_agent.dart';

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
  StreamSubscription _onDestroy;
  StreamSubscription<WebViewStateChanged> _onStateChanged;
  String token;


  String _userAgent;

  WebViewState() {
    FlutterUserAgent.getPropertyAsync('userAgent')
        .then((response) => setState((){
          _userAgent = response;
          print(_userAgent);
    }));
  }
  @override
  void initState() {
    super.initState();
    int count = 0;

    flutterWebviewPlugin.close();
    _onDestroy = flutterWebviewPlugin.onDestroy.listen((_) {
      print("destroy");
    });
    _onStateChanged = flutterWebviewPlugin.onStateChanged.listen((WebViewStateChanged state) {
          print("onStateChanged: ${state.type} ${state.url}");
    });
    _onUrlChanged = flutterWebviewPlugin.onUrlChanged.listen((String url) {
      count++;
      if (mounted && count == 7) {
        print("URL changed: $url");
        RegExp regExp = new RegExp("#access_token=(.*)");
        this.token = regExp.firstMatch(url)?.group(1);
        print("token $token");
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
    // Every listener should be canceled, the same should be done with this stream.
    _onDestroy.cancel();
    _onUrlChanged.cancel();
    _onStateChanged.cancel();
    flutterWebviewPlugin.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return webView();
  }

  webView() {
    print(_userAgent);

    return WebviewScaffold(
      key: scaffoldKey,
      url: 'https://accounts.google.com/o/oauth2/v2/auth?access_type=offline&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&response_type=code&client_id=377968007025-013sa07vehs51n1rau6qfmplp7esq964.apps.googleusercontent.com&redirect_uri=http%3A%2F%2F10.16.253.51/localhost%3A8080%2Foauth2%2Fgoogle',
      hidden: true,
      clearCache: true,
      userAgent: _userAgent,
      clearCookies: true,
//      appBar: AppBar(title: Text("Current Url")),
    );
  }
}
