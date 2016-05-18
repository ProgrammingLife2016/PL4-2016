package application.fxobjects.graph.cell

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import static org.mockito.Mockito.CALLS_REAL_METHODS

/**
 * Created by Ties on 11-5-2016.
 */
public class CellTest {
    @Mock
    public final Cell

    @Before
    void setUp() {
        c = Mockito.mock(Cell.class, CALLS_REAL_METHODS);
    }

    /**
     * Test the class constructor.
     */
    @Test
    void testConstructor() {
        assertNotNull(c);
    }
}
