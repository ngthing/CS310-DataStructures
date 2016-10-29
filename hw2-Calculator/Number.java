import java.util.ListIterator;

public class Number implements Comparable<Number>
{
	protected DigitList number;
	protected int digitCount = 0;
	protected int decimalPlaces = 0; //number of digits after decimal place
	protected boolean negative = false; // sign (positive or negative)

	// Constructor 
	Number()
	{
		this.number = new DigitList();

	}

	// Constructor that takes a String representation of a number 
	// (e.g. "-21.507"). Calls method accept().
	public Number(String str) throws BadNumberException 
	{
		this();
		accept(str);
	}

	// Builds a DigitList representation of the number represented by str 
	// after str has been validated and trimmed.   
	public void accept(String str) throws BadNumberException
	{

		validate(str);
		int i =0; char x; boolean hasPoint= false;
		do {
			x = str.charAt(i);
			if ( x == '-') { this.negative = true; str = str.substring(1); i= -1;} 
			if (x == '.'){ hasPoint = true; str = str.substring(i+1); i =-1;}
			if (x > 47 && x < 58)
			{
				this.number.addLast(x-48);
				this.digitCount++;
				if(hasPoint) {this.decimalPlaces++;}
			}
			i++;
		}while(i < str.length());

	trim();
	}

	// Returns a Number that represents "this + n". 
	// Target Complexity: O(n)
	public Number add(Number n)
	{
		Number sum = new Number();
		if (this.negative == false && n.negative==false)
		{ sum = this.addAbsolute(n); }
		else if (this.negative && n.negative)
		{ sum = this.addAbsolute(n); sum.negative = true; }
		else if (this.negative == false && n.negative ) 	
		{
			if (this.compareToAbsolute(n) > 0){
				sum = this.subtractAbsolute(n);
			}
			else if (this.compareToAbsolute(n) < 0){
				sum = n.subtractAbsolute(this);
				sum.negative = true;
			}
			else if (this.compareToAbsolute(n) == 0){
				sum.number.addFirst(0); sum.digitCount++;
			}
		}
		else if (this.negative && n.negative == false)
		{
			if (this.compareToAbsolute(n) > 0){
				sum = n.subtractAbsolute(this);
				sum.negative = true;
			}
			else if (this.compareToAbsolute(n) < 0){
				sum = this.subtractAbsolute(n);
			}
			else if (this.compareToAbsolute(n) == 0){
				sum.number.addFirst(0); sum.digitCount++;
			}
		}
		
		
		assert ( sum !=null && sum.digitCount > 0 && sum.decimalPlaces >= 0 );
		return sum;
		
	}

	// Returns a Number that represents "this - n". 
	// Target Complexity: O(n)
	public Number subtract(Number n)
	{
		Number difference = new Number ();
		if ( this.negative == false && n.negative)
		{ difference = this.addAbsolute(n); }
		else if (this.negative && n.negative == false)
		{ difference = this.addAbsolute(n); difference.negative = true; }
		else if (this.negative == false && n.negative == false )
		{
			if (this.compareToAbsolute(n) > 0){
				difference = this.subtractAbsolute(n); 
			}
			else if (this.compareToAbsolute(n)<0){
				difference = n.subtractAbsolute(this);
				difference.negative = true;
			}
			else if (this.compareToAbsolute(n) == 0)
			{ difference.number.addFirst(0); difference.digitCount++;}
		}
		else if ( this.negative && n.negative)
		{
			if (this.compareToAbsolute(n) > 0){
				difference = this.subtractAbsolute(n);
				difference.negative = true;
			}
			else if (this.compareToAbsolute(n) < 0){
				difference = n.subtractAbsolute(this);
			}
			else if (this.compareToAbsolute(n) == 0)
			{ difference.number.addFirst(0); difference.digitCount++;}
			}
		assert (difference != null && difference.digitCount >0 && difference.decimalPlaces >=0 );
		return difference; 
	}

