import 'package:flutter/material.dart';
import 'package:mobile/Page/Home/Container/notification.dart';
import 'package:mobile/global.dart' as global;

class ScrollContainer extends StatefulWidget {
  @override
  _ScrollContainerState createState() => _ScrollContainerState();
}

class _ScrollContainerState extends State<ScrollContainer> {
  final items = List<String>.generate(20, (i) => "Notif n° ${i + 1}");
  TextStyle style = TextStyle(fontSize: 20.0, color: Colors.blue);

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.all(5.0),
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border.all(
          color: Colors.white,
          width: 1,
        ),
        borderRadius: BorderRadius.circular(12.0)
      ),
      child: ListView.builder(
        itemCount: items.length,
        itemBuilder: (context, index) {
          final item = items[index];

          return Dismissible(
            // Each Dismissible must contain a Key. Keys allow Flutter to
            // uniquely identify widgets.
            key: Key(item),
            // Provide a function that tells the app
            // what to do after an item has been swiped away.
            onDismissed: (direction) {
              // Remove the item from the data source.
              setState(() {
                items.removeAt(index);
              });

              // Then show a snackbar.
              Scaffold.of(context)
                  .showSnackBar(SnackBar(content: Text("$item dismissed")));
            },
            // Show a red background as the item is swiped away.
            background: Container(color: Colors.red),
            child: NotificationContainer(Icons.accessible_forward, item),
          );
        },
      ),
    );
  }
}

/*

 */

/*
SingleChildScrollView(
        child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                NotificationContainer(Icons.accessible_forward, "Il est l'heure de se lever"),
                NotificationContainer(Icons.accessibility_new, "Nouveau msg d'une zoulette"),
                NotificationContainer(Icons.videogame_asset, "Vous avez reçu une invitation à jouer"),
                NotificationContainer(Icons.airplanemode_active, "Votre vol a était retardé"),
              ],
            )
        ),
      )
 */