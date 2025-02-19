import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArchiveFilesTest {

    private InputStream getFileStreamFromZip(String fileSuffix) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("sample.zip");
        if (is == null) {
            throw new FileNotFoundException("sample.zip not found in resources");
        }
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().endsWith(fileSuffix)) {
                return zis;
            } else {
                zis.closeEntry();
            }
        }
        throw new FileNotFoundException("File with suffix " + fileSuffix + " not found in zip");
    }

    @Test
    void testPdfInZip() throws Exception {
        try (InputStream pdfStream = getFileStreamFromZip(".pdf")) {
            PDF pdf = new PDF(pdfStream);
            assertNotNull(pdf);
            assertTrue(pdf.text.contains("Fun fun fun"));
        }
    }

    @Test
    void testXlsxInZip() throws Exception {
        try (InputStream xlsStream = getFileStreamFromZip(".xlsx")) {
            XLS xls = new XLS(xlsStream);
            String cellValue = xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();
            assertTrue(cellValue.contains("Dulce"));
        }
    }

    @Test
    void testCsvInZip() throws Exception {
        try (InputStream csvStream = getFileStreamFromZip(".csv");
             InputStreamReader isr = new InputStreamReader(csvStream);
             CSVReader reader = new CSVReader(isr)) {
            List<String[]> csvData = reader.readAll();
            assertNotNull(csvData);
            assertTrue(csvData.get(1)[2].contains("Sheryl"));
        }
    }
}