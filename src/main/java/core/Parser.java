package core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Skullyhoofd on 24/04/2016.
 * Parser to turn file format into data structure.
 */
public class Parser {

    /**
     * Map of all nodes.
     */
    private HashMap<Integer, Node> nodeMap;

    /**
     * Constructor.
     */
    public Parser() {
        nodeMap = new HashMap<>();
    }

    /**
     * Read the data from a .gfa file and put the nodes in a hashmap.
     *
     * @param input - filepath of .gfa file to be parsed.
     * @return - A HashMap containing the information from the .gfa file.
     * @throws IOException Throw exception on read GFA read failure.
     */
    @SuppressWarnings("checkstyle:magicnumbers")
    @SuppressFBWarnings("I18N")
    public final HashMap<Integer, Node>
        readGFA(final InputStream input)
            throws IOException {
        BufferedReader bReader;
        bReader = new BufferedReader(new InputStreamReader(input));
        String nextLine;
        while ((nextLine = bReader.readLine()) != null) {
            String[] content = nextLine.trim().split("\\s+");
            switch (nextLine.charAt(0)) {
                case 'H':
                    break;
                case 'S':
                    int id = Integer.parseInt(content[1]);
                    String sequence = content[2];
                    int z = Integer.parseInt(content[content.length - 1].split(":")[2]);
                    String[] genomes = content[4].split(":")[2].split(";");
                    for (int i = 0; i < genomes.length; i++) {
                        genomes[i] = genomes[i].substring(0, genomes[i].length() - 6);
                    }

                    if (!nodeMap.containsKey(id)) {
                        nodeMap.put(id, new Node(id, sequence, z));
                        if (!(sequence.contains("A") || sequence.contains("C")
                                || sequence.contains("T") || sequence.contains("G"))) {
                            //System.out.println("Node: " + id + " Contains NOT(ATCG). Seq: "
                                   // + sequence);
                        }
                    } else {
                        nodeMap.get(id).setSequence(sequence);
                        nodeMap.get(id).setzIndex(z);
                        //System.out.println("Duplicate node: " + id);
                    }

                    nodeMap.get(id).addAllGenome(genomes);
                    break;
                case 'L':
                    int orig = Integer.parseInt(content[1]);
                    int dest = Integer.parseInt(content[3]);
                    if (content[1].length() == 0 || content[3].length() == 0) {
                        //System.out.println("NULL LINK: " + content[1] + ", " + content[3]);
                    }
                    nodeMap.get(orig).addLink(dest);
                    break;
                default:
                    break;
            }
        }
        bReader.close();
        return nodeMap;
    }


}
