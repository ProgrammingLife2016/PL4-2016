package application.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;

import java.io.File;


/**
 * Created by Daphne van Tetering on 25-4-2016.
 */
public class MenuController  {

    MenuBar menuBar;
    GraphViewController mainController;
    private MenuItem loadPhylogeneticTree,loadGenome,resetView, shortcuts, showPhylogeneticTree, showGenomeSequence;

    public MenuController(GraphViewController main, MenuBar bar) {
        this.mainController = main;
        this.menuBar = bar;

        Menu fileMenu = initFileMenu();

        Menu viewMenu = initViewMenu();

        Menu helpMenu = initHelpMenu();

        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);

    }

    private Menu initHelpMenu() {
        shortcuts = initMenuItem("Shortcuts", new KeyCodeCombination(KeyCode.TAB), null);
        Menu helpMenu = initMenu("Help", shortcuts);
        return helpMenu;

    }

    private Menu initViewMenu() {
        showGenomeSequence = initMenuItem("Pick reference Genome Sequence", null, null);
        showPhylogeneticTree = initMenuItem("Show Phylogenetic Tree", null, null);

        resetView = initMenuItem("Reset", null, null);
        Menu viewMenu = initMenu("View", showGenomeSequence, showPhylogeneticTree, resetView);
        return viewMenu;

    }

    private Menu initFileMenu() {
        loadGenome = initMenuItem("Load Genome Sequence", new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN), null);
        loadPhylogeneticTree = initMenuItem("Load Phylogenetic Tree", new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN), null);

        loadGenome.setOnAction(t -> WindowFactory.createDirectoryChooser());
        Menu fileMenu = initMenu("File", loadGenome, loadPhylogeneticTree);
        return fileMenu;

    }

    private Menu initMenu(String title, final MenuItem... items){
        Menu newMenu = new Menu(title);
        newMenu.getItems().addAll(items);
        return newMenu;
    }

    private MenuItem initMenuItem(String title, KeyCombination combination, EventHandler<ActionEvent> handler) {
        MenuItem newItem = new MenuItem(title);
        newItem.setAccelerator(combination);
        newItem.setOnAction(handler);

        return newItem;
    }

}
