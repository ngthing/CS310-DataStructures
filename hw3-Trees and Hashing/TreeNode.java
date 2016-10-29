public class TreeNode<T> 
{
	protected TreeNode<T> left;
	protected TreeNode<T> right;
	protected TreeNode<T> parent;
	protected T data;

	// Constructor initializes data members.
	// Target Complexity: O(1)
	public TreeNode(T data,TreeNode<T> left,TreeNode<T> right,TreeNode<T> parent)
	{
		this.data = data;
		this.left = left;
		this.right = right;
		this.parent = parent;
	}
	public TreeNode()
	{
		this (null,null,null,null);
	}
	
	public TreeNode(T data)
	{
		this (data,null,null,null);
	}
	
	// Returns a pretty representation of the node.
	// Format: [data]. Example: [3]
	// Target Complexity: O(1)
	public String toString()
	{
		return "[" + data.toString() + "]";
		
	}
	public void initialize()
	{
		this.data = null;
		this.left = null;
		this.right = null;
		this.parent = null;
	}

	// Returns a pretty representation of the node for debugging.
	// Shows the data values of this node and its left, right, and parent 
	// nodes. Format:[D:data,L:left,R:right,P:parent]. 
	// Example:[D:3, L:1, R:2, P:0]
	// Target Complexity: O(1)
	public String debugToString()
	{
		return "[D:" + data.toString() + ", L:" + left.toString() + ", R:" + right.toString()
				+ ", P:" + parent.toString() +"]";
	}
} 