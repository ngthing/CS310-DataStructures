public class Node
{
	protected Node prev;
	protected Node next;
	protected int data;
	protected final static int NULL = -1;

	// Constructor initializes data members.
	// Target Complexity: O(1)
	public Node(int data, Node prev, Node next)
	{
		this.prev = prev;
		this.next = next;
		this.data = data;
	}

	// default constructor. initialize a node with default data, prev, and next
	// Constructor call init()
	public Node ()
	{
		init ();
	}

	// Constructor, create a node with given data
	public Node (int x)
	{
		this(x, null,null);
	}

	//Initialize data members. This method is called by constructor and remove methods
	public void init()
	{
		this.prev = null;
		this.next = null;
		this.data = NULL;
	}

	// Create a pretty representation of the Node
	// Format: [data]. Example: [3]
	// Target Complexity: O(1)
	public String toString()
	{ 
		String result = "[" + this.data + "]";
		return result;
	}

} 