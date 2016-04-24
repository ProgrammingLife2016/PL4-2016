package application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Skullyhoofd on 24/04/2016.
 */
public class Parser {

    private HashMap nodeMap;

    public Parser(){
        nodeMap = new HashMap();
    }

    private HashMap read(InputStream input){
        BufferedReader bReader = null;
        try{
            bReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String nextLine;

            while ((nextLine = bReader.readLine()) != null){
                System.out.println(nextLine);
            }

            bReader.close();
        } catch (Exception e){
            e.printStackTrace();
        }


        return nodeMap;
    }

}
