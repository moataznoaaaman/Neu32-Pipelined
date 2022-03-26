package other;

import java.util.Arrays;

public class formatter {

    /**
     * array containing the assembly instruction
     * in every stage.
     */
    public static String[] AssemblyStages =nops(5);
    public static boolean stall = false;

    /**
     *
     * @param instruction the instruction that recently entered the pipeline.
     * Function: it advances all the instructions in AssemblyStages by 1 stage.
     */
    public static void advance(String instruction){
        if (!stall) {
            for (int i = AssemblyStages.length - 2; i >= 0; i--) {
                AssemblyStages[i + 1] = AssemblyStages[i];
            }
            String temp = getAssembly(instruction);
            AssemblyStages[0] = (checknop(temp) ? temp + " (NOP)" : temp);
        }
        else {
            for (int i = AssemblyStages.length - 2; i >= 2; i--) {
                AssemblyStages[i + 1] = AssemblyStages[i];
            }
            AssemblyStages[2] = "add $0, $0, $0 (NOP)";
            stall = false;
        }
    }

    /**
     *
     * @param instruction as a 32 bit binary string.
     * @return assembly instruction as a string.
     */

    public static String getAssembly(String instruction){
        String op = instruction.substring(0,4);

        String rs = instruction.substring(4,9);
        String rt = instruction.substring(9,14);
        String rd = instruction.substring(14,19);

        String immediate = instruction.substring(14);
        String target = instruction.substring(4);

        String funct = instruction.substring(19);

        StringBuilder assembly = new StringBuilder();
        int r1 = Integer.parseInt(rs,2);
        int r2 = Integer.parseInt(rt,2);
        int r3 = Integer.parseInt(rd,2);
        int immInt = operations.Complement(immediate);
        int tInt = Integer.parseInt(target,2);

        switch (op){
            case "0000":
                switch (funct) {
                    case "0000000000000": assembly.append("add ");break;
                    case "0000000000001": assembly.append("sub ");break;
                    case "0000000000010": assembly.append("mul ");break;
                    case "0000000000011": assembly.append("and ");break;
                    case "0000000000100": assembly.append("slt ");break;
                    default:break;
                }
                assembly.append("$").append(r3).append(", ").append("$").append(r1).
                        append(", ").append("$").append(r2);
                break;
            case "0001": assembly.append("addi ");break;
            case "0010": assembly.append("ori ");break;
            case "0011": assembly.append("sll ");break;
            case "0100": assembly.append("srl ");break;
            case "0101":
                assembly.append("lw ").append("$").append(r2).append(", ").append(immInt).
                        append("(").append("$").append(r1).append(")");
                break;
            case "0110":
                assembly.append("sw ").append("$").append(r2).append(", ").append(immInt).
                        append("(").append("$").append(r1).append(")");
                break;
            case "0111":
                assembly.append("bne ").append("$").append(r1).append(", ").append("$").
                        append(r2).append(", ").append(immInt);
                break;
            case "1000":
                assembly.append("bgt ").append("$").append(r1).append(", ").append("$").
                        append(r2).append(", ").append(immInt);
                break;
            case "1001": assembly.append("j ").append(tInt);break;
            default:break;
        }

        if (Integer.parseInt(op,2) < 5 && !op.equals("0000")){
            assembly.append("$").append(r2).append(", ").append("$").
                    append(r1).append(", ").append(immInt);
        }

        return assembly.toString();
    }

    /**
     *
     * @param in any string (most likely a 32 bit binary string)
     * @return the same string with spaces every 4 characters.
     * example:
     *  in: 10000101
     *  out: 1000 0101
     */

    public static String formatOut(String in){
        return in.replaceAll("....", "$0 ");
    }

    /**
     *
     * @param size array size
     * @return String[size] containing "add $0, $0, $0 (NOP)"
     *
     */
    public static String[] nops(int size){
        String[] ret  = new String[size];
        Arrays.fill(ret,"add $0, $0, $0 (NOP)");
        return ret;
    }

    /**
     *
     * @param assembly assembly instruction
     * @return assembly is a NOP (add $0, $0, $0) == true
     */
    public static boolean checknop(String assembly){

        return assembly.equals("add $0, $0, $0 (NOP)") || assembly.equals("add $0, $0, $0");
    }
}
