// usr info //
String email;
String pass;
String name;

String userAgent;

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

