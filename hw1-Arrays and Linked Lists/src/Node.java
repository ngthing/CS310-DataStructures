public class Node<T>
  {
	  protected static final int NULL = -1;
	  protected int previous;
	  protected int next;
	  protected T data;
	 
	  // Constructor initializes data members. See method init().
	  protected Node()
	  {
		init();
	  }
	 
	  // Create a pretty representation of the pool
	  // Format: [previous,next,data]
	  // Target Complexity: O(n)
	  public String toString()
	  {
		  String anode = "[" + this.previous + "," + this.next + "," + this.data + "]";
		  return anode;
	  }
	 
	  // Initializes the data members. 
	  // Called by the constructor and also by method reset() 
	  // in class NodePool.
	  // Target Complexity: O(1) 
	  public void init()
	  {
		  this.previous = NULL;
		  this.next = NULL;
		  this.data = null;
	  }

	}
