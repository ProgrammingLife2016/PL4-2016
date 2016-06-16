package application.fxobjects.treeCells;

import application.fxobjects.LineageColor;
import core.parsers.MetaDataParser;
import core.typeEnums.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the LeafCell class.
 *
 * @author Niels Warnars
 */
public class LeafCellTest {
    public LeafCell c;
    public StackPane pane;
    /**
     * Sets up the leaf cell.
     */
    @Before
    public void setUp() {
        pane = new StackPane();
        c = new LeafCell(1, "test", pane);
    }

    /**
     * Tests the background color setting of the LeafCell constructor with a non-matching genome name.
     */
    @Test
    public void testLeafCell() {
        c = new LeafCell(1, "test", pane);
        assertEquals(Color.LIGHTGRAY, c.getBackground().getFills().get(0).getFill());
    }

    /**
     * Tests the background color setting of the LeafCell constructor with a genome name containing TKK.
     */
    @Test
    public void testLeafCellTKK() {
        MetaDataParser.readMetadataFromFile("src/main/resources/TestFiles/metadataTestFile.xlsx");
        assertEquals(1, MetaDataParser.getMetadata().size());

        c = new LeafCell(1, "TKKa", pane);
        assertEquals(LineageColor.determineLeafLinColor(4), c.getBackground().getFills().get(0).getFill());
    }
    
    /**
     * Tests the background color setting of the LeafCell constructor with a genome name containing G.
     */
    @Test
    public void testLeafCellG() {
        c = new LeafCell(1, "G", pane);
        assertEquals(LineageColor.determineLeafLinColor(4),
                c.getBackground().getFills().get(0).getFill());

    }

    /**
     * Tests the getType method.
     */
    @Test
    public void getType() {
        assertEquals(CellType.TREELEAF, c.getType());
    }

    /**
     * Tests the getName and setName methods.
     */
    @Test
    public void testName() {
        assertEquals("test", c.getName());
    }

}