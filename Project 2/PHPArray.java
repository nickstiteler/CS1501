import java.util.*;

//Nicholas Stiteler CS/COE 1501
//Assignment 2
// All warnings at compile time can be ignored
//runs without error

public class PHPArray<V> implements Iterable<V>
{
	private int cap; //capacity
	private int ele; //# of elements
	private boolean hashy; 
	private Node[] hashTable;
	private Node tail;
	private Node root;
	private Iterator<Pair<V>> iteral;
	private Node nextNode;
	private Node prevNode;


	public PHPArray()
	{
		//empty constructor
	}
	public PHPArray(int size)
	{
		//constructor for given size
		this.cap = size;
		this.hashy = false;
		hashTable = (Node[]) new Node[cap];
	}
	public PHPArray(int gcap,int gele,Node[] table,Node first, Node last)
	{
		//constructor with given elements 
		this.cap = gcap;
		this.ele = gele;
		this.hashTable = table;
		this.root = first;
		this.tail = last;
		this.hashy = false;
	}

	//node class
	private static class Node<V extends Comparable<V>> implements Comparable<V>
	{
		private Node nextNode;
		private Node prevNode;

		private Pair<V> val;

		Node()
		{
			this.nextNode = null;
			this.prevNode = null;
			this.val = null;
		}
		Node(Pair<V> keyVal)
		{
			this.nextNode = null;
			this.prevNode = null;
			this.val = new Pair<V>(keyVal.key,keyVal.value);
		}
		@Override
		public int compareTo(V obj)
		{
			V currVal = this.val.value;
			V newVal = obj;
			if ((!(currVal instanceof Comparable) || !(newVal instanceof Comparable))) {
                throw new IllegalArgumentException("NO SOUP FOR YOU");
            }
            return currVal.compareTo(obj);
		}
	}

	@Override
	public Iterator<V> iterator()
	{
		Iterator<V> iter = new Iterator<V>()
		{
			Node itNode = root;

			@Override
			public boolean hasNext()
			{
				return itNode != null;
			}
			@Override
			public V next()
			{
				Node temp = itNode;
				itNode = itNode.nextNode;
				return (V)temp.val.value;
			}
		};
		return iter;
	}
	public Iterator<Pair<V>> pIterator()
	{
		Iterator<Pair<V>> iter = new Iterator<Pair<V>>()
		{
			Node itNode = root;

			@Override
			public boolean hasNext()
			{
				return itNode != null;
			}
			@Override
			public Pair<V> next()
			{
				Node temp = itNode;
				itNode = itNode.nextNode;
				return temp.val;
			}
		};
		return iter;
	}


	//for storing key and values into a pair
	public static class Pair<V>
	{
		public String key;
		public V value; 
		Pair()
		{
			this(null,null);
		}
		Pair(String str, V val)
		{
			this.key = str;
			this.value = val;
		}
	}

	public void reset()
	{
		iteral = this.pIterator();
	}
	public Pair<V> each()
	{
		if(iteral == null)
		{
			reset();
		}
		if(iteral.hasNext())
		{
			return (Pair<V>) iteral.next();
		}
		return null;
	}

	private int hash(String key)
	{
		return (key.hashCode() & 0x7fffffff)%cap;
	}

	public int length()
	{
		return this.cap;
	}

