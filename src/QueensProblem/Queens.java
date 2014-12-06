package QueensProblem;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;
import java.awt.GridLayout;

import javax.swing.*;




public class Queens implements ActionListener{
	//false squares cannot have a queen on them
	static final int iQueens = 8;
	static final int iRows = 8;
	static final int iColumns = 8;
	static Boolean bChessBoard[][] = new Boolean[iRows][iColumns];
	static int iChessBoard[][] = {{22,22,22,22,22,22,22,22},
			                   	{22,24,24,24,24,24,24,22},
			                   	{22,24,26,26,26,26,24,22},
			                   	{22,24,26,28,28,26,24,22},
			                   	{22,24,26,28,28,26,24,22},
			                   	{22,24,26,26,26,26,24,22},
			                   	{22,24,24,24,24,24,24,22},
			                   	{22,22,22,22,22,22,22,22}};
	Random rand = new Random();
	public int iQueensPlaced = 0;
	public class Tuple<X, Y> { 
		  public X x; 
		  public Y y; 
		  public Tuple(X x, Y y) { 
		    this.x = x; 
		    this.y = y; 
		  } 
	}
	
	//One way to keep track of queen positions is as an array of (x, y) tuples
	//representing the column and row in the matrix
	Tuple[] tQueenPositions = new Tuple[iQueens];
	// Another way to keep track of queen positions: 
	// A queen position array of row positions -- indexes are columns
	public static int iRowQueenPositions[] = new int[iQueens];

	public static List<int[]> mySolutions = new ArrayList<int[]>();
	
	
	public Boolean[][] getbChessBoard(){
		return bChessBoard;
	}
	public int[] getiRowQueenPosition(){
		return iRowQueenPositions;
	}
	public List<int[]> getSolutions(){
		return mySolutions;
	}
	
	public void solver(String args[]){
		Queens Q8 = new Queens();
		Q8.reset();
		
		System.out.printf("Using brute force to find solutions...\n");
		long bruteTime = System.currentTimeMillis();
		Q8.exhaustive(0);
		bruteTime = System.currentTimeMillis() - bruteTime;
		Q8.printSolutions();
		Q8.reset();
		
		System.out.printf("\nSeeking a solution using an elimination heuristic...\n");
		long heuristicTime = System.currentTimeMillis();
		Q8.heuristicSolution();
		//ChessBoard ch = new ChessBoard();
		//ch.launchHeuristic(args);
		heuristicTime = System.currentTimeMillis() - heuristicTime;
		Q8.printBoard();
		Q8.reset();
		
		System.out.printf("\nSeeking a random solution...\n");
		long randomTime = System.currentTimeMillis();
		Q8.randomSolution();
		randomTime = System.currentTimeMillis() - randomTime;
		Q8.reset();
		
		
		
		System.out.printf("\n");
		System.out.printf("The brute force algorithm took %d milliseconds\n", bruteTime);
		System.out.printf("The random placement algorithm took %d milliseconds\n", randomTime);
		System.out.printf("The heuristic algorithm took %d milliseconds\n", heuristicTime);
		
		
		//final Swing8Queens myChessFrame = new Swing8Queens();
		//myChessFrame.launch(args);
	}
	
	/*initialize the chess board*/
	public void reset(){
		iQueensPlaced = 0;
		for(int i = 0; i < iRows; i++){
			iRowQueenPositions[i] = -1;
			for(int j = 0; j < iColumns; j++){
				bChessBoard[i][j] = true;
				iChessBoard[i][j] = 0;
			}
			
			tQueenPositions[i] = new Tuple<>(-1, -1);
		}
		int temp0[] = {22,22,22,22,22,22,22,22};
		iChessBoard[0] = temp0;
		int temp1[] = {22,24,24,24,24,24,24,22};
		iChessBoard[1] = temp1;
		int temp2[] = {22,24,26,26,26,26,24,22};
		iChessBoard[2] = temp2;
		int temp3[] = {22,24,26,28,28,26,24,22};
		iChessBoard[3] = temp3;
		int temp4[] = {22,24,26,28,28,26,24,22};
		iChessBoard[4] = temp4;
		int temp5[] = {22,24,26,26,26,26,24,22};
		iChessBoard[5] = temp5;
        int temp6[] = {22,24,24,24,24,24,24,22};
        iChessBoard[6] = temp6;
        int temp7[] = {22,22,22,22,22,22,22,22};
        iChessBoard[7] = temp7;
	}
	
