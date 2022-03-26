package components.pipelineRegs;

import java.util.HashMap;
import java.util.Hashtable;

public class ID_EX {
    /**
     * because we are not executing all the stages concurrently, stages will have to be executed
     * sequentially starting with IF. Completed stages will need a place to store their outputs
     * in without affecting the upcoming stages in the same cycle. A solution to this problem
     * is to store upcoming/outgoing values for the register. Each time we store something in
     * the register, the current incoming values will become the outgoing. Then we are going
     * to store the new values as incoming values.
     */
    private static HashMap<String,String> incoming;
    private static HashMap<String,String> outgoing;

    /**
     * to avoid copying values twice each time we write to the register (incoming -> outgoing, new values -> incoming),
     * we can make them alternate positions on every read. that way we only copy the values once (new values -> incoming).
     *
     * false = write to incoming, read from outgoing
     * true = write to outgoing, read from incoming
     */
    private static boolean reverse; //bit

    static {
        incoming = new HashMap<>();
        outgoing = new HashMap<>();

        incoming.put("rs", "00000");
        outgoing.put("rs", "00000");

        incoming.put("rt", "00000");
        outgoing.put("rt", "00000");

        incoming.put("rd", "00000");
        outgoing.put("rd", "00000");

        incoming.put("ReadData1", String.format("%032d", 0));
        outgoing.put("ReadData1", String.format("%032d", 0));

        incoming.put("ReadData2", String.format("%032d", 0));
        outgoing.put("ReadData2", String.format("%032d", 0));

        incoming.put("Immediate", String.format("%032d", 0));
        outgoing.put("Immediate", String.format("%032d", 0));

        incoming.put("BranchAddress", String.format("%032d", 0));
        outgoing.put("BranchAddress", String.format("%032d", 0));


        //===================EX_CONTROL=====================

        incoming.put("ALUop", "000");
        outgoing.put("ALUop", "000");

        incoming.put("ALUSrc", "0");
        outgoing.put("ALUSrc", "0");

        incoming.put("funct", "0000000000000");
        outgoing.put("funct", "0000000000000");

        //==================MEM_CONTROL======================

        incoming.put("MemRead", "0");
        outgoing.put("MemRead", "0");

        incoming.put("MemWrite", "0");
        outgoing.put("MemWrite", "0");

        incoming.put("Branch", "0");
        outgoing.put("Branch", "0");
        //===================WB_CONTROL======================

        incoming.put("MemToReg", "0");
        outgoing.put("MemToReg", "0");

        incoming.put("RegWrite", "0");
        outgoing.put("RegWrite", "0");

    }


    public static HashMap<String,String> EX_Control(){
        HashMap<String,String> ret = new HashMap<>();

        if (reverse){
            ret.put("ALUop",incoming.get("ALUop"));
            ret.put("ALUSrc",incoming.get("ALUSrc"));
        }
        else {
            ret.put("ALUop",outgoing.get("ALUop"));
            ret.put("ALUSrc",outgoing.get("ALUSrc"));
        }
        return ret;
    }


    public static HashMap<String,String> MEM_Control(){
        HashMap<String,String> ret = new HashMap<>();

        if (reverse){
            ret.put("MemRead",incoming.get("MemRead"));
            ret.put("MemWrite",incoming.get("MemWrite"));
            ret.put("Branch",incoming.get("Branch"));
        }
        else {
            ret.put("MemRead",outgoing.get("MemRead"));
            ret.put("MemWrite",outgoing.get("MemWrite"));
            ret.put("Branch",outgoing.get("Branch"));
        }
        return ret;
    }

    public static HashMap<String,String> WB_Control(){
        HashMap<String,String> ret = new HashMap<>();

        if (reverse){
            ret.put("MemToReg",incoming.get("MemToReg"));
            ret.put("RegWrite",incoming.get("RegWrite"));
        }
        else {
            ret.put("MemToReg",outgoing.get("MemToReg"));
            ret.put("RegWrite",outgoing.get("RegWrite"));
        }
        return ret;
    }

    public static String rs(){
        if (reverse) return incoming.get("rs");
        else return outgoing.get("rs");
    }

    public static String rt(){
        if (reverse) return incoming.get("rt");
        else return outgoing.get("rt");
    }

    public static String rd(){
        if (reverse) return incoming.get("rd");
        else return outgoing.get("rd");
    }

    public static String funct(){
        if (reverse) return incoming.get("funct");
        else return outgoing.get("funct");
    }

    public static String BranchAddress(){
        if (reverse) return incoming.get("BranchAddress");
        else return outgoing.get("BranchAddress");
    }

    //read = get the previous cycle's values + reverse (ONLY USED BY THE NEXT STAGE)
    public static HashMap<String, String> read(){

        if (reverse){
            reverse = false;
            return incoming;
        }

        reverse = true;
        return outgoing;
    }

    //write = store the output of ID in incoming (outgoing if the order is reversed)
    public static void write(String Readdata1, String ReadData2, String immediate,String BranchAddress,String rs,
            String rt, String rd,String funct,
                             Hashtable<String,String> control){
        if (reverse) {
            outgoing.put("ReadData1", Readdata1);
            outgoing.put("ReadData2", ReadData2);

            outgoing.put("Immediate", immediate);

            outgoing.put("BranchAddress", BranchAddress);

            outgoing.put("ALUop", control.get("ALUop"));
            outgoing.put("ALUSrc",  control.get("ALUSrc"));
            outgoing.put("funct",  funct);

            outgoing.put("MemRead", control.get("MemRead"));
            outgoing.put("MemWrite", control.get("MemWrite"));
            outgoing.put("Branch", control.get("Branch"));

            outgoing.put("MemToReg", control.get("MemToReg"));
            outgoing.put("RegWrite", control.get("RegWrite"));

            outgoing.put("rs", rs);
            outgoing.put("rt", rt);
            outgoing.put("rd", rd);
        }
        else{
            incoming.put("ReadData1", Readdata1);
            incoming.put("ReadData2", ReadData2);

            incoming.put("Immediate", immediate);

            incoming.put("BranchAddress", BranchAddress);

            incoming.put("ALUop", control.get("ALUop"));
            incoming.put("ALUSrc",  control.get("ALUSrc"));
            incoming.put("funct",  funct);

            incoming.put("MemRead", control.get("MemRead"));
            incoming.put("MemWrite", control.get("MemWrite"));
            incoming.put("Branch", control.get("Branch"));

            incoming.put("MemToReg", control.get("MemToReg"));
            incoming.put("RegWrite", control.get("RegWrite"));

            incoming.put("rs", rs);
            incoming.put("rt", rt);
            incoming.put("rd", rd);
        }
    }


}
