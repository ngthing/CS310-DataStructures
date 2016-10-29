import java.util.*;
import java.util.NoSuchElementException;
public class DigitList 
{
	protected Node head;  // reference to dummy node at the front
	protected Node tail;  // reference to dummy node at the end
	protected int size;   // number of nodes
	protected int modCount; 
	protected final static int NULL = -1; 
	// Workhorse constructor. Initialize variables: head and tail to dummy 
	// nodes, size to 0, modCount to 1. Initially, head.next references  
	// the tail node; tail.prev references the head node. An empty list
	// (having size 0) has only the two dummy nodes in it.
	DigitList()
	{
		size =0; modCount =1;
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.prev = head;
	}

	// Add digit x to the front of the list. 
	// Target Complexity: O(1)
	public void addFirst(int x)
	{
		Node toAdd = new Node(x);
		toAdd.next = head.next;
		head.next.prev = toAdd;
		head.next = toAdd;
		toAdd.prev = head;
		size++; modCount++;
	}

	// Returns the high-order digit (at the front of the list). 
	// Throws NoSuchElementException if the list is empty
	// Target Complexity: O(1)
	public int getFirst()
	{
		if (size == 0)
		{ throw new NoSuchElementException(); }
		else return head.next.data;
	}

	// Removes the high-order digit at the front of the list. 
	// Throws NoSuchElementException if the list is empty
	// Target Complexity: O(1)
	public void removeFirst()
	{
		if (size == 0)
		{ throw new NoSuchElementException(); }

		Node toRemove = head.next;
		head.next = toRemove.next;
		toRemove.next.prev = head;
		toRemove.init();
		size--; modCount++;
	}

	// Add digit x to the end of the list. 
	// Target Complexity: O(1)
	public void addLast(int x)
	{
		Node toAdd = new Node(x);
		toAdd.prev = tail.prev;
		tail.prev.next = toAdd;
		toAdd.next = tail;
		tail.prev = toAdd;
		size++; modCount++;
	}

	// Returns the low-order digit (at the end of the list). 
	// Throws NoSuchElementException if the list is empty
	// Target Complexity: O(1)
	public int getLast()
	{
		if (size == 0)
		{ throw new NoSuchElementException(); }
		else return tail.prev.data;

	}

	// Removes the low-order digit at the end of the list. 
	// Throws NoSuchElementException if the list is empty
	// Target Complexity: O(1)
	public void removeLast()
	{
		if (size == 0)
		{ throw new NoSuchElementException(); }

		Node toRemove = tail.prev;
		tail.prev = toRemove.prev;
		toRemove.prev.next = tail;
		toRemove.init();
		size--; modCount++;
	}

	// Returns a pretty representation of the nodes in the list.
	// (Note that a DigitList has no notion of a decimal point.)
	// Example: [3][1][4][1][6]
	// O(number of digits)
	public String toString()
	{
		Node tmp = head;
		String digitList = "";
		while (size>0 && tmp.next.data != NULL)
		{
			digitList += tmp.next.toString();
			tmp =tmp.next;
		}
		return digitList;
	}

	// Returns the current size of the list
	// Target Complexity: O(1)
	public int size()
	{
		return this.size;
	}

	// Obtains a ListIterator object used to traverse the list
	// bidirectionally.
	// Returns an iterator positioned prior to the first element
	public ListIterator<Integer> listIterator( )
	{
		return new DigitListIterator( 0 );
	}

	// Obtains a ListIterator object used to traverse the list
	// bidirectionally.
	// Returns an iterator positioned prior to the requested element.
	// Parameter idx is the index to start the iterator. Use size() 
	// to do complete reverse traversal. Use 0 to do a complete forward 
	// traversal.
	// Throws IndexOutOfBoundsException if idx is not between 0 
	// and size(), inclusive.
	//If idx is 0, you construct/initialize the iterator so that
	//the first call to next() returns the digit in the node at the front of the list, i.e.,
	//node 0, which contains the high-order digit.
	//If idx is 1, you construct/initialize the iterator so that the first call to
	//next() returns the data in node 1.
	 
	
	public ListIterator<Integer> listIterator( int idx )
	{
		return new DigitListIterator( idx );
	}

	// This class implements the ListIterator interface for the
	// DigitList.  It maintains a notion of a current position and an
	// implicit reference to the DigitList through the syntax
	// DigitList.this.  
	public class DigitListIterator implements ListIterator<Integer>
	{
		// Current node
		protected Node current; 
		// Necessary for implementing previous()
		protected boolean lastMoveWasPrev = false;    
		// How many modifications iterator expects
		protected int expectedModCount = modCount;      

		// Construct an iterator
		public DigitListIterator( int idx )
		{
			current = head;
			for (int i =0; i<idx;i++)
			{ current = current.next; }
		}

		// Returns true if this list iterator has more elements when 
		// traversing the list in the forward direction. (In other words, 
		// returns true if next() would return an element rather than 
		// throwing an exception.)
		public boolean hasNext( )
		{
			if ( current.next.data == NULL)
			return false;
			else return true;
		}

		// Returns the next element in the list and advances the cursor
		// position.
		// Throws: NoSuchElementException if the iteration has no next 
		// element.
		public Integer next( )
		{
			if (!hasNext())
			{ throw new NoSuchElementException() ;}
			else 
			{
				current = current.next;
				lastMoveWasPrev = false;
			}
			return current.data;

		}

		// Returns true if this list iterator has more elements when 
		// traversing the list in the reverse direction.     
		public boolean hasPrevious( )
		{
			if ( current.data == NULL)
			return false;
			else return true;

		}

		// Returns the previous element in the list and moves the cursor 
		// position backwards. This method may be called repeatedly to 
		// iterate through the list backwards, or intermixed with calls to 
		// next() to go back and forth. (Note that alternating calls to 
		// next and previous will return the same element repeatedly.)
		// Throws: NoSuchElementException if the iteration has no next 
		// element.
		public Integer previous( )
		{ 
			if (!hasPrevious())
			{ throw new NoSuchElementException() ;}
			current = current.prev;
			lastMoveWasPrev =true;
			return current.next.data;
		}

		// The following methods may be optionally implemented but will
		// not be tested and will not garner any additional credit.
		// 
		// OPTIONAL: Return the integer index associated with the element
		// that would be returned by next()
		public int nextIndex(){
			throw new RuntimeException("Implement me for fun");
		}
		// OPTIONAL: Return the integer index associated with the element
		// that would be returned by previous()
		public int previousIndex(){
			throw new RuntimeException("Implement me for fun");
		}
		// OPTIONAL: Inserts the specified element into the list. The element 
		// is inserted immediately before the element that would be returned 
		// by next(), if any, and after the element that would be returned by 
		// previous(), if any. (If the list contains no elements, the new 
		// element becomes the sole element on the list.) The new element is 
		// inserted before the implicit cursor: a subsequent call to next 
		// would be unaffected, and a subsequent call to previous would 
		// return the new element. (This call increases by one the value that 
		// would be returned by a call to nextIndex or previousIndex.)
		public void add(Integer x) {
			throw new RuntimeException("Implement me for fun");
		}

		// The following operations are part of the ListIterator interface
		// but are not supported by DigitLists or their iterators. Both
		// will throw UnsupportedOperationException exceptions if invoked.
		public void set(Integer x){
			throw new UnsupportedOperationException();
		}
		public void remove( ) {
			throw new UnsupportedOperationException();
		}
	}  
} 