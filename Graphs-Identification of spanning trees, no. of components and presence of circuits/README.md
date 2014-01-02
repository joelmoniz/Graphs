Consider a simple graph is represented using adjacency list. 
This Java project checks whether the graph is a spanning tree or it has disconnected 
components or it has circuits.

      2--------3
	 /|\       |\
    / | \      | \
   /  |  \     |  \
  1   |   \    |   4  
   \  |    \   |  /
    \ |     \  | /
     \|      \ |/
	  6--------5
	 
InputFile.txt Example
2,6
1,6,5,3
2,5,4
3,5
6,2,3,4
1,2,5

------------------------
Output File
(Not) Spanning Tree
Has circuit/ k-components/n vertices and n-1 edges
OutputFile.txt Example
Not Spanning Tree
Has circuit
