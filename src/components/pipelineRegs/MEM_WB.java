package components.pipelineRegs;

import java.util.HashMap;

public class MEM_WB {
	// TODO: PICKLE
	// lel asaf consistency is valued in this field more than spaghetti

	 /**
     * because we are not executing all the stages concurrently, stages will have to be executed
     * sequentially starting with IF. Completed stages will need a place to store their outputs
     * in without affecting the upcoming stages in the same cycle. A solution to this problem
     * is to store upcoming/outgoing values for the register. Each time we store something in
     * the register, the current incoming values will become the outgoing. Then we are going
     * to store the new values as incoming values.
     */
	private static HashMap<String, String> incoming;
	private static HashMap<String, String> outgoing;

	/**
	 * to avoid copying values twice each time we write to the register (incoming ->
	 * outgoing, new values -> incoming), we can make them alternate positions on
	 * every read. that way we only copy the values once (new values -> incoming).
	 *
	 * false = write to incoming, read from outgoing
	 * true = write to outgoing, read from incoming
	 */
	private static boolean reverse; // bit

	static {
		incoming = new HashMap<>();
		outgoing = new HashMap<>();

		incoming.put("ALUResult", String.format("%032d", 0));
		outgoing.put("ALUResult", String.format("%032d", 0));

		incoming.put("ReadData", String.format("%032d", 0));
		outgoing.put("ReadData", String.format("%032d", 0));
		
		incoming.put("rt", String.format("%032d", 0));
		outgoing.put("rt", String.format("%032d", 0));

		// ===================WB_CONTROL======================

		incoming.put("MemToReg", "0");
		outgoing.put("MemToReg", "0");

		incoming.put("RegWrite", "0");
		outgoing.put("RegWrite", "0");

	}
	
	public static String rd() 
	{
		if (reverse)
			return incoming.get("rt");
		else
			return outgoing.get("rt");
	}

	public static String readData() 
	{
		if (reverse)
			return incoming.get("ReadData");
		else
			return outgoing.get("ReadData");
	}

	public static String regWrite()
	{
		if (reverse)
			return incoming.get("RegWrite");
		else
			return outgoing.get("RegWrite");
	}

	// read = get the previous cycle's values + reverse (ONLY USED BY THE NEXT STAGE)
	public static HashMap<String, String> read() {

		if (reverse) {
			reverse = false;
			return incoming;
		}

		reverse = true;
		return outgoing;
	}
	
	
	//write = store the output of MEM in incoming (outgoing if the order is reversed)
    public static void write(String readData, String ALUres, String rt, HashMap<String, String> control){
        if (reverse) {
            outgoing.put("ALUResult", ALUres);
            outgoing.put("ReadData", readData);
            
            outgoing.put("rt", rt);

            outgoing.put("MemToReg", control.get("MemToReg"));
            outgoing.put("RegWrite", control.get("RegWrite"));
        }
        else{
            incoming.put("ALUResult", ALUres);
            incoming.put("ReadData", readData);

            incoming.put("rt", rt);
            
            incoming.put("MemToReg", control.get("MemToReg"));
            incoming.put("RegWrite", control.get("RegWrite"));
        }

    }

}
