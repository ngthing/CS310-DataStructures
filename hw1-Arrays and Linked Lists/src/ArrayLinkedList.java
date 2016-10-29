import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.print.attribute.standard.NumberOfDocuments;

class ArrayLinkedList<T> {
	protected final static int NULL = -1;      
	protected ArrayList<Node<T>> array;
	protected NodePool<T> pool;
	protected int head; // position of dummy node in array
	protected int tail; // position of tail node in array
	protected int firstEmpty; // head of the list of empty nodes
	protected int numberEmpty; // number of empty nodes
	protected int size; // number of non-empty nodes
	protected int modCount; // number of modifications made
	protected int numNodes; // number of Elements in array - dummyHead = array.size() -1
	protected final static int NODEPOOLSIZE = 8;

	// Constructor to initialize the data members, increment modCount,
	// create the dummy header Node, and add it to array
	public ArrayLinkedList()
	{	
		pool = new NodePool<T>(NODEPOOLSIZE);
		array = new ArrayList<Node<T>>();
		Node<T> dummyNode = pool.allocate();
		dummyNode.init();
		this.array.add(dummyNode);
		head =0 ; tail = 0; firstEmpty = NULL; numberEmpty =0; size =0; modCount =1;

	}

	// Return number of non-empty nodes
	// Target Complexity: O(1)
	public int size()
	{ return size;}

	public int getNumNodes()
	{
		return this.array.size()-1;
	}
	// convenience methods for debugging and testing
	protected int getFirstEmpty()  // return firstEmpty
	{ return firstEmpty; }

	protected int getHead() // return head
	{ return head; }

	protected int getTail() // return tail
	{ return tail; }
	protected ArrayList<Node<T>> getArray() // return array
	{ return array; }

