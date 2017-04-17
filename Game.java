public class Game {
	
	public static void main(String[] args) {
		// welcome message
		System.out.println("Welcome to Battleship! You will be playing against\n"
				+ "a CPU with randomized ship layous and shots. After their setup,\n"
				+ "you will be asked to set up your ships by entering a starting row\n"
				+ "and column for each ship. Row 0, Column 0 is the top left corner,\n"
				+ "while Row 9, Column 9 is the bottom right. Ships cannot overlap nor\n"
				+ "go off the edge of the board. After setup, you will get to take the first\n"
				+ "turn. At the start of each of your turns, your board will be displayed\n"
				+ "along with previous hit shots and missed shots. On your board's display,\n"
				+ "uppercase characters represent unhit cells of ships, while\n"
				+ "lowercase characters represent hit cells.\n\n"
				+ "Let's play!\n" );
		
		// initialize commanders
		Commander human = new Commander("human", false);
		Commander cpu = new Commander("cpu", true);
		
		// setup cpu's ships
		cpu.addShip("Carrier", 5);
		cpu.addShip("Battleship", 4);
		cpu.addShip("Cruiser", 3);
		cpu.addShip("Submarine", 3);
		cpu.addShip("Destroyer", 2);
		
		// have player setup ships
		human.addShip("Carrier", 5);
		human.addShip("Battleship", 4);
		human.addShip("Cruiser", 3);
		human.addShip("Submarine", 3);
		human.addShip("Destroyer", 2);

		// game
		boolean gameOn = true;
		while (gameOn) {
			gameOn = human.takeTurn(cpu);
			if (!gameOn) break;
			gameOn = cpu.takeTurn(human);
		}
	}	
}


