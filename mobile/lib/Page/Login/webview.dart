import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:mobile/global.dart';

class WebView extends StatefulWidget {
  final String url;
  final String title;
  final String service;
  WebView(this.url, this.title, this.service);
  @override
  WebViewState createState() {
    return new WebViewState();
  }
}

class WebViewState extends State<WebView> {
  var uri;
  final flutterWebViewPlugin = new FlutterWebviewPlugin();
  GlobalKey<ScaffoldState> scaffoldKey = GlobalKey<ScaffoldState>();
  StreamSubscription<String> _onUrlChanged;
  StreamSubscription _onDestroy;
  StreamSubscription<WebViewStateChanged> _onStateChanged;

//  String _userAgent = "CFNetwork/1121.2.1 Darwin/19.3.0 (iPhone/11_Pro_Max iOS/13.3)";

  @override
  void initState() {
    super.initState();
    List<String> split;

    flutterWebViewPlugin.close();
    _onDestroy = flutterWebViewPlugin.onDestroy.listen((_) {
      print("destroy");
    });
    _onStateChanged = flutterWebViewPlugin.onStateChanged.listen((WebViewStateChanged state) {
          print("onStateChanged: ${state.type} ${state.url}");
          print("split ${state.url.split("/")}");
          split = state.url.split("/");
          if (split[2] == "localhost:9090") {
            uri = Uri.parse(state.url);
            uri.queryParameters.forEach((k, v) {
              if (k == "id")
                user.id = v;
            });
            flutterWebViewPlugin.close();
            setState(() {
              switch (widget.service) {
                case "google" :
                  google = true;
                  break;
                case "github" :
                  github = true;
                  break;
                case "spotify" :
                  spotify = true;
                  break;
                case "linkedin" :
                  linkedin = true;
                  break;
                case "twitter" :
                  twitter = true;
                  break;
                case "facebook" :
                  facebook = true;
                  break;
                case "twitch" :
                  twitch = true;
                  break;
                case "reddit" :
                  reddit = true;
                  break;
                default :
                  break;
              }
            });
            Navigator.pushNamed(context, '/home/signin');
          }
    });
    _onUrlChanged = flutterWebViewPlugin.onUrlChanged.listen((String url) {
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
    print("USER AGENT:   $userAgent");

    return WebviewScaffold(
      appBar: AppBar(
        backgroundColor: Color(0x9900cc66),
        title: Text(widget.title),
        centerTitle: true,
      ),
      key: scaffoldKey,
      url: widget.url,
      hidden: true,
      clearCache: true,
      userAgent: userAgent,
      clearCookies: false,
//      appBar: AppBar(title: Text("Current Url")),
    );
  }
}

/* class InAppWebViewPage extends StatefulWidget {
  String url;

  InAppWebViewPage(this.url);
  @override
  _InAppWebViewPageState createState() => new _InAppWebViewPageState();
}

class _InAppWebViewPageState extends State<InAppWebViewPage> {
  InAppWebViewController webView;
  var height;
  var iframeUrl = 'https://www.youtube.com/embed/vlkNcHDFnGA';

  @override
  Widget build(BuildContext context) {
    height = MediaQuery.of(context).size.height;
    return Scaffold(
        appBar: AppBar(
            title: Text("InAppWebView")
        ),
        body: Container(
            child: Column(children: <Widget>[
              Expanded(
                child: Container(
                  child: InAppWebView(
                    initialData: InAppWebViewInitialData(
                        data: """
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Flutter InAppWebView</title>
    </head>
    <body>
        <iframe src="$iframeUrl}" width="100%" height="$height" frameborder="0" allowfullscreen></iframe>
    </body>
</html>"""
                    ),
                    initialHeaders: {},
                    initialOptions: InAppWebViewWidgetOptions(
                      inAppWebViewOptions: InAppWebViewOptions(
                        debuggingEnabled: true,
                      ),
                    ),
                    onWebViewCreated: (InAppWebViewController controller) {
                      webView = controller;
                    },
                    onLoadStart: (InAppWebViewController controller, String url) {

                    },
                    onLoadStop: (InAppWebViewController controller, String url) {

                    },
                  ),
                ),
              ),
            ]))
    );
  }
}
*/
