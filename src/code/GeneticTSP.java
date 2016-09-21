package code;

import java.util.Random;

import DataStructures.Graph;

public class GeneticTSP {
	private final int population_size = 30, FMAXER = 150;
	private final double mutationProbability = 0.005;
	private Graph g;
	private int[][] adjacencyMatrix;
	int population[][];
	int fitness[];
	double averageFitness;
	int fittest[];
	public GeneticTSP(Graph g) {
		this.g = g;
		adjacencyMatrix = g.getAdjacencyMatrix();
		population = new int[population_size][g.vertices];
		fitness = new int[population_size];
		generatePopulation();
		calculateFitness();
	}
	
	private void calculateFittest(){
		int maxFitness = 0, elitistIndex = 0,i=0;
		for(int fitnessValue : fitness){			
			if (fitnessValue>maxFitness){
				maxFitness = fitnessValue;
				elitistIndex = i;
			}
			i++;
		}
		fittest = population[elitistIndex];
	}
	
	public void elitistModel(){
		Random r = new Random();
		int randomIndex = r.nextInt(population_size);
		population[randomIndex] = fittest;
		calculateFitness();
		calculateFittest();
	}
	
	public void applyElitistCrossover(){
		applyCrossover();
		elitistModel();
	}

	private void calculateFitness() {
		averageFitness = 0.0;
		for (int i = 0; i < fitness.length; i++) {
			int totalCost = 0;
			int n;
			for(n = 0; n< population[i].length-1 ; n++){
				totalCost+=adjacencyMatrix[population[i][n]][population[i][n+1]];
			}
			totalCost += adjacencyMatrix[population[i][n]][population[i][0]];
			fitness[i] = FMAXER - totalCost;
			averageFitness += fitness[i];
		}
		averageFitness = (double)averageFitness/population_size;
	}

	private void generatePopulation() {				
		boolean flag[] = new boolean[g.vertices];
		for(int j = 0; j<population_size ; j++){
			int count = 0;
			for (int i = 0; i < g.vertices; i++) {
				population[j][i] = -1;
				flag[i] = false;
			}
			while (count < g.vertices) {
				Random r = new Random();
				int x = r.nextInt(g.vertices);
				if (!flag[x]) {
					population[j][count] = x;
					flag[x] = true;
					count++;
				}
			}
		}
	}
	
	public void printPopulationInConsole(){
		int j=0;
		for(int path[]: population){
			for (int i : path) {
				System.out.print((i+1)+"->");
			}
			System.out.println(path[0]+ " Fitness: " + fitness[j]);
			j++;
		}
	}
	
	public void applySelection(){
		calculateFittest();
		int newPopulation[][] = new int[population_size][g.vertices];
		int total=0,i,ends[] = new int[population_size];
		for(i=0;i<population_size;i++){
			total+=fitness[i];
			ends[i] = total;		
		}
		for(i=0;i<population_size;i++){
			int selector = (new Random()).nextInt(total);
			int j;
			for(j=0;j<population_size;j++){
				if(selector<ends[j])
					break;
			}
			newPopulation[i] = population[j];
		}
		for(i=0; i<population_size; i++){
			population[i] = newPopulation[i];
		}
		calculateFitness();
	}
	
	public void applyCrossover(){
		int newPopulation[][] = new int[population_size][g.vertices];
		/*
		Creating a flag array to indicate which chromosomes of parent population
		are already used in crossover. We dont select those for crossover again.
		*/
		boolean flag[] = new boolean[population_size];
		//Inintializing the flags.
		for (int i = 0; i < population_size; ++i)
		{
			flag[i] = true;
		}

		//Creating a seed to choose chromosomes from parent population.
		Random r = new Random();
		int seed = r.nextInt(population_size/2);
		if(seed==0)
			seed++;
		
		int j = 0;
		//i indicates the crossovers. So the # of crossovers will be half the population size
		for (int i = 0; i < population_size/2; ++i)
		{			
			//Not to select the used chromosomes.
			while(!flag[j])
				j = (j+1)%population_size;
			//Select 2 chromosomes and set corresponding flags.			
			int l = (j+seed)%population_size;
			while(!flag[l])
				l++;
			flag[j] = false;
			flag[l] = false;
			int chromosome1[] = population[j],chromosome2[] = population[l];

			//Select the crossover points.
			int start, end;
			start = r.nextInt(g.vertices);
			end = r.nextInt(g.vertices);
			if(start==end){
				//System.out.println("Came here");
				end = r.nextInt(g.vertices);
			}
			if(start>end){
				start = start + end;
				end = start - end;
				start = start - end;
			}	
			
			//System.out.println(start+"<--->"+ end);

			//printf("Start: %d End: %d\n",start,end);
			//Allocate the child spaces.
			int child1[] = new int[g.vertices];
			int child2[] = new int[g.vertices];
			
			//Free old population
			//efreeAll(newPopulation, vertices);

			//int *child1 = newPopulation[2*i];
			//int *child2 = newPopulation[2*i+1];

			//Set the genes within crossover points
			for (int k = start; k <= end; k++)
			{
				child1[k] = chromosome1[k];
				child2[k] = chromosome2[k];
			}

			//Set the genes outside crossover points.
			int place1 = 0,place2=0;

			for (int k = 0; k < g.vertices; ++k)
			{		
				//Ignore the space within crossover points
				if(place1 == start)
					place1 = end +1;
				if(place2 == start)
					place2 = end +1;

				//printf("%d %d %d\n", k, place1 , place2);

				//Check if gene already present from the other parent
				boolean isPlaced1 = false;
				for (l = start; l <= end; ++l)
				{
					if(chromosome2[k]==child1[l]){
						isPlaced1=true;
						break;
					}
				}
				boolean isPlaced2 = false;
				for (l = start; l <= end; ++l)
				{
					if(chromosome1[k]==child2[l]){
						isPlaced2=true;
						break;
					}
				}

				//If not placed, place the chromosome there.
				if(!isPlaced1){
					child1[place1] = chromosome2[k];
					place1++;
				}
				if(!isPlaced2){
					child2[place2] = chromosome1[k];
					place2++;
				}
			}

			//Place the children in new population.
			newPopulation[2*i] = child1;
			newPopulation[2*i+1] = child2;
		}
		for(int i = 0; i<population_size;i++)
			population[i] = newPopulation[i];
		calculateFitness();
	}
	
	public void applyMutation(){
		Random r = new Random();
		
		for(int chromosome[] : population){
			double mutate = r.nextInt(10000)/(double)10000;
			
			if(mutate<mutationProbability){
				int node1 = r.nextInt(g.vertices);
				int node2 = r.nextInt(g.vertices);
				
				int temp = chromosome[node1];
				chromosome[node1] = chromosome[node2];
				chromosome[node2] = temp;
				
				System.out.println("Mutated!!");
			}
		}
				
		calculateFitness();
	}
}
