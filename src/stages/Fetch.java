package stages;

import components.Cache;
import components.PC;
import components.VonNeumannMemory;
import components.pipelineRegs.IF_ID;
import other.DatapathException;
import other.formatter;

public class Fetch {

    /**
     * loads the instruction from the program counter.
     * instruction is set to 0 if we are flushing the pipeline.
     */
    public static char Flush = '0';

    public static void run() throws DatapathException {
        //===============load the instruction from cache===============
            String instruction = VonNeumannMemory.fetchinstruction(PC.getPC());

        //===================increment PC (PC + 4)=====================
            incPC();

        //===============pass the outputs to the next stage============
            IF_ID.write(instruction,PC.getPC());

        //=======================flushing==============================
            /*
             *  we assume that the branch is not taken. if that's incorrect, flush
             *  the current instruction (the one in IF).
             * How? replace the instruction inside IF/ID with 0s,
             * making it a NOP (add $0,$0,$0) instruction.
             */
            if (Flush == '1')
                IF_ID.flushOutgoing();

        //==================print the required output==================
            printStage(instruction,PC.get32bitPC());
    }

    public static void incPC(){
        PC.setPC(PC.getPC() + 4);
    }

    private static void printStage(String instruction,String pc){
        if (!formatter.checknop(formatter.AssemblyStages[0])) {
            StringBuilder out = new StringBuilder();
            out.append("\t").append(formatter.AssemblyStages[0])
                    .append(" in Fetch stage: \n\n\t\t")
                    .append("Next PC: ").append(formatter.formatOut(pc))
                    .append("\n\t\t").append("Instruction: ")
                    .append(formatter.formatOut(instruction)).append("\n\n");

            System.out.print(out.toString());
        }
    }
}
