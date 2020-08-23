# TournamentProject

## Getting started
1. Download [**Tournament.jar**](https://raw.githubusercontent.com/LOOHP/TournamentProject/master/Tournament.jar) from this repository
2. Make sure you have at least Java 8 installed on your computer
3. Run Tournament.jar (preferably in an empty folder), either by **double clicking** or by using
```
java -Xms1024M -Xmx1024M -jar Tournament.jar
```
4. A folder named **builds** will be created, containing all the latest program files for the tournament program.

## Launching the tournament server
1. Run the **TournamentServer.jar**, if you wish to launch the server without a gui, use the following start up flag `--nogui`
***
Normal launch (Same as **double clicking** the jar file)
```
java -Xms1024M -Xmx1024M -jar TournamentServer.jar
```
No GUI launch
```
java -Xms1024M -Xmx1024M -jar TournamentServer.jar --nogui
```

## Importing players
There are two import modes
- File
- Terminal

For the file input mode, players will be read from the file `configs/players.yml`, instructions on how to use the players.yml are printed as comments in the file itself.
***
The commands are
```
import <file/terminal> [-r (replace)]
```
## Starting a competition
Run the following command
```
start
```

## Exiting the program
Closing the program incorrectly might cause data to not be saved, to properly safe all data before exit, use the following command:
```
exit
```
or
```
stop
```

## Using the tournament client
To connect to a tournament server through the client, simply enter the **host and the port (default: 1720)** and hit connect.

## Using the help command
Simply by running `help` will list all the commands with its description, entering each command with the flag `--help` or incorrectly will display its usage.

***
## Protocol Specifications
[See the wiki page](https://github.com/LOOHP/TournamentProject/wiki/Protocol)
