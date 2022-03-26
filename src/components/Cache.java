package components;

import components.VonNeumannMemory;
import other.DatapathException;

public class Cache {

	//if you want to load word or store word instruction aka memory access
	//you will actually deal with the cash and ignore the vonneuman memory
	//as i took care of the hits and misses


	//cache size=128;
	private static String[][] cells=new String[3][128];//[!].[!]
	private static boolean isini=false;
	//[[vbit,tag,data]]

	private static void init()
	{
		if(isini==false)
			for(int i=0;i<128;i++)
			{
				cells[0][i]="0";
			}isini=true;

//		for (int i = 0; i < 128; i++)
//            for (int j = 0; j < 3; j++)
//                System.out.println("arr[" + j + "][" + i + "] = "+ cells[j][i]);
//
//		cells[0][0]="1";
//		cells[1][0]="0000000000000000000000000";
//		cells[2][0]="reach";
	}

	public static String load(String address) throws DatapathException {
		init();
		int index=Integer.parseInt(address.substring(0, 7),2);
		String tag=address.substring(7);
		//System.out.println(tag);
		if(cells[0][index].equals("1")&&cells[1][index].equals(tag))
		{
//			System.out.println("hit "+cells[2][index]);
			return cells[2][index];
		}
		else
		{
			//load from memory
			String temp= VonNeumannMemory.load(address);
			cells[0][index]="1";
			cells[1][index]=tag;
			cells[2][index]=temp;
//			System.out.print("miss "+temp);
			return temp;
		}

	}
	public static void store(String address,String data)
	{
		//write here then write to memory
		init();
		int index=Integer.parseInt(address.substring(0, 7),2);
		String tag=address.substring(7);
		cells[0][index]="1";
		cells[1][index]=tag;
		cells[2][index]=data;
		//System.out.println(tag);
		VonNeumannMemory.store(address, data);
	}
//	public static void main(String[]args)
//	{
//		//init();
//		store("00000000000000000000010000000100","10000000000000000000000000000000");
//		load("00000000000000000000010000000100");
//		VonNeumannMemory.load("00000000000000000000010000000100");
//		//System.out.print(Integer.parseInt("00000000000000000000010000000100",2));
//	}
}