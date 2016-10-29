import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedArrays<T> implements Iterable<T>
{
	protected int size;                 // number of elements
	protected int nodeCount;            // number of LinkedArrayNodes
	protected int modCount;
	protected final int lengthOfArrays; // value initialized in constructor
	protected static final int DEFAULTLENGTHOFARRAYS = 16;
	protected static final int NULL = -1;
	protected LinkedArrayNode<T> head;        // dummy nodes head and tail
	protected LinkedArrayNode<T> tail;
	
	@SuppressWarnings("unchecked")
	// Workhorse constructor that initializes variables.
	// Throws IllegalArgumentException if lengthOfArray < 0.
	LinkedArrays (int lengthOfArrays)
	{
		if (lengthOfArrays<0)
		{throw new IllegalArgumentException();}
		this.size = 0;
		this.nodeCount = 0;
		this.modCount =0;
		this.lengthOfArrays = lengthOfArrays;
		head = new LinkedArrayNode<T>(lengthOfArrays);
		tail = new LinkedArrayNode<T>(lengthOfArrays);
		this.head.next = this.tail;
		this.tail.prev = this.head;
		
	}

	// Convenience constructor. Calls the workhorse constructor with 
	// DEFAULTLENGTHOFARRAYS as the argument.
	LinkedArrays()
	{
		this(DEFAULTLENGTHOFARRAYS);
	}

	// Make this LinkedArrays logically empty.
	// Target Complexity: O(1)
	// Implementation note: It is not necessary to remove() all of the 
	// elements; instead, some data members can be reinitialized.
	// Target Complexity: O(1)
	public void clear()
	{
		LinkedArrayNode<T> tmp = head;
		while(this.nodeCount >0){
			tmp.next.initializeArray(); nodeCount--;
			tmp = tmp.next;	
		}
		if (this.nodeCount != 0) { System.out.println("Something wrong! nodeCount!=0"); }
		this.head.next = this.tail;
		this.tail.prev = this.head;
		this.size = 0;
	}

	// Returns the number of elements
	// Target Complexity: O(1)
	public int size()
	{
		return this.size;
	}

	// Returns the number of LinkedArrayNodes.
	// Target Complexity: O(1)
	public int nodeCount()
	{
		return this.nodeCount;
	}

	// Returns true if there are no elements in this LinkedArrays
	// Target Complexity: O(1)
	public boolean isEmpty( )
	{
		if (this.size() == 0)
			return true;
		else return false;
	}

	// Returns the first element that equals x, or null 
	// if there is no such element.
	// Throws IllegalArgumentException if x is null.
	// Target Complexity: O(n) for n elements
	@SuppressWarnings("unchecked")
	public T getMatch(T x)
	{
		T result = null;
		if (x == null)
		{ throw new IllegalArgumentException();}
		int nodeVisit = 0; // Number of nodes that have been visited to search for an element=x
		LinkedArrayNode<T> current = head;
		if (nodeCount>0)
		{
			while (nodeVisit < nodeCount)
			{
				current = current.next; nodeVisit++;
				if (current.getMatch(x)!= null)
				{ return current.getMatch(x); }
			}
		}
		
		
		return result;
	}

	// Returns true if this LinkedArrays contains the specified element.
	// Throws IllegalArgumentException if x is null. May call getMatch.
	// Target Complexity: O(n)
	public boolean contains (T x)
	{
		if (x == null)
		{ throw new IllegalArgumentException();}
		T result = this.getMatch(x);
		if (result == null){
			return false;
		}
		else return true; 
	}

	// Insert x into the first LinkedArrayNode with an available space in 
	// its array. 
	// Returns true if x was added.
	// Throws IllegalArgumentException if x is null.
	// Target Complexity: O(number of nodes)
	public boolean add(T x)
	{
	
		if (x == null)
		{ throw new IllegalArgumentException();}
		
		if (hasAvailableSpace())
		{
			getNextAvailableNode().add(x);
			modCount++; size++;
			//System.out.println(this.toString());
			return true;
		}
		else if (!hasAvailableSpace())
		{
			if (nodeCount == 0)
			{
				LinkedArrayNode<T> node = new LinkedArrayNode<T>(head,tail,lengthOfArrays);
				head.next = node;
				tail.prev = node;
				node.add(x); size++;
				nodeCount++;
				modCount++;
				//System.out.println(this.toString());
				return true;
			}
			else if(nodeCount >0)
			{
				LinkedArrayNode<T> node = new LinkedArrayNode<T>(tail.prev,tail,lengthOfArrays);
				tail.prev.next = node;
				tail.prev = node;
				node.add(x); size++;
				nodeCount++;
				modCount++;
				//System.out.println(this.toString());
				return true;
			}
		}
		return false;
		
	}

	// Remove the first occurrence of x if x is present. 
	// Returns a reference to the removed element if it is removed. 
	// When the last data element is removed from (the array in) a
	// LinkedArrayNode, the LinkedArrayNode is removed from the 
	// LinkedArrays.
	// Throws IllegalArgumentException if x is null.
	// Target Complexity: O(n)
	protected T remove(T x)
	{
		if (x == null)
		{ throw new IllegalArgumentException();}
		T result = null;
		int nodeVisit = 0; // Number of nodes that have been visited to search for an element=x
		LinkedArrayNode<T> current = head;
		if (nodeCount>0)
		{
			while (nodeVisit < nodeCount)
			{
				current = current.next; nodeVisit++;
				if (current.remove(x)!= null)
				{ 
					size--; modCount--;
					if (current.arraySize == 0)
					{
						current.prev.next = current.next;
						current.next.prev = current.prev;
						current.initializeArray();
						current.next = null;
						current.prev = null;
						nodeCount--;
					}
					return current.remove(x); 
				}
			}
		}
		return result;
	}

	// Returns a pretty representation of the LinkedArrays.
	// Example: A LinkedArrays with two LinkedArrayNodes that have arrays 
	// of length two. The size of the first array is two and the size of
	// the second array is one: | 4, 2 | 3 |
	public String toString()
	{
		String result =""; 
		int visitNode = 0;
		LinkedArrayNode<T> current = head;
		if (nodeCount >0)
		{
			while ( visitNode < this.nodeCount)
			{
				if (visitNode ==0 ) { result += "|";}
				current = current.next; visitNode++;

				result += current.toString() +"|"  ;
			}
		}
		
		return result;
		
	}
	
	// Check if this LinkedArray has an available space 
	public boolean hasAvailableSpace()
	{
		int visitNode =0; // number of LinkedArrayNodes that have been visit
		LinkedArrayNode<T> current = head;

		if (this.nodeCount >0 )
		{
			while ( visitNode < this.nodeCount)
			{
				current = current.next; visitNode++;
				if (current.hasAvailableSpace())
				{// System.out.println("this has available space");
					return true;
					}
			}
		}
		//System.out.println("this doesn't have available space");
		return false;
	}
	
	// get next available LinkedArrayNode if this.hasAvailableSpace is true. 
	public LinkedArrayNode<T> getNextAvailableNode()
	{
		int visitNode =0; // number of LinkedArrayNodes that have been visit
		int index = -1; // index of the return node
		LinkedArrayNode<T> current = head;
		LinkedArrayNode<T> result = null;

		while ( visitNode < this.nodeCount)
		{
			current = current.next; visitNode++; index++;
			if (current.hasAvailableSpace())
			{//System.out.println("index of the return node is " + index);
				return current;}
		}
		return result;
	}
	
	

	@Override
	public Iterator<T> iterator()
	{
		return new LinkedArraysIterator();
	}
	public class LinkedArraysIterator implements Iterator<T>
	{

		// the current index in this the current LinkedArrayNode
		protected int index; 
		// Current LinkedArrayNode
		protected LinkedArrayNode<T> current;
		Iterator<T> currentLANItr;
		
		// How many modifications iterator expects
		protected int expectedModCount = modCount;      

		// Construct an iterator
		public LinkedArraysIterator ()
		{
			if (nodeCount()> 0){
				current = head.next;
				currentLANItr = current.iterator();
				index = 0;
			}
			else current = head;
			
		}

		// Returns true if this list iterator has more elements when 
		// traversing the list in the forward direction. (In other words, 
		// returns true if next() would return an element rather than 
		// throwing an exception.)
		public boolean hasNext( )
		{
				
			if (currentLANItr != null && currentLANItr.hasNext())
				return true;
			else {
				if(current.next!=tail)
				{
					current = current.next;
					currentLANItr = current.iterator();
					if(currentLANItr.hasNext())
						return true;
					else return false;
				}
				
			}
			
			return false;
		}

		// Returns the next element in the list and advances the cursor
		// position.
		// Throws: NoSuchElementException if the iteration has no next 
		// element.
		@SuppressWarnings("unchecked")
		public T next( )
		{
			if (!hasNext())
				throw new NoSuchElementException();
			T result = currentLANItr.next();
			//System.out.println("in next iterator LinkedArray " + result);
			return result;

		}
		public void remove( )
		{
			throw new UnsupportedOperationException();
		}
	}
}