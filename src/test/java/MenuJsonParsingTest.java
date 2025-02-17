import com.fasterxml.jackson.databind.ObjectMapper;
import model.Item;
import model.Menu;
import model.MenuResponse;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MenuJsonParsingTest {

    @Test
    void parseMenuJsonTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("menu.json")) {
            assert is != null;
            MenuResponse menuResponse = objectMapper.readValue(is, MenuResponse.class);

            assertNotNull(menuResponse);
            assertNotNull(menuResponse.getMenu());

            Menu menu = menuResponse.getMenu();
            assertEquals("SVG Viewer", menu.getHeader());

            List<Item> items = menu.getItems();
            assertNotNull(items);
            assertEquals(10, items.size());

            Item firstItem = items.getFirst();
            assertEquals("OpenNew", firstItem.getId());
            assertEquals("Open New", firstItem.getLabel());
        }
    }
}