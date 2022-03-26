package components;

import components.pipelineRegs.EX_MEM;
import components.pipelineRegs.MEM_WB;

public class ForwardingUnit {
    public static String ForwardA = "00";
    public static String ForwardB = "00";

    public static void setFlags(String rs, String rt,String regWrite_EX_MEM,String regWrite_MEM_WB){
        if (regWrite_EX_MEM.equals("1") && !rs.equals("00000") && rs.equals(EX_MEM.rd())){
            ForwardA = "01";
        }
        else if (regWrite_MEM_WB.equals("1") && !rs.equals("00000") && rs.equals(MEM_WB.rd())){
            ForwardA = "10";
        }
        else {
            ForwardA = "00";
        }
        //==========================
        if (regWrite_EX_MEM.equals("1") && !rt.equals("00000") && rt.equals(EX_MEM.rd())){
            ForwardB = "01";
        }
        else if (regWrite_MEM_WB.equals("1") && !rt.equals("00000") && rt.equals(MEM_WB.rd())){
            ForwardB = "10";
        }
        else {
            ForwardB = "00";
        }

    }

    /*
     * Cases:
     *  - ForwardA = 00: First ALU operand comes from register file = Value of (rs)
     *  - ForwardA = 01: Forward result of previous instruction to A (from ALU stage)
     *  - ForwardA = 10: Forward result of 2nd previous instruction to A (from MEM stage)
     *  - ForwardA = 11: Don't care
     *
     *  - ForwardB = 00: First ALU operand comes from register file = Value of (rt)
     *  - ForwardB = 01: Forward result of previous instruction to A (from ALU stage)
     *  - ForwardB = 10: Forward result of 2nd previous instruction to A (from MEM stage)
     *  - ForwardB = 11: Don't care
     */

}
