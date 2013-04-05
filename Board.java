import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

//import java.lang.Math;

public class Board {
	public static int REACH;

	public String[][] board;
	public int[] x_pos;
	public int[] o_pos;
	public Board parent;
	public List<Board> children;


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

		x_pos = new int[2];
		x_pos[0] = 0;
		x_pos[1] = 0;

		o_pos = new int[2];
		o_pos[0] = 7;
		o_pos[1] = 7;
		children = new LinkedList<Board>();
		REACH = 2;

	}

	public Board(Board board2) {
		board = new String[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = board2.board[i][j];
			}
		}
		x_pos = new int[2];
		x_pos[0] = board2.x_pos[0];
		x_pos[1] = board2.x_pos[1];

		o_pos = new int[2];
		o_pos[0] = board2.o_pos[0];
		o_pos[1] = board2.o_pos[1];
		parent = board2;
		children = new LinkedList<Board>();

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

	public String opp(String player)
	{
		if (player.equals("x"))
			return "o";
		else
			return "x";
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

	/*
	 * params: 	String player - player
	 * 			x, y - coordinates, starting at zero (so as normally done)
	 */
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

		int r_pos, c_pos;
		if (player.equals("x")) {
			r_pos = x_pos[0];
			c_pos = x_pos[1];
		} else {				// player is "o"
			r_pos = o_pos[0];
			c_pos = o_pos[1];
		}

		int r_dist = Math.abs(r_pos - x);
		int c_dist = Math.abs(c_pos - y);

		if (r_dist == 0 && c_dist == 0) {
			System.out.println("Need to move to another spot!");
			return false;
		}

		if ( (x < r_pos) && (y == c_pos)) { 	// North?
			// check each square above "x"
			for (int i = 1; i <= r_dist; i++) {
				if (isFilled(r_pos-i, y))
					return false;
			} 
			return true; // checked each square above "x", no obstructions
		} 
		// South?
		else if ( (x > r_pos) && (y == c_pos)) {
			// check each square below "x"
			for (int i = 1; i <= r_dist; i++) {
				if (isFilled(r_pos+i, y))
					return false;
			}
			return true; // checked each square above "x", no obstructions
		}
		// West?
		else if ( (x == r_pos) && (y < c_pos)) {
			// check each square left of "x"
			for (int i = 1; i <= c_dist; i++) {
				if (isFilled(x, c_pos-i))
					return false;
			}
			return true; // checked each square above "x", no obstructions
		}
		// East?
		else if ( (x == r_pos) && (y > c_pos)) {
			// check each square right of "x"
			for (int i = 1; i <= c_dist; i++) {
				if (isFilled(x, c_pos+i))
					return false;
			}
			return true; // checked each square above "x", no obstructions
		} 
		// Now we have to check diagonals
		else if (r_dist == c_dist) {
			// NW?
			if (x < r_pos && y < c_pos) {
				for (int i = 1; i <= c_dist; i++) {
					if (isFilled(r_pos-i, c_pos-i))
						return false;
				}
				return true; // checked squares NW of "x", no obstructions
			} 
			// NE?
			else if (x < r_pos && y > c_pos) {
				for (int i = 1; i <= c_dist; i++) {
					if (isFilled(r_pos-i, c_pos+i))
						return false;
				}
				return true; // checked squares NW of "x", no obstructions
			} 
			// SE?
			else if (x > r_pos && y > c_pos) {
				for (int i = 1; i <= c_dist; i++) {
					if (isFilled(r_pos+i, c_pos+i))
						return false;
				}
				return true; // checked squares NW of "x", no obstructions
			}
			// SW?
			else if (x > r_pos && y < c_pos) {
				for (int i = 1; i <= c_dist; i++) {
					if (isFilled(r_pos+i, c_pos-i))
						return false;
				}
				return true; // checked squares NW of "x", no obstructions
			} 

		}		
		return false; // Neither diagonal nor cardinal
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

		int r_pos, c_pos;
		if (player.equals("x")) {
			r_pos = x_pos[0];
			c_pos = x_pos[1];
		} else {				// player is "o"
			r_pos = o_pos[0];
			c_pos = o_pos[1];
		}

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i != 0 || j != 0){
					if (!outOfBounds(r_pos + i, c_pos + j)) {
						if (isEmpty(r_pos+i, c_pos+j))
							return false;
					}
				}
			}
		}
		return true;

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


	/*
	 * If successful, moveNew returns a new Board object with board and 
	 * positions of players updated appropriately.
	 * 
	 * If the move is not valid, moveNew returns null
	 */
	public Board moveNew(String player, int x, int y)
	{
		x--;
		y--;
		Board new_board = null;
		//implement isValid() here!
		if (isValid(player, x, y)){
			new_board = new Board(this);
			/* Note! isValid is being run AGAIN in move(...). Optimize this! */
			// whoops. need to add 1 because in move we decrement it again.
			new_board.move(player, x+1, y+1);
		}
		return new_board;
	}

	public void generateMoves(String player)
	{
		children = new LinkedList<Board>();
		int r_pos, c_pos;
		if (player.equals("x")) { 	/* "x" location */
			r_pos = x_pos[0];
			c_pos = x_pos[1];
		} else {					/* "o" location */
			r_pos = o_pos[0];
			c_pos = o_pos[1];
		}
		/* increment r_pos and c_pos because we're sending them to moveNew,
		 * which assumes the parameters are User offset, i.e. starts at 1
		 */
		r_pos++;
		c_pos++;
		/* only one move away for now */
		for (int i = -REACH; i < REACH+1; i++) {
			for (int j = -REACH; j < REACH+1; j++) {
				if (i != 0 || j != 0) {
					Board b = this.moveNew(player, r_pos+i, c_pos+j);
					if (b != null)
						children.add(b);
				}
			}

		}
	}


	public LinkedList<Board> allMovesGen(String player)
	{
		LinkedList<Board> moves= new LinkedList<Board>();
		int r_pos, c_pos;
		if (player.equals("x")) { 	/* "x" location */
			r_pos = x_pos[0];
			c_pos = x_pos[1];
		} else {					/* "o" location */
			r_pos = o_pos[0];
			c_pos = o_pos[1];
		}
		/* increment r_pos and c_pos because we're sending them to moveNew,
		 * which assumes the parameters are User offset, i.e. starts at 1
		 */
		r_pos++;
		c_pos++;
		/* only one move away for now */
		for (int i = -REACH; i < REACH+1; i++) {
			for (int j = -REACH; j < REACH+1; j++) {
				if (i != 0 || j != 0) {
					Board b = this.moveNew(player, r_pos+i, c_pos+j);
					if (b != null)
						moves.add(b);
				}
			}

		}
		return moves;
	}



	public int eval(String player)
	{
		/* base case 1: if player has lost, return negative MAX */
		if (lose(player))
			return -Integer.MAX_VALUE;
		/* base case 2: if opp has lost, return positive MAX */
		else if (lose(opp(player)))
			return Integer.MAX_VALUE;
		else {


			int empty_spaces = 0;
			int r_pos, c_pos;
			if (player.equals("x")) { 	/* "x" location */
				r_pos = x_pos[0];
				c_pos = x_pos[1];
			} else {					/* "o" location */
				r_pos = o_pos[0];
				c_pos = o_pos[1];
			}
			for (int i = -2; i < 3; i++) {
				for (int j = -2; j < 3; j++) {
					/* if spot in question is our center (main),
					 * or is out of bounds, skip
					 */
					if ((i == 0 && j == 0) || outOfBounds(r_pos+i, c_pos+j))
						; 
					/* else if empty, increment count */
					else if (isEmpty(r_pos+i, c_pos+j))
						empty_spaces++;
				}
			}
			
			return empty_spaces;
		}
	}





	public Pair<Integer, Board> BESTMOVE(int depth, int MyBest, int HerBest, String player)
	{

		Pair<Integer, Board> p;

		if (depth == 0) {
			p = new Pair<Integer, Board>(eval(player), null);
			return p;
		} else if (lose(player)) {
			p = new Pair<Integer, Board>(-Integer.MAX_VALUE, this);
			return p;
			/* base case 2: if opp has lost, return positive MAX */
		} else if (lose(opp(player))) {
			p = new Pair<Integer, Board>(Integer.MAX_VALUE, this);
			return p;
		} else {

			LinkedList<Board> move_list = allMovesGen(player);

			int best_score = MyBest;
			Board best_move = null;

			while (move_list.size() > 0) {

				Pair<Integer, Board> Try = move_list.getFirst().BESTMOVE(depth-1, -HerBest, -MyBest, opp(player));
				int tryscore = -Try.getL();
				if (tryscore >= best_score) {
					best_score = tryscore;
					best_move = move_list.getFirst();
				}

				if (best_score > HerBest) {
					p = new Pair<Integer, Board>(best_score, best_move); // alpha-beta pruning here
					return p;
				}

				move_list.removeFirst();

			} // out of while
			
			p = new Pair<Integer, Board>(best_score, best_move);
			return p;
		} // end of proc
	}




	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Board asdf = new Board();
		System.out.println("Enter your player: ");
		Scanner scanner = new Scanner( System.in );
		String player = scanner.nextLine(); 
		Pair<Integer, Board> p;
		// set x up to lose
