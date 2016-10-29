import java.util.*;
public class BinarySearchTree<T extends Comparable<? super T>> 
{
	protected TreeNode<T> root;        // root of tree
	protected int size;                // size of tree
	protected enum PrePostIn {PREORDER, POSTORDER, INORDER};

	// Constructor
	// Initialize variables – root is null and size is 0.
	public BinarySearchTree( )
	{
		this.root = null;
		this.size =0;
	}
	

	// Constructor that builds a tree from the values in sorted List L.
	// Initialize variables – root is null and size is L.size() - and call 
	// recursive method buildTree() if L.size() > 0.
	// Throws IllegalArgumentException if L is null or any element in L is 
	// null.
	// Assume there are no duplicate elements in L.
	public BinarySearchTree(List<T> L)
	{
		if ( L == null)
			throw new IllegalArgumentException();
		for (T e : L) 
		{ if (e == null) throw new IllegalArgumentException(); }
		this.root = null;
		this.size = L.size();
		if (L.size() > 0)
			root = buildTree(0,L.size() -1, L);
		
	}

	// Builds a balanced tree from the values in sorted List L.
	// Start and end are the start and end positions of a sub-list of L.
	// Returns the root of the subtree containing the elements in the 
	// sub-list of L.
	// Throws IllegalArgumentException if L or any element in L is null.
	// Called by BinarySearchTree(List<T> L) and balance().
	// This is a recursive method.
	protected TreeNode<T> buildTree(int start, int end, List<T> L)
	{
		if ( L == null)
			throw new IllegalArgumentException();
		for (T e : L) 
		{ if (e == null) throw new IllegalArgumentException(); }
	
		if (start > end) return null;
		TreeNode<T> t = new TreeNode<T>();
		TreeNode<T> parentNode = new TreeNode<T>();
		int mid = middle(start, end);
		t.data = L.get(mid);
		parentNode = t;
		
		
		t.left = buildTree(start, mid -1, L);
		t.right = buildTree(mid +1, end, L);
	
		if (t.left!=null) 
		{
			t.left.parent = t;
		}
		if (t.right!=null) 
		{	
			t.right.parent = t;
		}
		
		return t;
		
	}

	// If x is not already in the tree, inserts x and returns true.
	// If x is already in the tree, does not insert x and returns false.
	// Sets the new nodes left, right, and parent references. 
	// Throws IllegalArgumentException if x is null.
	// This is a non-recursive method.
	public boolean insert(T x)
	{
		if (x == null)
			throw new IllegalArgumentException();
		if (contains(x))
			return false;
		if (root == null)
		{
			root = new TreeNode<T>(x);
			size++;
			return true;
		}
		TreeNode<T>current = root;
		TreeNode<T>parent = root;
		while(current!=null)
		{
			if (x.compareTo(current.data)<0)
			{
				parent = current;
				current = current.left;
				if(current == null)
				{
					current = new TreeNode<T>(x);
					parent.left = current;
					current.parent = parent;
					size++;
					return true;
				}

			}
			else if (x.compareTo(current.data)>0)
			{
				parent = current;				
				current = current.right;
				if(current == null)
				{
					current = new TreeNode<T>(x);
					parent.right = current;
					current.parent = parent;
					size++;
					return true;
				}

			}
		}
		return false;
	}

	// Removes x from the tree.
	// Return true if x is removed; otherwise, return false;
	// Throws IllegalArgumentException if x is null.
	// This is a non-recursive method.
	public boolean remove( T x)
	{
		if (x == null)
			throw new IllegalArgumentException();
		TreeNode<T>current = search(x);
		if (current == null)
			return false;
		if (current == root && size == 1 && current.parent == null)
		{
			root = null;
			size--;
			return true;
		}
		
		TreeNode<T> p = current.parent; 
		if(current.left == null && current.right == null)
		{
			current = null;
			size--;
			return true;
		}
		else if (current.left != null && current.right != null)
		{
			TreeNode<T> toReplace = getAndRemoveMinNode(current.right);
			current.data = toReplace.data;
			size--;
			return true;
		}
		
		else if (current.left != null && current.right == null) 
		{	
			if( (p!= null) &&current.data.compareTo(p.data)<0)
			{
				p.left = current.left;
				current.left.parent = p;
				current = null;
				size--;
				return true;
			}
			else if((p!= null) &&current.data.compareTo(p.data)>0)
			{
				p.right = current.left;
				current.left.parent = p;
				current = null;
				size--;
				return true;
			}
			else if (p == null)
			{
				root = current.left;
				root.parent = null;
				size--;
			}
		}
		else if (current.right != null && current.left == null ) 
		{
			if((p!= null) &&current.data.compareTo(p.data)<0)
			{
				p.left = current.right;
				current.right.parent = p;
				current = null;
				size--;
				return true;
			}
			else if((p!= null) &&current.data.compareTo(p.data)>0)
			{
				p.right = current.right;
				current.right.parent = p;
				current = null;
				size--;
				return true;
			}
			else if (p == null)
			{
				root = current.right;
				root.parent = null;
				size--;
			}
		}
		
		return false;
	}

