import java.util.*;
import java.io.*;
import java.math.*;
import java.awt.*;

public class DLB implements DictInterface  
{
	public boolean isEmpty;
	protected LinkedList root; 
	private int size; 
	private final char SENTINEL='*';
	private Node iteratorNode;
	private LinkedList iteratorList;
	
	public DLB()
	{
		this.isEmpty=true;
	}
	public DLB(MyDictionary M)
	{
		this.isEmpty=true;
	}
	public DLB (char c)
	{
		this.isEmpty=true;
		String s = new String(new Character(c).toString());
		add(s);
	}
	public DLB(String s)
	{
		this.isEmpty=true;
		this.add(s);
	}
	public DLB(StringBuilder s)
	{
		this.isEmpty=true;
		this.add(s.toString());
	}

	public boolean add(String s)
	{
		int pos=-1;
		if (this.isEmpty==true)  
		{
			LinkedList prevList = new LinkedList(s.charAt(0));
			this.root = prevList;
			for (int i = 1; i < s.length(); i++) 	
			{
				LinkedList curList = new LinkedList(s.charAt(i)); 
				prevList.head.childNode = curList.head; 	
				prevList = curList;
			} 	
			LinkedList sentinelList = new LinkedList(this.SENTINEL);
			prevList.head.childNode=sentinelList.head;
			this.isEmpty=false; 	
			return true;
		}
		else
		{
			int letterFound = 0;
			int temp=0;
			Node curNode = this.root.head;
			for (int i = 0; i<=s.length()-1; i++) 
			{
				
				if ((i==s.length()-1) && curNode.data==s.charAt(i)) 
				{
					
					if (curNode.childNode.data!=this.SENTINEL) 
					{
						curNode = curNode.childNode; 
						if (curNode.siblingNode==null) 
						{
							curNode.siblingNode = new Node(this.SENTINEL);
							
							return true;
						}
						else 
						{
							Node prevNode=curNode;
							while (curNode.siblingNode!=null)
							{
								
								if (curNode.data==this.SENTINEL)
								{
									System.out.println("add() error: String has already been added to the DLB.");
									return false;
								}
								
								curNode = curNode.siblingNode;
							}
							curNode.siblingNode=new Node(this.SENTINEL);
							curNode=curNode.siblingNode;
							
							return true;
						}
					}
					else 
					{
						System.out.println("add() error: String has already been added to the DLB.");
						return false;
					}
				}
				if (curNode.data!=s.charAt(i))
				{
					if (curNode.siblingNode!=null) 
					{
						while (curNode.siblingNode!=null) 
						{
							
							curNode = curNode.siblingNode;
							if (curNode.data==s.charAt(i)) 
							{
								
								letterFound=1;
								break;	
							}
							
						}
						if (letterFound==0)
						{
							curNode.siblingNode = new Node(s.charAt(i));
							curNode = curNode.siblingNode;
							temp = i+1;
							break; 
						}
						else
						{
							letterFound=0;	
						}
					}
					else 
					{
						
						curNode.siblingNode = new Node(s.charAt(i));
						curNode=curNode.siblingNode;
						temp = i+1;
						break; 
					}
				}
				
				curNode = curNode.childNode;
			}
			
			if (temp>s.length()-1)
			{
				LinkedList endList = new LinkedList(this.SENTINEL);
				curNode.childNode=endList.head;
				return true;
			}
			else if (temp==s.length()-1)
			{
				LinkedList newList = new LinkedList(s.charAt(temp));
				curNode.childNode = newList.head;
				curNode = curNode.childNode;
				LinkedList endList = new LinkedList(this.SENTINEL);
				curNode.childNode=endList.head;
				return true;
			}
			else
			{
				
				for (int i = temp; i<=s.length()-1; i++) 
				{
					LinkedList newList = new LinkedList(s.charAt(i));
					curNode.childNode=newList.head;
					curNode=curNode.childNode;
				}
				LinkedList endList = new LinkedList(this.SENTINEL);
				curNode.childNode=endList.head;
				return true;
			}
		}
	}

