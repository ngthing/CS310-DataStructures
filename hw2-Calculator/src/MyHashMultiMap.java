import java.util.*;
public class MyHashMultiMap<KeyType, ValueType> 
{

	// Hash table to hold key/value pairs 
	protected MyHashTable<Entry<KeyType,ValueType>> items;  

	// Constructor
	public MyHashMultiMap()
	{
		items = new MyHashTable<Entry<KeyType,ValueType>>();
	}

	// Associates the specified key with the specified value in this map.
	// If key is already associated with a List of values, then value
	// is added to this list; otherwise, a new List is created, value is 
	// added to this List, and key is associated with this new List.
	// Throws IllegalArgumentException if key or value is null.
	public void put(KeyType key, ValueType value)
	{
		if (key == null || value == null)
			throw new IllegalArgumentException();
		LinkedList<ValueType> list = new LinkedList<ValueType>();
		Entry<KeyType,ValueType> entry = makeEntry(key);
		Entry<KeyType,ValueType> e = items.getMatch(entry); // find a match entry's key
		
		if (e!= null) // if key already exists in this map, add value to the valueList of the key 
		{
			e.valueList.add(value);
		}
		// if key doesn't exist in map, add value to list, create an entry with key and list, 
		//and then insert to items
		if (e == null)
		{
			list.add(value);
			Entry<KeyType,ValueType> toInsert = makeEntry(key,list);
			items.insert(toInsert);
		}
		
		
	}

	// Returns the List of values that key is associated with, or null if 
	// there is no mapping for key.
	// Throws IllegalArgumentException if key is null.
	public List<ValueType> get(KeyType key)
	{
		if (key == null)
			throw new IllegalArgumentException();
		List<ValueType> list = null;
		Entry<KeyType,ValueType> entry = makeEntry(key);
		Entry<KeyType,ValueType> e = items.getMatch(entry);
		if (e!= null)
		{
			list = e.valueList;
		}
		
		return list;	
		
	}

	// Returns the number of elements
	// Target Complexity: O(1)
	public int size()
	{
		return items.size;
	}

	// Returns true if there are no elements.
	// Target Complexity: O(1)
	public boolean isEmpty()
	{
		if (this.size() == 0)
			return true;
		else return false;
	}

	// Make the map logically empty.
	// Target Complexity: O(1)
	public void clear()
	{
		items.clear();
	}

	// Returns a Set of the Entries contained in this map.
	public Set<Entry<KeyType, ValueType>> entrySet()
	{
		return items.toSet();
	}

	// Returns the MyHashTable<T> of items
	protected MyHashTable<Entry<KeyType,ValueType>> getItems()
	{
		return items;
	}

	// A helper method that returns an Entry created from the 
	// specified key and List. Called by put and get.
	protected MyHashMultiMap.Entry<KeyType,ValueType> 
	makeEntry( KeyType key, LinkedList<ValueType> valueList ) 
	{
		return new MyHashMultiMap.Entry<KeyType, ValueType>(key,valueList);
	}

	// A helper method for creating an Entry from a key value.
	// Called by put and get.
	protected MyHashMultiMap.Entry<KeyType,ValueType> 
	makeEntry( KeyType key )
	{
		return makeEntry(key, null );
	}

	public static class Entry<KeyType, ValueType> 
	{
		protected KeyType key;
		protected LinkedList<ValueType> valueList;

		// Constructor
		// Target Complexity: O(1)               
		public Entry( KeyType k, LinkedList<ValueType> list )
		{
			key =k;
			valueList = list;
		}

		// Returns the hash code value for this map entry. 
		// The hashCode of the Entry is the hashCode of the Entry's key, i.e,
		// the Entry’s value is ignored during hashing.
		public int hashCode()
		{
			return key.hashCode();

		}

		// Compares the specified object with this entry for equality.
		// The equality of two Entries is based on the equality of their keys.
		public boolean equals( Object rhs )
		{
			if (this == rhs)
				return true;
			else if ((rhs == null) || this.getClass()!= rhs.getClass())
				return false;
			else
			{
				@SuppressWarnings("unchecked")
				Entry<KeyType,ValueType> that = (Entry<KeyType,ValueType>) rhs;
				return this.key.equals(that.key);
			}
		}

		// Returns the key corresponding to this entry.          
		public KeyType getKey()
		{
			return this.key;
		}

		// Returns the value corresponding to this entry.
		public LinkedList<ValueType> getValue()
		{
			return this.valueList;
		}

		// Returns a pretty representation of the Entry.
		// Format: key, valueList. Example: When key = "Carver" and valueList = 
		// [CS310-003, SWE437-001]: “Carver, [CS310-003, SWE437-001]”
		// Note: “[a, b]” is returned by LinkedList.toString for a LinkedList 
		// containing Strings “a” and “b”.
		public String toString()
		{
			return this.key.toString() + ", " + this.valueList.toString();
		}
	}
}