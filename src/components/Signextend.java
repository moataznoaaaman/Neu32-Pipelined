package components;

public class Signextend {

	public static String signeextend(String x)
	{
		String f=x.substring(0, 1);
		for (int i=x.length();i<32;i++)
		{
			x=f+x;
		}
		
		return x;
	}
}
