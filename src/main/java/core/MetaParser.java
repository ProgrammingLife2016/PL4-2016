package core;

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
public class MetaParser {

    /**
     * Parses needed MetaData into a Treemap.
     * <p>
     * LINEAGE INT VALUES:
     * 0 - Unknown.
     * 1-7 - normal numeric.
     * 8 - animal.
     * 9 - B.
     * 10 - CANETTII.
     */
    public static TreeMap<String, Integer> parse(InputStream stream) {
        TreeMap<String, Integer> map = new TreeMap<>();
        try {
            XSSFWorkbook book = new XSSFWorkbook(stream);
            XSSFSheet sheet = book.getSheetAt(0);

            sheet.forEach(row -> {
                if (row.getCell(0) != null && row.getCell(0).getStringCellValue().contains("TKK")) {
                    if (row.getCell(23).getStringCellValue().equals("unknown")) {
                        map.put(row.getCell(0).getStringCellValue(), 0);
                    }
                    else if (row.getCell(23).getStringCellValue().equals("LIN animal")) {
                        map.put(row.getCell(0).getStringCellValue(), 8);
                    }
                    else if (row.getCell(23).getStringCellValue().equals("LIN B")) {
                        map.put(row.getCell(0).getStringCellValue(), 9);
                    }
                    else if (row.getCell(23).getStringCellValue().equals("LIN CANETTII")) {
                        map.put(row.getCell(0).getStringCellValue(), 10);
                    } else {
                        map.put(row.getCell(0).getStringCellValue(),
                                Integer.parseInt(row.getCell(23).getStringCellValue().replace("LIN ", "")));
                    }
                }
            });
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