	// Returns an element in the list that equals x, or null if there is no 
	// such element.
	// Throws IllegalArgumentException if x is null.
	// May call method search.
	// This is a non-recursive method.
	public T getMatch(T x)
	{
		if (x == null)
			throw new IllegalArgumentException();
		if (!contains(x))
			return null;
		T result = search(x).data;
		return result;
		
			
	}

	// Returns true if there is an element in the list that equals x.
	// Throws IllegalArgumentException if x is null.
	// May call method search.
	// This is a non-recursive method.
	public boolean contains(T x)
	{
		if (x == null)
			throw new IllegalArgumentException();
		if (root == null) // no items in tree
			return false;
		TreeNode<T> current = root;
		while (current!=null)
		{
			if (x.compareTo(current.data)<0)
				current = current.left;
			else if (x.compareTo(current.data)>0)
				current = current.right;
			else if (x.compareTo(current.data)==0)
				return true;
		}
		return false;
	}

	// Return a reference to the node that contains an element that equals 
	// x or null if there is no such node.
	// Any method that calls this method should ensure that x is not null.
	// This is a non-recursive method.
	protected TreeNode<T> search(T x)
	{
		if(!contains(x))
			return null;
		TreeNode<T> current = root;
		while (current!=null)
		{
			if (x.compareTo(current.data)<0)
				current = current.left;
			else if (x.compareTo(current.data)>0)
				current = current.right;
			else if (x.compareTo(current.data)==0)
				return current;
		}
		return null;
	}

	// Returns a reference to the node that contains the minimum element in 
	// the subtree rooted at node n.
	// Called by method next(); method next() should ensure that node n is 
	// not null.
	// This is a non-recursive method.
	protected TreeNode<T> getMinNode(TreeNode<T> n)
	{
		TreeNode<T> current = n;
		while(current.left !=null)
			current= current.left;
		return current;
	}

	// Returns a reference to the node that contains the max element in 
	// the subtree rooted at node n.
	// Called by method previous(); method previous() should ensure that node n is 
	// not null.
	// This is a non-recursive method.
	protected TreeNode<T> getMaxNode(TreeNode<T> n)
	{
		TreeNode<T> current = n;
		while(current.right !=null)
			current= current.right;
		return current;
	}
	
	// Returns a reference to the node that contains the minimum element in 
	// the subtree rooted at node n; the found node is also removed from the 
	// tree. Note that n may be the node containing the minimum element. 
	// Any method that calls this method should ensure that n is not null
	//   and that n is not the root.
	// This is a non-recursive method.
	protected TreeNode<T> getAndRemoveMinNode(TreeNode<T> n)
	{
		TreeNode<T> minNode = getMinNode(n);
		TreeNode<T> p = minNode.parent;
		if(minNode.right!=null && p!=null)
		{ 
			if (minNode.data.compareTo(p.data)<0)
			{	p.left = minNode.right; 
				minNode.right.parent = p;
			}
			if (minNode.data.compareTo(p.data)>0)
			{	p.right = minNode.right; 
				minNode.right.parent = p;
			}
		}
		
		else if(minNode.right==null && p!=null)
		{
			if (minNode.data.compareTo(p.data)<0)
				p.left = null;
			if (minNode.data.compareTo(p.data)>0)
				p.right = null;
		}
		
		return minNode;
	}

	// Returns the node that is the node after node n in sorted order,
	// as determined by an inorder traversal of the tree. The next node is 
	// node with the smallest data element greater than n.data.
	// May be called by remove().
	// This is a non-recursive method.
	protected TreeNode<T> next(TreeNode<T> n)
	{
		if (n == null)
			throw new IllegalArgumentException();
		TreeNode<T> current = n;
		TreeNode<T> p;
		if (current.right!=null)
			return getMinNode(current.right);
		else if(current.right == null)
			{
				if(current.parent!=null){
					p = current.parent;
					if(p.data.compareTo(n.data)>0) return p;
					else if (p.data.compareTo(n.data)<0)
					{
						if (p.parent!=null) return p.parent;
					}
				}			
			}
			
		return null;
	}

