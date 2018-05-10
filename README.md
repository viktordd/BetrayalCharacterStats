# Betrayal Board Game
Betrayal is a board game where you explore a haunted mansion with friends (3-6 player game).  The gameplay starts with everybody cooperating, exploring, adding rooms, and collecting items. Each turn players can choose to explore the mansion existing rooms, or to add on additional rooms if they want to explore a direction that hasn't been setup yet (this will add on a new random tile the player can draw).  The game gets really interesting when you encounter the "haunt" event;  a "haunt" can unite all the players against a common enemy, or turn one of the players into the enemy.  The mansion is random every time you play and is paired with one of 50 "haunt" scenarios (or more with the expansion pack). You can play as one of 6 characters, each with his own strengths and weaknesses. Each character has 4 attributes Speed, Sanity, Might, and Knowledge which contain stats that can change throughout the game. 
https://boardgamegeek.com/boardgame/10547/betrayal-house-hill

# Overview of Betrayal Character Stats app
The purpose of BetrayalCharacterStats is to keep track of player stats in the "Betrayal at House on The Hill" board game.  I built it using Android Studio and Java. 

The game comes with cardboard sheets with the player names and little plastic nubs to keep tracks of the "points" under each player's stats. I created the app because it is very easy to accidentally shift the tabs wrongly or drop the cardboard and lose the stats.  

<img src="/docs/Character.PNG" alt="character" width="400px">

The app keeps track of stats by positioning 4 pin images on top of a character image. You can change stats by touching and sliding pins up and down, it also has haptic feedback to let you know you have shifted a stat. The app stores all stats locally on the phone. The user can reset stats for individual characters or for the whole app. The app also allows users to rearrange characters, to allow multiple players to keep track of their stats one a single device. 

The app supports casting to [Google Chromecast](https://store.google.com/product/chromecast_2015). Google Chromecast is an easy way to add smart TV functionality to any TV. I decided to integrate Chromecast since all player stats in the game are supposed to be visible to everybody. All players can cast to a single TV where each character shows the name and stats of each player either by using one shared device or using multiple devices and connecting each to the same session.  

# Where to Download The Live App - Google PlayStore
<a href='https://play.google.com/store/apps/details?id=com.ViktorDikov.BetrayalCharacterStats&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' width="240px" src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

# Technical Architecture 
The app is written using Java, Android Studio, and the Android SDK.

The app uses the SharedPreferences API to store data. This allows apps to store simple key-value pairs which is perfect for storing only 4 values for each character and looking it up quickly.

I'm using drag-sort-listview https://github.com/bauerca/drag-sort-listview. This provides an easy way to create a list that allows you to drag items up and down to rearrange.

The source is separated in several sections. The Data namespace holds data objects such as the Stats, Settings, as well as the Provider classes responsible for instantiating data objects and getting stored data or defaults. 

The Helpers namespace has several classes that encapsulate functionality. The CastChannel and CastOptionsProvider are part of the Chromecast implementation. The CastChannel mainly provides the namespace required to communicate with the Cast app. The options provider has the Chromecast App Id which is a random alphanumeric id used to tell Chromecast which app to load. 

The ImageLoaderTask offloads the image loading to a separate thread. This helps the application to be more responsive and have less lag spikes due to resource loading.

The App has a single Activity which is responsible for managing the main menu and loading either the CharacterViewPagerFragment or the ReorderCharsFragment. The CharacterViewPagerFragment is a carousel of character cards, this is the main screen where characters and their stats are shown. You can swipe left and right between all the characters. The ReorderCharsFragment is responsible for reordering the characters. It implements drag-sort-listview which gives users the ability to reorder and remove characters. 

# Setup Instructions
To compile and run the app you need Android Studio https://developer.android.com/studio/. After downloading and installing Android Studio, it will ask you to download the necessary portions of Android SDK.

To open the app, select the "Open an existing project" option and navigate to the folder where you downloaded the source code.

<img src="/docs/AndroidStudioStart.PNG" alt="Open an existing project" width="300px">

To run the app, connect your phone, ensure that you have USB debugging enabled in the Developer options, then Run app button (green play button).

<img src="/docs/Run.PNG" alt="Run">

Android Studio will ask you where you want to run the app. Select the phone you have connected, or alternatively you can create a virtual device to run and test the app.

<img src="/docs/RunDevice.PNG" alt="Run on device">

# App Walkthrough
Using the app is simple, when the app starts you are presented with the first character card, you can swipe left and right to view all characters, each character is double sided so you can flip the current card to view the other side by going to the vertical ellipsis menu and selecting the "Flip Current Character". Each character card also has a pull up drawer that has more information about the character as well as one time stats increases when a player ends their turn in a specific room.

<img src="https://lh3.googleusercontent.com/-CmOHGGOeB1Sw8LhRfHlhavcD7KoI8YbIWHJShaPJe0JXCAQByanr3D_bJYawgfwazg=w2048-h1047-rw" alt="app" width="200px"> <img src="https://lh3.googleusercontent.com/tvMOm6nkjbzz5ntaMxnBc_qZumx7KRhjDw3nkUjUi9CZppCPSF_FhL4KjzXtuKHqXpw=w2048-h1047-rw" alt=" app " width="200px"> <img src="https://lh3.googleusercontent.com/Je2uG8eJOkXwhH6_REK2r3jcTfRdwFB1u4lrOi1HRhVGySLFXj3mm3Zf9U9QSOZFKQ=w2048-h1047-rw" alt=" app " width="200px">

The side menu shows all characters, allows you to flip any of them or to jump to any character on the list. From there you can also set the always-on display option, turn vibrate on and off (phone vibrates when you change stats), set the player name that appears when you cast your character, and reset all stats for all characters.

<img src="https://lh3.googleusercontent.com/XD4gXDkzz0pSnfmbIZmV5v17DadrSMDyfU1yBU3Bl9-8IzNST4WmSD5w3r_rj0apheM=w2048-h1047-rw" alt=" app " width="200px"> <img src="https://lh3.googleusercontent.com/XD4gXDkzz0pSnfmbIZmV5v17DadrSMDyfU1yBU3Bl9-8IzNST4WmSD5w3r_rj0apheM=w2048-h1047-rw" alt=" app " width="200px">

Finally, you can change the character order from the Re-Order Characters screen. You can drag characters up and down to rearrange them or swipe them away to remove them from the list. This allows multiple players to use a single device. The reset option resets both the order and adds all removed characters back in. The reset all options in the side menu also resets the character order.

<img src="https://lh3.googleusercontent.com/l6Q5tvDAM7LBhh3w5Uf6Y4xhr2gMd_H7G3HUQAIqZazgtszVD7-0aKMT44nnhGcECdQL=w2048-h1047-rw" alt="app" width="200px"> <img src="https://lh3.googleusercontent.com/Oni3eGkW2qYyXm2A0R7i_QES8LdLAa-QseaHyxJT1ej2o4bwVadY1Dl4jUNOKV17faY=w2048-h1047-rw" alt="app" width="200px"> <img src="https://lh3.googleusercontent.com/2K3kqYW-i9XYwSpt86p6Lo2Z-I7d787cPSCbFuACZQGZ8mgdQYzbRDzrw502RG0lRatQ=w2048-h1047-rw" alt="app" width="200px">


# Resources to Build Android Apps
A good place to start learning how to develop Android apps is the Android Developer guide: https://developer.android.com/guide/.
