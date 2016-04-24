package application;

import java.io.FileInputStream;
import java.util.HashMap;

/**
 * Main class for initialization purposes.
 */
public class Main {

	/**
	 * Standard main method.
	 * @param args argument parameter.
	 *
	 */
	public static void main(String[] args) {

		Parser parser = new Parser();

		//TODO (?) ik krijg de filepath niet werkend met ../
		HashMap nodeMap = parser.readGFA("C:/Users/Skullyhoofd/Documents/PL4-2016/src/main/resources/TB10.gfa");
	}
}
