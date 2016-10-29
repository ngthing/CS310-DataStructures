
public class NodePool<T> extends ObjectPool<Node<T>>
{ // The data element of a Node has type T
  // Constructor invokes the constructor of the parent class.
  // Throws IllegalArgumentException if maxSize < 1
  NodePool(int maxSize)
  {
	super(maxSize);
	if (maxSize < 1)
	  {	throw new IllegalArgumentException(" maxSize < 1"); }
  }
 
  // Returns a newly constructed Node with data type T. 
  // Called when an object is requested from an empty pool.
  // Target Complexity: O(1)
  protected Node<T> create()
  {
	  Node<T> temp = new Node<T>();
	  return temp;
	  
  }
 
  // Resets values of x to their initial values. 
  // Called when an empty Node is released back to the pool.
  // Empty nodes are released to the NodePool by methods clear() 
  // and compress() in class ArrayLinkedList, as described below.
  // Target Complexity: O(1)
  protected Node<T> reset (Node<T> x)
  {
	  x.init();
	  return x;
  }

  
}