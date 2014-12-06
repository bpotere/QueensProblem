package QueensProblem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.*;

public class ChessBoard {

	private final static Queens Q8 = new Queens();
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] buttonsMatrix = new JButton[8][8];
    private JPanel chessBoard;
    private final JLabel message = new JLabel(
            "Place 8 queens on the board so that they do not attack each other.");
    //private static final String COLS = "ABCDEFGH";
    
    private ImageIcon QueenIcon = new ImageIcon("C:\\users\\bryan\\eclipse_workspace\\QueenIcon.jpg");
    JButton butReset = new JButton("RESET");
    private ImageIcon HintIcon = new ImageIcon("C:\\users\\bryan\\eclipse_workspace\\HintIcon.jpg");
    JButton butHint = new JButton("HINT");
    
    ChessBoard() {
        initializeGui();
    }

    public final void initializeGui() {
        // set up the main GUI
        gui.setBorder(new EmptyBorder(2, 2, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);

        myHandler h = new myHandler();
        butReset.addActionListener(h);
        butHint.addActionListener(h);
        tools.add(butReset); // TODO - add functionality!
        tools.add(butHint); // TODO - add functionality!
        //tools.add(new JButton("Restore"));
        tools.addSeparator();
        
        tools.addSeparator();
        tools.add(message);

        //gui.add(new JLabel("?"), BorderLayout.LINE_START);

        chessBoard = new JPanel(new GridLayout(0, 9));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(chessBoard);

        // create the chess board squares
        Insets buttonMargin = new Insets(0,0,0,0);
        for (int a = 0; a < buttonsMatrix.length; a++) {
            for (int b = 0; b < buttonsMatrix[a].length; b++) {
                JButton mybutton = new JButton();
                mybutton.setMargin(buttonMargin);
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..
                //ImageIcon icon = new ImageIcon(
                //        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                //mybutton.setIcon(icon);
                mybutton.setPreferredSize(new Dimension(64,64));
                
                mybutton.addActionListener(h);
                if (b % 2 == 1 && a % 2 == 1){
                	mybutton.setBackground(Color.WHITE);
                } else if(b % 2 == 0 && a % 2 == 0){
                    mybutton.setBackground(Color.WHITE);
                } else {
                    mybutton.setBackground(Color.GRAY);
                }
                buttonsMatrix[b][a] = mybutton;
            }
        }
        /*
        //fill the chess board
        chessBoard.add(new JLabel(""));
        // fill the top row
        for (int h = 0; h < 8; h++) {
            chessBoard.add(
                    new JLabel(COLS.substring(h, h + 1),
                    SwingConstants.CENTER));
        }*/
        // fill the black non-pawn piece row
        for (int i = 0; i < 8; i++) {
        	chessBoard.add(new JLabel());
            for (int j = 0; j < 8; j++) {
                chessBoard.add(buttonsMatrix[j][i]);
                
            }
        }
    }

    public final JComponent getChessBoard() {
        return chessBoard;
    }

    public final JComponent getGui() {
        return gui;
    }
    
    public void resetBoard(){
    	Q8.reset();
    	
    	for (int i = 0; i < 8; i++){
    		for (int j = 0; j < 8; j++){
    			buttonsMatrix[i][j].setIcon(null);
    		}
    	}
    	
    	message.setText("Place 8 queens on the board so that they do not attack each other.");
    }
    
    public static void main(String[] args) {
    	
    	Q8.solver(args);
    	
        Runnable r = new Runnable() {

      	
            @Override
            public void run() {
                ChessBoard cb =
                        new ChessBoard();

                JFrame f = new JFrame("8 Queens Problem");
                f.add(cb.getGui());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // ensures the minimum size is enforced.
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
    
    private class myHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == butReset){
				resetBoard();
			}
			
			if (arg0.getSource() == butHint){
				java.util.List<int[]> solutions = Q8.getSolutions();
				
				int hintSquareX = -1;
				int hintSquareY = -1;
				Iterator<int[]> it = Q8.mySolutions.iterator();
				while (it.hasNext()){
					int matches = 0;
					int[] temparray = it.next();
					for (int i = 0; i < 8; i++){
						if(	Q8.iRowQueenPositions[i] == temparray[i]){
							matches++;
						}
					}
					if (matches == Q8.iQueensPlaced){
						int j = 0;
						while (j < 7 && hintSquareX == -1){
							if(Q8.iRowQueenPositions[j] == -1){
								
								hintSquareX = temparray[j];
								hintSquareY = j;
							}
							j++;
						}
						if(arg0.getSource() == butHint && hintSquareX != -1){
							buttonsMatrix[hintSquareY][hintSquareX].setIcon(HintIcon);
							return;
						}
					}
				}
				
				if(hintSquareX == -1){
					Toolkit.getDefaultToolkit().beep();
				}
				
			}//End if for Hint button
			
			for(int j = 0; j < 8; j++){
				for(int i = 0; i < 8; i++){
					if (arg0.getSource() == buttonsMatrix[j][i]){
						System.out.printf("Button %d, %d clicked\n", i, j);
						if (Q8.getbChessBoard()[j][i]){
							Q8.setAttackSquares(j, i, false);
							buttonsMatrix[j][i].setIcon(QueenIcon);
							if (Q8.iQueensPlaced == 8){
								message.setText("Problem solved!");
							}
						}
						else if(Q8.getiRowQueenPosition()[j] == i ){
							Q8.setAttackSquares(j,  i, true);
							buttonsMatrix[j][i].setIcon(null);
						}
						else{
							//add beep
							Toolkit.getDefaultToolkit().beep();
						}
					}
				}
			}
		}
		
	}
}