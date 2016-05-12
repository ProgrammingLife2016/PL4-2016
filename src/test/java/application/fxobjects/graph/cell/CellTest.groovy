package application.fxobjects.graph.cell

import org.mockito.Mock
import org.mockito.Mockito
import static org.mockito.Mockito.CALLS_REAL_METHODS

/**
 * Created by Ties on 11-5-2016.
 */
class CellTest extends GroovyTestCase {
    @Mock
    public final Cell c = Mockito.mock(Cell.class, CALLS_REAL_METHODS)

    void setUp() {

    }

    void testConstructor() {
        assertNotNull(c);
    }
}
