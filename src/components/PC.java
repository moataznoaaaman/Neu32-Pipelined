package components;

public class PC {

	private static int PC=0;
	
	public static int getPC()
	{
		return PC;
	}
	
	public static String get32bitPC()
	{//this method returns this pc as a 32 bit binary string representing the value of the pc
		String res=Integer.toBinaryString(PC);
		for (int i=res.length();i<32;i++)
		{
			res=0+res;
		}
		return res;
		
	}
	
	public static void setPC(int pc)
	{
			PC=pc;
	}
	
	public static void set32bitpc(String pc) {
			//the input is a32 bit input representing pc in binary
			PC = Integer.parseInt(pc, 2);
	}
	
}
