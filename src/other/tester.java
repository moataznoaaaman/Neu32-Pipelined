package other;

import components.RegisterFile;
import components.VonNeumannMemory;
import stages.*;

/**
 * used for testing the datapath. you may delete this when we are done
 */
public class tester {
    public static void main(String[] args) throws DatapathException {

        /*
         * loops 5 times, in each iteration 2 is added to the value of register 16.
         * The final value is then saved to memory location 1028.
         */

        String[] program1 = {
                                 "00011000000001000000000000000101", //addi 0001-00000-00001-000000000000000101
                                 "00010000001100000000000000000001", //addi 0001-00000-01100-000000000000000001
                                 "01100000000001000000010000000000", //sw 0110-00000-00001-000000010000000000
                                 "01010000000010000000010000000000", //lw  0101-00000-00010-000000010000000000
                                 //======loop until the condition is false=============
                                 "00011000010000000000000000000010", //addi 0001-10000-10000-000000000000000010
                                 "00000001001100000100000000000001", //sub 0000-00010-01100-00010-0000000000001
                                 "01110001000000111111111111111101", //bne 0111-00010-00000-111111111111111110
                                 //====================End of loop=====================
                                 //jump is flushed if the branch succeeded
                                 "10010000000000000000000000001001", //j 1001-0000000000000000000000000110
                                 //ori is always flushed (skipped)
                                 "00100110101110000000000000000011", //ori 0010-01101-01110-000000000000000011
                                 //jump destination
                                 "01100000010000000000010000000100", //sw 0110-00000-10000-000000010000000100
                                 };


        //load the program into memory
        VonNeumannMemory.addinstructions(program1);

        //execute the program
        for (int i = 0; i < 35; i++) {
            System.out.print("After clock-cycle: "+(i+1)+":\n\n");
            cycle();
        }
        System.out.println("===================================END OF PROGRAM=====================================");

        //debugging info
        System.out.println("register 2: " + RegisterFile.readdata(2));
        System.out.println("register 16: " + RegisterFile.readdata(16));
        System.out.println("memory location 1024: " + VonNeumannMemory.load("10000000000"));
        System.out.println("memory location 1028: " + VonNeumannMemory.load("10000000100"));

    }

    /**
     * simulates a clock cycle.
     * executes the stages sequentially
     * @throws DatapathException
     */
    public static void cycle() throws DatapathException {
        Fetch.run();
        Decode.run();
        Execute.run();
        Memory.mem();
        WriteBack.wb();

    }
}
