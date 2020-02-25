import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:flutter_user_agent/flutter_user_agent.dart';

class WebView extends StatefulWidget {
  final String url;
  WebView(this.url);
  @override
  WebViewState createState() {
    return new WebViewState();
  }
}

class WebViewState extends State<WebView> {
  final flutterWebViewPlugin = new FlutterWebviewPlugin();
  GlobalKey<ScaffoldState> scaffoldKey = GlobalKey<ScaffoldState>();
  StreamSubscription<String> _onUrlChanged;
  StreamSubscription _onDestroy;
  StreamSubscription<WebViewStateChanged> _onStateChanged;

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
    List<String> split;

    flutterWebViewPlugin.close();
    _onDestroy = flutterWebViewPlugin.onDestroy.listen((_) {
      print("destroy");
    });
    _onStateChanged = flutterWebViewPlugin.onStateChanged.listen((WebViewStateChanged state) {
          print("onStateChanged: ${state.type} ${state.url}");
          print("split ${state.url.split("/")}");
          split = state.url.split("/");
          if (split[2] == "10.0.2.2.xip.io:9090") {
            flutterWebViewPlugin.close();
            Navigator.pushNamed(context, '/home');
          }
    });
    _onUrlChanged = flutterWebViewPlugin.onUrlChanged.listen((String url) {
      count++;
      if (mounted && count == 7) {
        print("URL changed: $url");
      }
    });
  }

  @override
  void dispose() {
    // Every listener should be canceled, the same should be done with this stream.
    _onDestroy.cancel();
    _onUrlChanged.cancel();
    _onStateChanged.cancel();
    flutterWebViewPlugin.dispose();
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
      url: widget.url,
      hidden: true,
      clearCache: true,
      userAgent: _userAgent,
      clearCookies: false,
//      appBar: AppBar(title: Text("Current Url")),
    );
  }
}
