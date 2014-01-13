import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JOptionPane;

// If using, use for educational purposes only and don't remove the following line
// Tyler Adams - Friday June 7th, 2013
// Everything that affects the actual game goes here

public class connect {
	int board[][]= new int [6][7];// red counters in integer value
	ArrayList redsprite = new ArrayList();
	ArrayList bluesprite = new ArrayList();
	boolean over = false;	// game state
	int player=1;
	int winplayer=0;
	boolean won = false;	// have you won

	public connect(){		// initialize an instance of the connect4 game
		board= new int [6][7];
		redsprite = new ArrayList();
		bluesprite = new ArrayList();
		over = false;				// game is not over yet
	}
	public static void main (String args[]){
	}

	public void place(int pos){
		int turn=turn();			// get current turn
		int c=0; // column is currently unknown
		if(integrate.sx==465)c=1;		// depending on the position of the selector, set the column
		else if(integrate.sx==525)c=2;
		else if(integrate.sx==585)c=3;
		else if(integrate.sx==645)c=4;
		else if(integrate.sx==705)c=5;
		else if(integrate.sx==765)c=6;

		for (int x = 0; x < 6; x++) { // search every row for in this column for an empty space
			if(over==false){ // if the game isn't over
				ArrayList store = new ArrayList(0);	// temporary arraylist that holds the 2 points that constitute a tick on the board
				point first,second,third,fourth;		
				if (board[x][c] > 0) {
					// do nothing because a counter is already at this position
				} else {	// a counter can be placed at this position
					board[x][c] = turn; // the current player owns this position
					//first = new point(c - 3.5,2.5 - x, 4);	// get the first point to draw a line by measuring the length of the position
					//second = new point (c - 2.5,2.5 - x,4); // get the second point to draw a line by measuring that same position
					first = new point(c - 3.5,2 - x, 4);	// get the first point of a cross by measuring the length of the diagonal of the current position
					second = new point (c - 2.5,3 - x,4);   // get the second point by measuring the length of the other diagonal
					third = new point(c - 2.5,2 - x,4);		// third point to make the first part of an outer box for a unique cross
					fourth = new point(c - 3.5,3 - x,4); 	// fourth point to make the last part of an outer box for a unique cross
					store.add(first);			// add points together to form a polygon
					store.add(second);
					store.add(third);
					store.add(fourth);
					if (turn==1){redsprite.add(store);	// player one now graphically owns a position
					}else{ bluesprite.add(store); 	// player two now graphically owns a position
					}
					winplayer=player;			// save current player to check for a win
					nextturn();					// switch turns
					x=6; 						// break the loop
				}
			}
		}
	}

	public ArrayList getred(){
		return redsprite;
	}

	public int turn(){
		return player;
	}

	public void nextturn(){
		if(player==2){
			player--;
		}else{
			player++;
		}
	}

	public ArrayList getblue(){
		return bluesprite;
	}

	public void checkdiagur(int player){ // checks different variations of diagonal win possibilities when the line is upright
		boolean win=false;
		//dr and dc represent diagonal row and diagonal column
		for(int dr=3;dr<6;++dr){// dr checks first 4 rows (at the bottom of the grid)
			for(int dc=0;dc<4;++dc){// dc checks first 6 columns
				if((board[dr][dc]==player)&&(board[dr-1][dc+1]==player)&&(board[dr-2][dc+2]==player)&&(board[dr-3][dc+3]==player)){
					win=true;
				}
			}
		}
		if (win==true){
			win(player);
		}
	}

	public void checkdiagdr(int player){//checks different variations of diagonal win occurences when the line is downright
		boolean win=false;
		//dr and dc represent diagonal row and diagonal column
		for(int dr=0;dr<3;++dr){// dr checks last 3 rows (at the top of the grid) 
			for(int dc=0;dc<4;++dc){// dc checks first 6 columns
				if((board[dr][dc]==player)&&(board[dr+1][dc+1]==player)&&(board[dr+2][dc+2]==player)&&(board[dr+3][dc+3]==player)){ //gives non-fatal error that doesn't cause malfunctions in the game
					win=true;
				}
				if (win==true){
					win(player);
				}
			}
		}
	}
	public void checkrow(int player){ // checks different variations of row win occurences
		boolean win=false;
		for(int rowctr=0;rowctr<6;++rowctr){
			if((board[rowctr][6]==player)&&(board[rowctr][5]==player)&&(board[rowctr][4]==player)&&(board[rowctr][3]==player)){
				win=true;
			}
			else if((board[rowctr][5]==player)&&(board[rowctr][4]==player)&&(board[rowctr][3]==player)&&(board[rowctr][2]==player)){
				win=true;
			}
			else if((board[rowctr][4]==player)&&(board[rowctr][3]==player)&&(board[rowctr][2]==player)&&(board[rowctr][1]==player)){
				win=true;
			}
			else if((board[rowctr][3]==player)&&(board[rowctr][2]==player)&&(board[rowctr][1]==player)&&(board[rowctr][0]==player)){
				win=true;
			}
		}
		if (win==true){
			win(player);
		}
	}

	public void checkcol(int player){ // checks different variations of column win occurences
		boolean win=false;
		for(int colctr=0;colctr<7;++colctr){
			if((board[0][colctr]==player)&&(board[1][colctr]==player)&&(board[2][colctr]==player)&&(board[3][colctr]==player)){
				win=true;
			}
			else if((board[1][colctr]==player)&&(board[2][colctr]==player)&&(board[3][colctr]==player)&&(board[4][colctr]==player)){
				win=true;
			}
			else if((board[2][colctr]==player)&&(board[3][colctr]==player)&&(board[4][colctr]==player)&&(board[5][colctr]==player)){
				win=true;
			}	
		}
		if (win==true){
			win(player);
		}
	} 

	public void check(int player){
		checkrow(player);
		checkcol(player);
		checkdiagur(player);
		checkdiagdr(player);
	}

	public void win(int player){
		won = true;			// indicate the instance is over
		over = true;		// disable the instance
	}


}