	/* randomSolution
	 * Randomly places a queen on some square in each row
	 * until it finds a solution.
	 */
	public void randomSolution(){
		int placedQueens = 0;
		while (placedQueens < 8){
			//Reset everything
			reset();
			placedQueens = 0;
			
			//Initialize an array with the possible column numbers to choose from
			int ColNums[] = new int[iColumns];
			for (int h = 0; h < iColumns; h++){
				ColNums[h] = h;
			}
			
			//Place a queen in each row
			for (int i = 0; i < iRows; i++){
				
				//Choose a column randomly from those that are available:
				int randomIndex = rand.nextInt((ColNums.length - 1 - 0)) + 0;
				int colNum = ColNums[randomIndex];
				//Remove that column number from the available column numbers:
				//int tempColNums[] = new int[ColNums.length - 1];
				for (int c = colNum; c < ColNums.length  - 1; c++){
					ColNums[c] = ColNums[c + 1];
					//This shifts everything down, copying over the removed element
				}
				ColNums = Arrays.copyOfRange(ColNums, 0, ColNums.length);  //Cut off the last indexed element

				//Now attempt to place the queen
				if (bChessBoard[i][colNum]){
					tQueenPositions[i].x = colNum;
					tQueenPositions[i].y = i;
					setAttackSquares(i, colNum, false);
					placedQueens++;
					//System.out.printf("\n");
				}
				
			}
			//printBoard();
		}
		System.out.printf("Random solution found!:\n");
		printBoard();
	}
	
		
	boolean isSafe(int queen_num, int row_pos){
		for (int i = 0; i < queen_num; i++){
			
			//Get the next queen's row position
			int other_row_position = iRowQueenPositions[i];
			
			//Check diagonals:
			if (other_row_position == row_pos 
					|| other_row_position == row_pos - (queen_num - i)
					|| other_row_position == row_pos + (queen_num - i) )
					return false;
		}
		return true;
	}
	
	/* exhaustive
	 * brute force to find a solution
	 * */
	public void exhaustive(int placed){
		if(placed  == iQueens){
			for (int j = 0; j < iQueens; j++){
				setAttackSquares(j, iRowQueenPositions[j], false);
				tQueenPositions[j].y = j;
				tQueenPositions[j].x = iRowQueenPositions[j];
			}
			int temp[] = new int[iQueens];
			for (int i = 0; i < temp.length; i++){
				temp[i] = iRowQueenPositions[i];
			}
			mySolutions.add(temp);
			//printBoard();
			//System.out.printf("\n");
		}
		else{
			for (int i = 0; i < iQueens; i++){
				if (isSafe(placed, i)){
					iRowQueenPositions[placed] = i;
					exhaustive(placed + 1);
				}
			}
		}
	}
	
	/* setAttackSquares
	 * Purpose:
	 * 		When a queen is placed or removed from a square, set that square
	 * and all the attacked squares of that row, column, and diagonals to true
	 * (a queen can be placed there) or false (attacked by a queen and a queen
	 * can't be placed there). 
	 * Preconditions:
	 * 		y is the row coordinate to place/remove a queen
	 * 		x is the column coordinate to place/remove a queen
	 * 		bAttacked is true for removing a queen, false for placing a queen
	 * */
	public void setAttackSquares(int y, int x, Boolean bAttacked){
		iRowQueenPositions[y] = x;
		
		bChessBoard[y][x] = bAttacked;
		for (int i = 0; i < iRows; i++){
			bChessBoard[y][i] = bAttacked;
		}
		for (int j = 0; j < iColumns; j++){
			bChessBoard[j][x] = bAttacked;
		}
		int X = x;
		int Y = y;
		while (X > 0 && Y > 0){
			X--; Y--;
		}
		/*first diagonal -- upper left to lower right*/
		int k = X;
		for (int l = Y; l < iRows && k < iColumns; k++, l++){
			bChessBoard[l][k] = bAttacked;
		}
		
		X = x;
		Y = y;
		while (X > 0 && Y < iRows - 1){
			X--; Y++;
		}
		/*second diagonal -- upper right to lower left*/
		int n = Y;
		for (int m = X; m < iColumns && n > 0; m++, n--){
			bChessBoard[n][m] = bAttacked;
		}
		//printBoard();
		//System.out.printf("\n");
		if(bAttacked){
			iQueensPlaced--;
		}
		else{
			iQueensPlaced++;
		}
	}
	
	public void heuristicSolution(){
		
		int placedQueens = 0;
		
		while(placedQueens < 7){
			int tempRowNum = 0;
			int tempColNum = 0;
			int tempSquareElim = Integer.MAX_VALUE;
			for(int i = 0; i < iRows; i++){
				//find the lowest elimination square 
				
				for(int j = 0; j < iColumns; j++){
					if(iChessBoard[i][j] < tempSquareElim && iChessBoard[i][j] > 0){
						tempSquareElim = iChessBoard[i][j];
						tempRowNum = j;
						tempColNum = i;
					}
				}
			}
			//Place the queen here
			tQueenPositions[placedQueens].y = tempColNum;
			tQueenPositions[placedQueens].x = tempRowNum;
			setAttackSquares(tempColNum, tempRowNum, false);
			//TODO Update the square numbers here
			setHeuristicSquares(tempRowNum, tempColNum, 0);
			setElimCounts();
			placedQueens++;
			
			/*
			for (int k = 0; k < iColumns; k++){
				for (int m = 0; m < iRows; m++){
					System.out.printf("%d\t", iChessBoard[k][m]);
				}
				System.out.printf("\n");
			}
			System.out.printf("\n");
			*/
		}
		if(placedQueens < 7){
			System.out.printf("Error: the heuristic algorithm only placed %d\n", iQueens);
			
		}	
	}
	
