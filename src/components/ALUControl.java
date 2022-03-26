package components;

import other.DatapathException;

public class ALUControl {
	
	/*
	 * returns a String of 4 bits dictating the components.ALU operation.
	 * 
	 * inputs: funct (!3 bits), and ALUop (3 bits)
	 * 
	 */
	
	public static String ALUSignals(String funct, String ALUop) throws DatapathException {
		
		if((ALUop.length()!=3 && !ALUop.equals("x"))|| funct.length()!=13) {
			throw new DatapathException("Length of either funct or ALUop is incorrect as passed to ALUSignals.");
		}
		
		String ret = "";
		
		
		switch(ALUop) {
			case "x": ret = "xxxx"; break;
			case "000": ret = "0000";break;
			case "010": ret = "0100";break;
			case "011": ret = "0101";break;
			case "100": ret = "0110";break;
			case "101": ret = "0001";break;
			case "001":
				switch(funct) {
					case "0000000000000":ret = "0000";break;
					case "0000000000001":ret = "0001";break;
					case "0000000000010":ret = "0010";break;
					case "0000000000011":ret = "0011";break;
					case "0000000000100":ret = "0111";break;
					default: throw new DatapathException("Invalid funct field @components.ALUControl!");
				}
				break;
			default: throw new DatapathException("invalid ALUOp @components.ALUControl!");}

		return ret;
	}
	
	public static void main(String blabla[]) {
		
	}

}