	// Appends the specified element to the end of this list. 
	// Returns true.
	// If no empty Nodes are available, then get a new Node from pool.
	// Throws IllegalArgumentException if e is null.
	// Checks assertions at the start and end of its execution.
	// Target Complexity: Amortized O(1)
	public boolean add(T e) 
	{
		assert size>=0 && head==0 && numberEmpty >=0 && (numberEmpty==0  
				&& firstEmpty==NULL) || (numberEmpty>0 && firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);         

		if ( e == null)
		{ throw new IllegalArgumentException(); }
		if (getFirstEmpty()== NULL) // insert the data at the last position in array
		{
			Node<T> node = pool.allocate();
			node.data = e;
			node.previous = tail;
			this.array.add(node);
			array.get(tail).next =getNumNodes();
			this.tail = getNumNodes();
			this.size++;
			this.modCount++;

		}
		else if (getFirstEmpty() != NULL)
		{
			int current = getFirstEmpty();
			array.get(current).data = e;
			array.get(current).previous = tail;
			array.get(tail).next = current;

			if( array.get(current).next != NULL)
			{
				this.firstEmpty = array.get(current).next;
				array.get(firstEmpty).previous = NULL;
			}
			else if (array.get(current).next == NULL)
			{ this.firstEmpty = NULL; }


			array.get(current).next = NULL;
			this.tail = current;
			this.size++;
			this.modCount++;
			this.numberEmpty--;

		}


		// check this assertion before each return statement
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);
		return true;
	}

	// Inserts the specified element at the specified index in this list.  
	// Shifts the element currently at that index (if any) and any 
	// subsequent elements to the right (adds one to their indices).    
	// Throws IndexOutOfBoundsException if the index is out of range 
	// (index < 0 || index > size()).
	// Throws IllegalArgumentException if e is null.
	// Can call add(T e) if index equals size, i.e., add at the end
	// Checks assertions at the start and end of its execution.
	// Target Complexity: O(n)
	public void add(int index, T e)
	{
		assert size>=0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL) && (array.size() == size+numberEmpty+1);

		if ( index < 0 || index > size())
		{ throw new IndexOutOfBoundsException(); }
		if ( e == null)
		{ throw new IllegalArgumentException(); }
		if ( index == this.size)
		{ add(e); } 


		else if (getFirstEmpty() == NULL)   
		{
			Node <T> node = pool.allocate();
			node.data = e;
			array.add(node);

			int tmp = 0; int i = 0; int prevIndexPosition; 

			for (i = 0 ; i <= index ; i++)
			{ tmp = array.get(tmp).next; }

			prevIndexPosition = array.get(tmp).previous;
			array.get(getNumNodes()).previous = prevIndexPosition; 
			array.get(getNumNodes()).next = tmp;
			array.get(prevIndexPosition).next = getNumNodes();
			array.get(tmp).previous = getNumNodes();

			this.size++;
			this.modCount++;

		}

		else if ( getFirstEmpty() != NULL)
		{
			int current = getFirstEmpty(); 
			firstEmpty = array.get(current).next;
			array.get(current).data =e;

			int tmp = 0; int i = 0; int prevIndexPosition; 
			for (i = 0 ; i <= index  ; i++)
			{ tmp = array.get(tmp).next; }

			prevIndexPosition = array.get(tmp).previous;
			array.get(current).previous = prevIndexPosition; 
			array.get(current).next = tmp;
			array.get(prevIndexPosition).next = current;
			array.get(tmp).previous = current;

			this.size++;
			this.modCount++;
			this.numberEmpty--;

		}
		// check this assertion before each return statement
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL) && (array.size() == 
						size+numberEmpty+1);
		return;
	}

	// Equivalent to add(0,e).
	// Throws IllegalArgumentException if e is null
	// Target Complexity: O(1)
	public void addFirst(T e)
	{
		if ( e == null)
		{ throw new IllegalArgumentException(); }

		add(0,e);
	}

	// Equivalent to add(e).
	// Throws IllegalArgumentException if e is null
	// Target Complexity: O(1)
	public void addLast(T e)
	{
		if ( e == null)
		{ throw new IllegalArgumentException(); }

		add(e);
	}

	// Add all of the elements (if any) in Collection c to the end 
	// of the list, one-by-one.
	// Returns true if this list changed as a result of the call.
	// Throws NullPointerException if the specified collection is null
	// Target Complexity: O(number of elements in c)
	public boolean addAll(Collection<? extends T> c)
	{
		if ( c == null)
		{ throw new NullPointerException(); }

		for (T e: c)
		{ 
			add(e);
		} 
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);
		return true; 
	}

	// Returns true if this list contains the specified element.
	// Throws IllegalArgumentException if e is null
	// May call indexOf(e)
	// Target Complexity: O(n)
	public boolean contains(T e)
	{
		if ( e == null)
		{ throw new IllegalArgumentException(); }

		if(indexOf(e) != NULL)
		{ 
			assert size>0 && head==0 && numberEmpty >=0 
					&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
							&& firstEmpty!=NULL)
					&& (array.size() == size+numberEmpty+1);
			return true;
		}
		else
			assert size>0 && head==0 && numberEmpty >=0 
			&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
					&& firstEmpty!=NULL)
			&& (array.size() == size+numberEmpty+1);
			return false; 
	}


	// Returns the index of the first occurrence of the 
	// specified element in this list,
	// or NULL if this list does not contain the element.
	// Throws IllegalArgumentException if e is null
	// Target Complexity: O(n)
	public int indexOf(T e)
	{
		if ( e == null)
		{ throw new IllegalArgumentException(); }

		int i = 0; int tmp = 0;
		for (i =0; i< getNumNodes(); i++)
		{
			tmp = array.get(tmp).next;
			if (array.get(tmp).data.equals(e))
				return i;
		}
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);
		
		return NULL;
	}

	// Returns the array position of the first occurrence of 
	// the specified element in array
	// or NULL (-1) if this list does not contain the element. 
	// Note that the return value is a position in the array, 
	// not the index of an element in the list.
	// Called by remove(T e) to find the position of e in the array.
	// Throws IllegalArgumentException if e is null
	// Target Complexity: O(n)
	protected int positionOf(T e)
	{
		if ( e == null)
		{ throw new IllegalArgumentException(); }

		int i = 0; int tmp = 0;
		for (i =0; i< getNumNodes(); i++)
		{
			tmp = array.get(tmp).next;
			if (array.get(tmp).data.equals(e))
				return tmp;
		}
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);
		
		return NULL;
	}

	// Returns the element at the specified index in this list.
	// Throws IndexOutOfBoundsException if the index is out 
	// of range (index < 0 || index >= size())
	// Target Complexity: O(n)
	public T get(int index)
	{
		if ( index < 0 || index > size())
		{ throw new IndexOutOfBoundsException(); }

		int i =0; int tmp =0;
		for (i =0 ; i <= index; i++)
		{ tmp = array.get(tmp).next;}
		
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);
		
		return array.get(tmp).data;

	}

	// Returns the first element in the list.
	// Throws NoSuchElementException if the list is empty
	// Target Complexity: O(1)
	public T getFirst()
	{
		if (getNumNodes() == 0)
		{ throw new NoSuchElementException();} 
		
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);
		return	get(0);
	}

	// Returns the last element in the list
	// Throws NoSuchElementException if the list is empty
	// Target Complexity: O(1)
	public T getLast()
	{
		if (getNumNodes() == 0)
		{ throw new NoSuchElementException();} 
		
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);
		return get(getNumNodes()-1);
	}

	// Remove the node at position current in the array.
	// Note that current is a position in the array, not the 
	// index of an element in the list.
	// The removed node is made empty and added to the front 
	// of the list of empty Nodes. Dummy node cannot be removed.
	// Called by remove(T e) and remove(int index) to 
	// remove the target Node.
	// Target Complexity: O(1)
	protected void removeNode(int current) 
	{
		assert current > 0 && current < array.size();

		int prevNode = array.get(current).previous;
		int nextNode = array.get(current).next;

		array.get(prevNode).next = nextNode; 
		if (nextNode != NULL)
		{ array.get(nextNode).previous = prevNode; }
		array.get(current).init();

		if (getFirstEmpty() != NULL)
		{
			array.get(current).next = firstEmpty;
			array.get(firstEmpty).previous = current;
		}
		if (current == tail)
		{ this.tail = prevNode; }
		this.firstEmpty = current;
		this.numberEmpty++;
		this.size--;
		this.modCount++;
	}

	// Removes the first occurrence of the specified element from 
	// this list, if it is present. Returns true if this
	// list contained the specified element.
	// Throws IllegalArgumentException if e is null.
	// Checks assertions at the start and end of its execution.
	// Target Complexity: O(n)

	public boolean remove(T e) 
	{
		assert size>=0 && head==0 && numberEmpty >=0
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL) && (array.size() == size+numberEmpty+1);

		if ( e == null)
		{ throw new IllegalArgumentException(); }

		if (positionOf(e) == NULL)
		{ return false; }
		else 
		{removeNode(positionOf(e));}


		// check this assertion before each return statement
		assert size>=0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL) && (array.size() == size+numberEmpty+1);
		return true;
	}

	// Removes the element at the specified index in this list.
	// Shifts any subsequent elements to the left (subtracts one from
	// their indices). Returns the element that was removed from the 
	// list. Throws IndexOutOfBoundsException if the index is 
	// out of range (index < 0 || index >= size)
	// Checks assertions at the start and end of its execution.
	// Target Complexity: O(n)
	public T remove(int index) 
	{
		assert size>=0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL) && (array.size() == size+numberEmpty+1);

		if (index < 0 || index >= size)
		{ throw new IndexOutOfBoundsException(); } 

		int i; int tmp =0;
		for (i = 0; i <= index; i++)
		{ tmp= array.get(tmp).next; }
		T removedData = array.get(tmp).data;
		removeNode(tmp);

		// check this assertion before each return statement
		assert size>=0 && head==0 && numberEmpty >=0 && (numberEmpty==0 
				&& firstEmpty==NULL) || (numberEmpty>0 && firstEmpty!=NULL) 
				&& (array.size() == size+numberEmpty+1);
		return removedData;
	}

	// Returns the first element in the list.
	// Throws NoSuchElementException if the list is empty.
	// Equivalent to remove(0), for index 0
	// Target Complexity: O(1)
	public T removeFirst()
	{
		if (getNumNodes() == 0)
		{ throw new NoSuchElementException(); }

		T removedData = getFirst();
		remove(0);
		return removedData;
	}

	// Returns the last element in the list
	// Throws NoSuchElementException if the list is empty
	// Equivalent to remove(size-1), for index size-1
	// Target Complexity: O(1)
	public T removeLast()
	{
		if (getNumNodes() == 0)
		{ throw new NoSuchElementException(); }

		T removedData = getLast();
		remove(getNumNodes()-1);
		
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);
		return removedData;
	}

	// Returns true if the Node at the specified position in the 
	// array is an empty node. The dummy node is never considered to be
	// an empty node.
	// Target Complexity: O(1)
	protected boolean positionIsEmpty(int position)
	{
		assert position > 0 && position < array.size();
		if( array.get(position).data == null)
		{ return true; }
		else 
			return false;

	}

	// Returns number of empty nodes.
	// Target Complexity: O(1)
	protected int numEmpty()
	{
		return numberEmpty;
	}

	// Replaces the element at the specified position in this 
	// list with the specified element. Returns the element 
	// previously at the specified position.    
	// Throws IllegalArgumentException if e is null.
	// Throws IndexOutOfBoundsException if index is out of 
	// range (index < 0 || index >= size)
	// Target Complexity: O(n)
	public T set(int index, T e)
	{
		if ( e == null)
		{ throw new IllegalArgumentException();}
		if (index < 0 || index >= size)
		{ throw new IndexOutOfBoundsException();}

		int i =0; int tmp =0;
		for (i =0 ; i <= index; i++)
		{ tmp = array.get(tmp).next;}
		T toReturn = array.get(tmp).data;
		array.get(tmp).data = e;
		
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);
		
		return toReturn;

	}

	// Removes all of the elements from this list. 
	// The list will be empty after this call returns.
	// The array will only contain the dummy head node.
	// Some data members are reinitialized and all Nodes
	// are released to the node pool. modCount is incremented.
	// Target Complexity: O(n)
	public void clear() 
	{
		assert size>=0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL) && (array.size() == size+numberEmpty+1);


		for (int i =1; i< array.size(); i++)
		{
			pool.release(array.get(i));
		}
		array.get(0).init();
		this.modCount++;
		head =0 ; tail = 0; firstEmpty = NULL; numberEmpty =0; size =0; 


		// check this assertion before each return statement
		assert size==0 && head==0 && numberEmpty==0 && firstEmpty==NULL
				&& (array.size() == size+numberEmpty+1);
		return;
	}

	// Returns an Iterator of the elements in this list, 
	// starting at the front of the list
	// Target Complexity: O(1)
	Iterator<T> iterator()
	{
		return new ArrayLinkedListIterator();
	}

	// Convenience debugging method to display the internal 
	// values of the list, including ArrayList array
	protected void dump() 
	{
		System.out.println();
		System.out.println("**** dump start ****");
		System.out.println("size:" + size);
		System.out.println("number empty:" + numberEmpty);
		System.out.println("first empty:"+firstEmpty);
		System.out.println("head:" + head);
		System.out.println("tail:" + tail); 
		System.out.println("list:");
		System.out.println("array:");
		for (int i=0; i<array.size(); i++) // show all elements of array
			System.out.println(i + ": " + array.get(i));
		System.out.println("**** dump end ****");
		System.out.println();
	}

	// Returns a textual representation of the list, i.e., the 
	// data values of the non-empty nodes in list order.
	public String toString()
	{	
		int i =0; int tmp =0;

		String result = "";
		for (i = 0 ; i < getNumNodes(); i++)
		{ 
			tmp = array.get(tmp).next; 
			result += array.get(tmp).data + "\n";

		}
		assert size>0 && head==0 && numberEmpty >=0 
				&& (numberEmpty==0 && firstEmpty==NULL) || (numberEmpty>0 
						&& firstEmpty!=NULL)
				&& (array.size() == size+numberEmpty+1);
		
		return result;
	}

	// Compress the array by releasing all of the empty nodes to the 
	// node pool.  A new array is created in which the order of the 
	// Nodes in the new array matches the order of the elements in the 
	// list. When compress completes, there are no empty nodes. Resets 
	// tail accordingly.
	// Target Complexity: O(n)
	public void compress()
	{
		int position =1;
		while(position<array.size())
		{
			if(positionIsEmpty(position))
				pool.release(array.get(position));
			position++;
		}

		this.numberEmpty = 0;
		this.firstEmpty = -1;

		ArrayList<Node<T>> tmpArray = new ArrayList<Node<T>>();
		int index = head;


		for (int i =0; i<= this.size(); i++)
		{
			tmpArray.add(array.get(index));
			tmpArray.get(i).data= array.get(index).data;
			tmpArray.get(i).previous = i-1;
			if(array.get(index).next == NULL)
			{ tmpArray.get(i).next = NULL;
			break;}
			if (array.get(index).next != NULL)
			{tmpArray.get(i).next = i+1;}
			index = array.get(index).next;
		}
		array = tmpArray;

		this.head =0;
		this.tail = array.size() -1;
		this.modCount++;
		this.size = array.size() -1;

	}

	// Iterator for ArrayLinkedList. (See the description below.)
	private class ArrayLinkedListIterator implements Iterator<T>
	{
		private int current;
		// Constructor
		// Target Complexity: O(1)
		public ArrayLinkedListIterator ()
		{
			current = head;
		}

		// Returns true if the iterator can be moved to the next() element.
		public boolean hasNext()
		{

			return ArrayLinkedList.this.array.get(current).next != NULL; 
		}

		// Move the iterator forward and return the passed-over element
		public T next()
		{	
			current = ArrayLinkedList.this.array.get(current).next;
			T result = ArrayLinkedList.this.array.get(current).data;

			return result;

		}

		// The following operation is part of the Iterator interface 
		// but is not supported by the iterator. 
		// Throws an UnsupportedOperationException if invoked.
		public void remove()
		{
			throw new UnsupportedOperationException(); 
		}
	}
}