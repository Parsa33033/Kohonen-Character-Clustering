import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main 
{
  //field
	private final String fileName = "learningInput.txt";
	private Map<String, Integer[][]> learningChars;
	private int row , col;
	private double alpha = 0.6;
	private int radius = 0;
	private int n , m , inputQuantity;
	private double[][] weightMat;
	private List<String> listOfChars;
	private static final String TOPOLOGY = "diamond";//either square or diamond or linear
  //methods
	
	public Main()
	{
		numOfRowCol();
		learningChars = new TreeMap<String, Integer[][]>();
		listOfChars = new ArrayList<String>();
	}
	
	
	public static void main(String[] args)
	{
		Main main = new Main();
		main.extractCharacter();
		main.n = main.learningChars.size();
		main.m = main.n + 5;
		main.inputQuantity = main.col * main.row;
		main.weightMatrix();
		KohonenCluster kohonen = 
				new KohonenCluster(
						main.learningChars, main.weightMat, main.listOfChars,
						main.n, main.m, main.radius, main.alpha, main.inputQuantity, main.row, main.col, TOPOLOGY);
		
	}//main method
	
	
	public void extractCharacter()
	{
		try
		{
			FileInputStream fileInput = new FileInputStream(new File(Main.class.getResource(fileName).toURI()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));
			Integer[][] characterMap = new Integer[row][col];
			String str = "";
			int i = 0;
			int j = 0;
			while(reader.ready())
			{
				str = reader.readLine();
				if(!str.contains("//"))
				{
					for(int k = 0 ; k<str.length() ; k++)
					{
						if(str.charAt(k) == '#')
						{
							characterMap[i][j] = 1;
							j++;
						}//if
						else if(str.charAt(k) == '*')
						{
							characterMap[i][j] = 0;
							j++;
						}//else if
						if(j>=col)
						{
							j = 0;
						}
					}//for
					if(str.contains("#") || str.contains("*"))
					{
						i++;
					}
				}//if row of string contains //
				else
				{
					if(i>=row)
					{
						i = 0;
					}
					String s = str.replaceAll("[^A-Za-z0-9]", "");
					learningChars.put(s , characterMap);
					listOfChars.add(s);
					characterMap = new Integer[row][col];
				}//else
			}//while
		}//try
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}//extractCharacter method
	
	
	public void numOfRowCol()
	{
		try
		{
			FileInputStream fileInput = new FileInputStream(new File(Main.class.getResource(fileName).toURI()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));
			row = 0; 
			col = 0;
			String str ="";
			boolean flag = true;
			while(!(str = reader.readLine()).contains("//"))
			{
				
				if(str.contains("#") || str.contains("*"))
				{
					if(flag)
					{
						str = str.replaceAll("\\s+", "");
						col = str.length();
						flag = false;
					}
					row++;
				}
			}
			
		}//try
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}//numOfRowCol method
	
	
	
	public void weightMatrix()
	{
		weightMat = new double[m][inputQuantity];
		
		for(int i = 0 ; i<m ; i++)
		{
			for(int j = 0 ; j<inputQuantity ; j++)
			{
				weightMat[i][j] = new SecureRandom().nextDouble();
			}//for j
		}//for i
		
		System.out.println("Initial Weights are as below:( "+ n + " number of learning inputs and " + m + " number of clusters )");
		for(int i = 0 ; i<m ; i++)
		{
			System.out.print("W"+(i+1)+"==> \t");
			for(int j = 0 ; j<inputQuantity ; j++)
			{
				System.out.print(String.format("%.2f", weightMat[i][j])+"  ");
			}
			System.out.println();
		}
	}
}