//		asdf.move("x", 2, 1);
//		asdf.move("x", 2, 2);
//		asdf.move("x", 2, 3);
//		asdf.move("x", 2, 4);
//		asdf.move("x", 1, 4);
//		asdf.move("x", 1, 3);
		asdf.printBoard();
		
		
		if (player.equals("x")) {
			
			System.out.println("Enter row: ");
			int row = scanner.nextInt();
			System.out.println("Enter col:");
			int col = scanner.nextInt();
			while(!asdf.isValid(player, row-1, col-1)){
				System.out.println("Not a valid move, please try again");
				System.out.println("Enter row: ");
				row = scanner.nextInt();
				System.out.println("Enter col:");
				col = scanner.nextInt();
			}
			asdf.move(player, row, col);
			asdf.printBoard();
		}
		
		while (true){ 
			/* start the game: if player was o, computer, or "x" will go first 
			 * Remember, you are player, computer is always opp(player)
			 */
			p = asdf.BESTMOVE(5, -Integer.MAX_VALUE, Integer.MAX_VALUE, asdf.opp(player));
			asdf = p.getR();
			asdf.printBoard();
			
			if (asdf.lose(asdf.opp(player))) {
				System.out.println(player + " wins!");
				return;
			}
			/* then your turn */
			System.out.println("Enter row: ");
			int row = scanner.nextInt();
			System.out.println("Enter col:");			
			int col = scanner.nextInt();
			while(!asdf.isValid(player, row-1, col-1)){
				System.out.println("Not a valid move, please try again");
				System.out.println("Enter row: ");
				row = scanner.nextInt();
				System.out.println("Enter col:");
				col = scanner.nextInt();
			}
			asdf.move(player, row, col);
			asdf.printBoard();
			if (asdf.lose(player)) {
				System.out.println(asdf.opp(player) + " wins!");
				return;
			}
		}

	}

}


