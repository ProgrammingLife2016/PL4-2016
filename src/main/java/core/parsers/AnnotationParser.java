package core.parsers;

import core.annotation.Annotation;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser for Annotation data from a gff file.
 *
 */
public final class AnnotationParser {

    /**
     * Class constructor
     */
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
    public static List<Annotation> readGFF(InputStream input) throws IOException {
        List<Annotation> annotations = new ArrayList<>();

        String nextLine;
        BufferedReader bReader = new BufferedReader(new InputStreamReader(input));

        while ((nextLine = bReader.readLine()) != null) {
            String[] content = nextLine.trim().split("\t");
            Annotation ann = new Annotation();

            fillAnnotation(ann, content);

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
     * Method to create an Annotation with the needed information
     * @param ann the Annotation
     * @param content the information to be given to the Annotation
     */
    private static void fillAnnotation(Annotation ann, String[] content) {
        ann.setSeqid(content[0]);
        ann.setSource(content[1]);
        ann.setType(content[2]);
        ann.setStart(Integer.parseInt(content[3]));
        ann.setEnd(Integer.parseInt(content[4]));
        ann.setScore(Float.parseFloat(content[5]));
        ann.setStrand(content[6]);
        ann.setPhase(content[7]);
    }

    /**
     * Gets a list of CDS filtered and sorted annotations from disk.
     *
     * @param path The path to the annotation data file.
     * @return A filtered and sorted list of annotations.
     * @throws FileNotFoundException Throw an exception on read failure.
     */
    public static List<Annotation> readGFFFromFile(String path) {
        List<Annotation> annotations = new ArrayList<>();

        if (path != null && new File(path).exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(path);
                annotations = readGFF(fileInputStream).stream()
                        .filter(a -> a.getType().equals("CDS")).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(annotations);
        return annotations;
    }

}