package application.controllers;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.util.ArrayList;

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
        loadGenome = initMenuItem("Load Genome Sequence",
                new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN),
                t -> WindowFactory.createGraphChooser());
        loadPhylogeneticTree = initMenuItem("Load Phylogenetic Tree",
                new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN),
                t -> WindowFactory.createTreeChooser());

        return initMenu("File", loadGenome, loadPhylogeneticTree);
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
        filterLineage = new Menu("Lineage");
        CheckMenuItem lin1 = new CheckMenuItem("LIN 1");
        lin1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(
                        LIN1, lin1.isSelected()));
        CheckMenuItem lin2 = new CheckMenuItem("LIN 2");
        lin2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(
                        LIN2, lin2.isSelected()));
        CheckMenuItem lin3 = new CheckMenuItem("LIN 3");
        lin3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(
                        LIN3, lin3.isSelected()));
        CheckMenuItem lin4 = new CheckMenuItem("LIN 4");
        lin4.setOnAction(event ->
                mainController.getTreeController().modifyFilter(
                        LIN4, lin4.isSelected()));
        CheckMenuItem lin5 = new CheckMenuItem("LIN 5");
        lin5.setOnAction(event ->
                mainController.getTreeController().modifyFilter(
                        LIN5, lin5.isSelected()));
        CheckMenuItem lin6 = new CheckMenuItem("LIN 6");
        lin6.setOnAction(event ->
                mainController.getTreeController().modifyFilter(
                        LIN6, lin6.isSelected()));
        CheckMenuItem lin7 = new CheckMenuItem("LIN 7");
        lin7.setOnAction(event ->
                mainController.getTreeController().modifyFilter(
                        LIN7, lin7.isSelected()));
        CheckMenuItem lin8 = new CheckMenuItem("LIN animal");
        lin8.setOnAction(event ->
                mainController.getTreeController().modifyFilter(
                        LIN8, lin8.isSelected()));
        CheckMenuItem lin9 = new CheckMenuItem("LIN B");
        lin9.setOnAction(event ->
                mainController.getTreeController().modifyFilter(
                        LIN9, lin9.isSelected()));
        CheckMenuItem lin10 = new CheckMenuItem("LIN CANETTII");
        lin10.setOnAction(event ->
                mainController.getTreeController().modifyFilter(
                        LIN10, lin10.isSelected()));

        filterLineage = initMenu("Lineage", lin1, lin2, lin3, lin4, lin5, lin6, lin7, lin8, lin9, lin10);
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

        hiv.selectedToggleProperty().addListener(ob -> {
            mainController.getTreeController().modifyFilter(HIVP, pos.isSelected());
            mainController.getTreeController().modifyFilter(HIVN, neg.isSelected());
        });

        filterHIV = initMenu("HIV", pos, neg, non);
    }

    private void initCohortFilter() {
        filterCohort = new Menu("Cohort");
        CheckMenuItem cohort1 = new CheckMenuItem("KZNSUR");
        cohort1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(KZNSUR, cohort1.isSelected()));
        CheckMenuItem cohort2 = new CheckMenuItem("PROX");
        cohort2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PROX, cohort2.isSelected()));
        CheckMenuItem cohort3 = new CheckMenuItem("NHLS");
        cohort3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(NHLS, cohort3.isSelected()));
        CheckMenuItem cohort4 = new CheckMenuItem("CUBS");
        cohort4.setOnAction(event ->
                mainController.getTreeController().modifyFilter(CUBS, cohort4.isSelected()));
        CheckMenuItem cohort5 = new CheckMenuItem("Phage");
        cohort5.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PHAGE, cohort5.isSelected()));

        filterCohort = initMenu("Cohort", cohort1, cohort2, cohort3, cohort4, cohort5);
    }

    private void initDistrictFilter() {
        CheckMenuItem dist1 = new CheckMenuItem("Amajuba");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(AMAJUBA, dist1.isSelected()));
        CheckMenuItem dist2 = new CheckMenuItem("eThekwini");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ETHEKWINI, dist2.isSelected()));
        CheckMenuItem dist3 = new CheckMenuItem("iLembe");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ILEMBE, dist3.isSelected()));
        CheckMenuItem dist4 = new CheckMenuItem("Sisonke");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SISONKE, dist4.isSelected()));
        CheckMenuItem dist5 = new CheckMenuItem("Ugu");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(UGU, dist5.isSelected()));
        CheckMenuItem dist6 = new CheckMenuItem("uMgungundlovu");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(UMGUNGUNDLOVU, dist6.isSelected()));
        CheckMenuItem dist7 = new CheckMenuItem("uMkhanyakude");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(UMKHANYAKUDE, dist7.isSelected()));
        CheckMenuItem dist8 = new CheckMenuItem("uMzinyathi");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(UMZINYATHI, dist8.isSelected()));
        CheckMenuItem dist9 = new CheckMenuItem("Uthukela");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(UTHUKELA, dist9.isSelected()));
        CheckMenuItem dist10 = new CheckMenuItem("uThungulu");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(UTHUNGULU, dist10.isSelected()));
        CheckMenuItem dist11 = new CheckMenuItem("Zululand");
        dist1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ZULULAND, dist11.isSelected()));

        filterStudyDistrict = initMenu("Study District", dist1, dist2, dist3, dist4, dist5,
                dist6, dist7, dist8, dist9, dist10, dist11);
    }

    private void initSpecimenFilter() {
        CheckMenuItem spec1 = new CheckMenuItem("blood");
                spec1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(BLOOD, spec1.isSelected()));
        CheckMenuItem spec2 = new CheckMenuItem("CSF");
                spec2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(CSF, spec2.isSelected()));
        CheckMenuItem spec3 = new CheckMenuItem("pleura");
                spec3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PLEURA, spec3.isSelected()));
        CheckMenuItem spec4 = new CheckMenuItem("pleural fluid");
                spec4.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PLEURAL_FLUID, spec4.isSelected()));
        CheckMenuItem spec5 = new CheckMenuItem("pus");
                spec5.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PUS, spec5.isSelected()));
        CheckMenuItem spec6 = new CheckMenuItem("sputum");
                spec6.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPUTUM, spec6.isSelected()));

        filterSpecimenType = initMenu("Specimen type", spec1, spec2, spec3, spec4, spec5, spec6);
    }

    private void initIsolationFilter() {
        CheckMenuItem iso1 = new CheckMenuItem("single colony");
                iso1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SINGLE_COLONY, iso1.isSelected()));
        CheckMenuItem iso2 = new CheckMenuItem("non-single colony");
                iso2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(NON_SINGLE_COLONY, iso2.isSelected()));

        filterIsolation = initMenu("DNA isolation", iso1, iso2);
    }

    private void initPhenoDSTfilter() {
        CheckMenuItem dst1 = new CheckMenuItem("MDR");
                dst1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PHENO_MDR, dst1.isSelected()));
        CheckMenuItem dst2 = new CheckMenuItem("mono");
                dst2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PHENO_MONO, dst2.isSelected()));
        CheckMenuItem dst3 = new CheckMenuItem("poly");
                dst3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PHENO_POLY, dst3.isSelected()));
        CheckMenuItem dst4 = new CheckMenuItem("susceptible");
                dst4.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PHENO_SUSCEPTIBLE, dst4.isSelected()));
        CheckMenuItem dst5 = new CheckMenuItem("XDR");
                dst5.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PHENO_XDR, dst5.isSelected()));

        filterPhenoDST = initMenu("Phenotypic DST", dst1, dst2, dst3, dst4, dst5);
    }

    private void initCapreomycinFilter() {
        CheckMenuItem cap1 = new CheckMenuItem("R");
                cap1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(CAPREOMYCIN_R, cap1.isSelected()));
        CheckMenuItem cap2 = new CheckMenuItem("S");
                cap2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(CAPREOMYCIN_S, cap2.isSelected()));
        CheckMenuItem cap3 = new CheckMenuItem("U");
                cap3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(CAPREOMYCIN_U, cap3.isSelected()));

        filterCapreomycin = initMenu("Capreomycin", cap1, cap2, cap3);
    }

    private void initEthambutolFilter() {
        CheckMenuItem eth1 = new CheckMenuItem("R");
                eth1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ETHAMBUTOL_R, eth1.isSelected()));
        CheckMenuItem eth2 = new CheckMenuItem("S");
                eth2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ETHAMBUTOL_S, eth2.isSelected()));
        CheckMenuItem eth3 = new CheckMenuItem("U");
                eth3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ETHAMBUTOL_R, eth3.isSelected()));

        filterEthambutol = initMenu("Ethambutol", eth1, eth2, eth3);
    }

    private void initEthionamideFilter() {
        CheckMenuItem eth1 = new CheckMenuItem("R");
                eth1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ETHIONAMIDE_R, eth1.isSelected()));
        CheckMenuItem eth2 = new CheckMenuItem("S");
                eth2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ETHIONAMIDE_S, eth2.isSelected()));
        CheckMenuItem eth3 = new CheckMenuItem("U");
                eth3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ETHIONAMIDE_U, eth3.isSelected()));

        filterEthionAmide = initMenu("Ethionamide", eth1, eth2, eth3);
    }

    private void initIsoniazidFilter() {
        CheckMenuItem iso1 = new CheckMenuItem("R");
                iso1.setOnAction(event ->
                        mainController.getTreeController().modifyFilter(ISONIAZID_R, iso1.isSelected()));
        CheckMenuItem iso2 = new CheckMenuItem("S");
                iso2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ISONIAZID_S, iso2.isSelected()));
        CheckMenuItem iso3 = new CheckMenuItem("U");
                iso3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(ISONIAZID_U, iso3.isSelected()));

        filterIsoniazid = initMenu("Isoniazid", iso1, iso2, iso3);
    }

    private void initKanamycinFilter() {
        CheckMenuItem kan1 = new CheckMenuItem("R");
                kan1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(KANAMYCIN_R, kan1.isSelected()));
        CheckMenuItem kan2 = new CheckMenuItem("S");
                kan2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(KANAMYCIN_S, kan2.isSelected()));
        CheckMenuItem kan3 = new CheckMenuItem("U");
                kan3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(KANAMYCIN_U, kan3.isSelected()));

        filterKanamycin = initMenu("Kanamycin", kan1, kan2, kan3);
    }

    private void initPyrazinamideFilter() {
        CheckMenuItem pyr1 = new CheckMenuItem("R");
                pyr1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PYRAZINAMIDE_R, pyr1.isSelected()));
        CheckMenuItem pyr2 = new CheckMenuItem("S");
                pyr2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PYRAZINAMIDE_S, pyr2.isSelected()));
        CheckMenuItem pyr3 = new CheckMenuItem("U");
                pyr3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(PYRAZINAMIDE_U, pyr3.isSelected()));

        filterPyrazinamide = initMenu("Pyrazinamide", pyr1, pyr2, pyr3);
    }

    private void initOfloxacinFilter() {
        CheckMenuItem ofl1 = new CheckMenuItem("R");
                ofl1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(OFLOXACIN_R, ofl1.isSelected()));
        CheckMenuItem ofl2 = new CheckMenuItem("S");
                ofl2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(OFLOXACIN_S, ofl2.isSelected()));
        CheckMenuItem ofl3 = new CheckMenuItem("U");
                ofl3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(OFLOXACIN_U, ofl3.isSelected()));

        filterOfloxacin = initMenu("Ofloxacin", ofl1, ofl2, ofl3);
    }

    private void initRifampinFilter() {
        CheckMenuItem rif1 = new CheckMenuItem("R");
                rif1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(RIFAMPIN_R, rif1.isSelected()));
        CheckMenuItem rif2 = new CheckMenuItem("S");
                rif2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(RIFAMPIN_S, rif2.isSelected()));
        CheckMenuItem rif3 = new CheckMenuItem("U");
                rif3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(RIFAMPIN_U, rif3.isSelected()));

        filterRifampin = initMenu("Rifampin", rif1, rif2, rif3);
    }

    private void initStreptomycinFilter() {
        CheckMenuItem str1 = new CheckMenuItem("R");
                str1.setOnAction(event ->
                        mainController.getTreeController().modifyFilter(STREPTOMYCIN_R, str1.isSelected()));
        CheckMenuItem str2 = new CheckMenuItem("S");
                str2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(STREPTOMYCIN_S, str2.isSelected()));
        CheckMenuItem str3 = new CheckMenuItem("U");
                str3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(STREPTOMYCIN_U, str3.isSelected()));

        filterStreptomycin = initMenu("Streptomycin", str1, str2, str3);
    }

    private void initSpoligotypeFilter() {
        CheckMenuItem spo1 = new CheckMenuItem("Bejing");
                spo1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_BEJING, spo1.isSelected()));
        CheckMenuItem spo2 = new CheckMenuItem("CAS");
                spo2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_CAS, spo2.isSelected()));
        CheckMenuItem spo3 = new CheckMenuItem("CAS1-Delhi");
                spo3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_CAS1_DELHI, spo3.isSelected()));
        CheckMenuItem spo4 = new CheckMenuItem("CAS1-Kili");
                spo4.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_CAS1_KILI, spo4.isSelected()));
        CheckMenuItem spo5 = new CheckMenuItem("EAI1-SOM");
                spo5.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_EAI1_SOM, spo5.isSelected()));
        CheckMenuItem spo6 = new CheckMenuItem("H1");
                spo6.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_H1, spo6.isSelected()));
        CheckMenuItem spo7 = new CheckMenuItem("H37Rv");
                spo7.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_H37RV, spo7.isSelected()));
        CheckMenuItem spo8 = new CheckMenuItem("LAM11-ZWE");
                spo8.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_LAM11_ZWE, spo8.isSelected()));
        CheckMenuItem spo9 = new CheckMenuItem("LAM3");
                spo9.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_LAM3, spo9.isSelected()));
        CheckMenuItem spo11 = new CheckMenuItem("LAM4");
                spo11.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_LAM4, spo11.isSelected()));
        CheckMenuItem spo12 = new CheckMenuItem("LAM5");
                spo12.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_LAM5, spo12.isSelected()));
        CheckMenuItem spo13 = new CheckMenuItem("LAM6");
                spo13.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_LAM6, spo13.isSelected()));
        CheckMenuItem spo14 = new CheckMenuItem("LAM9");
                spo14.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_LAM9, spo14.isSelected()));
        CheckMenuItem spo15 = new CheckMenuItem("S");
                spo15.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_S, spo15.isSelected()));
        CheckMenuItem spo16 = new CheckMenuItem("T1");
                spo16.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_T1, spo16.isSelected()));
        CheckMenuItem spo17 = new CheckMenuItem("T2");
                spo17.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_T2, spo17.isSelected()));
        CheckMenuItem spo18 = new CheckMenuItem("T3");
                spo18.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_T3, spo18.isSelected()));
        CheckMenuItem spo19 = new CheckMenuItem("T5-RUS1");
                spo19.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_T5_RUS1, spo19.isSelected()));
        CheckMenuItem spo20 = new CheckMenuItem("X2");
                spo20.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_X2, spo20.isSelected()));
        CheckMenuItem spo21 = new CheckMenuItem("X3");
                spo21.setOnAction(event ->
                mainController.getTreeController().modifyFilter(SPOLIGOTYPE_X3, spo21.isSelected()));

        filterSpoligotype = initMenu("Digital spoligotype", spo1, spo2, spo3, spo4, spo5,
                spo6, spo7, spo8, spo9,
                spo11, spo12, spo13, spo14, spo15,
                spo16, spo17, spo18, spo19, spo20,
                spo21);
    }

    private void initGenoDSTFilter() {
        CheckMenuItem gen1 = new CheckMenuItem("Drug-resistant (other)");
                gen1.setOnAction(event ->
                mainController.getTreeController().modifyFilter(GENO_DRUG_RESIST, gen1.isSelected()));
        CheckMenuItem gen2 = new CheckMenuItem("MDR");
                gen2.setOnAction(event ->
                mainController.getTreeController().modifyFilter(GENO_MDR, gen2.isSelected()));
        CheckMenuItem gen3 = new CheckMenuItem("susceptible");
                gen3.setOnAction(event ->
                mainController.getTreeController().modifyFilter(GENO_SUSCEPTIBLE, gen3.isSelected()));
        CheckMenuItem gen4 = new CheckMenuItem("XDR");
                gen4.setOnAction(event ->
                mainController.getTreeController().modifyFilter(GENO_XDR, gen4.isSelected()));

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

        tf.selectedToggleProperty().addListener(ob -> {
            mainController.getTreeController().modifyFilter(TF, pos.isSelected());
            mainController.getTreeController().modifyFilter(NTF, neg.isSelected());
        });

        filterTF = initMenu("Tugela Ferry XDR", pos, neg, non);
    }
}
