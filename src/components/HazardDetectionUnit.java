package components;

import components.pipelineRegs.EX_MEM;
import components.pipelineRegs.ID_EX;
import components.pipelineRegs.IF_ID;

public class HazardDetectionUnit {
    public static char NOP = '0';
    public static void  setFlags(String rs,String rt, boolean branch){
        if ((ID_EX.MEM_Control().get("MemRead").equals("1") && !ID_EX.rd().equals("00000") &&
           (rs.equals(ID_EX.rd()) || rt.equals(ID_EX.rd())))
        ||
           (branch && EX_MEM.MEM_Control().get("MemRead").equals("1") && !EX_MEM.rd().equals("00000") &&
           (rs.equals(EX_MEM.rd()) || rt.equals(EX_MEM.rd())))
        ||
           (branch && !ID_EX.rd().equals("00000") && (rs.equals(ID_EX.rd()) || rt.equals(ID_EX.rd()))))
        {
            PC.setPC(PC.getPC()-4);
            NOP = '1';
            IF_ID.stall();
        }
        else {
            NOP = '0';
        }
    }
}
