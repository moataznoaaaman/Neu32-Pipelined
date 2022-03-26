package stages;

import java.util.HashMap;

import components.MUX;
import components.RegisterFile;
import components.pipelineRegs.MEM_WB;
import other.DatapathException;
import other.formatter;

public class WriteBack {

	public static void wb() throws DatapathException
	{
		//getting the data from the pipeline register
		HashMap<String, String> input = MEM_WB.read();

		// WB controls
		String memToReg = input.get("MemToReg");
		String regWrite = input.get("RegWrite");

		// you can never be too sure
		if (!regWrite.equals("1") && !regWrite.equals("0"))
			throw new DatapathException("RegWrite value of: " + regWrite + " is invalid");
		if (!memToReg.equals("1") && !memToReg.equals("0") && !memToReg.equals("x"))
			throw new DatapathException("MemToReg value of: " + memToReg + " is invalid");

		//to data input line 1 of 2x1 MUX
		String readData = input.get("ReadData");
		//to data input line 0 of 2x1 MUX
		String ALUres = input.get("ALUResult");
		//to write register
		String rt = input.get("rt");

		//let's play a dangerous game
		String writeData = (String) MUX.mux2in(ALUres, readData, memToReg.equals("x") ? (int)(Math.random()*2) : Integer.parseInt(memToReg));

		//write the data chosen by the MUX to the register
		if(regWrite.equals("1"))
			RegisterFile.writedata(writeData, rt);

		print();
	}

	public static void print()
	{
		if(!formatter.checknop(formatter.AssemblyStages[4]))
			System.out.println("\t"+formatter.AssemblyStages[4] + " in WB stage\n");
	}
}