package application.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.security.auth.Subject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Test for MainController.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
public class MainControllerTest {
//    private MainController controller;

	/**
	 * Set-up the controller.
	 */
    @Before
    public void setUp() {

    }

    /**
     * Clean-up.
     */
    @After
    public void tearDown() {

    }


    /*
    @Test
    public void testInitialize() throws MalformedURLException{
        MainController mc = Mockito.mock(MainController.class);
        ResourceBundle rb = Mockito.mock(ResourceBundle.class);
        mc.initialize(Mockito.any(), rb);
        Mockito.verify(mc).createMenu();
        Mockito.verify(mc).createList();
    }


    @Test
    public void testFillGraph() {
        MainController mc = new MainController();
        Object ref = Mockito.mock(Object.class);
        mc.fillGraph(ref);
        assert(mc.screen.equals(mc.getRoot().getCenter()));
    }
    */


    /**
     * Test for the FillTree method.
     */
    @Test
    public void testFillTree() {

    }

    /*
    @Test
    public void testCreateMenu() {
        MainController mc = new MainController();
        mc.createMenu();
        assert(mc.menuBar != null);
        assert(mc.getRoot().getTop().equals(mc.menuBar));
    }
    */

    /**
     * Test for the SwitchScene method.
     */
    @Test
    public void testSwitchScene() {

    }

    /**
     * Test for the SwitchTreeScene method.
     */
    @Test
    public void testSwitchTreeScene() {

    }
}