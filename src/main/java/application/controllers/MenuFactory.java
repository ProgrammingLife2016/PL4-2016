package application.controllers;

import core.Filter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import static core.Filter.*;


/**
 * Created by Daphne van Tetering on 4-5-2016.
 */
@SuppressFBWarnings("MS_PKGPROTECT")
public class MenuFactory {
    protected static Menu filterLineage, filterHIV, filterCohort, filterStudyDistrict,
            filterSpecimenType, filterIsolation, filterPhenoDST, filterCapreomycin, filterEthambutol,
            filterEthionAmide, filterIsoniazid, filterKanamycin, filterPyrazinamide, filterOfloxacin,
            filterRifampin, filterStreptomycin, filterSpoligotype, filterGenoDST, filterTF;
    protected static MenuItem loadPhylogeneticTree, loadGenome, loadAnnotations, resetView,
            shortcuts, showPhylogeneticTree, showGenomeSequence, showSelectedStrains,
            showOnlyThisStrain;
    private MainController mainController;

    private Menu fileMenu;

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
        fileMenu = initFileMenu();
        Menu viewMenu = initViewMenu();
        Menu filterMenu = initFilterMenu();
        Menu helpMenu = initHelpMenu();
        bar.getMenus().addAll(fileMenu, viewMenu, filterMenu, helpMenu);
        return bar;
    }

    private Menu initHelpMenu() {
        shortcuts = initMenuItem("Shortcuts", new KeyCodeCombination(KeyCode.TAB), null);

        return initMenu("Help", shortcuts);
    }

    private Menu initViewMenu() {
        showGenomeSequence = initMenuItem("Show Graph", null, event -> {
            mainController.fillGraph(new ArrayList<>(), new ArrayList<>());

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
            mainController.getGraphController().getZoomBox().reset();
            mainController.getGraphController().getGraphMouseHandling().setPrevClick(null);
            mainController.createList();

        });

        showSelectedStrains.setDisable(true);
        showOnlyThisStrain.setDisable(true);

        return initMenu("View",
                showGenomeSequence, showPhylogeneticTree, separatorOne,
                showSelectedStrains, showOnlyThisStrain, separatorTwo, resetView);
    }

    private Menu initFileMenu() {
        loadAnnotations = initMenuItem("Load Annotation data",
                new KeyCodeCombination(KeyCode.A, KeyCodeCombination.CONTROL_DOWN),
                t -> {
                    WindowFactory.createAnnotationChooser();
                });
        loadGenome = initMenuItem("Load Genome Sequence",
                new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN),
                t -> {
                    WindowFactory.createGraphChooser();

                });
        loadPhylogeneticTree = initMenuItem("Load Phylogenetic Tree",
                new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN),
                t -> {
                    WindowFactory.createTreeChooser();

                });

        return initMenu("File", loadGenome, loadPhylogeneticTree,
                initMostRecentGFAMenu(), initMostRecentNWKMenu(), loadAnnotations);
    }

    private Menu initMostRecentGFAMenu() {
        LinkedList<String> mostRecent = mainController.getMostRecentGFA();
        String recent01 = "Empty";
        String recent02 = "Empty";
        String recent03 = "Empty";

        if (mostRecent.size() >= 1 && !(mostRecent.get(0).equals("Empty"))) {
            recent01 = mostRecent.get(0);
        }

        if (mostRecent.size() >= 2 && !(mostRecent.get(1).equals("Empty"))) {
            recent02 = mostRecent.get(1);
        }

        if (mostRecent.size() >= 3 && !(mostRecent.get(2).equals("Empty"))) {
            recent03 = mostRecent.get(2);
        }

        final String finalRecent1 = recent01;
        final String finalRecent2 = recent02;
        final String finalRecent3 = recent03;

        MenuItem recent1 = initMenuItem(recent01, null, event -> {
            if (finalRecent1 != "") {
                mainController.getGraphController().getGraph().getNodeMapFromFile(finalRecent1.toString());
                mainController.initGraph();
//                File file = new File(finalRecent1);
//                File parentDir = file.getParentFile();
//                WindowFactory.
            }
        });

        MenuItem recent2 = initMenuItem(recent02, null, event -> {
            if (finalRecent2 != "") {
                mainController.getGraphController().getGraph().getNodeMapFromFile(finalRecent2.toString());
                mainController.initGraph();
            }
        });

        MenuItem recent3 = initMenuItem(recent03, null, event -> {
            if (finalRecent3 != "") {
                mainController.getGraphController().getGraph().getNodeMapFromFile(finalRecent3.toString());
                mainController.initGraph();
            }
        });

        return initMenu("Load recently opened GFA file", recent1, recent2, recent3);
    }

    private Menu initMostRecentNWKMenu() {
        LinkedList<String> mostRecent = mainController.getMostRecentNWK();
        String recent01 = "Empty";
        String recent02 = "Empty";
        String recent03 = "Empty";

        if (mostRecent.size() >= 1 && !(mostRecent.get(0) == "Empty")) {
            recent01 = mostRecent.get(0);
        }

        if (mostRecent.size() >= 2 && !(mostRecent.get(1) == "Empty")) {
            recent02 = mostRecent.get(1);
        }

        if (mostRecent.size() >= 3 && !(mostRecent.get(2) == "Empty")) {
            recent03 = mostRecent.get(2);
        }

        final String finalRecent1 = recent01;
        final String finalRecent2 = recent02;
        final String finalRecent3 = recent03;

        MenuItem recent1 = initMenuItem(recent01, null, event -> {
            if (finalRecent1 != "") {
                mainController.initTree(finalRecent1.toString());
            }
        });

        MenuItem recent2 = initMenuItem(recent02, null, event -> {
            if (finalRecent2.toString() != "") {
                mainController.initTree(finalRecent2.toString());
            }
        });


        MenuItem recent3 = initMenuItem(recent03, null, event -> {
            if (finalRecent3.toString() != "") {
                mainController.initTree(finalRecent3.toString());
            }
        });

        return initMenu("Load recently opened NWK file", recent1, recent2, recent3);
    }


    private Menu initFilterMenu() {
        initLineageFilter();
        initHIVFilter();
        initCohortFilter();
        initDistrictFilter();
        initSpecimenFilter();
        initIsolationFilter();
        initPhenoDSTfilter();
        initCapreomycinFilter();
        initEthambutolFilter();
        initEthionamideFilter();
        initIsoniazidFilter();
        initKanamycinFilter();
        initPyrazinamideFilter();
        initOfloxacinFilter();
        initRifampinFilter();
        initStreptomycinFilter();
        initSpoligotypeFilter();
        initGenoDSTFilter();
        initTFFilter();

        return initMenu("Filter", filterCapreomycin, filterCohort, filterEthambutol, filterEthionAmide,
                filterGenoDST, filterHIV, filterIsolation, filterIsoniazid, filterKanamycin, filterLineage,
                filterOfloxacin, filterPhenoDST, filterPyrazinamide, filterRifampin, filterSpecimenType,
                filterSpoligotype, filterStreptomycin, filterStudyDistrict, filterTF);
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

    private CheckMenuItem initCheckMenuItem(String title, KeyCombination combination,
                                            EventHandler<ActionEvent> handler) {
        CheckMenuItem newItem = new CheckMenuItem(title);
        newItem.setAccelerator(combination);
        newItem.setOnAction(handler);
        return newItem;
    }

    private void initLineageFilter() {
        List<String> lineageExtensions = new ArrayList<>(Arrays.asList(
                "1", "2", "3", "4", "5", "6", "7", "animal", "B", "CANETTI"));
        List<Filter> linFilters = new ArrayList<>(Arrays.asList(
                LIN1, LIN2, LIN3, LIN4, LIN5, LIN6, LIN7, LIN8, LIN9, LIN10));

        filterLineage = new Menu("Lineage");
        for (int idx = 0; idx < lineageExtensions.size(); idx++) {
            CheckMenuItem lin = new CheckMenuItem("LIN " + lineageExtensions.get(idx));

            int finalIdx = idx;
            lin.setOnAction(event ->
                    mainController.getTreeController().filterPhyloLineage(linFilters.get(finalIdx), lin.isSelected())
            );

            filterLineage.getItems().add(lin);
        }
    }

    private void initHIVFilter() {
        final ToggleGroup hiv = new ToggleGroup();
        RadioMenuItem pos = new RadioMenuItem("Positive");
        RadioMenuItem neg = new RadioMenuItem("Negative");
        RadioMenuItem non = new RadioMenuItem("Either");

        pos.setToggleGroup(hiv);
        neg.setToggleGroup(hiv);
        non.setToggleGroup(hiv);

        non.setSelected(true);

        filterHIV = initMenu("HIV", pos, neg, non);
    }

    private void initCohortFilter() {
        filterCohort = new Menu("Cohort");
        CheckMenuItem cohort1 = initCheckMenuItem("KZNSUR", null, null);
        CheckMenuItem cohort2 = initCheckMenuItem("PROX", null, null);
        CheckMenuItem cohort3 = initCheckMenuItem("NHLS", null, null);
        CheckMenuItem cohort4 = initCheckMenuItem("CUBS", null, null);
        CheckMenuItem cohort5 = initCheckMenuItem("Phage", null, null);

        filterCohort = initMenu("Cohort", cohort1, cohort2, cohort3, cohort4, cohort5);
    }

    private void initDistrictFilter() {
        CheckMenuItem dist1 = initCheckMenuItem("Amajuba", null, null);
        CheckMenuItem dist2 = initCheckMenuItem("eThekwini", null, null);
        CheckMenuItem dist3 = initCheckMenuItem("iLembe", null, null);
        CheckMenuItem dist4 = initCheckMenuItem("Sisonke", null, null);
        CheckMenuItem dist5 = initCheckMenuItem("Ugu", null, null);
        CheckMenuItem dist6 = initCheckMenuItem("uMgungundlovu", null, null);
        CheckMenuItem dist7 = initCheckMenuItem("uMkhanyakude", null, null);
        CheckMenuItem dist8 = initCheckMenuItem("uMzinyathi", null, null);
        CheckMenuItem dist9 = initCheckMenuItem("Uthukela", null, null);
        CheckMenuItem dist10 = initCheckMenuItem("uThungulu", null, null);
        CheckMenuItem dist11 = initCheckMenuItem("Zululand", null, null);

        filterStudyDistrict = initMenu("Study District", dist1, dist2, dist3, dist4, dist5,
                dist6, dist7, dist8, dist9, dist10, dist11);
    }

    private void initSpecimenFilter() {
        CheckMenuItem spec1 = initCheckMenuItem("blood", null, null);
        CheckMenuItem spec2 = initCheckMenuItem("CSF", null, null);
        CheckMenuItem spec3 = initCheckMenuItem("pleura", null, null);
        CheckMenuItem spec4 = initCheckMenuItem("pleural fluid", null, null);
        CheckMenuItem spec5 = initCheckMenuItem("pus", null, null);
        CheckMenuItem spec6 = initCheckMenuItem("sputum", null, null);

        filterSpecimenType = initMenu("Specimen type", spec1, spec2, spec3, spec4, spec5, spec6);
    }

    private void initIsolationFilter() {
        CheckMenuItem iso1 = initCheckMenuItem("single colony", null, null);
        CheckMenuItem iso2 = initCheckMenuItem("non-single colony", null, null);

        filterIsolation = initMenu("DNA isolation", iso1, iso2);
    }

    private void initPhenoDSTfilter() {
        CheckMenuItem dst1 = initCheckMenuItem("MDR", null, null);
        CheckMenuItem dst2 = initCheckMenuItem("mono", null, null);
        CheckMenuItem dst3 = initCheckMenuItem("poly", null, null);
        CheckMenuItem dst4 = initCheckMenuItem("susceptible", null, null);
        CheckMenuItem dst5 = initCheckMenuItem("XDR", null, null);

        filterPhenoDST = initMenu("Phenotypic DST", dst1, dst2, dst3, dst4, dst5);
    }

    private void initCapreomycinFilter() {
        CheckMenuItem cap1 = initCheckMenuItem("R", null, null);
        CheckMenuItem cap2 = initCheckMenuItem("S", null, null);
        CheckMenuItem cap3 = initCheckMenuItem("U", null, null);

        filterCapreomycin = initMenu("Capreomycin", cap1, cap2, cap3);
    }

    private void initEthambutolFilter() {
        CheckMenuItem eth1 = initCheckMenuItem("R", null, null);
        CheckMenuItem eth2 = initCheckMenuItem("S", null, null);
        CheckMenuItem eth3 = initCheckMenuItem("U", null, null);

        filterEthambutol = initMenu("Ethambutol", eth1, eth2, eth3);
    }

    private void initEthionamideFilter() {
        CheckMenuItem eth1 = initCheckMenuItem("R", null, null);
        CheckMenuItem eth2 = initCheckMenuItem("S", null, null);
        CheckMenuItem eth3 = initCheckMenuItem("U", null, null);

        filterEthionAmide = initMenu("Ethionamide", eth1, eth2, eth3);
    }

    private void initIsoniazidFilter() {
        CheckMenuItem iso1 = initCheckMenuItem("R", null, null);
        CheckMenuItem iso2 = initCheckMenuItem("S", null, null);
        CheckMenuItem iso3 = initCheckMenuItem("U", null, null);

        filterIsoniazid = initMenu("Isoniazid", iso1, iso2, iso3);
    }

    private void initKanamycinFilter() {
        CheckMenuItem kan1 = initCheckMenuItem("R", null, null);
        CheckMenuItem kan2 = initCheckMenuItem("S", null, null);
        CheckMenuItem kan3 = initCheckMenuItem("U", null, null);

        filterKanamycin = initMenu("Kanamycin", kan1, kan2, kan3);
    }

    private void initPyrazinamideFilter() {
        CheckMenuItem pyr1 = initCheckMenuItem("R", null, null);
        CheckMenuItem pyr2 = initCheckMenuItem("S", null, null);
        CheckMenuItem pyr3 = initCheckMenuItem("U", null, null);

        filterPyrazinamide = initMenu("Pyrazinamide", pyr1, pyr2, pyr3);
    }

    private void initOfloxacinFilter() {
        CheckMenuItem ofl1 = initCheckMenuItem("R", null, null);
        CheckMenuItem ofl2 = initCheckMenuItem("S", null, null);
        CheckMenuItem ofl3 = initCheckMenuItem("U", null, null);

        filterOfloxacin = initMenu("Ofloxacin", ofl1, ofl2, ofl3);
    }

    private void initRifampinFilter() {
        CheckMenuItem rif1 = initCheckMenuItem("R", null, null);
        CheckMenuItem rif2 = initCheckMenuItem("S", null, null);
        CheckMenuItem rif3 = initCheckMenuItem("U", null, null);

        filterRifampin = initMenu("Rifampin", rif1, rif2, rif3);
    }

    private void initStreptomycinFilter() {
        CheckMenuItem str1 = initCheckMenuItem("R", null, null);
        CheckMenuItem str2 = initCheckMenuItem("S", null, null);
        CheckMenuItem str3 = initCheckMenuItem("U", null, null);

        filterStreptomycin = initMenu("Streptomycin", str1, str2, str3);
    }

    private void initSpoligotypeFilter() {
        CheckMenuItem spo1 = initCheckMenuItem("Bejing", null, null);
        CheckMenuItem spo2 = initCheckMenuItem("CAS", null, null);
        CheckMenuItem spo3 = initCheckMenuItem("CAS1-Delhi", null, null);
        CheckMenuItem spo4 = initCheckMenuItem("CAS1-Kili", null, null);
        CheckMenuItem spo5 = initCheckMenuItem("EAI1-SOM", null, null);
        CheckMenuItem spo6 = initCheckMenuItem("H1", null, null);
        CheckMenuItem spo7 = initCheckMenuItem("H37Rv", null, null);
        CheckMenuItem spo8 = initCheckMenuItem("LAM11-ZWE", null, null);
        CheckMenuItem spo9 = initCheckMenuItem("LAM3", null, null);
        CheckMenuItem spo10 = initCheckMenuItem("LAM4", null, null);
        CheckMenuItem spo11 = initCheckMenuItem("LAM4", null, null);
        CheckMenuItem spo12 = initCheckMenuItem("LAM5", null, null);
        CheckMenuItem spo13 = initCheckMenuItem("LAM6", null, null);
        CheckMenuItem spo14 = initCheckMenuItem("LAM9", null, null);
        CheckMenuItem spo15 = initCheckMenuItem("S", null, null);
        CheckMenuItem spo16 = initCheckMenuItem("T1", null, null);
        CheckMenuItem spo17 = initCheckMenuItem("T2", null, null);
        CheckMenuItem spo18 = initCheckMenuItem("T3", null, null);
        CheckMenuItem spo19 = initCheckMenuItem("T5-RUS1", null, null);
        CheckMenuItem spo20 = initCheckMenuItem("X2", null, null);
        CheckMenuItem spo21 = initCheckMenuItem("X3", null, null);

        filterSpoligotype = initMenu("Digital spoligotype", spo1, spo2, spo3, spo4, spo5,
                spo6, spo7, spo8, spo9, spo10,
                spo11, spo12, spo13, spo14, spo15,
                spo16, spo17, spo18, spo19, spo20,
                spo21);
    }

    private void initGenoDSTFilter() {
        CheckMenuItem gen1 = initCheckMenuItem("Drug-resistant (other)", null, null);
        CheckMenuItem gen2 = initCheckMenuItem("MDR", null, null);
        CheckMenuItem gen3 = initCheckMenuItem("susceptible", null, null);
        CheckMenuItem gen4 = initCheckMenuItem("XDR", null, null);

        filterGenoDST = initMenu("Genotypic DST", gen1, gen2, gen3, gen4);
    }

    private void initTFFilter() {
        final ToggleGroup tf = new ToggleGroup();
        RadioMenuItem pos = new RadioMenuItem("True");
        RadioMenuItem neg = new RadioMenuItem("False");
        RadioMenuItem non = new RadioMenuItem("Either");

        pos.setToggleGroup(tf);
        neg.setToggleGroup(tf);
        non.setToggleGroup(tf);

        non.setSelected(true);

        filterTF = initMenu("Tugela Ferry XDR", pos, neg, non);
    }
}
