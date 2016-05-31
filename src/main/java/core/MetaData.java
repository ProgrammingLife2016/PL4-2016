package core;

import core.genome.Genome;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.TreeMap;

/**
 * Parser for MetaData stored in .xlsx files.
 *
 * @author Arthur Breurkes.
 * @version 1.0
 * @since 23-05-2016.
 */
public final class MetaData {

    /**
     * Contructor method for utility class.
     */
    protected MetaData() {
        throw new UnsupportedOperationException();
    }

    /**
     * Parses MetaData into a Treemap.
     *
     * @param stream InputStream of the file to parse.
     * @return a TreeMap with genomes sorted on their name.
     */
    @SuppressWarnings({"checkstyle:linelength", "checkstyle:methodlength"})
    public static TreeMap<String, Genome> parse(InputStream stream) {
        TreeMap<String, Genome> metaMap = new TreeMap<>();
        try {
            XSSFWorkbook book = new XSSFWorkbook(stream);
            XSSFSheet sheet = book.getSheetAt(0);

            sheet.forEach(row -> {
                if (row.getCell(0) != null && row.getCell(0).getStringCellValue().contains("TKK")) {
                    Genome gen = new Genome();
                    String name = row.getCell(0).getStringCellValue();
                    for (int i = 1; i < 28; i++) {
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
                            case 16:
                                gen.setIsoniazid(row.getCell(16).getStringCellValue());
                                break;
                            case 17:
                                gen.setKanamycin(row.getCell(17).getStringCellValue());
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
                        metaMap.put(name, gen);
                    }
                }
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
    private static int detLineage(String s) {
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
}
