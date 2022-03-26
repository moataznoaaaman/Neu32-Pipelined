package components.pipelineRegs;

import java.util.HashMap;

public class EX_MEM {
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

        incoming.put("ALUResult", String.format("%032d", 0));
        outgoing.put("ALUResult", String.format("%032d", 0));

        incoming.put("ReadData2", String.format("%032d", 0));
        outgoing.put("ReadData2", String.format("%032d", 0));

        incoming.put("ZFlag", "1");
        outgoing.put("ZFlag", "1");


        incoming.put("rd", String.format("%032d", 0));
        outgoing.put("rd", String.format("%032d", 0));

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

    public static String ALUResult(){
        if (reverse) return incoming.get("ALUResult");
        else return outgoing.get("ALUResult");
    }

    public static String ZFlag(){
        if (reverse) return incoming.get("ZFlag");
        else return outgoing.get("ZFlag");
    }

    public static String rd(){
        if (reverse) return incoming.get("rd");
        else return outgoing.get("rd");
    }

    public static String ReadData2(){
        if (reverse) return incoming.get("ReadData2");
        else return outgoing.get("ReadData2");
    }

    //read = get the previous cycle's values + reverse (ONLY USED BY THE NEXT STAGE)
    //TODO: FOR KHEYAR: USE read() AT THE START OF THE MEMORY STAGE
    public static HashMap<String, String> read(){

        if (reverse){
            reverse = false;
            return incoming;
        }

        reverse = true;
        return outgoing;
    }

    //write = store the output of EX in incoming (outgoing if the order is reversed)
    public static void write(String ALUResult,String ReadData2, String ZFlag, String rd, HashMap<String, String> control){
        if (reverse) {
            outgoing.put("ALUResult", ALUResult);
            outgoing.put("rd", rd);
            outgoing.put("ReadData2", ReadData2);
            outgoing.put("ZFlag", ZFlag);

            outgoing.put("MemRead", control.get("MemRead"));
            outgoing.put("MemWrite", control.get("MemWrite"));
            outgoing.put("Branch", control.get("Branch"));

            outgoing.put("MemToReg", control.get("MemToReg"));
            outgoing.put("RegWrite", control.get("RegWrite"));
        }
        else{
            incoming.put("ALUResult", ALUResult);
            incoming.put("rd", rd);
            incoming.put("ReadData2", ReadData2);
            incoming.put("ZFlag", ZFlag);

            incoming.put("MemRead", control.get("MemRead"));
            incoming.put("MemWrite", control.get("MemWrite"));
            incoming.put("Branch", control.get("Branch"));

            incoming.put("MemToReg", control.get("MemToReg"));
            incoming.put("RegWrite", control.get("RegWrite"));
        }

    }
}
