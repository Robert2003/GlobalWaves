# GlobalWaves - Audio Management and Playback System

## Overview

This project is an audio management system designed to handle various aspects of audio content, including playlists,
podcasts, songs, and user interactions. It allows users to create, manage, and play audio content, providing
functionality such as search, selection, and playback control. It was extended to support pagination system, that
simulates a GUI page that the user can be on, premium/free user and monetization for artists. The app now supports user
recommendations, notifications and statistics that simulate a real app's wrapped.

## Features

- **User Management**: Add and delete users, manage user interactions and check if an user is interacting with another
  one.
- **Playlist Management**: Create and manage playlists with functionalities like adding songs, setting visibility, and
  more.
- **Podcast Handling**: Manage podcasts, including episodes and playback.
- **Song Management**: Add, categorize, and manage songs with various attributes.
- **User Interaction**: User profiles and interactions, including playlist following and song preferences.
- **Search Functionality**: Robust search capabilities to find audio files/hosts/artists.
- **Audio Playback**: Control audio playback, including shuffle and repeat functionalities.
- **Command Handling**: Execute and manage various commands related to audio management and user interaction.
- **Pagination System**: Extended support for a pagination system that simulates a GUI page that the user can be on.
- **Premium/Free Users**: Users can now buy a subscribtion or have a free account where they receive ads.
- **Notification System**: Users can subscribe/unsubscribe to an artist/host and they will receive a notification
  everytime the content creator performs an action.
- **User Recommendations**: Users can receive a random song recommendation, a playlist based on top 5 fans of the
  current song's artist, or a playlist based on most listened 3 genres of the user.
- **Monetization System**: Artists receive money from an user's subscription, when a user receives an ad and he has
  listened one of the artist's song, or when an user buys one of the artis's merchandise.
- **Wrapped**: Users/Artists/Hosts can receive a personalised sumarry of their music activity.
- **Page Navigation**: Users can now navigate through the history of the accesed pages.

## Components

- **Main Components**: `Main`, `Test`, `Library` and `User`.
- **Audio Collections**: `AudioCollection`, `Playlist`, `Podcast`, `Album`.
- **Audio Files**: `AudioFile`, `Song`, `Episode`.
- **Player Management
  **: `AudioPlayer`, `PlaylistTimeManagerStrategy`, `PodcastTimeManagerStrategy`, `SongTimeManagerStrategy`. `AlbumTimeManagerStrategy`.
- **Search Strategies and Selection**: Implementations of searching and selecting songs, playlists, podcasts, albums,
  artists and hosts.
- **Pagination system**: `Page`, `PageVisitable`, `PageVisitor`, `PageFactory`.
- **Nodes**: Various nodes like `InputNode`, `OutputNode`, `SearchNode`, `SelectNode`, etc., to handle different
  functionalities.
- **Utility Classes**: `Filter`, `Constants` and strategy patterns for handling time management and
  searching.

## Design Pattern used:

- **Strategy Pattern**: used for searching functionality and time management, because Songs, Playlists and Podcasts are
  listened differently. Also used for Wrapped command for different types of users, free or premium monetization and
  different types of recommendations.
- **Factory Pattern**: used to create the appropriate strategy patterns and the needed page for changePage command.
- **Command Pattern**: interface Executable implemented by every executable command.
- **Singleton Pattern**: used by the library to be used as a database.
- **Visitor Pattern**: used by the pagination system. There are two types of visitors: **PopulatePageVisitor** which
  puts all the data in the calling page and **PrintPageVisitor** which prints the content of the page in different ways
  for different pages.
- **Observer Pattern**: used for the notifications system. Each users is being notified when a new change was made by an
  artist/host.

### Informations about the code and interaction between classes

- Each user has a different searchbar, audio player and a different page he is watching.
- Searchbar uses strategy design pattern to search different types of searchable entities (
  song/podcast/playlist/album/artist/host).
- Audio player has functionality as: load, repeat, shuffle, printing the status of the playback and wrappers for: next,
  prev, forward, backwards.
- Each audio player has a time management strategy that simulates the trackbar of the audio player in a different way
  for songs/podcasts/playlists/albums.
- Each user has a listened history that is being used to calculate wrapped statistics.
- Each artist/host can receive a payment from a premium/free user.
- Each user user can have recommendations and load them into the audio player.

## Usage

This audio management system operates based on commands provided in a JSON file and prints the results to another JSON
file.

### Running the Application

- Execute the `Main` class. The program will read the commands from the provided JSON file, process them, and perform
  the respective actions for searchbar functionality, audio management and playback.

### Receiving Outputs

- After processing the commands, the program will generate an output JSON file containing the results of the executed
  commands. The output JSON will detail the status of the command execution, such as success messages or error
  information.

<details>
<summary style="font-size:20px; font-weight:bold;">Commands and How To Create Custom Input</summary>

- **Create a New .json File:**
    - Begin by creating a new .json file, naming it appropriately for your test.

