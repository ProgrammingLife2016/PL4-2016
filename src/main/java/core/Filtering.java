package core;

import application.fxobjects.cell.Cell;
import core.genome.Genome;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Arthur on 6/6/16.
 */
public class Filtering {
    private List<Filter> filters;
    private List<Cell> selectedCells;

    public Filtering() {
        this.filters = new ArrayList<>();
        this.selectedCells = new ArrayList<>();
    }

    public void applyFilter(Filter f, boolean state) {
        filters.add(f);
        filter();
    }

    public void removeFilter(Filter f) {
        filters.remove(f);
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public boolean isFiltering() {
        return !filters.isEmpty();
    }

    public List<Cell> getSelectedCells() {
        return selectedCells;
    }

    private Predicate<Genome> createPredicate() {
        Predicate<Genome> predicate = g -> g != null;

        filters.forEach(f -> {
            switch (f) {
                case LIN1: predicate.and(g -> g.getLineage() == 1);
                case LIN2: predicate.and(g -> g.getLineage() == 2);
                case LIN3: predicate.and(g -> g.getLineage() == 3);
                case LIN4: predicate.and(g -> g.getLineage() == 4);
                case LIN5: predicate.and(g -> g.getLineage() == 5);
                case LIN6: predicate.and(g -> g.getLineage() == 6);
                case LIN7: predicate.and(g -> g.getLineage() == 7);
                case LIN8: predicate.and(g -> g.getLineage() == 8);
                case LIN9: predicate.and(g -> g.getLineage() == 9);
                case LIN10: predicate.and(g -> g.getLineage() == 10);
                case HIVP: predicate.and(Genome::isHiv);
                case HIVN: predicate.and(g -> !g.isHiv());
                case KZNSUR: predicate.and(g -> g.getCohort().equals("KZNSUR"));
                case PROX: predicate.and(g -> g.getCohort().equals("PROX"));
                case NHLS: predicate.and(g -> g.getCohort().equals("NHLS"));
                case CUBS: predicate.and(g -> g.getCohort().equals("CUBS"));
                case PHAGE: predicate.and(g -> g.getCohort().equals("Phage"));
                case AMAJUBA: predicate.and(g -> g.getStudyDistrict().equals("Amajuba"));
                case ETHEKWINI: predicate.and(g -> g.getStudyDistrict().equals("eThekwini"));
                case ILEMBE: predicate.and(g -> g.getStudyDistrict().equals("iLembe"));
                case SISONKE: predicate.and(g -> g.getStudyDistrict().equals("Sisonke"));
                case UGU: predicate.and(g -> g.getStudyDistrict().equals("Ugu"));
                case UMGUNGUNDLOVU: predicate.and(g -> g.getStudyDistrict().equals("uMgungundlovu"));
                case UMKHANYAKUDE: predicate.and(g -> g.getStudyDistrict().equals("uMkhanyakude"));
                case UMZINYATHI: predicate.and(g -> g.getStudyDistrict().equals("uMzinyathi"));
                case UTHUKELA: predicate.and(g -> g.getStudyDistrict().equals("Uthukela"));
                case UTHUNGULU: predicate.and(g -> g.getStudyDistrict().equals("uThungulu"));
                case ZULULAND: predicate.and(g -> g.getStudyDistrict().equals("Zululand"));
                case BLOOD: predicate.and(g -> g.getSpecimenType().equals("blood"));
                case CSF: predicate.and(g -> g.getSpecimenType().equals("CSF"));
                case PLEURA: predicate.and(g -> g.getSpecimenType().equals("pleura"));
                case PLEURAL_FLUID: predicate.and(g -> g.getSpecimenType().equals("pleural fluid"));
                case SPUTUM: predicate.and(g -> g.getSpecimenType().equals("sputum"));
                case SINGLE_COLONY: predicate.and(g -> g.getIsolation().equals("single colony"));
                case NON_SINGLE_COLONY: predicate.and(g -> g.getIsolation().equals("non-single colony"));
                case PHENO_MDR: predicate.and(g-> g.getPhenoDST().equals("MDR"));
                case PHENO_MONO: predicate.and(g-> g.getPhenoDST().equals("mono"));
                case PHENO_POLY: predicate.and(g-> g.getPhenoDST().equals("poly"));
                case PHENO_SUSCEPTIBLE: predicate.and(g-> g.getPhenoDST().equals("susceptible"));
                case PHENO_XDR: predicate.and(g-> g.getPhenoDST().equals("XDR"));
                case CAPREOMYCIN_R: predicate.and(g -> g.getCapreomycin().equals("R"));
                case CAPREOMYCIN_S: predicate.and(g -> g.getCapreomycin().equals("S"));
                case CAPREOMYCIN_U: predicate.and(g -> g.getCapreomycin().equals("U"));
                case ETHAMBUTOL_R: predicate.and(g -> g.getEthambutol().equals("R"));
                case ETHAMBUTOL_S: predicate.and(g -> g.getEthambutol().equals("S"));
                case ETHAMBUTOL_U: predicate.and(g -> g.getEthambutol().equals("U"));
                case ETHIONAMIDE_R: predicate.and(g -> g.getEthionamide().equals("R"));
                case ETHIONAMIDE_S: predicate.and(g -> g.getEthionamide().equals("S"));
                case ETHIONAMIDE_U: predicate.and(g -> g.getEthionamide().equals("U"));
                case ISONIAZID_R: predicate.and(g -> g.getIsoniazid().equals("R"));
                case ISONIAZID_S: predicate.and(g -> g.getIsoniazid().equals("S"));
                case ISONIAZID_U: predicate.and(g -> g.getIsoniazid().equals("U"));
                case KANAMYCIN_R: predicate.and(g -> g.getKanamycin().equals("R"));
                case KANAMYCIN_S: predicate.and(g -> g.getKanamycin().equals("S"));
                case KANAMYCIN_U: predicate.and(g -> g.getKanamycin().equals("U"));
                case PYRAZINAMIDE_R: predicate.and(g -> g.getPyrazinamide().equals("R"));
                case PYRAZINAMIDE_S: predicate.and(g -> g.getPyrazinamide().equals("S"));
                case PYRAZINAMIDE_U: predicate.and(g -> g.getPyrazinamide().equals("U"));
                case OFLOXACIN_R: predicate.and(g -> g.getOfloxacin().equals("R"));
                case OFLOXACIN_S: predicate.and(g -> g.getOfloxacin().equals("S"));
                case OFLOXACIN_U: predicate.and(g -> g.getOfloxacin().equals("U"));
                case RIFAMPIN_R: predicate.and(g -> g.getRifampin().equals("R"));
                case RIFAMPIN_S: predicate.and(g -> g.getRifampin().equals("S"));
                case RIFAMPIN_U: predicate.and(g -> g.getRifampin().equals("U"));
                case STREPTOMYCIN_R: predicate.and(g -> g.getStreptomycin().equals("R"));
                case STREPTOMYCIN_S: predicate.and(g -> g.getStreptomycin().equals("S"));
                case STREPTOMYCIN_U: predicate.and(g -> g.getStreptomycin().equals("U"));
                case SPOLIGOTYPE_BEJING: predicate.and(g -> g.getSpoligotype().equals("Beijing"));
                case SPOLIGOTYPE_CAS: predicate.and(g -> g.getSpoligotype().equals("CAS"));
                case SPOLIGOTYPE_CAS1_DELHI: predicate.and(g -> g.getSpoligotype().equals("CAS1-Delhi"));
                case SPOLIGOTYPE_CAS1_KILI: predicate.and(g -> g.getSpoligotype().equals("CAS1-Kili"));
                case SPOLIGOTYPE_EAI1_SOM: predicate.and(g -> g.getSpoligotype().equals("EAI1-SOM"));
                case SPOLIGOTYPE_H1: predicate.and(g -> g.getSpoligotype().equals("H1"));
                case SPOLIGOTYPE_H37RV: predicate.and(g -> g.getSpoligotype().equals("H37Rv"));
                case SPOLIGOTYPE_LAM11_ZWE: predicate.and(g -> g.getSpoligotype().equals("LAM11_ZWE"));
                case SPOLIGOTYPE_LAM3: predicate.and(g -> g.getSpoligotype().equals("LAM3"));
                case SPOLIGOTYPE_LAM4: predicate.and(g -> g.getSpoligotype().equals("LAM4"));
                case SPOLIGOTYPE_LAM5: predicate.and(g -> g.getSpoligotype().equals("LAM5"));
                case SPOLIGOTYPE_LAM6: predicate.and(g -> g.getSpoligotype().equals("LAM6"));
                case SPOLIGOTYPE_LAM9: predicate.and(g -> g.getSpoligotype().equals("LAM9"));
                case SPOLIGOTYPE_S: predicate.and(g -> g.getSpoligotype().equals("S"));
                case SPOLIGOTYPE_T1: predicate.and(g -> g.getSpoligotype().equals("T1"));
                case SPOLIGOTYPE_T2: predicate.and(g -> g.getSpoligotype().equals("T2"));
                case SPOLIGOTYPE_T3: predicate.and(g -> g.getSpoligotype().equals("T3"));
                case SPOLIGOTYPE_T5_RUS1: predicate.and(g -> g.getSpoligotype().equals("T5-RUS1"));
                case SPOLIGOTYPE_X2: predicate.and(g -> g.getSpoligotype().equals("X2"));
                case SPOLIGOTYPE_X3: predicate.and(g -> g.getSpoligotype().equals("X3"));
                case GENO_DRUG_RESIST:  predicate.and(g -> g.getGenoDST().equals("Drug-resistant (other)"));
                case GENO_MDR: predicate.and(g -> g.getGenoDST().equals("MDR"));
                case GENO_SUSCEPTIBLE: predicate.and(g -> g.getGenoDST().equals("susceptible"));
                case GENO_XDR: predicate.and(g -> g.getGenoDST().equals("XDR"));
                case TF: predicate.and(g -> g.getTf().equals("Tugela Ferry XDR"));
            }
        });
        return predicate;
    }

    private void filter() {

    }
}
