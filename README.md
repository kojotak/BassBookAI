# Bass Book 

## implementation

Using local LM studio with model qwen3-30b-a3b-2507

Create a web application in java for storing play along videos on youtube for the bass guiter.

## data model

### channel

Channel represents youtube channel and has an id and a display name.

### artist

Artist has a name and contains a couple of songs.

### song

Song has a name and a reference to an artist

### tuning

Bass guitar tuning is a enumeration of tunings, such as EADG, DADG, CGCF for 4 string or BEADG for 5 string bass guitar.

### technique

Represents one particular playing technique used in given video, such as slap, hammer on, pull of, pick, slide, double stop and so on.

### tag

Represents one of the states stored by logged in user:

- PLAY user can play this video witout any poblem
- PRACTICE user can play this video, but want to practice
- SLOWDOWN user can not play this video at full speed
- TODO represents song to be investigated
- FORGET user knows this song but does not want to play it anymore 

### video

Youtube video has an id (youtube video id), reference to a song of given artist and a reference to a channel. Optional attributes are tuning, list of technique, bpm (beats per minute) and tag (only for logged in user).

## application structure

### main page

The main page displays table of all videos in tabular fashion allowing to filter and paginate songs. One row of the table represents one songs with the following columns:

- song name
- artist name
- number of youtube channels having a video for this song
- tunings
- techniques
- tag (visible only for logged in user)

One row can be expanded to display all youtube channels displaying tuning and techniques used for each particular youtube channel. Only one row can be expanded at given time, so expading one column collapses any previous one.

There is a table filter allowing to full text song name, select artist, tuning, technique or tag. Each filter change refreshes song table.

There is a pagination displaying navigation controls under the main table.

If user is logged in, tag column is visible an the user can change the tag for given song.

### form page

Form page allows to paste a youtube video URL. After that, the URL is parsed and youtube channel and video names are parsed. The form allows to add new video into database. The user can select an artist in combo box. Next to it, there is an add button allowing to add new artist. Parsed video title is used as a name to edit. After saving new artist, the combobox preselects new artist and allows to select song from a combo box. This combobox contains only songs for given artist. There is an add button next to song combo box allowing to add new song for given artist. Parsed video title is used as a name to edit. After saving new song, the combobox preselects new song and allows to select tuning and multi selecte techniques. Optionally, bpm can be set as well. Last optional field is the tag. After that the save button saves the new youtube video.

### header

There is a header containing link to login using 3rd party provider - github. After login, the header displays the currently logged in user name and button for logout. In main page, the tag column is visible with values stored by given user. Moreover, there is a link to the form page.

## programming guide

Make the github integration optional. If Oath is not set, allow the application to start without any error but disable user login.

Do not use Lombok.
