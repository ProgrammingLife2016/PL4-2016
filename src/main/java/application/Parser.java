package application;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Skullyhoofd on 24/04/2016.
 * Parser to turn file format into data structure.
 */
public class Parser {

    /**
     * Map of all nodes
     */
    private HashMap nodeMap;

    /**
     * Constructor
     */
    public Parser(){
        nodeMap = new HashMap();
    }

    /**
     * Read and parse the data from a .gfa file and put the nodes in a hashmap.
     * @param input - filepath of .gfa file to be parsed.
     * @return - A HashMap containing the information from the .gfa file.
     */
    public HashMap readGFA(String input){
        BufferedReader bReader = null;
        try{
            bReader = new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF-8"));
            String nextLine;
            while ((nextLine = bReader.readLine()) != null){
                System.out.println(nextLine);
                switch (nextLine.charAt(0)){
                    case 'H':
                        break;
                    case 'S':
                        //TODO parse node
                        break;
                    case 'L':
                        //TODO parse link
                        break;
                    default:
                        break;
                }
            }

            bReader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return nodeMap;
    }

}
