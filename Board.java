//import java.lang.Math;


public class Board {
	public String[][] board;
	public int[] x_pos = {0, 0};
	public int[] o_pos = {7, 7};

	public Board()
	{
		board = new String[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = "-";
			}
		}
		board[0][0] = "x";
		board[7][7] = "o";
	}

	public void printBoard()
	{
		System.out.print("    ");
		for (int h = 1; h < 9; h++) {
			System.out.print(h + " ");
		}
		System.out.println();
		for (int i = 0; i < 8; i++) {
			System.out.print((i+1) + " [ ");
			for (int j = 0; j < 8; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println(" ]");
		}
		System.out.println();
	}

	public boolean isFilled(int x, int y)
	{
		if (board[x][y].equals("-"))
			return false;

		return true;
	}

	public boolean isEmpty(int x, int y)
	{
		return !isFilled(x, y);
	}

	public boolean isValid(String player, int x, int y)
	{
		/*
		 *  if there is an obstacle in the path to destination (x, y),
		 *  return false.
		 */
		// for this first go, we assume all given moves are valid "queen" moves
		// or legitimate cardinal directions (N, NE, E, SE...so on)

		if (outOfBounds(x, y))
			return false;

		if (player.equals("x")) {
			
			int r_dist = Math.abs(x_pos[0] - x);
			int c_dist = Math.abs(x_pos[1] - y);

			if (r_dist == 0 && c_dist == 0) {
				System.out.println("Need to move to another spot!");
				return false;
			}

			if ( (x < x_pos[0]) && (y == x_pos[1])) { 	// North?
				// check each square above "x"
				for (int i = 1; i <= r_dist; i++) {
					if (isFilled(x_pos[0]-i, y))
						return false;
				} 
				return true; // checked each square above "x", no obstructions
			} 
			// South?
			else if ( (x > x_pos[0]) && (y == x_pos[1])) {
				// check each square below "x"
				for (int i = 1; i <= r_dist; i++) {
					if (isFilled(x_pos[0]+i, y))
						return false;
				}
				return true; // checked each square above "x", no obstructions
			}
			// West?
			else if ( (x == x_pos[0]) && (y < x_pos[1])) {
				// check each square left of "x"
				for (int i = 1; i <= c_dist; i++) {
					if (isFilled(x, x_pos[1]-i))
						return false;
				}
				return true; // checked each square above "x", no obstructions
			}
			// East?
			else if ( (x == x_pos[0]) && (y > x_pos[1])) {
				// check each square right of "x"
				for (int i = 1; i <= c_dist; i++) {
					if (isFilled(x, x_pos[1]+i))
						return false;
				}
				return true; // checked each square above "x", no obstructions
			} 
			// Now we have to check diagonals
			else if (r_dist == c_dist) {
				System.out.println("Diagonals!");
				// NW?
				if (x < x_pos[0] && y < x_pos[1]) {
					for (int i = 1; i <= c_dist; i++) {
						if (isFilled(x_pos[0]-i, x_pos[1]-i))
							return false;
					}
				}

			}

		} else {

		}

		return true;
	}

	public boolean outOfBounds(int x, int y)
	{
		if (x > 7 || x < 0 || y > 7 || y < 0)
			return true;

		return false;
	}

	/*
	 * lose returns true if the given player has lost, i.e. has been "isolated"
	 * Our strategy here is to go around to each of the neighboring squares and
	 * see if any of them are empty
	 */
	public boolean lose(String player)
	{
		// outOfBounds(int x, int y)

		if (player.equals("x")) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i != 0 && j != 0){
						if (!outOfBounds(x_pos[0] + i, x_pos[1] + j)) {
							if (isEmpty(x_pos[0]+i, x_pos[1]+j))
								return false;
						}
					}
				}
			}
			return true;
		} else {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i != 0 && j != 0){
						if (!outOfBounds(o_pos[0] + i, o_pos[1] + j)) {
							if (isEmpty(o_pos[0]+i, o_pos[1]+j))
								return false;
						}
					}
				}
			}
			return true;
		}
	}

	/*
	 * implements a player ("x" or "o") making a move.
	 * 
	 * Returns:  if valid, modifies board.
	 *           otherwise, prints out error message.
	 */
	public void move(String player, int x, int y)
	{
		x--;
		y--;
		//implement isValid() here!
		if (isValid(player, x, y)){
			if (player.equals("x")) {
				// replace x's pos with "*"
				board[x_pos[0]][x_pos[1]] = "*";
				// board now has new position of x
				board[x][y] = "x";
				// update x's registered position
				x_pos[0] = x;
				x_pos[1] = y;
			} else if (player.equals("o")){
				board[o_pos[0]][o_pos[1]] = "*";
				board[x][y] = "o";
				o_pos[0] = x;
				o_pos[1] = y;
			} else {
				System.out.println("error: invalid player");
			}
		} else
			System.out.println("Not valid");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Board asdf = new Board();
		asdf.printBoard();

		


	}

}