	// Returns a Number that represents "this * n". 
	// Target Complexity for multiplication of an n digit number and an m 
	// digit number: O(m x n)
	public Number multiply(Number n)
	{
		Number product = new Number();
		int nDigit;
		ListIterator<Integer> nItr = n.number.listIterator(0);
		while(nItr.hasNext()){
			nDigit= nItr.next();
			Number partialProduct = new Number();
			int carry =0;
			int thisDigit = 0, newDigit; 
			ListIterator<Integer> thisItr = this.number.listIterator(this.number.size());
			while(thisItr.hasPrevious()){
				thisDigit = thisItr.previous();
				newDigit = nDigit * thisDigit + carry;
				carry = newDigit/10;
				newDigit = newDigit%10;
				partialProduct.number.addFirst(newDigit);
				partialProduct.digitCount++;
			}
			if (carry!=0){
				partialProduct.number.addFirst(carry); partialProduct.digitCount++;
			}
			
			product.number.addLast(0); product.digitCount++;
			product = product.addAbsolute(partialProduct);
			
		}
		
		product.decimalPlaces = this.decimalPlaces + n.decimalPlaces;
		
		while(product.digitCount < product.decimalPlaces ){
			product.number.addFirst(0); product.digitCount++;
		}
		if ( this.negative != n.negative) // this and n have different sign
		{ product.negative = true; }
		
		product.trim();
		
		assert (product.number != null);
		return product;
	}

	//Reverses the value of negative. 
	public void reverseSign()
	{
		if (this.negative) { this.negative = false;}
		else if (!this.negative) {this.negative = true; }
	}

	public boolean equals(Object rhs)
	{
		// check for self-comparison
		if (this == rhs) return true; 
		if (!(rhs instanceof Number)) return false;
		
		//cast to Number is now safe
		Number that = (Number)rhs;
		return this.compareTo(that) == 0 ;
	}

	// Implements compareTo of Comparable
	public int compareTo(Number n)
	{
		int result = 0;
		if (this.negative == true && n.negative == false) // this is neg while n is pos
		{ result = -1; }
		if (this.negative == false && n.negative == true) // this is pos while n is neg
		{ result = 1;}
		if ( this.negative && n.negative) // both number are negative
		{
			if (this.compareToAbsolute(n) == 0)
			{ result = 0; }
			else if (this.compareToAbsolute(n) < 1) // this absolute < n absolute
			{ result = 1; } // both negative, which has absolute value smaller is greater 
			else if (this.compareToAbsolute(n) > 1)
			{ result = -1; }
			
		}
		if (this.negative == false && n.negative == false) // both number are positive
		{ result = this.compareToAbsolute(n); }
		return result;
	}
	

	//The next three methods perform operations disregarding the signs 
	// (i.e. the negative field) of the Numbers. See descriptions below.

