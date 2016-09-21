package DataStructures;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
	public int vertices,edges;
	public ArrayList<Edge> edgeList;
	
	public Graph() {
		edgeList = new ArrayList<Edge>();
	}
	
	public int[][] getAdjacencyMatrix(){
		int [][] matrix = new int[vertices][vertices];
		
		for (Edge e : edgeList) {
			matrix[e.start-1][e.end-1] = e.cost;
			matrix[e.end-1][e.start-1] = e.cost;
		}
		
		return matrix;
	}
	
	public void GenerateFromFile(String fileName){
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		vertices = scanner.nextInt();
		edges = scanner.nextInt();
		
		scanner.nextLine();
		
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			
			String values[] = line.split(" ");
			
			int start = Integer.parseInt(values[0]);
			int end = Integer.parseInt(values[1]);
			int cost = Integer.parseInt(values[2]);
			
			edgeList.add(new Edge(start, end, cost));
		}
	}
	
	public void displayGraph(){
		int a[][] = this.getAdjacencyMatrix();
		
		for(int x[]: a){
			for(int y : x){
				System.out.print(y+ "\t");
			}
			System.out.println();
		}
	}
}
