package core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.*;
import java.util.HashMap;
import java.util.List;

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
     * @param annotations A list of annotations.
     * @param input - filepath of .gfa file to be parsed.
     * @return - A HashMap containing the information from the .gfa file.
     */
    @SuppressWarnings({"checkstyle:magicnumbers", "checkstyle:methodlength"})
    @SuppressFBWarnings("I18N")
    public final HashMap<Integer, Node>

    readGFA(final InputStream input, List<Annotation> annotations) {
        try {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(input));
            String nextLine;
            while ((nextLine = bReader.readLine()) != null) {
                processNextLine(nextLine);
            }

            bReader.close();

            new AnnotationProcessor(nodeMap, annotations)
                    .matchNodesAndAnnotations();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodeMap;
    }

    /**
     * Processes a given line from the GFA file.
     *
     * @param nextLine A given line to process.
     */
    private void processNextLine(String nextLine) {
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
                } else {
                    nodeMap.get(id).setSequence(sequence);
                    nodeMap.get(id).setzIndex(z);
                }

                nodeMap.get(id).addAllGenome(genomes);
                break;
            case 'L':
                int orig = Integer.parseInt(content[1]);
                int dest = Integer.parseInt(content[3]);
                nodeMap.get(orig).addLink(dest);
                break;
            default:
                break;
        }
    }

//    /**
//     * Read gfa file as a resource in the jar.
//     * @param input - the input.
//     * @return - A HashMap containing the information from the .gfa file.
//     */
//    public final HashMap<Integer, Node> readGFAAsResource(final InputStream input){
//        BufferedReader bReader;
//        bReader = new BufferedReader(new InputStreamReader(input));
//        return readGFA(bReader, null);
//    }

    /**
     * Read gfa file from a filepath.
     * @param input - the input
     * @return - A HashMap containing the information from the .gfa file.
     * @throws IOException
     */
    public final HashMap<Integer, Node> readGFAAsString(final String input, List<Annotation> a) throws IOException{
//        InputStream stream =
//

//        BufferedReader bReader;
//        BufferedReader = new BufferedReader(new FileReader(input));
//
        FileInputStream fileInputStream = new FileInputStream(input);

//        InputStream inputStream = new InputStream(bReader);

        return readGFA(fileInputStream, a);
    }

}
