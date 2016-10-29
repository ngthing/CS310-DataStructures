import java.util.*;



import java.util.*;

public class MyHashTable<T> implements Iterable<T>
{
	protected static final int DEFAULTTABLESIZE = 101; 
	protected int size;
	protected int tableSize;
	protected Object[] table;

	// Workhorse constructor. The internal table size is tableSize if
	// tableSize is prime or the next prime number that is
	// greater than tableSize if tableSize is not prime.
	public MyHashTable( int tableSize )
	{
		this.size = 0;
		this.tableSize = nextPrime(tableSize);
		table = new Object[this.tableSize];
		for ( int i =0; i< this.tableSize; i++)
		{ this.table[i] = new LinkedArrays<T>(); }
	
	}

	// Convenience constructor. DEFAULTTABLESIZE is 101
	public MyHashTable( )
	{
		this(DEFAULTTABLESIZE);
		
			
	}

	// Make the hash table logically empty.
	// Target Complexity: O(n)
	@SuppressWarnings("unchecked")
	public void clear( )
	{
		for (int i =0; i< this.tableSize; i++)
			{ ((LinkedArrays<T>) table[i]).clear(); }
		this.size = 0; 
		
	}

	// Insert x into the hash table. If x is already present, then do 
	// nothing.
	// Throws IllegalArgumentException if x is null.
	@SuppressWarnings("unchecked")
	public void insert(T x)
	{
		if (x == null)
			throw new IllegalArgumentException();
		if (!this.contains(x))
		{
			int index = myhash(x);
			((LinkedArrays<T>) table[index]).add(x);
			this.size++;
			if (this.size > this.tableSize/2) rehash();
		}
		//System.out.println( this.toString());
	}

	// Remove x from the hash table.
	// Throws IllegalArgumentException if x is null.
	@SuppressWarnings("unchecked")
	public void remove( T x )
	{
		if (x == null)
			throw new IllegalArgumentException();
		for (int i =0; i< this.tableSize; i++)
		{ 
			LinkedArrays<T> L = (LinkedArrays<T>) table[i];  
			boolean b = L.contains(x);
			if (b)
			{
				L.remove(x);this.size--;
				return;
			}
				
		}
	}

	// Return true if x is in the hash table
	// Throws IllegalArgumentException if x is null.
	@SuppressWarnings("unchecked")
	public boolean contains(T x )
	{
		if (x == null)
			throw new IllegalArgumentException();
		for (int i =0; i< this.tableSize; i++)
		{ 
			LinkedArrays<T> L = (LinkedArrays<T>) table[i];  
			boolean b = L.contains(x);
			if(b)
			return true;
		}
		return false;
	}

	// Return the first element in the hashed-to LinkedArrays that equals 
	// x, or null if there is no such element.
	// Throws IllegalArgumentException if x is null.
	@SuppressWarnings("unchecked")
	public T getMatch(T x)
	{
		if (x == null)
			throw new IllegalArgumentException();
		T result = null;
		for (int i =0; i< this.tableSize; i++)
		{ 
			LinkedArrays<T> L = (LinkedArrays<T>) table[i];  
			T y  = L.getMatch(x);
			if(y != null)
			return y;
		}
		return result;
	}

	// Returns the number of elements
	// Target Complexity: O(1)
	public int size()
	{
		return this.size;
	}

	// Returns true if there are no elements.
	// Target Complexity: O(1)
	public boolean isEmpty( )
	{
		if (this.size == 0)
			return true;
		return false;
	}

	// Returns a Set containing all of the T elements in the table. (Set is
	// an interface implemented by classes HashSet and TreeSet.)
	public Set<T> toSet()
	{
		Set<T> s = new HashSet<T>();
//		System.out.println("in toSet method");
//		System.out.println("thisTable.tostring " + this.toString());
		Iterator<T> thisTableIter = this.iterator();
		
		while (thisTableIter.hasNext())
		{
			T e = thisTableIter.next();
		//	System.out.println(e + " ");
			s.add(e);
		}
		return s;
	}

	// Returns a pretty representation of the hash table.
	// Uses toString() of LinkedArrays.
	// Example: For a table of size 3
	// Table:
	// 0: | two |
	// 1: | one, four |
	// 2: 
	@SuppressWarnings("unchecked")
	public String toString()
	{
		String result ="Table:\n";
		for (int i =0; i< this.tableSize; i++)
		{ 
			LinkedArrays<T> L = (LinkedArrays<T>) table[i];  
			result+= i+": ";
			result+= L.toString() ;
			if(i<this.tableSize-1)
			{ result +="\n"; }
			
		}
	
		return result;
	}

	// Increases the size of the table by finding a prime number 
	// (nextPrime) at least as large as twice the current table size. 
	// Rehashes the elements of the hash table when size is greater than 
	// tableSize/2.
	protected void rehash( )
	{
		int newSize = nextPrime(this.tableSize*2);
		MyHashTable<T> newHashTable = new MyHashTable<T>(newSize);
		Iterator<T> thisTableIter = this.iterator();
		while(thisTableIter.hasNext())
		{
			T e = thisTableIter.next();
			newHashTable.insert(e);
		}
		this.clear();
		this.size = newHashTable.size;
		this.tableSize = newHashTable.tableSize;
		this.table = newHashTable.table;
	}

	// Internal method for computing the hash value from the hashCode of x.
	protected int myhash(T x)
	{
		int hashVal = x.hashCode( );
		hashVal %= tableSize;
		if( hashVal < 0 )
			hashVal += tableSize;
		return hashVal;
	}

	// Internal method to find a prime number at least as large as n. 
	protected static int nextPrime(int n )
	{
		if( n % 2 == 0 )
			n++;
		for( ; !isPrime( n ); n += 2 )
			;
		return n;
	}

	// Internal method to test if a number is prime. Not an efficient 
	// algorithm. 
	protected static boolean isPrime(int n )
	{
		if( n == 2 || n == 3 )
			return true;
		if( n == 1 || n % 2 == 0 )
			return false;
		for( int i = 3; i * i <= n; i += 2 )
			if( n % i == 0 )
				return false;
		return true;
	}

	public Object[] getTable()
	{
		
		return this.table;
	}
	@Override
	public Iterator<T> iterator()
	{
		return new MyHashTableIterator();
	}
	public class MyHashTableIterator implements Iterator<T>
	{

		// The current index of hash table
		protected int currentIndex = 0; 
		// Current LinkedArrays
		protected LinkedArrays<T> currentArray;
		Iterator<T> currentArrayItr;
		
		int visited = 0; // number of LinkedArrayNodes that have been visited
		// Construct an iterator
		@SuppressWarnings("unchecked")
		public MyHashTableIterator ()
		{
			currentIndex =0;
			currentArray = (LinkedArrays<T>) table[currentIndex];
			currentArrayItr= currentArray.iterator();
		}

		// Returns true if this list iterator has more elements when 
		// traversing the list in the forward direction. (In other words, 
		// returns true if next() would return an element rather than 
		// throwing an exception.)
		@SuppressWarnings("unchecked")
		public boolean hasNext( )
		{
			if ( currentArrayItr.hasNext())
				return true;
			else {
				while (currentIndex< tableSize-1)
				{
					currentIndex++;
					currentArray = (LinkedArrays<T>) table[currentIndex];
					currentArrayItr= currentArray.iterator();
					
					if ( currentArrayItr.hasNext())
						return true;
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
			
			T result = currentArrayItr.next();
			//System.out.println("in next iterator LinkedArray " + result);
			return result;

		}
		public void remove( )
		{
			throw new UnsupportedOperationException();
		}
	}
}