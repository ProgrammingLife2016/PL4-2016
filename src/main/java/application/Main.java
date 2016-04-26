package application;

import core.Node;
import core.Parser;

import java.util.HashMap;

/**
 * Main class for initialization purposes.
 */
public class Main {
	static HashMap nodeMap;

	/**
	 * Standard main method.
	 * @param args argument parameter.
	 *
	 */
	public static void main(String[] args) {

		Parser parser = new Parser();
		nodeMap = parser.readGFA("src/main/resources/TBTestFile.gfa");
		//System.out.println(nodeMap.values());

		Node root = (Node)nodeMap.get(1);
		System.out.println("Root: " + root.getSequence());
		dfs(root, 1,new boolean[nodeMap.size()]);
		}

	private static void dfs(Node n,int ni,boolean[] marked){
		if(n == null && ni>0) return;

		System.out.print(n.getSequence() + "\t");
		marked[ni-1] = true;

		//for every child
		for(int i: n.getLinks())
		{
			//if childs state is not visited then recurse
			if(!marked[i - 1])
			{
				dfs((Node)nodeMap.get(i),i,marked);
				marked[i-1] =true;
			}
		}
	}
}
