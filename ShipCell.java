import java.awt.Point;

// Represents a "cell" or unit of a ship in the game
public class ShipCell {
	public enum Status {
		HIT, SAFE
	}
	
	Point location;
	Status status;
	
	// Given a row and column, initializes a new ShipCell
	// with the given row, column, and SAFE status
	public ShipCell(int row, int col) {
		location = new Point(row, col);
		status = Status.SAFE;
	}
	
	@Override 
	public String toString() {
		return "(" + (int)this.location.getX() + ", " + (int)this.location.getY() +  ", " + this.status + ")";
	}
}
