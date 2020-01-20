import 'dart:async';
import 'dart:convert';
import 'package:mobile/global.dart' as global;
import 'package:http/http.dart' as http;

Future<Post> fetchPost() async {
  String id = "1";
  final response =
  await http.get('http://api.relax-max.fr/mood/get/' + id);

  if (response.statusCode == 200) {
    // If the call to the server was successful, parse the JSON.
    return Post.fromJson(json.decode(response.body));
  } else {
    // If that call was not successful, throw an error.
    throw Exception('Failed to load post');
  }
}

class Post {
  final String name;


  Post({this.name});

  factory Post.fromJson(Map<String, dynamic> json) {
    return Post(
      name: json['name'],
    );
  }
}

initalize_value(Post data) {
  global.name = data.name;
}