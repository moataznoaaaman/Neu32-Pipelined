package stages;

import java.util.HashMap;

import components.Cache;
import components.pipelineRegs.EX_MEM;
import components.pipelineRegs.MEM_WB;
import other.DatapathException;
import other.formatter;

public class Memory {

	//TODO: what should the mem access stage do?
	//Read + Write and set the pc
	//MR, MW, ALUres, readData2
	public static void mem() throws DatapathException
	{
		//getting the data from the pipeline register
		HashMap<String, String> input = EX_MEM.read();

		//MEM controls
		int memRead = Integer.parseInt(input.get("MemRead"));
		int memWrite = Integer.parseInt(input.get("MemWrite"));

		//directly goes to the MEM/WB register
		String rt = input.get("rd");

		//goes to address
		String ALUres = input.get("ALUResult");
		//goes to write data
		String readData2 = input.get("ReadData2");


		//we're only focusing on lw and sw
		//which can only set either MemRead and MemWrite for lw and sw respectively
		//but never both
		if(memRead==1 && memWrite==1)
			throw new DatapathException("MemRead and MemWrite signals are both set.");
			//reads data in the calculated address in ALUres
		else if(memRead==1 && memWrite==0)
		{
			String readData = Cache.load(ALUres);
			MEM_WB.write(readData, ALUres, rt, input);
			print(ALUres, readData, input.get("rd"), input.get("MemToReg"), input.get("RegWrite"));
		}
		//saves read data 2 in the address calculated in the ALUres
		else if(memWrite==1 && memRead==0)
		{
			Cache.store(ALUres, readData2);
			//the strings we deal with are binary strings
			//so it can never be coincidentally that the data read is "don't care"
			String readData = "00000000000000000000000000000000";
			MEM_WB.write(readData, ALUres, rt, input);
			print(ALUres, readData, input.get("rd"), input.get("MemToReg"), input.get("RegWrite"));
		}
		//do nothing
		else if(memRead==0 && memWrite==0)
		{
			String readData = ALUres;
			MEM_WB.write(readData, ALUres, rt, input);
			print(ALUres, readData, input.get("rd"), input.get("MemToReg"), input.get("RegWrite"));
		}
		else
			throw new DatapathException("Invalid signal value for MemRead and/or MemWrite." + '\n'
					+"MemRead: " + memRead + '\n' + "MemWrite: " + memWrite);


	}

	public static void print(String ALUres, String readData, String rt, String MemToReg, String RegWrite)
	{
		if(!formatter.checknop(formatter.AssemblyStages[3]))
			System.out.println("\t"+formatter.AssemblyStages[3] + " in Memory stage:" + "\n\n\t\t" +
					"ALU result: " + formatter.formatOut(ALUres) + "\n\t\t" +
					"memory word read: " + (readData.equals("don't care") ? readData : formatter.formatOut(readData))
					+ "\n\t\t" + "rt/rd field: " + rt + "\n\t\t" + "WB controls: MemToReg: " +
					MemToReg + ", RegWrite: " + RegWrite+"\n");
	}
}