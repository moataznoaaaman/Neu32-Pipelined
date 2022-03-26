package components;

import other.DatapathException;

import java.util.Hashtable;

public class MainControl {
	
	/*
	 * to use:
	 * 1. components.MainControl.controlSignals(opcode)
	 * input: pass opcode (0-3) as a string field
	 * output: hashtable of the following format:
	 * 
	 */
	public static Hashtable<String, String> controlSignals(String opcode) throws DatapathException {
		
		if(opcode.length()!=4) {
			throw new DatapathException("Opcode field's length is invalid!");
		}
		
		Hashtable<String,String> ret = new Hashtable<String,String>();
		
		switch(opcode) {
		    //r-type
			case "0000": 
				ret.put("Branch","0");
				ret.put("Jump","0");
				ret.put("DstReg","1");
				ret.put("ALUSrc","0");
				ret.put("ALUop","001");
				ret.put("MemRead","0");
				ret.put("MemWrite","0");
				ret.put("RegWrite","1");
				ret.put("MemToReg","0");
				break;
			
			//addi
			case "0001": 
				ret.put("Branch","0");
				ret.put("Jump","0");
				ret.put("DstReg","0");
				ret.put("ALUSrc","1");
				ret.put("ALUop","000");
				ret.put("MemRead","0");
				ret.put("MemWrite","0");
				ret.put("RegWrite","1");
				ret.put("MemToReg","0");
				break;
			
			//ori
			case "0010": 
				ret.put("Branch","0");
				ret.put("Jump","0");
				ret.put("DstReg","0");
				ret.put("ALUSrc","1");
				ret.put("ALUop","010");
				ret.put("MemRead","0");
				ret.put("MemWrite","0");
				ret.put("RegWrite","1");
				ret.put("MemToReg","0");
				break;
				
			//sll
			case "0011": 
				ret.put("Branch","0");
				ret.put("Jump","0");
				ret.put("DstReg","0");
				ret.put("ALUSrc","1");
				ret.put("ALUop","011");
				ret.put("MemRead","0");
				ret.put("MemWrite","0");
				ret.put("RegWrite","1");
				ret.put("MemToReg","0");
				break;
			
			//srl
			case "0100": 
				ret.put("Branch","0");
				ret.put("Jump","0");
				ret.put("DstReg","0");
				ret.put("ALUSrc","1");
				ret.put("ALUop","100");
				ret.put("MemRead","0");
				ret.put("MemWrite","0");
				ret.put("RegWrite","1");
				ret.put("MemToReg","0");
				break;
				
			//bne
			case "0111": case "1000": 
				ret.put("Branch","1");
				ret.put("Jump","0");
				ret.put("DstReg","x");
				ret.put("ALUSrc","0");
				ret.put("ALUop","101");
				ret.put("MemRead","0");
				ret.put("MemWrite","0");
				ret.put("RegWrite","0");
				ret.put("MemToReg","x");
				break;
				
			//lw
			case "0101": 
				ret.put("Branch","0");
				ret.put("Jump","0");
				ret.put("DstReg","0");
				ret.put("ALUSrc","1");
				ret.put("ALUop","000");
				ret.put("MemRead","1");
				ret.put("MemWrite","0");
				ret.put("RegWrite","1");
				ret.put("MemToReg","1");
				break;
				
			//sw
			case "0110": 
				ret.put("Branch","0");
				ret.put("Jump","0");
				ret.put("DstReg","x");
				ret.put("ALUSrc","1");
				ret.put("ALUop","000");
				ret.put("MemRead","0");
				ret.put("MemWrite","1");
				ret.put("RegWrite","0");
				ret.put("MemToReg","x");
				break;
				
			//jump
				
			case "1001": 
				ret.put("Branch","0");
				ret.put("Jump","1");
				ret.put("DstReg","x");
				ret.put("ALUSrc","x");
				ret.put("ALUop","x");
				ret.put("MemRead","0");
				ret.put("MemWrite","0");
				ret.put("RegWrite","0");
				ret.put("MemToReg","x");
				break;
			default: throw new DatapathException("Invalid opcode entered @components.MainControl");
		}
		
		return ret;
		
		
		
	}
	
}
