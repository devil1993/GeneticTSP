package code;

import DataStructures.Graph;

public class Launcher {
	public static void main(String args[]) {
		Graph g = new Graph();
	
		g.GenerateFromFile("./resources/TSPFile");
		
		g.displayGraph();
		
		GeneticTSP gtsp = new GeneticTSP(g);
		
		System.out.println("---------------Initial--------------------");
		gtsp.printPopulationInConsole();
		System.out.println(gtsp.averageFitness);
		
		System.out.println();
		
		double initialFitness, finalFitness = 0;
		int uselessCount = 0,generations = 0;
		do{
			initialFitness = gtsp.averageFitness;
			gtsp.applySelection();
			gtsp.applyCrossover();
			gtsp.applyMutation();
			gtsp.elitistModel();
			System.out.println("-------------------------------------------");
			gtsp.printPopulationInConsole();
			System.out.println(gtsp.averageFitness);
			finalFitness  = gtsp.averageFitness;
			if(Math.abs(initialFitness- finalFitness) < 0.9)
				uselessCount++;
			else if(uselessCount>0)
				uselessCount--;
			generations++;
		}while(uselessCount<=10);
		
		System.out.println("Stopped after "+generations+" generations");
	}
}