	// Target Complexity: O(n)
	protected Number addAbsolute(Number n)
	{
		Number sum = new Number(); 

		int thisbefore =0, nbefore =0; // the number of digits before decimal point in this num and n num)
		
		int i =0;
		// match this and n digitCount and decimalPoints 
		if (this.decimalPlaces > n.decimalPlaces)
		{
			sum.decimalPlaces = this.decimalPlaces; 
			int dif = this.decimalPlaces - n.decimalPlaces;
			do {
				n.number.addLast(0); i++;
				n.decimalPlaces++; n.digitCount++;
			}while (i<dif);
			
		}
		i =0;
		if (n.decimalPlaces > this.decimalPlaces)
		{
			sum.decimalPlaces = n.decimalPlaces; 
			int dif = n.decimalPlaces - this.decimalPlaces;
			do {
				this.number.addLast(0); i++;
				this.decimalPlaces ++; this.digitCount++;
			}while (i<dif);
			
		}
		thisbefore = this.digitCount - this.decimalPlaces;
		nbefore = n.digitCount - n.decimalPlaces;
		i =0;
		if (thisbefore > nbefore)
		{ 
			int dif = thisbefore - nbefore; //the difference 
			do {
				n.number.addFirst(0); i++; 
			}while(i<dif);
			n.digitCount = this.digitCount;
		}
		i =0;
		if (nbefore > thisbefore)
		{ 
			int dif = nbefore - thisbefore;
			do {
				this.number.addFirst(0); i++;
			}while(i<dif);
			 this.digitCount=n.digitCount;
		}
		// finish matching this and n digitCount and decimalPoints 
		
		if (n.decimalPlaces == this.decimalPlaces)
		{sum.decimalPlaces = n.decimalPlaces;}
		
		int carry =0; int thisDigit, nDigit, newDigit=0;
	
		ListIterator<Integer> thisItr = this.number.listIterator(this.number.size());
		ListIterator<Integer> nItr = n.number.listIterator(n.number.size());
		
		while(thisItr.hasPrevious() && nItr.hasPrevious())
		{
			thisDigit = thisItr.previous(); 
			nDigit = nItr.previous();
			newDigit = thisDigit + nDigit + carry;
			carry = newDigit/10;
			sum.number.addFirst(newDigit%10);
			sum.digitCount++;
		}
		if(carry !=0) { sum.number.addFirst(carry); sum.digitCount++; }
		if (sum.digitCount == 0 && sum.decimalPlaces == 0) {sum.number.addFirst(0);}
		sum.trim();
		
		assert ( sum.number != null);
		return sum;
	}

	// Target Complexity: O(n)
	protected Number subtractAbsolute(Number n) 
	{
		
		Number difference = new Number(); 

		int thisbefore =0, nbefore =0; // the number of digits before decimal point in this num and n num)
		Number thisnew = null, nnew = null; //new this and n after compareToAbsolute. newthis has the largest magnitude
		
		
		if (this.compareToAbsolute(n) > 0 || this.compareToAbsolute(n) == 0 )
		{  thisnew =this; nnew = n; }
		else if (this.compareToAbsolute(n) < 0)
		{ thisnew = n; nnew =this; }
		
		assert (thisnew!=null && nnew !=null); 
	
		int i =0;
		// match this and n digitCount and decimalPoints 
		if (thisnew.decimalPlaces > nnew.decimalPlaces)
		{
			difference.decimalPlaces = thisnew.decimalPlaces; 
			int dif = thisnew.decimalPlaces - nnew.decimalPlaces;
			do {
				nnew.number.addLast(0); i++;
				nnew.decimalPlaces++; nnew.digitCount++;
			}while (i<dif);
			
		}
		i =0;
		if (nnew.decimalPlaces > thisnew.decimalPlaces)
		{
			difference.decimalPlaces = nnew.decimalPlaces; 
			int dif = nnew.decimalPlaces - thisnew.decimalPlaces;
			do {
				thisnew.number.addLast(0); i++;
				thisnew.decimalPlaces ++; thisnew.digitCount++;
			}while (i<dif);
			
		}
		i =0; 
		thisbefore = thisnew.digitCount - thisnew.decimalPlaces;
		nbefore = nnew.digitCount - nnew.decimalPlaces;
		
		if (thisbefore > nbefore)
		{ 
			int dif = thisbefore - nbefore; //the difference 
			do {
				nnew.number.addFirst(0); i++;
			}while(i<dif);
			nnew.digitCount = thisnew.digitCount;
		}
		i =0;
		if (nbefore > thisbefore)
		{ 
			int dif = nbefore - thisbefore;
			do {
				thisnew.number.addFirst(0); i++;
			}while(i<dif);
			 thisnew.digitCount=nnew.digitCount;
		}
		// finish matching this and n digitCount and decimalPoints 
		
		if (nnew.decimalPlaces == thisnew.decimalPlaces)
		{difference.decimalPlaces = nnew.decimalPlaces;}
		
		int borrow =0; int thisDigit, nDigit, newDigit=0;
	
		ListIterator<Integer> thisItr = thisnew.number.listIterator(thisnew.number.size());
		ListIterator<Integer> nItr = nnew.number.listIterator(nnew.number.size());
		
		
		while(thisItr.hasPrevious() && nItr.hasPrevious())
		{
			thisDigit = thisItr.previous(); 
			nDigit = nItr.previous();
			newDigit = thisDigit - nDigit - borrow;
			
			
			if (newDigit < 0){
				newDigit += 10; borrow =1;
			}
			else {borrow = 0;}
			difference.number.addFirst(newDigit);
			difference.digitCount++;
		}
		
		if (difference.digitCount == 0 && difference.decimalPlaces == 0) {difference.number.addFirst(0);}
		difference.trim();
		
		assert ( difference.number != null);
		return difference;
	}

