package application;

import core.Node;
import core.Parser;

import java.util.HashMap;
import application.controllers.Controller;
import application.controllers.GraphViewController;
import application.controllers.MainController;
import application.controllers.WindowFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	static HashMap nodeMap;

	/**
     * MainController used for the application.
     */
    private Controller mainController;

    /**
     * Usual main method to start the application.
     * @param args runtime arguments.
     */
    public static void main(String[] args) {
        launch(args);

		Parser parser = new Parser();
		nodeMap = parser.readGFA("src/main/resources/TBTestFile.gfa");
		//System.out.println(nodeMap.values());

		Node root = (Node)nodeMap.get(1);
		System.out.println("Root: " + root.getSequence());
		dfs(root, 1,new boolean[nodeMap.size()]);
    }

    /**
     * Method to launch the application.
     * @param primaryStage stage used to initialize GUI.
     */
    @Override
    public void start(Stage primaryStage) {
        //mainController = new MainController();
        mainController = new GraphViewController(primaryStage);
        WindowFactory.createWindow(mainController.getRoot());
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