	public int searchPrefix(StringBuilder s)
	{
		iteratorList = this.root;
		iteratorNode = this.root.head;
		boolean isPrefix = false;
		boolean isWord = false;
		for (int i = 0; i<=s.length()-1; i++) 
		{
			if ((i==s.length()-1) && (searchList(iteratorList, s.charAt(i))==true))
			{
					isPrefix=true;
					iteratorNode = iteratorNode.childNode;
					iteratorList=iteratorNode.thisList;
					int iteratorCount=0;
					while (iteratorNode.siblingNode!=null)
					{
						iteratorCount++;
						iteratorNode=iteratorNode.siblingNode;
					}
					if(findSentinel(iteratorList)==true)
					{
						isWord=true;
						if(iteratorCount<1)
							isPrefix=false;
						break;						
					}
			}
			else if (searchList(iteratorList, s.charAt(i))==true) 
			{
				iteratorNode = iteratorNode.childNode;
				iteratorList = iteratorNode.thisList;
			}
		}
		if (isWord==true && isPrefix==true)
		{
			return 3;
		}
		else if (isWord==true && isPrefix!=true)
		{
			return 2;
		}
		else if (isWord!=true && isPrefix==true)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	public int searchPrefix(StringBuilder s, int start, int end)
	{
		iteratorList = this.root;
		iteratorNode = this.root.head;
		boolean isPrefix = false;
		boolean isWord = false;
		for (int i = start; i<=end; i++) 
		{
			if ((i==end) && (searchList(iteratorList, s.charAt(i))==true)) 
			{
					isPrefix=true;
					iteratorNode = iteratorNode.childNode;
					iteratorList=iteratorNode.thisList;
					int iteratorCount=0;
					while (iteratorNode.siblingNode!=null)
					{
						iteratorCount++;
						iteratorNode=iteratorNode.siblingNode;
					}
					if(findSentinel(iteratorList)==true)
					{
						isWord=true;
						if(iteratorCount<1)
							isPrefix=false;
						break;						
					}
			}
			else if (searchList(iteratorList, s.charAt(i))==true) 
			{
				iteratorNode = iteratorNode.childNode;
				iteratorList = iteratorNode.thisList;
			}
		}
		if (isWord==true && isPrefix==true)
		{
			return 3;
		}
		else if (isWord==true && isPrefix!=true)
		{
			return 2;
		}
		else if (isWord!=true && isPrefix==true)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	protected boolean findSentinel(LinkedList curList)
	{
		iteratorList = curList;
		iteratorNode = curList.head;
		if (searchList(curList, this.SENTINEL)!=true)
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}
	protected boolean searchList(LinkedList curList, char c)
	{
		if(iteratorList.head.data==c)
		{
			return true;
		}
		else
		{
			while (iteratorNode!=null)
			{
				if(iteratorNode.data==c)
				{
					return true;
				}
				iteratorNode=iteratorNode.siblingNode;
			}
			return false;
		}
	}
	public void print(String s) 
	{
		int endFlag=-1;
		Node curNode = this.root.head;
		LinkedList curList = this.root;
		
		for (int i = 0; i<=s.length()-1;i++) 
		{
			if (curNode!=null)
			{
				do
				{

					if (curNode.data==s.charAt(i)) 
					{
						System.out.print(s.charAt(i));
						endFlag=0; 
						break;
					}
					else
					{
						
					}
					curNode=curNode.siblingNode;
				
				} while(curNode!=null);
				
			}
			if(endFlag==-1) 
			{
				System.out.println("\nERROR: String \"" + s + "\" is not contained within DLB.");
				break;
			}
			else if(endFlag==0) 
			{
				curNode = curNode.childNode;
				endFlag=-1; 
			}				
		}
	}
	protected void printRoot() 
	{
		if (this.isEmpty!=true)
		{
			System.out.println("The following characters are stored in the root of the Linked List: ");
			Node curNode = root.head;
			System.out.println(curNode.data);
			if (curNode.siblingNode!=null)
			{
				while (curNode.siblingNode!=null)
				{
					curNode=curNode.siblingNode;
					System.out.println(curNode.data);
				}
			}
						
		}
		else
		{
			System.out.println("DLB is empty.");
		}

		
	}
	protected boolean insertSentinel(Node curNode) 
	{	
		if(curNode.childNode.getData()!=this.SENTINEL)
		{
			curNode = curNode.childNode; 
			while (curNode.siblingNode!=null) 
			{
				curNode = curNode.siblingNode;
			}
			curNode.siblingNode = new Node(this.SENTINEL);
			return true;
		}
		else
		{
			return false;
		}
	}
	private void createList(char c) 
	{
		LinkedList newList = new LinkedList(c);
	}
	private void insert(char c, LinkedList curList) 
	{
		Node tempNode = curList.head;
		while (tempNode!=null)
		{
			tempNode = tempNode.siblingNode;
		}
		tempNode = new Node(c);
	}

	protected class LinkedList 
	{
		public Node head; 
		public int i;
		public LinkedList thisList = this;
		public int size;
	
		public LinkedList()
		{
		
		}
		public LinkedList(char c)
		{
			Node newNode = new Node(c);
			this.head = newNode;
			newNode.thisList=this;
		}
		
		public void insert(char c)
		{
		
		}
	
	}
	
	protected class Node
	{
		public Node siblingNode=null; 
		public Node childNode=null; 
		public char data; 
		public LinkedList thisList;
		
		public Node(char c)  
		{
			this.data = c;
		}
		
		public char getData()
		{
			return this.data;
		}
		public void setData(char c)
		{
			this.data = c;
		}
	}
}