	// Returns the node that is the node before n in sorted order, as
	// determined by an inorder traversal of the tree. The previous node is 
	// node with the largest data element smaller than n.data.
	// Methods next() and previous() are symmetric.
	// This is a non-recursive method.
	protected TreeNode<T> previous(TreeNode<T> n)
	{
		if (n == null)
			throw new IllegalArgumentException();
		TreeNode<T> current = n;
		TreeNode<T> p;
		if (current.left!=null)
			return getMaxNode(current.left);
		else if(current.left == null)
		{
			if(current.parent!=null)
			{
				p = current.parent;
				if(p.data.compareTo(n.data)<0) return p;
				else if(p.data.compareTo(n.data)>0)
				{
					if(p.parent!= null) return p.parent;
				}
			}
		}
		return null;
	}

	// Returns the number of elements in the tree
	// Target Complexity: O(1)
	public int size()
	{
		return size;
	}

	// Returns true if there are no elements.
	// Target Complexity: O(1)
	public boolean isEmpty()
	{
		return root == null;
	}

	// Make the tree logically empty.
	// Target Complexity: O(1)
	public void clear()
	{
		root = null;
		size =0;
	}

	// Convenience method that returns a List of elements in sorted order.  
	// Calls getListOfElements(PrePostIn.INORDER);
	public List<T> getSortedListOfElements()
	{
		return getListOfElements(PrePostIn.INORDER);
	}

	// Returns a List of elements in the order specified by parameter order, 
	// which is either PREORDER, POSTORDER, or INORDER.
	// Calls fillListOfElements(root,order)
	public List<T> getListOfElements(PrePostIn order)
	{
		return fillListOfElements(root,order);
	}
	

	// Returns a List of elements in the order specified by parameter order, 
	// which is either PREORDER, POSTORDER, or INORDER.
	// This is a non-recursive method.
	// Target Complexity: O(n)
	protected List<T> fillListOfElements(TreeNode<T> node, PrePostIn order)
	{
		List<T> list = new ArrayList<T>();
		MyHashMultiMap<T,Integer> map = new MyHashMultiMap<T,Integer>();
		TreeNode<T> current = node;
		int i = 0, visit = 0;
		if(node == null)
			throw new IllegalArgumentException();
		
		switch (order){
		case PREORDER:
			while(current!=null){
				map.put(current.data, i); i++;
				visit = map.get(current.data).size();
				if(visit ==1)
				{
					list.add(current.data);
					if (current.left != null)
						current = current.left;
					
				}
				if (visit == 2)
				{
					
					if(current.right != null)
						current = current.right;
				}
				if (visit == 3)
				{
					current = current.parent;
				}
			}
			
			return list; 
		
		case POSTORDER:
			while(current!=null){
				map.put(current.data, i); i++;
				visit = map.get(current.data).size();
				
				if(visit ==1)
				{
					
					if (current.left != null)
						current = current.left;
					
				}
				if (visit == 2)
				{
					
					if(current.right != null)
						current = current.right;
				}
				if (visit == 3)
				{
					list.add(current.data);
					current = current.parent;
				}
			}
			
			return list; 
		case INORDER:
			while(current!=null){
				map.put(current.data, i); i++;
				visit = map.get(current.data).size();
				
				if(visit ==1)
				{
					if (current.left != null)
						current = current.left;
					
				}
				if (visit == 2)
				{
					list.add(current.data);
				
					if(current.right != null)
						current = current.right;
				}
				if (visit == 3)
				{
					current = current.parent;
				}
			}
			
			return list; 
		}
		
		
		return list; 
		
	}

	// Balances the tree.
	// Calls buildTree(int start, int end, List<T> L) where L is a sorted 
	// List of the elements in the tree, and start and end are the positions 
	// at the beginning and end of L.
	public void balance()
	{
		List<T> L = getSortedListOfElements();
		root = buildTree(0, L.size()-1, L);
		
	}

	// Helper middle to compute the middle position of a sub-list
	protected int middle(int start, int end) {return (start + end) / 2;}
}