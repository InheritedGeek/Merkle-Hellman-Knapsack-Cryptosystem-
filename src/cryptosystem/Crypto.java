package cryptosystem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Crypto {

	private static Scanner input;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Enter a string and I will encrypt it as single large integer.");
		input = new Scanner(System.in);
		String str = input.nextLine();
		System.out.println("Clear text:"+"\n"+str+"\n"+"Number of clear text bytes = "+str.length());
		
		if (str.length() >80) {
			System.out.println("The string entered is too long please try again.");
			return;
		}
		
		//The private key consists of w, q and r.
		// Generating a super increasing sequence in a Singly Linked List (i.e. w)
		SinglyLinkedList w = new SinglyLinkedList();
		BigInteger sum = SuperIncreasing(w);
		w.countNodes();
		
		// Choosing q which is a random No. greater than the sum of Linked List sequence
		// Choosing r by an iterative process to ensure it's in the range [1,q) and is coprime to q.
		BigInteger q = sum.add(BigInteger.valueOf(new Random().nextInt() & Integer.MAX_VALUE));
		BigInteger r = q.subtract(BigInteger.valueOf(1));

		// Co-prime check using GCD
		BigInteger gcd = q.gcd(r);
		while (gcd.compareTo(BigInteger.valueOf(1))!=0)
			r = r.subtract(BigInteger.valueOf(1));
		
		// Generating a new list b by multiplying each element in w by r mod q
		// b here makes up the public key
		SinglyLinkedList b = new SinglyLinkedList();
		int indx = 0; //Index of linked list
		while (w.hasNext() && indx < w.countNodes) {
			BigInteger var = (BigInteger) w.getObjectAt(indx);
			BigInteger Data = (r.multiply(var)).mod(q);
			if (indx ==0)
				b.head = new ObjectNode(Data, null);
			else				
				b.addAtEndNode(new ObjectNode(Data, null));
			indx++;
		}

		//Encryption Process
		BigInteger encryption = encryption(str, b);
		System.out.println(str +" is encrypted as"+"\n"+encryption);

		//Decryption Process
		System.out.print("Result of decryption: "+decryption(r, q, encryption, w));
	}
	
	
	public static BigInteger SuperIncreasing(SinglyLinkedList w) {
		// Creating Super Increasing Sequence
		
		int cnt =0;
		BigInteger NodeData = new BigInteger("1");
		BigInteger sum = new BigInteger("0");

		while (cnt < 640){
			NodeData = NodeData.multiply(BigInteger.valueOf(2));		
			if (cnt ==0)
				w.head = new ObjectNode(NodeData, w.tail);
			else
				w.addAtEndNode(new ObjectNode(NodeData, w.tail));		
			sum = sum.add(NodeData);
			cnt++;
		}
		return sum;
	}
	
	// Encryption works by translating the input string to binary
	// Then multiplying each respective bit by the corresponding number in b.
	public static BigInteger encryption(String str, SinglyLinkedList b){
		
		BigInteger output = new BigInteger("0");
		String binaryValue = AsciiConverter(str);
		
		for (int i = 0; i <binaryValue.length();i++) {
			int m =  Character.getNumericValue(binaryValue.charAt(i));
			BigInteger n = (BigInteger) (b.getObjectAt(i));
			BigInteger result = n.multiply(BigInteger.valueOf(m));
			output = output.add(result);
		}
		return output;
	}
	
	// Decryption works by Multiplying the decimal(Base 10) encryption by r inverse mod q
	// Lets store the value we get from above as a big integer called result
	// After that we start by subtracting the result from the largest 
	// element in w which is less than the result.
	// This process is done repeatedly until the resultant becomes 0
	// The elements we selected from our private key correspond to the 1 bits in the message.
	// When we result the binary from above, we get the actual message
	public static String decryption(BigInteger r, BigInteger q, BigInteger encryption, SinglyLinkedList w) {
		
		BigInteger rInv = r.modInverse(q);
		BigInteger result = (encryption.multiply(rInv)).mod(q);
		int lastIndex =0;
		
		ArrayList<Integer> list1 = new ArrayList<Integer>(Collections.nCopies(640, 0));
		while (result.compareTo(BigInteger.valueOf(0))==1) {
			BigInteger maxval = new BigInteger("0");
			int k =0;
			for (int i =0; i < w.countNodes;i++) {
				BigInteger val = (BigInteger) w.getObjectAt(i);
				if (val.compareTo(result) == -1 || val.compareTo(result) == 0) {  //Node is smaller or equal to Result
						maxval = val;
						k = i;
				} else {
					if (i>lastIndex)
						lastIndex =i;
						break;
				}
			}
			result = result.subtract(maxval);
			list1.set(k,1);
		}
		
		String binaryvalue = "";
		for (int i =0;i<list1.size();i++) {
			binaryvalue += list1.get(i);
		}
		
		String output = "";
		for (int i =0;i<lastIndex; i+=8){
			int charCode = Integer.parseInt(slicer(binaryvalue, i, i+8), 2);
			output += new Character((char)charCode).toString();
		}
		return output;
	}
	
	
	public static String slicer(String input, int startIndex, int endIndex) {
	    if (startIndex < 0) startIndex = input.length() + startIndex;
	    if (endIndex < 0) endIndex = input.length() + endIndex;
	    return input.substring(startIndex, endIndex);
	}
	
	
	public static String AsciiConverter(String message){
        
        byte[] MessageBytes = message.getBytes();
        StringBuilder sb = new StringBuilder();
        
        for (byte b : MessageBytes)
        {
           int val = b;
           for (int i = 0; i < 8; i++)
           {
        	  sb.append((val & 128) == 0 ? 0 : 1);
              val <<= 1;
           }
        }
        return sb.toString();
	}
	
}
