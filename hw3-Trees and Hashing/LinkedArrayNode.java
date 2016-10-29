import java.util.*;


//import ArrayLinkedList.ArrayLinkedListIterator;

public class LinkedArrayNode<T> 
{
	protected LinkedArrayNode<T> prev;
	protected LinkedArrayNode<T> next;
	protected Object[] array;          // array holds T objects
	protected static final int DEFAULTLENGTHOFARRAY = 16;
	protected static final int NULL = -1;
	protected int arraySize;    // number of elements in the array.


	// Workhorse constructor. Initialize prev and next and the size of the
	// array, and create an array of Objects of the specified length.
	// Parameter lengthOfArray is used to create array. The value of 
	// lengthOfArray need not be saved since it is essentially array.length.
	// Throws IllegalArgumentException if lengthOfArray < 0.
	public LinkedArrayNode(LinkedArrayNode<T> prev, 
			LinkedArrayNode<T> next, int lengthOfArray)
	{
		if ( lengthOfArray < 0)
		{throw new IllegalArgumentException();}
		this.prev = prev;
		this.next = next;
		this.array = new Object[lengthOfArray];
		this.arraySize = 0;
	}

	// Convenience constructor. Calls the workhorse constructor with 
	// DEFAULTLENGTHOFARRAY as the argument.
	public LinkedArrayNode(LinkedArrayNode<T> prev, 
			LinkedArrayNode<T> next)
	{
		this (prev,next, DEFAULTLENGTHOFARRAY);
	}
	//default constructor
	public LinkedArrayNode(int lengthOfArray)
	{
		this(null,null, lengthOfArray);
	}

	// Add element x to the end of the array.
	// Throws IllegalArgumentException if x is null.
	// Target Complexity: O(1)
	public void add(T x)
	{
		if (x == null)
		{ throw new IllegalArgumentException();}
		if (this.hasAvailableSpace()){
			this.array[this.arraySize]= x;
			this.arraySize++;
		}
		else System.out.println("cannot add - array is full");
		
	}

	// Locate and remove the first element that equals x. This may require 
	// elements to be shifted (left). Returns a reference to the removed 
	// element, or null if the element was not removed.
	// Throws IllegalArgumentException if x is null.
	// Target Complexity: O(n)
	@SuppressWarnings("unchecked")
	public T remove(T x)
	{
		if (x == null)
		{ throw new IllegalArgumentException();}
		T result = null;
		int index = findFirstX(x);
		if (index == NULL){

			return result;
		}
		else if (index !=NULL){
			result = (T) this.array[index]; 
	
			this.arraySize--;
			int i = index; index++;
			while (index<this.array.length){
				array[i++]=array[index++];
			}
			array[i] = null;
		}
		return result;
	}

	// Returns a reference to the first element in the array that equals x, or 
	// null if there is no matching element. 
	// Throws IllegalArgumentException if x is null.
	// Target Complexity: O(N)
	@SuppressWarnings("unchecked")
	public T getMatch(T x)
	{
		if (x == null)
		{ throw new IllegalArgumentException();}
		T result = null;
		int index = findFirstX(x);
		if (index == NULL){
		
			return result;
		}
		else if (index !=NULL){
			result = (T) this.array[index]; 
		
		}
		return result;
	}

	// toString() - create a pretty representation of the node by
	// showing all of the elements in the array.
	// Target Complexity: O(n)
	// Example: an array with size four and length five: 1, 2, 4, 6
	public String toString()
	{
		String result="" ;//= "An array with size " + this.arraySize + " and length " +this.array.length + " : ";
		int i =0;
		for (i = 0; i< this.arraySize ; i++){
			if ( i <this.arraySize -1)
			{result += array[i] + ",";}
			if (i == this.arraySize -1)
			{ result += array[i];}
		}
		
		return result;
	}
	
	//findFirstX will return the index of the first element in the array that equals x.
	//if such element is not in the array, it will return -1. 
	//Throws IllegalArgumentException if x is null.
	public int findFirstX(T x)
	{
		if (x == null)
		{ throw new IllegalArgumentException();}
		int index = NULL, i =0;
		
		if (this.arraySize == 0){
			
			return index;
		}
			
		while (i < arraySize && index == NULL){
			if (x.equals(array[i])) { 
				index = i;
			}
			else {i++; }
		}
			
		
		return index; 
	}
	//initialize data in array of LinkedArrayNode
	public void initializeArray()
	{
		Arrays.fill(array, null);
		this.arraySize =0;
	}
	// this method returns true if this.array has available space
	public boolean hasAvailableSpace()
	{
		if (this.array.length - this.arraySize > 0)
			return true;
		else return false;
	}
	
	// return the next available index of this LinkedArrayNode if hasAvailableSpace is true
	public int getNextAvailableIndex()
	{
		if (this.hasAvailableSpace())
			return this.arraySize;
		else 
		{
			System.out.println("this LAN has no more space");
			return NULL;
		}
		
	}
	
	// Returns an Iterator of the elements in this list, 
	// starting at the front of the list
	// Target Complexity: O(1)
	Iterator<T> iterator()
	{
		return new LinkedArrayNodeIterator();
	}
	
	
	// Iterator for ArrayLinkedList. 
	private class LinkedArrayNodeIterator implements Iterator<T>
	{
		private int current; // current index
		
		// Constructor
		// Target Complexity: O(1)
		public LinkedArrayNodeIterator ()
		{
			current = 0;
		
		}

		// Returns true if the iterator can be moved to the next() element.
		public boolean hasNext()
		{
			return current < LinkedArrayNode.this.arraySize; 
		}

		// Move the iterator forward and return the passed-over element
		@SuppressWarnings("unchecked")
		public T next()
		{	
			if (!hasNext())
			{
				throw new NoSuchElementException(); 
			}
			return (T) LinkedArrayNode.this.array[current++];

		}
	}

}