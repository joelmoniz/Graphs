package com.joel.graphs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Spanning_Circuit_Component {

	static int[][] adjList = null;// Adjacency List
	static int[][] pAdjList = null;// Processed Adjacency List
	static int nodes = 0; // No. of nodes
	static int edges = 0; // No. of edges
	static boolean circuit = false;// boolean which stores whether graph has a
									// circuit
	static boolean spanTree = false;// boolean which stores whether graph is a
									// spanning tree
	static int componentCount;// integer counting number of connected components

	static void getAdjList(String fn) // Function to store adjacency
										// list in adjList array from text file
	{
		String storeLine = "";
		int i = 0;
		int j = 0;
		StringTokenizer st = null;
		Scanner sc;
		String num;
		try {
			sc = new Scanner(new FileInputStream(fn));// Reading in the file
			if (!sc.hasNext())
				return;
			adjList = new int[100][100];
			while (sc.hasNext()) // Outer loop to store integers into adjList
			{
				i = 0;
				storeLine = sc.nextLine();
				st = new StringTokenizer(storeLine);
				while (st.hasMoreTokens()) // Inner loop to store integers into
											// adjList
				{
					num = st.nextToken(",");
					num = num.trim();// To remove white spaces before and after
										// an integer
					if (num.equalsIgnoreCase("null"))// If it is null, exit the
														// loop
						break;
					if (Integer.parseInt(num) <= 0) // If number entered is
													// negative or zero,
													// print "Error!!!" in the
													// file and terminate
													// program
					{
						System.out.println("Error!!!");
						System.exit(0);
					}
					adjList[nodes][i] = Integer.parseInt(num);
					i++;
					j++;

				}// End inner loop
				adjList[nodes][i] = -1; // -1 has been used as delimiter
				nodes++;
			}// End outer loop
			if (j % 2 != 0) {
				System.out.println("Invalid input");
				System.exit(0);
			}
			edges = j / 2;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}// End getAdjList()

	static void removeElement(int e) {
		for (int j = 0; pAdjList[e][j] != -1; j++) {
			int k = 0;
			while (pAdjList[pAdjList[e][j] - 1][k] != -1) { // Outer loop to
				// remove an element
				// from the lists of those elements that the vertex has in
				// its list
				if (pAdjList[pAdjList[e][j] - 1][k] == e + 1) {// If the
					// list of the element contained
					int l = k;
					while (pAdjList[pAdjList[e][j] - 1][l] != -1) {// Inner
						// loop to remove an element
						// from the lists of those elements that the vertex
						// has in its list by shifting all elements after
						// that element to the left
						pAdjList[pAdjList[e][j] - 1][l] = pAdjList[pAdjList[e][j] - 1][l + 1];
						l++;
					} // End of inner loop
				}// End of if
				k++;
			}// End of outer loop
		}
	}

	// Following class is used to implement depth first traversal
	static class DFSStack {
		static ArrayList<Integer> i = new ArrayList<Integer>();

		static void push(int j) // push an integer element into the stack
		{
			i.add(new Integer(j));
		}

		static int pop() // pop an integer element from the stack
		{
			int j = i.get(i.size() - 1);
			i.remove(i.size() - 1);
			return j;
		}

		static boolean hasNext() // check if there are any more integer elements
									// in the stack
		{
			return !i.isEmpty();
		}

		static void clearAll() {
			i.clear();
		}
	}

	// End of class to implement stack

	static boolean hasCircuit() // Function to check whether the given graph has
								// a circuit
	{
		pAdjList = new int[nodes][nodes];
		for (int i = 0; i < nodes; i++)// Copies adjList into pAdjList
		{
			int j;
			for (j = 0; adjList[i][j] != -1; j++)
				pAdjList[i][j] = adjList[i][j];
			pAdjList[i][j] = -1;
		}
		boolean[] visited = new boolean[nodes];// initialized with false by
												// default
		DFSStack.push(1);// push 1 (first element) into the stack
		int pres = 0;
		int j;

		while (DFSStack.hasNext()) // Loop to visit one element present in the
							// stack at a
		// time, to add the neighboring elements of the present vertex into the
		// stack, and
		// to see if a vertex is visited twice
		{
			j = 0;
			pres = DFSStack.pop();
			if (visited[pres - 1]) // If present vertex has been visited, a
									// circuit exists
			{
				circuit = true;
				DFSStack.clearAll();
				return true;
			}
			visited[pres - 1] = true;
			removeElement(pres - 1);// Function is called to modify pAdjList.
			// In pAdjList, if the present element contains b as
			// part of its adjacency list,
			// then this element is
			// removed from b's list, so that when b is called, this element
			// isn't
			// revisited and a circuit isn't marked. Further, when this element
			// is marked as visited, if it is encountered again, that means a
			// circuit is present. Thus, a vertex has to simply be visited twice
			// in
			// this processed list when using depth-first traversal to confirm a
			// circuit.

			while (pAdjList[pres - 1][j] != -1) // Push all elements in pAdjList
												// into the stack
			{
				DFSStack.push(pAdjList[pres - 1][j]);
				j++;
			}
			if (!DFSStack.hasNext()) // To check whether all elements have been
							// traversed or
			// not, in case it is a component not yet considered that has a
			// circuit
			{
				for (int i = 0; i < nodes; i++) {
					if (!visited[i]) {
						DFSStack.push(i + 1);
						break;
					}
				}
			}// End if
		}// End while loop
		DFSStack.clearAll();
		circuit = false;
		return false;
	}// End hasCircuit()

	static boolean isSpanningTree() // Function to check whether given simple
									// graph is a
	// spanning tree or not
	{
		if (!circuit && edges == nodes - 1) // Simple graph is a spanning tree
											// if:
											// No. of edges = No. of vertices -
											// 1
											// AND
											// No cycles are present in the
											// simple graph
		{
			spanTree = true;
			return true;
		} else {
			spanTree = false;
			return false;
		}
	}// End isSpanningTree()

	static int countComponents() {
		boolean[] visited = new boolean[nodes];// initialized with false by
												// default
		pAdjList = new int[nodes][nodes];
		for (int i = 0; i < nodes; i++)// Copies adjList into pAdjList- done for
										// security
		// to prevent accidental modification of original
		{
			int j;
			for (j = 0; adjList[i][j] != -1; j++)
				pAdjList[i][j] = adjList[i][j];
			pAdjList[i][j] = -1;
		}
		int num = 0;
		DFSStack.push(1);
		int pres = 0;
		int j;
		while (num < nodes)// As long as there are unvisited nodes
		{
			while (DFSStack.hasNext()) {
				j = 0;
				pres = DFSStack.pop();
				if (!visited[pres - 1]) // If the node hasn't already been
										// visited
				{
					num++;
					visited[pres - 1] = true;// Mark node as visited
					while (pAdjList[pres - 1][j] != -1)// Add all of the nodes
														// in the present
					// adjacency list into the stack
					{
						DFSStack.push(pAdjList[pres - 1][j]);
						j++;
					}
				}
			}
			componentCount++; // When code reaches here, one more disconnected
								// component
			// is present
			for (int x = 0; x < nodes; x++) {
				if (!visited[x]) {
					DFSStack.push(x + 1);
					break;
				}
			}

		}
		DFSStack.clearAll();
		return componentCount;

	}

	private static void printOutput(String fn) // Function to write output to a
												// file
	{
		try {

			File file = new File(fn);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			if (spanTree) {
				bw.write("Spanning Tree");
				bw.newLine();
				bw.write(nodes + " vertices and " + edges + " edges");
			} else {
				bw.write("Not Spanning Tree");
				bw.newLine();
			}

			if (circuit)
				bw.write("Has circuit");
			if (circuit && componentCount > 1)
				bw.write("/" + componentCount + "-components");
			else if (componentCount > 1)
				bw.write(componentCount + "-components");
			bw.newLine();
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.print("Insufficient arguments");
			System.exit(0);
		}
		getAdjList(args[0]);
		if (adjList == null) {
			System.out.println("No graph found");
			System.exit(0);
		}
		// processAdjList();
		hasCircuit();
		isSpanningTree();
		countComponents();
		printOutput(args[1]);
	}

}