	public void setElimCounts(){
		
		for (int i = 0; i < iColumns; i++){
			for (int j = 0; j < iRows; j++){
				//The coordinates of the current square that is being checked
				//is the (j, i) pair.
				
				iChessBoard[i][j] = heuristicCalculator(i, j);
			}
		}
	}
	
	public int heuristicCalculator(int y, int x){
		int iCounter = 0;
		if (!bChessBoard[y][x]){
			return iCounter;
		}
				
		// Counting the non-false column squares that aren't [i][j]
		for (int a = 0; a < iColumns; a++) {
			if (a != y && bChessBoard[a][x]) {
				iCounter++;
			}
		}
		// Counting the row
		for (int b = 0; b < iRows; b++) {
			if (b != x && bChessBoard[y][b]) {
				iCounter++;
			}
		}
		// Count the upper left to lower right diagonal
		int d = x;
		int c;
		// Lower right diagonal
		for (c = y; c < iRows && d < iColumns; c++, d++) {
			if (c != x && d != y && bChessBoard[c][d]) {
				iCounter++;
			}
		}

		d = x;
		// Upper left diagonal
		for (c = y; c > 0 && d > 0; c--, d--) {
			if (c != x && d != y && bChessBoard[c][d]) {
				iCounter++;
			}
		}
		// Count the upper right to lower left diagonal
		// Lower left diagonal
		d = x;
		for (c = y; c < iRows && d > 0; c++, d--) {
			if (c != x && d != y && bChessBoard[c][d]) {
				iCounter++;
			}
		}
		// Upper right diagonal
		d = x;
		for (c = y; c > 0 && d < iColumns; c--, d++) {
			if (c != x && d != y && bChessBoard[c][d]) {
				iCounter++;
			}
		}

		
		return iCounter;
		
	}
	
	public void setHeuristicSquares(int y, int x, int iNum){
		iChessBoard[y][x] = iNum;
		for (int i = 0; i < iRows; i++){
			iChessBoard[y][i] = iNum;
		}
		for (int j = 0; j < iColumns; j++){
			iChessBoard[j][x] = iNum;
		}
		int X = x;
		int Y = y;
		while (X > 0 && Y > 0){
			X--; Y--;
		}
		/*first diagonal -- upper left to lower right*/
		int k = X;
		for (int l = Y; l < iRows && k < iColumns; k++, l++){
			iChessBoard[l][k] = iNum;
		}
		
		X = x;
		Y = y;
		while (X > 0 && Y < iRows - 1){
			X--; Y++;
		}
		/*second diagonal -- upper right to lower left*/
		int n = Y;
		for (int m = X; m < iColumns && n > 0; m++, n--){
			iChessBoard[n][m] = iNum;
		}
		//printBoard();
		//System.out.printf("\n");
		iQueensPlaced++;
	}
	
	public void printBoard(){
		/*
		for (int a = 0; a < iRows; a++){
			for (int b = 0; b < iColumns; b++){
				System.out.printf("%b ", bChessBoard[a][b]);
			}
			System.out.printf("\n");
		}
		*/
		for(int i = 0; i < iRows; i++){
			for (int j = 0; j < iColumns; j++){
				Boolean QueenSquare = false;
				for (int k = 0; k < iQueens; k++){
					if((int)tQueenPositions[k].y == i && ((int)tQueenPositions[k].x) == j){
						System.out.printf("Q ");
						QueenSquare = true;
					}
					
				}
				if(bChessBoard[i][j]){
					System.out.printf("1 ");
				}
				else if (!QueenSquare){
					System.out.printf("0 ");
				}
				//System.out.printf("%b ", bChessBoard[i][j]);
			}
			System.out.printf("\n");
		}
	}

	public void printSolutions(){
		int i = 0;
		for(int[] solution : mySolutions){
			reset();
			for (int j = 0; j < iQueens; j++){
				tQueenPositions[j].y = j;
				tQueenPositions[j].x = solution[j];
				setAttackSquares(j, (int)tQueenPositions[j].x, false);
			}
			System.out.printf("Solution %d:\n", i++ + 1);
			printBoard();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}