- **Structure of the .json File:**
    - The .json file will consist of various commands to customize the test. Below are the supported commands:
- **Run the program**
    - Run the `Main` class to obtain the output. This will generate a new file in `result` folder containing the output
      of the
      new test.
      <br> <br>

1. `search` - Used for searching a song/playlist/podcast in the library.

### Usage:

```json
{
  "command": "search",
  "username": "alice22",
  "timestamp": 750,
  "type": "song",
  "filters": {
    "genre": "Rock",
    "releaseYear": "<1990"
  }
}
```

2. `select` - Used for selecting a song/playlist/podcast from the last search results.

### Usage:

```json
{
  "command": "select",
  "username": "alice22",
  "timestamp": 15,
  "itemNumber": 1
}
```

3. `load` - Used for loading the last selected song/playlist/podcast into the audio player.

### Usage:

```json
{
  "command": "load",
  "username": "alice22",
  "timestamp": 20
}
```

4. `playPause` - Used for toggling between play and pause state of the audio player.

### Usage:

```json
{
  "command": "playPause",
  "username": "alice22",
  "timestamp": 30
}
```

5. `repeat` - Used for changing the repeat state of the audio player.

### Usage:

```json
{
  "command": "repeat",
  "username": "alice22",
  "timestamp": 31
}
```

6. `suffle` - Used for activating or deactivating shuffle mode of the audio player. Used only for playlists.

### Usage:

```json
{
  "command": "shuffle",
  "username": "bob35",
  "timestamp": 850,
  "seed": 1024
}
```

7. `forward` - Used for skipping 90 seconds forward in a podcast.

### Usage:

```json
{
  "command": "forward",
  "user": "bob35",
  "timestamp": 1050
}
```

8. `backward` - Used for skipping 90 seconds backwards in a podcast.

### Usage:

```json
{
  "command": "backward",
  "user": "bob35",
  "timestamp": 1050
}
```

9. `like` - Used for toggling between like and unlike state of the currently playing song. If a playlist is playing, the
   playing song from the playlist is being liked/unliked.

### Usage:

```json
{
  "command": "like",
  "username": "bob35",
  "timestamp": 205
}
```

10. `next` - Used for skipping to the next song in a playlist.

### Usage:

```json
{
  "command": "next",
  "username": "bob35",
  "timestamp": 590
}
```

11. `prev` - Used for rewinding to the previous song in a playlist. If the current song is the first in the song queue,
    or if has passed at least 1 second since it started playback, it return to the beginning of the song. Else it goes
    to the beginning of the previous song.

### Usage:

```json
{
  "command": "prev",
  "username": "bob35",
  "timestamp": 710
}
```

12. `addRemoveInPlaylist` - Used for adding or removing a song in/from a playlist.

### Usage:

```json
{
  "command": "addRemoveInPlaylist",
  "username": "alice22",
  "timestamp": 24,
  "playlistId": 1
}
```

13. `status` - Used for showing the status of the audio player (eg: if it is playing, what it's playing, what is the
    remaining time, etc.)

### Usage:

```json
{
  "command": "status",
  "username": "alice22",
  "timestamp": 59
}
```

14. `createPlaylist` - Used for creating a new playlist, if it doesn't already exist.

### Usage:

```json
{
  "command": "createPlaylist",
  "username": "alice22",
  "timestamp": 5,
  "playlistName": "Playlist bengos"
}
```

15. `switchVisibility` - Used for toggling between oublic/private state of a playlist.

### Usage:

```json
{
  "command": "switchVisibility",
  "username": "carol19",
  "timestamp": 1130,
  "playlistId": 100
}
```

16. `follow` - Used by an user to follow the selected playlist resuted by the `select` command.

### Usage:

```json
{
  "command": "follow",
  "username": "carol19",
  "timestamp": 1050
}
```

17. `showPlaylists` - Used for printing all playlists created by an user.

### Usage:

```json
{
  "command": "showPlaylists",
  "username": "alice22",
  "timestamp": 65
}
```

18. `showPreferredSongs` - Used by an user to print all the songs that he has liked.

### Usage:

```json
{
  "command": "showPreferredSongs",
  "username": "carol19",
  "timestamp": 1000
}
```

19. `getTop5Songs` - Used for printing top 5 songs from the library that have received the most likes.

### Usage:

```json
  {
  "command": "getTop5Songs",
  "timestamp": 3300
}
```

20. `getTop5Playlists` - Used for printing top 5 playlists from the library that have received the most follows.

### Usage:

```json
{
  "command": "getTop5Playlists",
  "timestamp": 2560
}
```

21. `changePage` - Used by a normal user to change the current page he is viewing.

### Usage:

```json
{
  "command": "changePage",
  "username": "alice22",
  "timestamp": 100,
  "nextPage": "Home"
}
```

22. `printCurrentPage` - Used by a normal user to print the current page he is viewing.

### Usage:

