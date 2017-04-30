PrismaTD is a tower defence game for Android and Desktop.

# Repo Contents

* PrismaTD for Desktop Java (tested with Ubuntu 16.04)
* PrismaTD for Android (tested with Android 6.0.1)

# Repo Organisation

* android         - all Android related files
* bin             - binary files (optional)
* doc             - all documentation related files
* doc/screenshots - screenshots
* gfx             - graphics used within the game
* issues          - bug tracker and feature requests (uses "ditz" distributed issue tracker)
* towerdef        - src to the game engine and the Desktop Java version
* README          - this file

# Building / Running

Gradle is used as build system.

## Ubuntu

Use IntelliJ IDEA to build and run.

The following libraries are required:
`sudo apt install libjogl2-java`

## Android

Use Android Studio to build and run.

# Further Information

The files in `./doc/` are plain text files but are best viewed with zim-wiki.
All available information on how to build, run, modify, ... is located there.

# Screenshots
![PrismaTD Screenshot](/doc/screenshots/screenshot-2017-04-30.png?raw=true)

More screenshots can be found in `./doc/screenshots/`.

# License

All code and content created within this project should be licensed GPL2+.
Look at the individual file headers for definite information.
