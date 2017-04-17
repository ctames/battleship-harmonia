import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;
import java.util.Scanner;

// Represents a player of the game and his/her ships
// Their ships are stored in a list "fleet" of Ships
// Can be either a human or CPU player, and the methods
// for ship placement and taking turns act accordingly
// given each type of player
public class Commander {
	public static Scanner in = new Scanner(System.in);
	public static Random rng = new Random();
	
	List<Ship> fleet;
	List<Point> hits;
	boolean cpu;
	String name;
	
	public Commander (String name, boolean cpu) {
		this.name = name;
		this.cpu = cpu;
		fleet = new ArrayList<Ship>();
		hits = new ArrayList<Point>();
	}
	
	// Method for adding a ship to a Commander's fleet
	// Enforces legal placement for both human and cpu player
	// by making use of the Ship.checkForCollision method
	// in order to avoid overlaps as well as simple board bounds
	public void addShip(String shipName, int length) {
		boolean notPlaced = true;
		while(notPlaced) {
			boolean canPlace = true;
			int row, col, orient;
			Ship.Orientation orientation;
			
			// cpu player placement
			if (this.cpu) {
				// random row, column, orientation
				row = rng.nextInt(10);
				col = rng.nextInt(10);
				orient = rng.nextInt(2);
				// check for going over edge
				if (orient == 1) {
					orientation = Ship.Orientation.HORIZONTAL;
					if (col + length > 9) canPlace = false;
				}
				else {
					orientation = Ship.Orientation.VERTICAL;
					if (row + length > 9) canPlace = false;
				}
				Ship newShip = new Ship(shipName, length, row, col, orientation);
				// check collision
				for (Ship ship : this.fleet) {
					if (newShip.checkForCollision(ship)) canPlace = false;
				}
				// place if possible, loop again if not
				if (canPlace) {
					this.fleet.add(newShip);
					notPlaced = false;
				}
			}
			// human placement
			else {
				// prompt for row, column, orientation
				System.out.println("Now adding " + shipName + ". This ship is " + length + " units long.");
				System.out.println("Input starting row (0 - 9, growing down): ");
				row = in.nextInt();
				System.out.println("Input starting column (0 - 9, growing right): ");
				col = in.nextInt();
				System.out.println("Place ship horizontally (1) or vertically (2)? (extend to the right or downwards)");
				orient = in.nextInt();
				// check for appropriate range
				if (row < 0 | row > 9 | col < 0 | col > 9) canPlace = false;
				// check for going over edge
				if (orient == 1) {
					orientation = Ship.Orientation.HORIZONTAL;
					if (col + length > 9) canPlace = false;
				}
				else {
					orientation = Ship.Orientation.VERTICAL;
					if (row + length > 9) canPlace = false;
				}
				Ship newShip = new Ship(shipName, length, row, col, orientation);
				// check for collision
				for (Ship ship : this.fleet) {
					if (newShip.checkForCollision(ship)) canPlace = false;
				}
				// place if possible, loop again if not
				if (canPlace) {
					this.fleet.add(newShip);
					notPlaced = false;
				}
				else {
					System.out.println("Error adding ship, please try again.");
				}
			}
		}
	}
	
	// Handles a turn of a commander
	// For human turn, prints our current board and successful hits
	// Like ship placement, chooses random location for CPU's shot,
	// prompts a real user for location
	// Calls methods of Ship to check for hits, sinking
	// Returns true if the game needs to continue,
	// false if game win condition met 
	public boolean takeTurn(Commander enemy) {
		int row = -1;
		int col = -1;
		boolean invalid = true;
		
		// HUMAN TURN
		if (!this.cpu) {
			// loop until a valid shot location is entered
			while (invalid) {
				System.out.println("Your turn!");
				System.out.println("Displaying your board and hits:");
				System.out.println("BOARD");
				this.displayBoard();
				System.out.println("HITS");
				this.displayHits();
				System.out.println("Enter row for missile (0 - 9, growing down): ");
				row = in.nextInt();
				System.out.println("Enter column for missile (0 - 9, growing right): ");
				col = in.nextInt();
				if (row >= 0 && row < 10 && col >= 0 && col < 10) {
					invalid = false;
				}
				else {
					System.out.println("Invalid missle target, try again.");
				}
			}
			// For a valid shot, check for a hit in each enemy's ship
			// For a successful hit, check if ship is now sunk
			// If ship sunk, check for win
			System.out.println("Launching missile at row " + row + ", col " + col);
			for (Ship ship : enemy.fleet) {
				if (ship.checkHit(row, col)) {
					System.out.println("You hit the " + ship.name + "!");
					this.hits.add(new Point(row, col));
					if (ship.checkSunk()) {
						System.out.println("You sunk the " + ship.name + "!");
						if (enemy.checkForAllSunk()) {
							System.out.println("All opponent ships sunk! Game over, you won!");
							return false;
						}
					}	
				}		
			}
			return true;
		}
		// END HUMAN TURN
		// CPU TURN
		// same process with randomly chosen location
		else {
			System.out.println("CPU's turn!");
			row = rng.nextInt(10);
			col = rng.nextInt(10);
			System.out.println("CPU launching at row " + row + ", col " + col) ;
			for (Ship ship : enemy.fleet) {
				if (ship.checkHit(row, col)) {
					System.out.println("CPU hit the " + ship.name + "!");
					if (ship.checkSunk()) {
						System.out.println("CPU sunk the " + ship.name + "!");
						if (enemy.checkForAllSunk()) {
							System.out.println("The CPU sunk all of your ships! Game over, you lost :(");
							return false;
						}
					}	
				}
			}
			return true;
		}
		// END CPU TURN
		
	}
		
	// Called after a hit sinks a ship
	// Returns true for all sunk, false for not
	public boolean checkForAllSunk() {
		boolean allSunk = true;
		for (Ship ship : this.fleet) {
			if (!ship.checkSunk()) allSunk = false;
		}
		return allSunk;
	}
	
	// Displays a board to the console
	// Represents ship cells with first letter of name
	// SAFE cells remain uppercase, HIT cells lowercase
	public void displayBoard() {
		
		// fill 10x10 char array with spaces
		char[][] board = new char[10][10];
		for (char[] array : board) Arrays.fill(array, ' ');
		int x, y;
		char letter;
		// replace appropriate locations with correct letter
		for (Ship ship : this.fleet) {
			for (ShipCell cell: ship.cells) {
				letter = ship.name.charAt(0);
				x = (int) cell.location.getX();
				y = (int) cell.location.getY();
				if (cell.status == ShipCell.Status.HIT) letter = Character.toLowerCase(letter);
				board[x][y] = letter;
			}
		}
		
		// print out board 
		System.out.println("--------------------"); // row separator 
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				System.out.print('|'); // column separator
				System.out.print(board[row][col]);
			}
			System.out.println('|');
			System.out.println("--------------------");
		}
	}
	
	// Print out array holding successful hits
	public void displayHits() {
		if (this.hits.size() == 0) {
			System.out.println("<none>");
		}
 		for (Point p: this.hits) {
			System.out.print("(" + (int)p.getX() + "," + (int)p.getY() + ") ");
			System.out.println("");
		}
	}
}
