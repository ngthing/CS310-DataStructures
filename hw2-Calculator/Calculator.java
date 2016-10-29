import java.util.*;

public class Calculator 
{
	protected static Number current ;

	public static void main(String[] args)
	{
		Scanner keyboard = new Scanner(System.in);
		Number n = new Number(); 
		current = new Number();
		String menu="", input="";
		boolean inloop = true;
		while(inloop){
			try{
				menu = showMenu();
				switch (menu){
				case "e":
				case "E":
					System.out.println("value: ");
					input = keyboard.next();
					current = new Number(input);
					System.out.println(current.toString()+ "\n");
					break;
				case "c":
				case "C":
					current = new Number();
					System.out.println("0\n");
					break;
				case "s":
				case "S":
					System.out.println("value: ");
					input = keyboard.next();
					n = new Number(input);
					current = current.subtract(n);
					System.out.println(current.toString()+ "\n");
					break;
				case "a":
				case "A":
					System.out.println("value: ");
					input = keyboard.next();
					n = new Number(input);
					current = current.add(n);
					System.out.println(current.toString()+ "\n");
					break;
				case "m":
				case "M":
					System.out.println("value: ");
					input = keyboard.next();
					n = new Number(input);
					current = current.multiply(n);
					System.out.println(current.toString()+ "\n");
					break;
				case "r":
				case "R":
					current.reverseSign();
					System.out.println(current.toString()+ "\n");
					break;
				case "q":
				case "Q":
					inloop=false;
					break;
				}

			}
			catch (BadNumberException e){
				System.out.println("Invalid number input. Please try again!");
			}
		}

	}



	private static  String showMenu()
	{
		Scanner keyboard = new Scanner(System.in);
		String input= "";
		System.out.println("Enter a value: e	Add: a");
		System.out.println("Subtract: s		Multiply: m");
		System.out.println("Reverse sign: r		Clear: c");
		System.out.println("Quit: q");
		System.out.println("Enter option here:");
		input = keyboard.next();
		System.out.println("-> " + input);

		return input;
	}
}


