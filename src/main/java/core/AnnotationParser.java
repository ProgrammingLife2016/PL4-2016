package core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
    public static List<Annotation> readGFF(final InputStream input) throws IOException {
        List<Annotation> annotations = new ArrayList<Annotation>();

        String nextLine;
        BufferedReader bReader = new BufferedReader(new InputStreamReader(input));

        while ((nextLine = bReader.readLine()) != null) {
            String[] content = nextLine.trim().split("\\s+");
            String seqid = content[0];
            String source = content[1];
            String type = content[2];
            int start = Integer.parseInt(content[3]);
            int end = Integer.parseInt(content[4]);
            float score = Float.parseFloat(content[5]);
            String strand = content[6];
            String phase = content[7];
            String[] attributes = content[8].split(";");

            String callhounClassAttr = "";
            double idAttr = 0;
            String nameAttr = "";
            String displayNameAttr = "";

            for (int i = 0; i < attributes.length; i++) {
                String[] pair = attributes[i].split("=");
                String key = pair[0];
                String value = pair[1];

                if (key.equals("calhounClass")) {
                    callhounClassAttr = value;
                } else if (key.equals("ID")) {
                    idAttr = Double.parseDouble(value);
                } else if (key.equals("Name")) {
                    nameAttr = value;
                } else if (key.equals("displayName")) {
                    displayNameAttr = value;
                }
            }

            Annotation ann = new Annotation(seqid, source, type, start, end, score, strand, phase,
                    callhounClassAttr, idAttr, nameAttr, displayNameAttr);

            annotations.add(ann);
        }

        bReader.close();
        return annotations;
    }

}