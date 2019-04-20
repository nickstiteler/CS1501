//	Nicholas Stiteler
//  Project 1
//  CS/COE 1501
//  Sherif Khattab
// Enter program java crossword (board name) (dictionary name) (DLB for my version and 
// anything else for the standard version)

// Has trouble finding solutions for boards with walls and letters

import java.io.*;
import java.util.*;
import java.util.Timer;


public class Crossword //board dict interface rowstr colstr row col boardlength alpha
{
	public static DictInterface dict;
	public static char[][] board;
	public static int[][] valBoard;
	public static StringBuilder[] rowStr;
	public static StringBuilder[] colStr;
	public static int row =0;
	public static int col = 0;
	public static int size;
	public static char[] alpha;

	public static boolean solutions;
	public static int counter;

	public Crossword(String inTXT, String dictTxt,String type)throws Exception
	{
		Scanner dictScan = null;
		Scanner boardScan = null;

		try
		{
			dictScan = new Scanner(new FileInputStream(dictTxt));
		}
		catch(FileNotFoundException fnf)
		{
			System.out.println("Error: Dict not found");
			System.out.println("Enter: \"java Crossword PUZZLE_NAME DICT_NAME\"");
			System.exit(0);
		}
		try
		{
			boardScan = new Scanner(new FileInputStream(inTXT));
		}
		catch(FileNotFoundException fnf)
		{
			System.out.println("Error: Puzzle not found");
			System.out.println("Enter: \"java Crossword PUZZLE_NAME DICT_NAME\"");
			System.exit(0);
		}
		if(type.equals("DLB"))
		{
			dict = new DLB();
			solutions = true;
		}
		else
		{
			dict = new MyDictionary();
			solutions = false;
		}	

		String firstLine = boardScan.nextLine();
		size = Integer.parseInt(firstLine);
		colStr = new StringBuilder[size];
		rowStr = new StringBuilder[size];

		board = new char[size][size];
		valBoard = new int[size][size];

		for(int i = 0;i<size;i++)
		{
			rowStr[i] = new StringBuilder();
		}
		for(int i = 0;i<size;i++)
		{
			colStr[i] = new StringBuilder();
		}


		for(int i = 0; i<size;i++)
		{
			String arr=boardScan.next();
			for(int j = 0;j<size;j++)
			{
				board[i][j] = arr.charAt(j);
				rowStr[i].append(arr.charAt(j));
				colStr[j].append(arr.charAt(j));

				if(arr.charAt(j) == '+')
				{
					valBoard[i][j] = 0; //space is empty
				}
				else if (arr.charAt(j) == '-')
				{
					valBoard[i][j] = -1; // space is filled
				}
				else
				{
					valBoard[i][j] = 1; // space has a letter
				}

			}	
		}

		String str = new String();

		while(dictScan.hasNext()) // adds all the words from the dict file to the dict data structure
		{
			str = dictScan.next();
			dict.add(str);
		}

		dictScan.close();
		boardScan.close();

		row = 0;
		col = 0;

		for(int i=0;i<size;i++)
		{
			for(int j = 0;j<size;j++)
			{
				System.out.print(board[i][j]);
			}
			System.out.print("\n");
		}

		alpha = "abcdefghijklmnopqrstuvwxyz".toCharArray();

	}
	public static void main(String[] args) throws Exception
	{
		Crossword cross = new Crossword(args[0],args[1],args[2]);

		

		cross.solveBoard(row,col);

		

		if(args[2].equals("DLB"))
		{
			System.out.println();
			for(int j=0; j<size; j++)
			{
				for(int k=0; k<size; k++)
				{
					System.out.print(board[j][k] + " ");
				}
				System.out.println();
			}
			System.out.println(counter +" Solutions found");
		}
		else
		{
			System.out.println("No solution found");
		}
	}
	public static void solveBoard(int row,int col)
	{
		//long startTime = System.nanoTime();
		//long elapsedTime;
		boolean count = false;
		do
		{
			if(valBoard[row][col] != 0)
			{
				if(col == size-1 && row == size-1)
				{
					if(solutions)
					{
						if(counter%10000 == 0)
						{
							for(int j=0; j<size; j++)
							{
								for(int k=0; k<size; k++)
								{
									System.out.print(board[j][k] + " ");
								}
								System.out.println();
							}
							System.out.println("Solution number: " +counter);
							//elapsedTime = (System.nanoTime()-startTime);
							//System.out.println("Run Time: "+elapsedTime);
						}
						else
						{
							counter++;
						}
						break;
						
					}
					else
					{
						System.out.println();
						for(int j=0; j<size; j++)
						{
							for(int k=0; k<size; k++)
							{
								System.out.print(board[j][k] + " ");
							}
							System.out.println();
						}
						//elapsedTime = (System.nanoTime()-startTime);
						//System.out.println("Run Time: "+elapsedTime);
						System.exit(0);
					}
					
				}
				col = (col+1)%size;
				if(col == 0 && row<size-1)
				{
					row++;
				}
			}
			else
			{
				count = true;
			}
		}while(!count);

		for(int i = 0;i<26;i++)
		{
			boolean legal = isSafe(i,row,col);

			if(legal == true)
			{
				char tempChar = board[row][col];
				board[row][col] = alpha[i];
				rowStr[row].deleteCharAt(col);
				colStr[col].deleteCharAt(row);
				rowStr[row].insert(col,alpha[i]);
				colStr[col].insert(row,alpha[i]);

				if(row == size-1 && col == size-1)
				{
					if(solutions)
					{
						counter++;
						for(int j=0; j<size; j++)
						{
							for(int k=0; k<size; k++)
							{
								System.out.print(board[j][k] + " ");
							}
							System.out.println();
						}
						System.out.println("Solution number: " +counter);
						//elapsedTime = (System.nanoTime()-startTime);
						//System.out.println("Run Time: "+elapsedTime);
					}
					else
					{
						System.out.println();
						for(int j=0; j<size; j++)
						{
							for(int k=0; k<size; k++)
							{
								System.out.print(board[j][k] + " ");
							}
							System.out.println();
						}
						//elapsedTime = (System.nanoTime()-startTime);
						//System.out.println("Run Time: "+elapsedTime);
						System.exit(0);
					}
				}
				else if(col == size-1)
				{
					solveBoard(row+1,0);
				}
				else
				{
					solveBoard(row,col+1);
				}
				rowStr[row].deleteCharAt(col);
				colStr[col].deleteCharAt(row);
				rowStr[row].insert(col,'+');
				colStr[col].insert(row,'+');
				board[row][col]=tempChar;
			}
		}
	}	
	public static boolean isSafe(int a,int row,int col)
	{
		boolean legalRow = false;
		boolean legalCol = false;

		StringBuilder temp1 = rowStr[row];
		StringBuilder temp2 = colStr[col];

		int rowst = row;
		int colst = col;
		int firstR;
		int firstC;
		int lastR;
		int lastC;

		char tempRow = temp1.charAt(col);
		char tempCol = temp2.charAt(row);

		temp1.deleteCharAt(col);
		temp2.deleteCharAt(row);
		temp1.insert(col,alpha[a]);
		temp2.insert(row,alpha[a]);
		

		while(rowst>=1 && temp1.charAt(rowst-1) != '-')
		{
			rowst--;
		}
		firstR=rowst;
		rowst=col;

		while(rowst<size-1 && temp1.charAt(rowst+1)!='-' && temp1.charAt(rowst+1)!='+')
		{
			rowst++;	
		}
		lastR =rowst;

		while(colst>=1 && temp2.charAt(colst-1) != '-')
		{
			colst--;	
		}

		firstC=colst;
		colst=row;

		while(colst<size-1 && temp2.charAt(colst+1)!='-' && temp2.charAt(colst+1)!='+')
		{
			colst++;	
		}
		lastC=colst; 

		int rowSafe = dict.searchPrefix(temp1,firstR,lastR);
		int colSafe = dict.searchPrefix(temp2,firstC,lastC);

		temp1.deleteCharAt(col);
		temp2.deleteCharAt(row);

		temp1.insert(col,tempRow);
		temp2.insert(row,tempCol); 

		if(col ==size-1 && rowSafe == 3)
		{
			legalCol = true;
		}
		else if(col == size-1 && rowSafe ==2)
		{
			legalCol = true;
		}
		else if(col<size-1 && rowSafe != 0)
		{
			if(lastR<size-1)
			{
				if(rowSafe==1 && temp1.charAt(lastR+1) == '-')
				{
					return false; 
				}
				else if(rowSafe==2 && temp1.charAt(lastR+1) == '+')
				{
					return false; 
				}
				else
				{
					legalCol = true;	
				}
			}
			else
			{
				legalCol = true;
			}
			
		}

		if(row==size-1 && colSafe==3)
		{
			legalRow=true;
		}
		
		else if(row==size-1 && colSafe==2)
		{
			legalRow =true;
		}
		
		else if(row<size-1 && colSafe!=0)
		{
			if(lastC<size-1)
			{
				if(colSafe==1 && temp2.charAt(lastC+1) == '-')
				{
					return false; 
				}
				else if(colSafe==2 && temp2.charAt(lastC+1) == '+')
				{
					return false;	
				}
				else
				{
					legalRow= true;
				}
			}
			else
			{
				legalRow = true;
			}	
		}
		
		if(legalRow && legalCol)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}

