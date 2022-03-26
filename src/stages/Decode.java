package stages;

import components.*;
import components.pipelineRegs.EX_MEM;
import components.pipelineRegs.ID_EX;
import components.pipelineRegs.IF_ID;
import components.pipelineRegs.MEM_WB;
import other.DatapathException;
import other.formatter;
import other.operations;

import java.util.HashMap;
import java.util.Hashtable;

public class Decode {

    public static void run() throws DatapathException {

        //=========================get all inputs from IF_ID============================
            HashMap<String, String> input = IF_ID.read();
            String instruction = input.get("Instruction");
            int incrementedPC = Integer.parseInt(input.get("PC"), 2);

        //===========================break the instruction down=========================
            String opCode = instruction.substring(0,4);

            String rs = instruction.substring(4,9);
            String rt = instruction.substring(9,14);
            String rd = instruction.substring(14,19);

            String immediate = instruction.substring(14);
            String target = instruction.substring(4);

            String funct = instruction.substring(19);

            HazardDetectionUnit.setFlags(rs, rt,opCode.equals("0111") || opCode.equals("1000"));
        //======================control signals are NOP initially=======================
            Hashtable<String, String> control = new Hashtable<>();
            control.put("Branch","0");
            control.put("Jump","0");
            control.put("DstReg","0");
            control.put("ALUSrc","0");
            control.put("ALUop","000");
            control.put("MemRead","0");
            control.put("MemWrite","0");
            control.put("RegWrite","0");
            control.put("MemToReg","0");

        //=====set the controls according to the hazard detection unit's NOP signal=====
            control = (Hashtable<String, String>) MUX.mux2in(MainControl.controlSignals(opCode),
                       control,HazardDetectionUnit.NOP - '0');

        //======================get the correct destination register====================
            rd = (String) MUX.mux2in(rt,rd,control.get("DstReg").charAt(0) - '0');

        //=========================read the source registers============================
            String[] values = {RegisterFile.readdata(rs), RegisterFile.readdata(rt)};

        //============================extend the immediate==============================
            immediate = Signextend.signeextend(immediate);

        //====================if shift amount > 32, set it to 32========================
            /*
             * previously when sll and srl were R-type instructions, this wasn't an issue,
             * because the shift amount was 5 bits. Now they're I-type and the immediate(18 bits) is
             * the shift amount. Shifting 2^18 times in unneeded and can cause performance complications
             * for the processor (in reality)
             */
            if ((opCode.equals("0011") || opCode.equals("0100")) && operations.Complement(immediate) > 32)
                immediate = "00000000000000000000000000100000";

        //==========================calculate branch address=============================
            //branch address is always calculated regardless of the instruction
            int branchAddress = incrementedPC + (operations.Complement(immediate) << 2);

        //==========================calculate jump address===============================
            /*
             * calculate jump address:
             * shift the 28 bit target left by 2 bits and get the
             * remaining 2 bits from the incremented PC's most left bits
             */
            String shiftedTarget = String.format("%30s", Integer.toBinaryString (Integer.parseInt(target,2) << 2))
                .replace(' ', '0');

            int jumpAddress = Integer.parseInt(input.get("PC").substring(0,3) + shiftedTarget,2);



        //======================Set the flags needed for branching=======================

            /*
             * Because the values in a branch comparison are needed during ID but may be produced later in time,
             * it is possible that a data hazard can occur and a stall will be needed. For example, if an ALU
             * instruction immediately preceding a branch produces one of the operands for the comparison in
             * the branch, a stall will be required, since the EX stage for the ALU instruction will occur after
             * the ID cycle of the branch. By extension, if a load is immediately followed by a conditional
             * branch that is on the load result, two stall cycles will be needed, as the result from the load
             * appears at the end of the MEM cycle but is needed at the beginning of ID for the branch.
             */

            //===Forwarding unit===
            ForwardingUnit.setFlags(rs,rt,EX_MEM.WB_Control().get("RegWrite"),MEM_WB.regWrite());
            //===========use the forwarding unit to get the operands===========
            //Data hazard: get the most recent values of rs,rt.

            String ForwardA = ForwardingUnit.ForwardA;
            String ForwardB = ForwardingUnit.ForwardB;

            String operand1 = (String) MUX.mux4in(values[0], EX_MEM.ALUResult(), MEM_WB.readData(),
                    values[0],ForwardA.charAt(0)+"",ForwardA.charAt(1)+"");

            String operand2 = (String) MUX.mux4in(values[1], EX_MEM.ALUResult(),MEM_WB.readData(),
                    values[1],ForwardB.charAt(0)+"",ForwardB.charAt(1)+"");

            //=======zero flag=======
            String ZFlag = Comparator.compare("=",operand1,operand2);

            //===greater than flag===
            String GFlag = Comparator.compare(">",operand1,operand1);

        //================================get branch signals==============================
            String branchSignals = BranchControl.branchSignals(opCode, control.get("Jump"),control
                    .get("Branch"),GFlag,ZFlag);

        //====================================decide branch===============================
            int newPC = (int) MUX.mux4in(incrementedPC,branchAddress,branchAddress,jumpAddress,
                    branchSignals.charAt(0)+"",branchSignals.charAt(1)+"");

            //if we are branching to the current PC address, don't take the branch
            if (newPC != incrementedPC){
                PC.setPC(newPC);

                //flush the pipeline
                Fetch.Flush = '1';
            }

            //no branching, set the flush signal to 0 for the next instruction
            else Fetch.Flush = '0';

        //==========================pass the outputs to the next stage===================
            /*
             * Any instruction may operate on a single source register, two source
             * registers, one source register and a sign-extended 32-bit immediate value, a sign-extended 32-bit
             * immediate value, or no operands. Additionally, we may need the destination register's number if
             * the instruction requires writing back to a register. We can either prepare only the required
             * operands for the EX stage based on the opcode or prepare all the operands that might be required
             * for any opcode. The latter design is simpler, so let's prepare all of them and store them at fixed
             * locations in the ID/EX register. It simplifies the design.
             */

            ID_EX.write(operand1, operand2, immediate,String.format("%32s", Integer.toBinaryString(branchAddress))
                    .replace(' ', '0'), rs, rt, rd, funct, control);

        //================================print the required output======================
            printStage(operand1,operand2,immediate,input.get("PC"),rt,rd,control);
    }

