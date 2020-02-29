import 'package:http/http.dart' as http;

String userAgent;

int count = 1;
List<Post> post = new List<Post>();
User user = new User();
API api = new API();
List<String> actionList = [];
List<String> reactionList = [];
List<String> serviceList;

bool google = false;
bool github = false;
bool spotify = false;
bool linkedin = false;
bool twitter = false;
bool facebook = false;
bool twitch = false;
bool reddit = false;

Map<String, bool> connectedService() => {
  'google' : google,
  'github' : github,
  'spotify' : spotify,
  'linkedin' : linkedin,
  'twitter' : twitter,
  'facebook' : facebook,
  'twitch' : twitch,
  'reddit' : reddit,
};

class Post {
  String action;
  String reaction;
  String actionValue;
  String reactionValue;
}

class API {
  static Future getID() {
    return http.get('localhost:9090/getId');
  }
}


class User {
  String id;
  String name;
  String pass = "";
}