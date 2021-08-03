# Remembrall 

<img src="app/src/res/minmap-xxxhdpi/ic_launcher_foreground.png">

App to add tasks to never forget about them.

Within this app, you can add tasks, with a name description and priority.
If you log in with your Google account you will be able to add the tasks to 
your calendar by adding a due date to them.

This app is written in Jetpack Compose and use other cools libs like retrofit, room..

## Setup 

If you want to play with the repo or contribute add the following keys in you 
`local.properties` file replacing the values

```bash
keystoreAlias=<KeyStore Alias>
keystorePassword=<KeyStore Password>
keyPassword=<KeyStore Password>
keyPath=<Key path>
googleClientId=<Google client id for Google sign in>
```

We use firebase and we have enabled the oauth api also on Google Console,
you might need to do the same and add an API key within your `google-services.json` file, 
feel free to contact me if you want to contribute so I can share this file with you if you do not 
want to do the setup.