	// Target Complexity: O(n)
	protected int compareToAbsolute(Number n)
	{
		assert (n!=null);
		int result = 0 , nbefore, thisbefore;
		thisbefore = this.digitCount - this.decimalPlaces;
		nbefore = n.digitCount - n.decimalPlaces;
		String thisString = this.toString();
		String nString = n.toString();
		
		if(nbefore == thisbefore)
		{result = thisString.compareTo(nString);}
		if (thisbefore < nbefore)
		{ result = -1; }
		if (thisbefore > nbefore)
		{ result =1; }
		
		return result;
	}

	// Returns a String representation of the Number.
	// Example: 3.1416 (And see the examples in the Section 1 sample
	// execution.)
	public String toString()
	{
		String result = "";
		int digitBeforeDec = this.digitCount - this.decimalPlaces;
		int i =0;
		ListIterator<Integer> iterator = number.listIterator(0);
		while(iterator.hasNext())
		{
			if (i == digitBeforeDec && this.decimalPlaces != 0)
			{result += '.';}
			result += iterator.next().toString();
			i++;
			
		}
		if (negative) { result = '-' +result;}
		assert (result !=null);
		return result;

	}

	// Removes all extra leading 0s which precede the decimal point 
	// and all extra trailing 0s which follow the decimal point. 
	public void trim()
	{
		
		//remove trailing 0 after decimal place
		while(this.decimalPlaces>0 && this.digitCount >0 && this.number.getLast()== 0)
		{ 
			this.number.removeLast(); 
			this.decimalPlaces--; this.digitCount--;
		}
		// remove heading 0 
		while(this.digitCount >0 && this.digitCount != this.decimalPlaces && 
				this.number.getFirst()==0)
		{
			
			this.number.removeFirst();
			this.digitCount--; 
			
		}
		
		if (this.digitCount == 0){ this.number.addFirst(0); this.digitCount++;}
		
	}

	// Ensures that the string passed to accept() or to the constructor 
	// represents a valid number.
	// Throws BadNumberException if str is not valid.
	public void validate (String str) throws BadNumberException
	{
		if(str.length() ==0) throw new BadNumberException("No input");
		if ((str.charAt(0) == '+' || str.charAt(0) == '-') && str.length() == 1)
		{throw new BadNumberException("Input only contains sign");}
		if ( str.charAt(0) == '+')throw new BadNumberException("Input contains '+' sign");

		if (str.charAt(0) == '-') { str = str.substring(1);}
		int decPoint =0; boolean valid = true; int i =0; char x; 

		do {
			x = str.charAt(i);
			if ( x == '.') { decPoint++;}
			if (x < 48 || x > 57) { if (x != '.') valid= false;}
			i++;
		}while (i<str.length() && valid && decPoint < 2);

		if (decPoint >1) { throw new BadNumberException("Input has more than one decimal points");}

		if (!valid) { throw new BadNumberException("Input has non-digit value");}
	}

}

