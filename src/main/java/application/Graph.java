package application;

public class Graph {
	public int[] nodes = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
	public int[] referenceNodes = {1,2,3,4,5,6,7,8,9,10};	
	
	public int[][] referenceEdges = new int[][]{
		{1,2},
		{2,3},
		{3,4},
		{4,5},
		{5,6},
		{6,7},
		{7,8},
		{8,9},
		{9,10},
		};
	
	public int[][] bubbleEdges = new int[][]{
		{1,3},
		
		{3,11},
		{11,12},
		{12,4},
		
		{9,13},
		{13,14},
		{14,15},
		{15,16},
		{16,17},
		{17,10}
		};
}
