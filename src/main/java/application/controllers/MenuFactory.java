package application.controllers;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.util.ArrayList;

/**
 * Created by Daphne van Tetering on 4-5-2016.
 */
@SuppressFBWarnings("MS_PKGPROTECT")
public class MenuFactory {
    protected static MenuItem loadPhylogeneticTree, loadGenome, resetView, shortcuts,
            showPhylogeneticTree, showGenomeSequence, showSelectedStrains, showOnlyThisStrain;
    private MainController mainController;

    /**
     * Constructor method for this class.
     *
     * @param controller the mainController to use.
     */
    public MenuFactory(MainController controller) {
        mainController = controller;
    }

    /**
     * Method that creates a Menu.
     *
     * @param bar a MenuBar.
     * @return the completed MenuBar.
     */
    public MenuBar createMenu(MenuBar bar) {
        Menu fileMenu = initFileMenu();
        Menu viewMenu = initViewMenu();
        Menu helpMenu = initHelpMenu();

        bar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        return bar;
    }

    private Menu initHelpMenu() {
        shortcuts = initMenuItem("Shortcuts", new KeyCodeCombination(KeyCode.TAB), null);

        Menu helpMenu = initMenu("Help", shortcuts);
        return helpMenu;

    }

    private Menu initViewMenu() {
        showGenomeSequence = initMenuItem("Show Graph", null, event -> {
            mainController.fillGraph(null, new ArrayList<>());

        });
        showPhylogeneticTree = initMenuItem("Show Phylogenetic Tree", null, event ->
                mainController.fillTree());
        showOnlyThisStrain = initMenuItem("Show graph & highlight selected strain",
                null, event -> {
                    mainController.getGraphController().getGraph().reset();
                    mainController.soloStrainSelection(mainController.getTreeController().
                            getSelectedGenomes());
                });

        showSelectedStrains = initMenuItem("Show only the selected strains in graph", null, event ->
                mainController.strainSelection(mainController.getTreeController().
                        getSelectedGenomes()));
        MenuItem separatorOne = new SeparatorMenuItem();
        MenuItem separatorTwo = new SeparatorMenuItem();
        resetView = initMenuItem("Reset", null, event -> {
            mainController.getGraphController().getGraph().reset();
            mainController.setCurrentView(mainController.getGraphController()
                    .getGraph().getLevelMaps().size() - 1);
            mainController.fillGraph(null, new ArrayList<>());
        });

        showSelectedStrains.setDisable(true);
        showOnlyThisStrain.setDisable(true);

        Menu viewMenu = initMenu("View",
                showGenomeSequence, showPhylogeneticTree, separatorOne,
                showSelectedStrains, showOnlyThisStrain, separatorTwo, resetView);
        return viewMenu;

    }

    private Menu initFileMenu() {
        loadGenome = initMenuItem("Load Genome Sequence",
                new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN),
                t -> WindowFactory.createGraphChooser());
        loadPhylogeneticTree = initMenuItem("Load Phylogenetic Tree",
                new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN),
                t -> WindowFactory.createTreeChooser());

        Menu fileMenu = initMenu("File", loadGenome, loadPhylogeneticTree);
        return fileMenu;

    }

    private Menu initMenu(String title, final MenuItem... items) {
        Menu newMenu = new Menu(title);
        newMenu.getItems().addAll(items);
        return newMenu;
    }

    private MenuItem initMenuItem(String title, KeyCombination combination,
                                  EventHandler<ActionEvent> handler) {
        MenuItem newItem = new MenuItem(title);
        newItem.setAccelerator(combination);
        newItem.setOnAction(handler);
        return newItem;
    }
}
