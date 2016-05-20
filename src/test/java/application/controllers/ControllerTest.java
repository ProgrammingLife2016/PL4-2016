package application.controllers;

        import javafx.beans.property.SimpleBooleanProperty;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import org.junit.After;
        import org.junit.Before;
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.mockito.Mock;
        import org.mockito.Mockito;
        import org.mockito.runners.MockitoJUnitRunner;

        import java.io.IOException;
        import java.io.InputStream;
        import java.util.Observer;

/**
 * Created by Niek on 5/18/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {


        Controller mc;
        @Mock
        Observer mockedObserver;

        @Before
        public void setUp() {

        }

        /**
         * Clean-up.
         */
        @After
        public void tearDown() {

        }

        /**
         * Test RuntimeException on wrong FXML file.
         */
        @Test(expected = RuntimeException.class)
        public void testLoadWrongFXMLfile() {
                mc = Mockito.mock(Controller.class, Mockito.CALLS_REAL_METHODS);
                mc.loadFXMLfile("");
        }


        /**
         * Test whether the root is set correctly
         */
        @Test
        public void testGetRoot() {
                mc = Mockito.mock(Controller.class, Mockito.CALLS_REAL_METHODS);
                Parent parent = Mockito.mock(Parent.class);
                mc.setRoot(parent);
                assert(mc.getRoot().equals(parent));
        }


        @Test
        public void testSetRoot() {
                mc = Mockito.mock(Controller.class, Mockito.CALLS_REAL_METHODS);
                Parent parent = Mockito.mock(Parent.class);
                mc.setRoot(parent);
                Mockito.verify(mc).setRoot(parent);
                assert(mc.getRoot().equals(parent));
        }

}
