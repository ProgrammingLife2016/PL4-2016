package application.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;


/**
 * Created by Daphne van Tetering on 25-4-2016.
 */
public class MenuController  {

    MenuBar menuBar;
    MainController mainController;
    private MenuItem loadPhylogeneticTree,loadGenome,resetView, shortcuts, showPhylogeneticTree, showGenomeSequence;



    public MenuController(MainController main, MenuBar bar) {
        this.mainController = main;
        this.menuBar = bar;

        Menu fileMenu = initFileMenu();
        Menu helpMenu = initHelpMenu();
        Menu viewMenu = initViewMenu();

        menuBar.getMenus().addAll(fileMenu, helpMenu, viewMenu);

    }


    private Menu initHelpMenu() {
        shortcuts = initMenuItem("Shortcuts", new KeyCodeCombination(KeyCode.TAB), null);
        Menu helpMenu = initMenu("Help", shortcuts);
        return helpMenu;

    }

    private Menu initViewMenu() {
        showGenomeSequence = initMenuItem("Show Genome Sequence", null, null);
        showPhylogeneticTree = initMenuItem("Show Phylogenetic Tree", null, null);
        Menu viewMenu = initMenu("View", showGenomeSequence, showPhylogeneticTree);
        return viewMenu;

    }

    private Menu initFileMenu() {
        loadPhylogeneticTree = initMenuItem("Load Phylogenetic Tree", new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN), null);
        Menu fileMenu = initMenu("File", loadPhylogeneticTree);
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
