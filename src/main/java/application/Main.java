package application;

import core.Parser;

import java.io.File;
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
		HashMap nodeMap = parser.readGFA("src/main/resources/TB10.gfa");

    }
}
