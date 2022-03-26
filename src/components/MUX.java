package components;

public class MUX {

		public static Object mux2in(Object data0,Object data1,int signal)
	{
		if(signal==1)
		{
			return data1;
		}
		return data0;
	}
	
	public static Object mux4in(Object data00,Object data01,Object data10,Object data11,String signal1,String signal2)
	{
		String signals=signal1+""+signal2;
		switch (signals)
		{
		case "00":return data00;
		case "01":return data01;
		case "10":return data10;
		case "11":return data11;
		default:return null;
		}
		
		
	}
	
//	public static void main (String []args)
//	{
//		String[] myIntArray = new String[3];
//		System.out.print(myIntArray[1]);
//	}
}
