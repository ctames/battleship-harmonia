Here are the sources files for my small battleship game. The game is organized as follows:
* Game.java - Main method, sets up players and ships, and loops around turns until win.
* Ship.java - Class representing a ship in the game. Methods for checking for hits, sinks, and collisions during ship placement. Effectively a wrapper around a list of ShipCells.
* ShipCell.java - Simple class representing a cell of a ship by its location status.
* Commander.java - Class representing a player in the game. Works for CPU and human players. Workhorse class of the game. Has methods for player turns, adding ships, displaying the board, and checking for win conditions. 