```json
{
  "command": "printCurrentPage",
  "username": "alice22",
  "timestamp": 420
}
```

23. `addUser` - Used by the admin to add a new user in the database.

### Usage:

```json
{
  "command": "addUser",
  "timestamp": 100,
  "type": "user/artist/host",
  "username": "anaaremere",
  "age": 20,
  "city": "New York"
}
```

24. `deleteUser` - Used by the admin to delete an existing user from the database.

### Usage:

```json
{
  "command": "deleteUser",
  "username": "alice22",
  "timestamp": 120
}
```

25. `showAlbums` - Used to print all albums of an artist.

### Usage:

```json
{
  "command": "showAlbums",
  "username": "Ed Sheeran",
  "timestamp": 400
}
```

26. `showPodcasts` - Used to print all podcasts of a host.

### Usage:

```json
{
  "command": "showPodcasts",
  "username": "Joe Rogan",
  "timestamp": 420
}
```

27. `addAlbum` - Used by an artist to add a new album.

### Usage:

```json
{
  "command": "addAlbum",
  "username": "Ed Sheeran",
  "timestamp": 160,
  "name": "Album1",
  "releaseYear": 2023,
  "description": "Primul album bro!"
  "songs": [
    {
      "name": "Shape of You",
      "duration": 233,
      "album": "Divide",
      "tags": [
        "#pop",
        "#mostlistenedthisyear",
        "#spotify"
      ],
      "lyrics": "The club isn't the best place to find a lover, So the bar is where I go (mm-mm)",
      "genre": "Pop",
      "releaseYear": 2017,
      "artist": "Ed Sheeran"
    }
  ]
}
```

28. `removeAlbum` - Used by an artist to remove an existing album.

### Usage:

```json
{
  "command": "removeAlbum",
  "username": "Ed Sheeran",
  "timestamp": 180,
  "name": "Album1"
}
```

29. `addEvent` - Used by an artist to add a new event.

### Usage:

```json
{
  "command": "addEvent",
  "username": "Ed Sheeran",
  "timestamp": 200,
  "name": "Event1",
  "description": "Primul Event adaugat!",
  "date": "01-01-2022"
}
```

30. `removeEvent` - Used by an artist to remove an existing event.

### Usage:

```json
{
  "command": "addEvent",
  "username": "Ed Sheeran",
  "timestamp": 200,
  "name": "Event1",
  "description": "Primul Event adaugat!",
  "date": "01-01-2022"
}
```

31. `addMerch` - Used by an artist to add a new merchandise.

### Usage:

```json
{
  "command": "addMerch",
  "username": "Ed Sheeran",
  "timestamp": 240,
  "name": "Merch1",
  "description": "Primul Merch adaugat!",
  "price": 100
}
```

32. `addPodcast` - Used by a host to add a new podcast.

### Usage:

```json
{
  "command": "addPodcast",
  "username": "Mike",
  "timestamp": 260,
  "name": "Podcast1",
  "episodes": [
    {
      "name": "Elon Musk Returns",
      "duration": 11927,
      "description": "Elon Musk, CEO of SpaceX and Tesla, returns to discuss various topics."
    },
    {
      "name": "Jordan Peterson",
      "duration": 9916,
      "description": "Dr. Jordan Peterson joins Joe to discuss psychology, philosophy, and current events."
    }
  ]
}
```

33. `removePodcast` - Used by a host to remove an existing podcast.

### Usage:

```json
{
  "command": "removePodcast",
  "username": "Mike",
  "timestamp": 280,
  "name": "Podcast1"
}
```

34. `addAnnouncement` - Used by a host to add a new announcement.

### Usage:

```json
{
  "command": "addAnnouncement",
  "username": "Mike",
  "timestamp": 300,
  "name": "Announcement1",
  "description": "Primul anunt adaugat!"
}
```

35. `removeAnnouncement` - Used by a host to remove an existing announcement.

### Usage:

```json
{
  "command": "removeAnnouncement",
  "username": "Mike",
  "timestamp": 320,
  "name": "Announcement1"
}
```

36. `switchConnectionStatus` - Used by a normal user to toggle between ONLINE and OFFLINE status.

### Usage:

```json
{
  "command": "switchConnectionStatus",
  "username": "Mike",
  "timestamp": 320
}
```

37. `getTop5Albums` - Used for printing top 5 albums from the library that have received the most likes.

### Usage:

```json
{
  "command": "getTop5Albums",
  "timestamp": 540
}
```

38. `getTop5Artists` - Used for printing top 5 artists from the library that have received the most likes.

### Usage:

```json
{
  "command": "getTop5Artists",
  "timestamp": 560
}
```

39. `getAllUsers` - Used for printing all users from the library: normal users/hosts/artists.

### Usage:

```json
{
  "command": "getAllUsers",
  "timestamp": 580
}
```

40. `getOnlineUsers` - Used for printing all online normal users from the library.

### Usage:

```json
{
  "command": "getOnlineUsers",
  "timestamp": 600
}
```