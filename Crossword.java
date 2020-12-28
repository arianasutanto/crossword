import java.io.*;
import java.util.*;

public class Crossword
{

    private DictInterface D;
	private char [][] theBoard = null;
    StringBuilder[] rowStr,colStr;
    int maxRows;
    int maxCols;
    int [] rowMinus;
    int [] colMinus;
    int currScore=0;



   public static void main(String [] args) throws IOException
   {

   		new Crossword(args);


   }

    public Crossword(String [] args) throws IOException{
		//Read the dictionary
		Scanner fileScan = new Scanner(new FileInputStream(args[0]));
		String st;
		D = new MyDictionary();

		while (fileScan.hasNext())
		{
			st = fileScan.nextLine();
			D.add(st);
		}
		fileScan.close();



    Scanner inScan = new Scanner(new File(args[1]));
    int rows = inScan.nextInt();
    inScan.nextLine();
	int cols = rows;

    maxRows=rows;
    maxCols=cols;



    rowStr = new StringBuilder[rows];
    colStr = new StringBuilder[cols];
    
    for (int i =0; i<rows;i++){
    	rowStr[i] = new StringBuilder();
    	colStr[i] = new StringBuilder();
    }
   
	theBoard = new char[rows][cols];
	

	for (int i = 0; i < rows; i++)
	{
		String rowString = inScan.nextLine();
		for (int j = 0; j < rowString.length(); j++)
		{
				theBoard[i][j] =(rowString.charAt(j));
		}
	}
		
	inScan.close();

	
		solve(0,0);
	
	

    } //end crossword func


    
    private void solve(int row, int col){

     if (theBoard[row][col] == '+'){
	
		for(char c = 'a'; c <='z'; c ++){  //for all options (letter a - z)

		     
			if(isValid(c, row, col)){
			   // System.out.println(c);    // if that letter is valid
				rowStr[row].append(c);     //append to the stringbuilder
				colStr[col].append(c);
			

				if ((row == maxRows-1) && (col == maxCols-1)){ //if (end of board or found solution)

					for (int i = 0; i <maxRows; i++){
						currScore += score(rowStr[i]);
						System.out.println(rowStr[i].toString().toUpperCase());
					}

					System.out.println("Score: " + currScore);

					System.exit(0);
				}

				else{

					if(col<maxCols-1)		//recurse
					 {
						solve(row, col+1);
					 }
					else if(row < maxRows-1)
					{
						solve(row+1, 0);
					}
					
				}
					 
				rowStr[row].deleteCharAt(rowStr[row].length()-1); //undo
				colStr[col].deleteCharAt(colStr[col].length()-1);
				


			}//if isValid


		}//for char c
				
	}
      //if theBoard

	else if (theBoard[row][col] == '-')
	{

		rowStr[row].append('-');     //append to the stringbuilder
		colStr[col].append('-');


		if ((row == maxRows-1) && (col == maxCols-1)){ //if (end of board or found solution)

			for (int i = 0; i <maxRows; i++){
				currScore += score(rowStr[i]);
				System.out.println(rowStr[i].toString().toUpperCase());
			}

				System.out.println("Score: " + currScore);

				System.exit(0);
			}

		else{

			if(col<maxCols-1)		//recurse
			{
				solve(row, col+1);
			}
			else if(row < maxRows-1)
			{
				solve(row+1, 0);
			}
					
		}
					 
		rowStr[row].deleteCharAt(rowStr[row].length()-1); //undo
		colStr[col].deleteCharAt(colStr[col].length()-1);
		

	}//end else if '-'
	

	
	else if (Character.isLetter(theBoard[row][col])){

		char n = theBoard[row][col];
		
		if(isValid(n,row,col)){

		rowStr[row].append(theBoard[row][col]); //append to the stringbuilder
		colStr[col].append(theBoard[row][col]);
		
			

			if ((row == maxRows-1) && (col == maxCols-1)){ //if (end of board or found solution)

				for (int i = 0; i <maxRows; i++){
					currScore += score(rowStr[i]);
					System.out.println(rowStr[i].toString().toUpperCase());
				}

					System.out.println("Score: " + currScore);
					System.exit(0);
			}

			else{

				if(col<maxCols-1)		//recurse
				{
					solve(row, col+1);
				}
				else if(row < maxRows-1)
				{
					solve(row+1, 0);
				}
					
			}
					 
				rowStr[row].deleteCharAt(rowStr[row].length()-1); //undo
				colStr[col].deleteCharAt(colStr[col].length()-1);


		}//end if isValid for n,row,col

	}//end character.isLetter

  }// end curr solve

    
	private boolean isValid(char c, int row, int col){ //handle '-' use second search prefix
		
		StringBuilder tempRow = new StringBuilder(rowStr[row]);     
		StringBuilder tempCol = new StringBuilder(colStr[col]);
		char cc = c;
		tempRow.append(cc);
		tempCol.append(cc);

		int rowRes;
		int colRes;

		if(tempRow.toString().contains("-")) //if there is a '-' use second search prefix method with last index of '-'
		{
			rowRes = D.searchPrefix(tempRow,(tempRow.lastIndexOf("-"))+1,(tempRow.length()-1));
		}
		else
			rowRes = D.searchPrefix(tempRow);

		if(tempCol.toString().contains("-"))
		{
			colRes = D.searchPrefix(tempCol,tempCol.lastIndexOf("-")+1,(tempCol.length()-1));
		}
		else
			colRes = D.searchPrefix(tempCol);

		

//------------------------------------------------------------------------------------------------------------------------------- checking for validity starts

		//If j is not an end index, then rowStr[i] + the letter a must be a valid prefix in the dictionary
        if ((col < maxCols-1 ) && (rowRes == 0 || rowRes== 2)){
        	
        	return false;
        }

        

       //If j is an end index, then rowStr[i] + the letter must be a valid word in the dictionary
        if ((col == maxCols-1) && (rowRes == 0 || rowRes == 1)){
        	 
        	return false;
        }
        
       //If i is not an end index, then colStr[j] + the letter must be a valid prefix in the dictionary
        if((row < maxRows-1) && (colRes == 0 || colRes== 2)){
        	 
        	return false;
        }

        
      //If i is an end index, then colStr[j] + the letter must be a valid word in the dictionary
        if ((row == maxRows-1) && (colRes == 0 || colRes== 1)){
        	
        	return false;
        }



        return true;
	}// end isValid

	private int score(StringBuilder s){

	int point=0;

		for ( int j = 0; j < s.length(); j++ ) 
         {
     		switch(s.charAt(j))
     		{
     			case 'q':
     			case 'z':
					point += 10;
					break;
					
     			case 'j':
     			case 'x':
					point += 8;
					break;

     			case 'k':
					point += 5;
					break;

     			case 'f':
     			case 'h':
     			case 'v':
     			case 'w':
     			case 'y':
					point += 4;
					break;
					
     			case 'b':
     			case 'c':
     			case 'm':
     			case 'p':
					point += 3;
					break;

     			case 'd':
     			case 'g':
					point += 2;
					break;

				case '-':
					point +=0;
					
				default:
					point += 1;
					
     		}//end switch-case structure

       	 }//end point calculation loop

       	 return point;
}//end score


  
}





