import java.util.List;
import java.util.ArrayList;
import java.awt.Point; 

// Representation of a ship in the game
// Effectively a wrapper around a list of ShipCells
public class Ship {
	public enum Orientation {
		HORIZONTAL, VERTICAL
	}
	
	String name; 
	int length, row, col;
	List<ShipCell> cells; 
	boolean sunk;
	
	 // Given a starting row, column and orientation, initializes the ship by
	 // populating the "cells" ArrayList with ShipCells
	public Ship(String name, int length, int row, int col, Orientation orientation) {
		this.name = name;
		this.length = length;
		this.row = row;
		this.col = col;
		this.sunk = false;
		this.cells = new ArrayList<ShipCell>();
		// if placed horizontally, extend to the right by increasing column value
		if (orientation == Orientation.HORIZONTAL) {
			for (int i = 0; i < this.length; i++) {
				this.cells.add(new ShipCell(row, col+i));
			}
		}
		// if placed vertically, extend downwards by increasing row value
		else {
			for (int i = 0; i < this.length; i++) {
				this.cells.add(new ShipCell(row+i, col));
			}
		}
	}
	
	// Given another ship, check to see if the ships overlap
	// This is a utility used by the ship placement method
	// in the Commander class
	// returns true on overlap, otherwise false
	public boolean checkForCollision(Ship otherShip) {
		for (int i = 0; i < otherShip.length; i++) {
			Point otherLocation = otherShip.cells.get(i).location;
			for (int j = 0; j < this.length; j++) {
				Point location = this.cells.get(j).location;
				if (otherLocation.equals(location)) return true;
			}
		}
		return false;
	}
	
	// Checks for a hit given a row and column.
	// If a SAFE cell is hit, changes that cell's status
	// to hit and returns true
	// Otherwise return false
	public boolean checkHit(int row, int col) {
		Point tempPoint = new Point(row, col);
		for (int i = 0; i < length; i++) {
			ShipCell cell = this.cells.get(i);
			if (tempPoint.equals(cell.location) && cell.status == ShipCell.Status.SAFE) {
				cell.status = ShipCell.Status.HIT;
				return true;
			}
		}
		return false;
	}
	
	// This method is provided to be called after checkHit returns 
	// true in the Commander's takeTurn method, as well as to 
	// help check for the game win condition in the Commander's 
	// chekForAllSunk method. 
	public boolean checkSunk() {
		if (this.sunk) return this.sunk;
		for (int i = 0; i < this.length; i++) {
			if (this.cells.get(i).status == ShipCell.Status.SAFE) return false;
		}
		this.sunk = true;
		return this.sunk;
	}
	
	@Override
	public String toString() {
		String retval = "Ship \"" + this.name + "\": ";
		for (ShipCell cell : this.cells) {
			retval = retval + cell.toString() + " ";
		}
		return retval;
	}
}
