import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class KohonenCluster 
{
  //field
	private Map<String, Integer[][]> learningChars;
	private int n, m, radius, inputQuantity, row, col;
	private double alpha;
	private double[][] weightMat;
	private List<String> listOfChars;
	private String topology;
	private int R = 3;
	private int iterations = 1000;
  //methods
	
	public KohonenCluster(Map<String, Integer[][]> learningChars, double[][] weightMat, List<String> listOfChars,
			int n, int m, int radius, double alpha, int inputQuantity, int row, int col, String topology)
	{
		this.learningChars = learningChars;
		this.n = n;
		this.m = m;
		this.radius = radius;
		this.alpha = alpha;
		this.inputQuantity = inputQuantity;
		this.weightMat = weightMat;
		this.listOfChars = listOfChars;
		this.row = row;
		this.col = col;
		this.topology = topology;
		test();
	}//constructor
	
	
	public void test()
	{
		learning();
		Iterator iter = listOfChars.iterator();
		int[] input = new int[inputQuantity];
		Map<String, String> clustersFilled = new TreeMap<String, String>();
		while(iter.hasNext())
		{
			String next =(String) iter.next();
			input = extractInput(learningChars.get(next));
			int indexOfShortestDist = findShortestDistance(input);
			if(clustersFilled.containsKey(String.valueOf(indexOfShortestDist)))
			{
				clustersFilled.put(String.valueOf(indexOfShortestDist), clustersFilled.get(String.valueOf(indexOfShortestDist))+" "+next);
			}
			else
			{
				clustersFilled.put(String.valueOf(indexOfShortestDist), next);
			}
		}
		System.out.println();
		System.out.println("the Topology is "+topology );
		
		Iterator keySets = clustersFilled.keySet().iterator();
		while(keySets.hasNext())
		{
			System.out.println();
			String s =(String) keySets.next();
			System.out.print(s +"==> "+clustersFilled.get(s));
		}
		
	}//test
	
	
	public void learning()
	{
		int x = R;
		double a = alpha;
		for(int k = 0 ; k<iterations ; k++)
		{
			Iterator iter = listOfChars.iterator();
			int[] input = new int[inputQuantity];
			while(iter.hasNext())
			{
				String next =(String) iter.next();
				input = extractInput(learningChars.get(next));
				int indexOfShortestDist = findShortestDistance(input);
				for(int i = 0 ; i<inputQuantity ; i++)
				{ 
					topologyUpdateWeight(indexOfShortestDist, i, input);
				}//for
			}//while
			alpha = 0.5 * alpha;
		}//for 100 times
		
		System.out.println();
		System.out.println("The Updated weights are:");
		for(int i = 0 ; i<m ; i++)
		{
			System.out.print("W"+i + "==>\t");
			for(int j = 0 ; j<inputQuantity ; j++)
			{
				System.out.print(String.format("%.2f", weightMat[i][j]) + "  ");
			}//for
			System.out.println();
		}//for
		
		System.out.println();
		
		System.out.println("alpha before learning: " + a);
		System.out.println("alpha after learning: " + alpha);
		System.out.println("number of iterations is: " + iterations);
		System.out.println("R is: " + x);
	}//learning method
	
	
	public void topologyUpdateWeight(int indexOfShortestDist, int i, int[]input)
	{
		if(topology.equals("linear"))
		{
			weightMat[indexOfShortestDist][i] =
					weightMat[indexOfShortestDist][i] + 
					((double)alpha)*(input[i] - weightMat[indexOfShortestDist][i]);
			for(int v = 1 ; v <=R ; v++)
			{
				try{weightMat[indexOfShortestDist+R][i] = 
						weightMat[indexOfShortestDist+R][i] + 
						((double)alpha)*(input[i] - weightMat[indexOfShortestDist+R][i]);}catch(Exception e) {}
				try{weightMat[indexOfShortestDist-R][i] = 
						weightMat[indexOfShortestDist-R][i] + 
						((double)alpha)*(input[i] - weightMat[indexOfShortestDist-R][i]);}catch(Exception e) {}				
			}
		}//linear
		else if(topology.equals("square"))
		{
			for(int v = 0 ; v <=R ; v++)
			{
				try{weightMat[indexOfShortestDist+(R*v)][i] = 
						weightMat[indexOfShortestDist+(R*v)][i] + 
						((double)alpha)*(input[i] - weightMat[indexOfShortestDist+(R*v)][i]);}catch(Exception e) {}
				for(int w = 1 ; w<=R ; w++)
				{
					try{weightMat[indexOfShortestDist+(R*v)+w][i] = 
							weightMat[indexOfShortestDist+(R*v)+w][i] + 
							((double)alpha)*(input[i] - weightMat[indexOfShortestDist+(R*v)+w][i]);}catch(Exception e) {}
					try{weightMat[indexOfShortestDist-(R*v)-w][i] = 
							weightMat[indexOfShortestDist-(R*v)-w][i] + 
							((double)alpha)*(input[i] - weightMat[indexOfShortestDist-(R*v)-w][i]);}catch(Exception e) {}
				}//for
			}
		}//square
		else if(topology.equals("diamond"))
		{
			for(int v = 0 ; v <=R ; v++)
			{
				try{weightMat[indexOfShortestDist+(R*v)][i] = 
						weightMat[indexOfShortestDist+(R*v)][i] + 
						((double)alpha)*(input[i] - weightMat[indexOfShortestDist+(R*v)][i]);}catch(Exception e) {}
				for(int w = R ; w<=1 ; w++)
				{
					try{weightMat[indexOfShortestDist+(R*v)+w][i] = 
							weightMat[indexOfShortestDist+(R*v)+w][i] + 
							((double)alpha)*(input[i] - weightMat[indexOfShortestDist+(R*v)+w][i]);}catch(Exception e) {}
					try{weightMat[indexOfShortestDist-(R*v)-w][i] = 
							weightMat[indexOfShortestDist-(R*v)-w][i] + 
							((double)alpha)*(input[i] - weightMat[indexOfShortestDist-(R*v)-w][i]);}catch(Exception e) {}
				}//for
			}
		}//diamond
		if(R>0)
		{
			R--;
		}
	}//method topologyUpdateWeight
	
	
	public int findShortestDistance(int[] input)
	{
		List<Double> minDist = new ArrayList<>();
		double  temp = 0;
		for(int i = 0 ; i<m ; i++)
		{
			for(int j = 0 ; j<inputQuantity ; j++)
			{
				temp += Math.pow(input[j]-weightMat[i][j], 2);
			}//for
			minDist.add(temp);
			temp = 0;
		}//for i from number of weight clusters
		return minDist.indexOf(Collections.min(minDist));
	}//findShortestDistance method
	
	
	public int[] extractInput(Integer[][] charMat)
	{
		int[] input = new int[inputQuantity];
		int k = 0;
		for(int i = 0 ; i<row ; i++)
		{
			for(int j = 0 ; j<col ; j++)
			{
				input[k] = charMat[i][j];
				k++;
			}//for j
		}//for i
		return input;
	}//extractinput method
	
}//class
