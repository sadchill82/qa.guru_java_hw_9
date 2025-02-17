import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArchiveFilesTest {

    private final ClassLoader cl = ArchiveFilesTest.class.getClassLoader();

    @Test
    void testFilesInZip() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("sample.zip")) {
            assert is != null;
            try (ZipInputStream zis = new ZipInputStream(is)) {

                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    System.out.println(entryName);

                    if (entryName.endsWith(".pdf")) {
                        PDF pdf = new PDF(zis);
                        assertTrue(pdf.text.contains("Fun fun fun"));
                        System.out.println("PDF is OK");
                    } else if (entryName.endsWith(".xlsx") || entryName.endsWith(".xls")) {
                        XLS xls = new XLS(zis);
                        String cellValue = xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();
                        assertTrue(cellValue.contains("Dulce"));
                        System.out.println("XLS is OK");
                    } else if (entryName.endsWith(".csv")) {
                        try (CSVReader reader = new CSVReader(new InputStreamReader(zis))) {
                            List<String[]> csvData = reader.readAll();
                            assertTrue(csvData.get(1)[2].contains("Sheryl"));
                            System.out.println("CSV is OK");
                        }
                    }
                    zis.closeEntry();
                }
            }
        }
    }
}