	public void put(String key,V kval)
	{
		if(kval == null)
		{
			unset(key);
		}

		if(ele>=cap/2)
		{
			resize(cap*2);
		}
		int i = 0;
		for(i = hash(key); hashTable[i] != null; i = (i+1)%cap)
		{
			if(hashTable[i].val.key.equals(key))
			{
				hashTable[i].val.value = kval;
				return;
			}
		}
		Pair<V> tempPair = new Pair<V>(key,kval);
		hashTable[i] = new Node(tempPair);

		if(hashy)
		{
			hashTable[i].nextNode = this.nextNode;
			if(hashTable[i].nextNode != null)
			{
				hashTable[i].nextNode.prevNode = hashTable[i];
			}

			hashTable[i].prevNode = this.prevNode;
			if(hashTable[i].prevNode != null)
			{
				hashTable[i].prevNode.nextNode = hashTable[i];
			}
			hashy = false;
			return;
		}
		if(root == null)
		{
			root = hashTable[i];
			tail = root;
			return;
		}

		hashTable[i].prevNode = tail;
		hashTable[i].nextNode = null;

		tail.nextNode = hashTable[i];
		tail = hashTable[i];
		ele++;
	}
	public void put(int key, V kval)
	{
		put(String.valueOf(key),kval);
	}
	public void resize(int size)
	{
		System.out.println("\t\tSize: "+ele+" -- resizing array from "+cap+" to "+size);
		PHPArray<V> temp = new PHPArray<V>(size);
		Node newNode = root;

		while(newNode != null)
		{
			temp.put(newNode.val.key,(V)newNode.val.value);
			newNode = newNode.nextNode;
		}

		this.cap = size;
		this.ele = temp.ele;
		this.root = temp.root;
		this.tail = temp.tail;
		this.hashTable = Arrays.copyOf(temp.hashTable,size);
	}
	public void unset(String key)
	{
		if(!contains(key))
		{
			return;
		}

		int i = hash(key);
		while(!(key.equals(hashTable[i].val.key)))
		{
			i = (i+1)%cap;
		}

		if(root == tail)
		{
			root = null;
		}
		else if(root == hashTable[i])
		{
			root = root.nextNode;
			root.prevNode = null;
		}
		else if(tail == hashTable[i])
		{
			tail = hashTable[i].prevNode;
		}
		else
		{
			hashTable[i].nextNode.prevNode = hashTable[i].prevNode;
			hashTable[i].prevNode.nextNode = hashTable[i].nextNode;
		}

		hashTable[i] = null;
		i = (i+1)%cap;

		while(hashTable[i] != null)
		{
			System.out.println("\tKey " + hashTable[i].val.key + " rehashed...\n");
			String hashee = hashTable[i].val.key;
			V valer = (V)hashTable[i].val.value;

			this.nextNode = hashTable[i].nextNode;
			this.prevNode = hashTable[i].prevNode;

			hashTable[i] = null;

			ele = ele-1;
			hashy = true;

			put(hashee,valer);
			i = (i +1)%cap;
		}
		ele--;

		if(ele>0 && ele<=cap/8)
		{
			resize(cap/2);
		}
	}
	public void unset(int key)
	{
		Integer str = new Integer(key);
		unset(str.toString());
	}
	public boolean contains(String key)
	{
		return get(key) != null;
	}
	public V get(String key)
	{
		for(int i = hash(key);hashTable[i] != null;i = (i+1) %cap)
		{
			if(hashTable[i].val.key.equals(key))
			{
				return (V)hashTable[i].val.value;
			}
		}
		return null;
	}
	public V get(int key)
	{
		Integer str = new Integer(key);
		return get(str.toString());
	}
	public ArrayList<String> keys()
	{
		Node show = root;
		ArrayList<String> kshow = new ArrayList<>();

		while(show != null)
		{
			kshow.add(show.val.key);
			show = show.nextNode;
		}
		return kshow;
	}
	public ArrayList<V> values()
	{
		Node show = root;
		ArrayList<V> vshow = new ArrayList<>();

		while(show != null)
		{
			vshow.add((V)show.val.value);
			show = show.nextNode;
		}
		return vshow;
	}
	public void showTable()
	{
		System.out.println("\tRaw Hash Table Contents:");
		String keykey;
		V valval;
		for(int i = 0;i<hashTable.length;i++)
		{
			if(hashTable[i] == null)
			{
				System.out.println(i + ": null");
			}
			else
			{
				keykey = hashTable[i].val.key;
				valval = (V)hashTable[i].val.value;
				System.out.println(i+ ": Key: "+keykey+" Value: "+valval);
			}
		}
	}
	public void sort()
	{
		root = mergeSort(root);

		Node newKey  = root;
		PHPArray<V> sorted = new PHPArray<V>(cap);

		for(int i = 0;newKey != null; i++)
		{
			sorted.put(String.valueOf(i),(V)newKey.val.value);
			newKey = newKey.nextNode;
		}

		this.cap = sorted.cap;
        this.ele = sorted.ele;
        this.root = sorted.root;
        this.tail = sorted.tail;
        this.hashTable = Arrays.copyOf(sorted.hashTable, sorted.cap);
	}
	private Node mergeSort(Node n)
	{
		Node temp = n;

		if(temp == null || temp.nextNode == null)
		{
			return temp;
		}
		Node curr = getMid(temp);
		Node right = curr.nextNode;
		curr.nextNode = null;

		return merge(mergeSort(temp),mergeSort(right));
	}
	private Node merge(Node left, Node right)
	{
		Node temp;
		Node curr;
        temp = new Node<>();
        curr = temp;

        while (left != null && right != null) 
        {
            if (left.compareTo((Comparable)right.val.value) < 1) 
            {
                curr.nextNode = left;
                left = left.nextNode;
            } 
            else 
            {
                curr.nextNode = right;
                right = right.nextNode;
            }
            curr = curr.nextNode;
        }
        if (left == null) 
        {
            curr.nextNode = right;
        } 
        else 
        {
            curr.nextNode = left;
        }

        return temp.nextNode;
	}
	private Node getMid(Node curr)
	{
		if(curr == null)
		{
			return curr;
		}
		Node right;
		Node left;
		left = curr;
		right = left;
		while(left.nextNode != null && left.nextNode.nextNode != null)
		{
			right = right.nextNode;
			left = left.nextNode.nextNode;
		}
		return right;
	}
	public void asort()
	{
		root = mergeSort(root);

		Node newKey  = root;
		PHPArray<V> sorted = new PHPArray<>(cap);

		for(int i = 0;newKey != null; i++)
		{
			sorted.put(newKey.val.key,(V)newKey.val.value);
			newKey = newKey.nextNode;
		}

		this.cap = sorted.cap;
        this.ele = sorted.ele;
        this.root = sorted.root;
        this.tail = sorted.tail;
        this.hashTable = Arrays.copyOf(sorted.hashTable, sorted.cap);
	}
	public PHPArray<String> array_flip()
	{
		PHPArray<String> flipper = new PHPArray<>(cap);
		Node flippee = root;

		while(flippee != null)
		{
			flipper.put((String) flippee.val.value,flippee.val.key);
			flippee = flippee.nextNode;
		}
		return flipper;
	}


}

