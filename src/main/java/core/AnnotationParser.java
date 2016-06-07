package core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser for Annotation data from a gff file.
 *
 * @author Niels Warnars
 */
public final class AnnotationParser {

    private AnnotationParser() {
    }

    /**
     * Read the annotation data from a .gff file.
     *
     * @param input The input stream containing the annotation data.
     * @return A list of annotations read from the input stream.
     * @throws IOException Throw exception on read failure.
     */
    @SuppressFBWarnings("I18N")
    @SuppressWarnings("CheckStyle.MethodLength")
    public static List<Annotation> readGFF(InputStream input) throws IOException {
        List<Annotation> annotations = new ArrayList<Annotation>();

        String nextLine;
        InputStreamReader isr = new InputStreamReader(input);
        BufferedReader bReader = new BufferedReader(isr);

        while ((nextLine = bReader.readLine()) != null) {
            String[] content = nextLine.trim().split("\t");
            Annotation ann = new Annotation();

            ann.setSeqid(content[0]);
            ann.setSource(content[1]);
            ann.setType(content[2]);
            ann.setStart(Integer.parseInt(content[3]));
            ann.setEnd(Integer.parseInt(content[4]));
            ann.setScore(Float.parseFloat(content[5]));
            ann.setStrand(content[6]);
            ann.setPhase(content[7]);

            String[] attributes = content[8].split(";");
            for (int i = 0; i < attributes.length; i++) {
                String[] pair = attributes[i].split("=");

                if (pair[0].equals("calhounClass")) {
                    ann.setCallhounClassAttr(pair[1]);
                } else if (pair[0].equals("ID")) {
                    ann.setIdAttr(Long.parseLong(pair[1]));
                } else if (pair[0].equals("Name")) {
                    ann.setNameAttr(pair[1]);
                } else if (pair[0].equals("displayName")) {
                    ann.setDisplayNameAttr(pair[1]);
                }
            }

            annotations.add(ann);
        }

        bReader.close();
        return annotations;
    }

    /**
     * Gets a list of CDS filtered and sorted annotations from disk.
     *
     * @param path The path to the annotation data file.
     * @return A filtered and sorted list of annotations.
     * @throws FileNotFoundException Throw an exception on read failure.
     */
    public static List<Annotation> readCDSFilteredGFF(String path) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);

        List<Annotation> annotations = new ArrayList<>();

        try {
            annotations = readGFF(fileInputStream).stream()
                    .filter(a -> a.getType().equals("CDS")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(annotations);
        return annotations;
    }

}