package components;

public class BranchControl {
	
	/*
	 * inputs: OPCode 4 bits, jump 1 bit, branch 1 bit, gflag 1 bit, and zflag 1 bit.
	 * output: the 2 bit string that will go into the 4x1 mux
	 */
	
	public static String branchSignals(String opcode, String jump, String branch, String Gflag, String Zflag) {
		
		String ret;
		
		if((branch.equals("1")&&opcode.equals("0111")&&Zflag.equals("0"))||(branch.equals("1")&&opcode.equals("1000")&&Gflag.equals("1"))) {
			ret = "01";
		}
		
		else if(jump.equals("1")&&opcode.equals("1001")) {
			ret = "11";
		}
		
		else {
			ret = "00";
		}
		
		return ret;
		
	}
}
