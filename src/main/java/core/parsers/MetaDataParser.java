package core.parsers;

import core.genome.Genome;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

/**
 * Parser for MetaData stored in .xlsx files.
 *
 * @author Arthur Breurkes.
 * @version 1.0
 * @since 23-05-2016.
 */
public final class MetaDataParser {
    private static TreeMap<String, Genome> metadata = new TreeMap<>();

    private MetaDataParser() {
    }

    /**
     * Reads a meta data file from disk.
     *
     * @param path The path to the meta data file.
     */
    public static void readMetadataFromFile(String path) {
        if (path != null && new File(path).exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(path);
                metadata = parse(fileInputStream);
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Parses MetaData into a Treemap.
     *
     * @param stream an InputStream to read the xlsx sheet from.
     * @return a TreeMap with genomes sorted on their name.
     */
    public static TreeMap<String, Genome> parse(InputStream stream) {
        TreeMap<String, Genome> metaMap = new TreeMap<>();

        try {
            XSSFWorkbook book = new XSSFWorkbook(stream);
            XSSFSheet sheet = book.getSheetAt(0);

            sheet.forEach(row -> {
                parseRow(row, metaMap);

            });
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return metaMap;
    }

    /**
     * Return the lineage code belonging to a genome.
     * <p>
     * LINEAGE INT VALUES:
     * 0 - Unknown.
     * 1-7 - normal numeric.
     * 8 - animal.
     * 9 - B.
     * 10 - CANETTII.
     *
     * @param s lineage string.
     * @return lineage code.
     */
    public static int detLineage(String s) {
        if (s.equals("unknown")) {
            return 0;
        } else if (s.equals("LIN animal")) {
            return 8;
        } else if (s.equals("LIN B")) {
            return 9;
        } else if (s.equals("LIN CANETTII")) {
            return 10;
        } else {
            return Integer.parseInt(s.replace("LIN ", ""));
        }
    }


    /**
     * Gets the meta data.
     *
     * @return The meta data.
     */
    public static TreeMap<String, Genome> getMetadata() {
        return metadata;
    }


    /**
     * Method to parse a row.
     * @param row the row to be parsed.
     * @param metaMap map to put results in.
     */
    public static void parseRow(Row row, TreeMap<String, Genome> metaMap) {
        if (row.getCell(0) != null && row.getCell(0).getStringCellValue().contains("TKK")) {
            String name = row.getCell(0).getStringCellValue();
            Genome gen = new Genome(name);
            for (int i = 1; i < 28; i++) {
                switchRow1(i, row, gen);
                switchRow2(i, row, gen);
                metaMap.put(name, gen);
            }
        }
    }


    /**
     * Check all columns of a row.
     * @param i - index of column
     * @param row - the row
     * @param gen - genomes
     */
    private static void switchRow1(int i, Row row, Genome gen) {
        switch (i) {
            case 1:
                if (row.getCell(1).getCellType() == 0) {
                    gen.setAge((int) row.getCell(1).getNumericCellValue());
                }
                break;
            case 2:
                gen.setSex(row.getCell(2).getStringCellValue());
                break;
            case 3:
                gen.setHiv(row.getCell(3).getStringCellValue());
                break;
            case 4:
                gen.setCohort(row.getCell(4).getStringCellValue());
                break;
            case 6:
                gen.setStudyDistrict(row.getCell(6).getStringCellValue());
                break;
            case 7:
                gen.setSpecimenType(row.getCell(7).getStringCellValue());
                break;
            case 8:
                gen.setSmearStatus(row.getCell(8).getStringCellValue());
                break;
            case 10:
                gen.setIsolation(row.getCell(10).getStringCellValue());
                break;
            case 11:
                gen.setPhenoDST(row.getCell(11).getStringCellValue());
                break;
            case 12:
                gen.setCapreomycin(row.getCell(12).getStringCellValue());
                break;
            case 13:
                gen.setEthambutol(row.getCell(13).getStringCellValue());
                break;
            case 14:
                gen.setEthionamide(row.getCell(14).getStringCellValue());
                break;
            default:
                break;
        }
    }

    /**
     * Check all columns of a row.
     * @param i - index of column
     * @param row - the row
     * @param gen - genomes
     */
    private static void switchRow2(int i, Row row, Genome gen) {
        switch (i) {
            case 16:
                gen.setIsoniazid(row.getCell(15).getStringCellValue());
                break;
            case 17:
                gen.setKanamycin(row.getCell(16).getStringCellValue());
                break;
            case 18:
                gen.setPyrazinamide(row.getCell(18).getStringCellValue());
                break;
            case 19:
                gen.setOfloxacin(row.getCell(19).getStringCellValue());
                break;
            case 20:
                gen.setRifampin(row.getCell(20).getStringCellValue());
                break;
            case 21:
                gen.setStreptomycin(row.getCell(21).getStringCellValue());
                break;
            case 22:
                gen.setSpoligotype(row.getCell(22).getStringCellValue());
                break;
            case 23:
                gen.setLineage(detLineage(row.getCell(23).getStringCellValue()));
                break;
            case 24:
                gen.setGenoDST(row.getCell(24).getStringCellValue());
                break;
            case 26:
                gen.setTf(row.getCell(26).getStringCellValue());
                break;
            default:
                break;
        }
    }
}