    public static void printStage(String read1,String read2, String SE, String pc,String rt
    , String rd, Hashtable<String,String> control){
        if (!formatter.checknop(formatter.AssemblyStages[1])) {
            StringBuilder out = new StringBuilder();
            out.append("\t").append(formatter.AssemblyStages[1])
                    .append(" in Decode stage: \n\n\t\t")
                    .append("read data 1: ").append(formatter.formatOut(read1))
                    .append("\n\t\t").append("read data 2: ").append(formatter.formatOut(read2))
                    .append("\n\t\t").append("sign-extend: ").append(formatter.formatOut(SE))
                    .append("\n\t\t").append("Next PC: ").append(formatter.formatOut(pc))
                    .append("\n\t\t").append("rt: ").append(rt)
                    .append("\n\t\t").append("rd: ").append(rd)
                    .append("\n\t\t").append("WB controls: ").append("MemToReg: ").append(control.get("MemToReg"))
                    .append(", ").append("RegWrite: ").append(control.get("RegWrite"))
                    .append("\n\t\t").append("MEM controls: ").append("MemRead: ").append(control.get("MemRead"))
                    .append(", ").append("MemWrite: ").append(control.get("MemWrite"))
                    .append(", ").append("Branch: ").append(control.get("Branch"))
                    .append("\n\t\t").append("EX controls: ").append("RegDest: ").append(control.get("DstReg"))
                    .append(", ").append("ALUOp: ").append(control.get("ALUop")).append(", ")
                    .append("ALUSrc: ").append(control.get("ALUSrc")).append("\n\n");
            System.out.print(out.toString());
        }
    }

}
