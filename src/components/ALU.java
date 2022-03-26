package components;

import other.DatapathException;

public class ALU {
	
	private static int zflag = 0;

	public static int ALUEvaluator (String Op,int Operand1, int Operand2) throws DatapathException {
		String opname ="";
		int output=0;
		
		switch(Op) {

		case "xxxx":
			output = 0;
			break;
		case "0011":
			opname = "AND";
			output = ANDOp(Operand1,Operand2);
			break;
		case "0100":
			opname = "OR";
			output = OROp(Operand1, Operand2);
			break;
		case "0000":
			opname = "ADD";
			output = addOp(Operand1,Operand2);
			break;
		case "0001":
			opname = "SUB";
			output = subOp(Operand1,Operand2);
			break;
		case "0111":
			opname = "SLT";
			output = sltOp(Operand1,Operand2);
			break;
		case "0010":
			opname = "MUL";
			output = mul(Operand1,Operand2);
			break;
		case "0101":
			opname = "SLL";
			output = sll(Operand1,Operand2);
			break;
		case "0110":
			opname = "SRL";
			output = srl(Operand1, Operand2);
			break;
			
		default: 
			throw new DatapathException("Illegal operation @ components.ALU.");
		}
		
		
		
		
		//display
		
//		System.out.println("Input: \n1st Operand: "  + Operand1 + "\n2nd Operand: " + Operand2 + "\nOperation: " + Op);
//		System.out.println("\nOutput: \nOperation Name: " + opname + "\n1st Operand: "  + Operand1 + "\n2nd Operand: " + Operand2
//				+"\nOutput: " + output + "\nZ-Flag Value: " + zflag);

		return output;
	}

	
	//calculation helpers
	
	//NOTE: sll: first operand is the number to be shifted and the second operand is the shift amount
	
	public static int sll(int a, int b) {
		int temp = a << b;
		zflag = (temp==0)? 1:0;
		return temp ;
	}
	
	public static int srl(int a, int b) {
		int temp = a >> b;
		zflag = (temp==0)? 1:0;
		return temp;
	}
	
	public static int mul(int a, int b) {
		int temp =  a*b;
		zflag = (temp==0)? 1:0;
		return temp;

	}
	
	public static int ANDOp(int a, int b) {
		int temp = a & b;
		zflag = (temp==0)? 1:0;
		return temp;
	}
	
	public static int OROp(int a, int b) {
		int temp = a | b;
		zflag = (temp==0)? 1:0;
		return temp;
	}
	
	public static int NOR(int a , int b) {
		int temp = (~(a|b));
		zflag = (temp==0)? 1:0;
		return temp;
	}
	
	public static int sltOp(int a, int b) {
		int temp = 0;
		if(a<b) {
			temp = 1;
		}
		else {
			temp = 0;
		}
		
		zflag = (temp==0)? 1:0;

		return temp;
		
	}
	
	public static int addOp(int a, int b) {
		int temp = (a+b);
		zflag = (temp==0)? 1:0;
		return temp;
	}
	
	public static int subOp(int a, int b) {
		int temp = (a-b);
		zflag = (temp==0)? 1:0;
		return temp;
	}
	
	

	//testing
	
//	public static void main(String args[]) {
//		
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Enter your first input AS AN INT: ");
//		int inp1 = sc.nextInt();
//		System.out.println("Enter your second input AS AN INT: ");
//		int inp2 = sc.nextInt();
//		System.out.println("Enter the operation AS A BINARY STRING: ");
//		String binOP = sc.next();
//		
//		components.ALU.ALUEvaluator(binOP,inp1,inp2);
//	}
}
