package core.filtering;

import core.genome.Genome;
import core.parsers.MetaDataParser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Class Responsible for Filtering
 *
 */
public class Filtering {
    private List<Filter> filters;
    private List<Genome> selectedGenomes;
    private Predicate<Genome> predicate;
    private List<Predicate<Genome>> predicates;

    /**
     * Constructor for filtering.
     */
    public Filtering() {
        this.filters = new ArrayList<>();
        this.selectedGenomes = new ArrayList<>();
        this.predicates = new ArrayList<>();
        this.predicate = g -> true;
    }

    /**
     * Apply filters on the strains.
     * @param f the filter to apply.
     */
    public void applyFilter(Filter f) {
        filters.add(f);
        collectPredicates();

        filter();
    }

    /**
     * Remove a filter from the strains.
     * @param f the filter to remove.
     */
    public void removeFilter(Filter f) {
        filters.remove(f);
        collectPredicates();

        filter();
    }

    /**
     * Getter method for the applied filters.
     * @return the applied filters in a list.
     */
    public List<Filter> getFilters() {
        return filters;
    }

    /**
     * Check whether filters are applied.
     * @return true or false.
     */
    public boolean isFiltering() {
        return !filters.isEmpty();
    }

    /**
     * Return the selected genomes after filtering.
     * @return a list of selected genomes.
     */
    public List<Genome> getSelectedGenomes() {
        return selectedGenomes;
    }

    /**
     * Collect all predicates for filtering.
     */
    private void collectPredicates() {
        predicates.clear();
        predicate = g -> true;
        filters.forEach(f -> {
            checkFilter1(f);
            checkFilter2(f);
            checkFilter3(f);
            checkFilter4(f);
            checkFilter5(f);
            checkFilter6(f);
            checkFilter7(f);
            checkFilter8(f);
            checkFilter9(f);
            checkFilter10(f);
            checkFilter11(f);
            checkFilter12(f);
            checkFilter13(f);
            checkFilter14(f);
            checkFilter15(f);
            checkFilter16(f);
            checkFilter17(f);
            checkFilter18(f);
            checkFilter19(f);
        });
        predicate = predicates.stream().reduce(p -> true, Predicate::and);
    }

    /**
     * Make a selection of strains with filters applied.
     */
    private void filter() {
        selectedGenomes.clear();
        if (!filters.isEmpty()) {
            MetaDataParser.getMetadata().values().stream().filter(predicate).forEach(selectedGenomes::add);
        } else {
            selectedGenomes.clear();
        }
    }

    /**
     * Check the filters.
     * @param f Filter to be checked.
     */
    private void checkFilter1(Filter f) {
        switch (f) {
            case LIN1: predicates.add(g -> g.getLineage() == 1);
                break;
            case LIN2: predicates.add(predicate.and(g -> g.getLineage() == 2));
                break;
            case LIN3: predicates.add(predicate.and(g -> g.getLineage() == 3));
                break;
            case LIN4: predicates.add(predicate.and(g -> g.getLineage() == 4));
                break;
            case LIN5: predicates.add(predicate.and(g -> g.getLineage() == 5));
                break;
            default: break;
        }
    }

    private void checkFilter2(Filter f) {
        switch (f) {
            case LIN6: predicates.add(predicate.and(g -> g.getLineage() == 6));
                break;
            case LIN7: predicates.add(predicate.and(g -> g.getLineage() == 7));
                break;
            case LIN8: predicates.add(predicate.and(g -> g.getLineage() == 8));
                break;
            case LIN9: predicates.add(predicate.and(g -> g.getLineage() == 9));
                break;
            case LIN10: predicates.add(predicate.and(g -> g.getLineage() == 10));
                break;
            default: break;
        }
    }

    /**
     * Check the filters.
     * @param f Filter to be checked.
     */
    private void checkFilter3(Filter f) {
        switch (f) {
            case HIVP: predicates.add(predicate.and(Genome::isHiv));
                break;
            case HIVN: predicates.add(predicate.and(g -> !g.isHiv()));
                break;
            case NHLS: predicates.add(predicate.and(g -> g.getCohort().equals("NHLS")));
                break;
            case CUBS: predicates.add(predicate.and(g -> g.getCohort().equals("CUBS")));
                break;
            case PHAGE: predicates.add(predicate.and(g -> g.getCohort().equals("Phage")));
                break;
            default: break;
        }
    }

    private void checkFilter4(Filter f) {
        switch (f) {
            case AMAJUBA: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("Amajuba")));
                break;
            case ETHEKWINI: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("eThekwini")));
                break;
            case ILEMBE: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("iLembe")));
                break;
            case SISONKE: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("Sisonke")));
                break;
            case UGU: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("Ugu")));
                break;
            default: break;
        }
    }

    /**
     * Check the filters.
     * @param f Filter to be checked.
     */
    private void checkFilter5(Filter f) {
        switch (f) {
            case UMGUNGUNDLOVU: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("uMgungundlovu")));
                break;
            case UMKHANYAKUDE: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("uMkhanyakude")));
                break;
            case UMZINYATHI: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("uMzinyathi")));
                break;
            case UTHUKELA: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("Uthukela")));
                break;
            case BLOOD: predicates.add(predicate.and(g -> g.getSpecimenType().equals("blood")));
                break;
            default: break;
        }
    }

    private void checkFilter6(Filter f) {
        switch (f) {
            case CSF: predicates.add(predicate.and(g -> g.getSpecimenType().equals("CSF")));
                break;
            case PLEURA: predicates.add(predicate.and(g -> g.getSpecimenType().equals("pleura")));
                break;
            case PLEURAL_FLUID: predicates.add(predicate.and(g -> g.getSpecimenType().equals("pleural fluid")));
                break;
            case PUS: predicates.add(predicate.and(g -> g.getSpecimenType().equals("pus")));
                break;
            case SPUTUM: predicates.add(predicate.and(g -> g.getSpecimenType().equals("sputum")));
                break;
            default: break;
        }
    }

    /**
     * Check the filters.
     * @param f Filter to be checked.
     */
    private void checkFilter7(Filter f) {
        switch (f) {
            case SINGLE_COLONY: predicates.add(predicate.and(g -> g.getIsolation().equals("single colony")));
                break;
            case NON_SINGLE_COLONY: predicates.add(predicate.and(g -> g.getIsolation().equals("non-single colony")));
                break;
            case PHENO_MDR: predicates.add(predicate.and(g -> g.getPhenoDST().equals("MDR")));
                break;
            case PHENO_MONO: predicates.add(predicate.and(g -> g.getPhenoDST().equals("mono")));
                break;
            case PHENO_POLY: predicates.add(predicate.and(g -> g.getPhenoDST().equals("poly")));
                break;
            default: break;
        }
    }

    private void checkFilter8(Filter f) {
        switch (f) {
            case PHENO_SUSCEPTIBLE: predicates.add(predicate.and(g -> g.getPhenoDST().equals("susceptible")));
                break;
            case CAPREOMYCIN_S: predicates.add(predicate.and(g -> g.getCapreomycin().equals("S")));
                break;
            case CAPREOMYCIN_U: predicates.add(predicate.and(g -> g.getCapreomycin().equals("U")));
                break;
            case ETHAMBUTOL_R: predicates.add(predicate.and(g -> g.getEthambutol().equals("R")));
                break;
            case ETHAMBUTOL_S: predicates.add(predicate.and(g -> g.getEthambutol().equals("S")));
                break;
            default: break;
        }
    }

    /**
     * Check the filters.
     * @param f Filter to be checked.
     */
    private void checkFilter9(Filter f) {
        switch (f) {
            case ETHAMBUTOL_U: predicates.add(predicate.and(g -> g.getEthambutol().equals("U")));
                break;
            case ETHIONAMIDE_R: predicates.add(predicate.and(g -> g.getEthionamide().equals("R")));
                break;
            case ETHIONAMIDE_S: predicates.add(predicate.and(g -> g.getEthionamide().equals("S")));
                break;
            case ETHIONAMIDE_U: predicates.add(predicate.and(g -> g.getEthionamide().equals("U")));
                break;
            case ISONIAZID_R: predicates.add(predicate.and(g -> g.getIsoniazid().equals("R")));
                break;
            default: break;
        }
    }

    /**
     * Check the filters.
     * @param f Filter to be checked.
     */
    private void checkFilter10(Filter f) {
        switch (f) {
            case ISONIAZID_S: predicates.add(predicate.and(g -> g.getIsoniazid().equals("S")));
                break;
            case ISONIAZID_U: predicates.add(predicate.and(g -> g.getIsoniazid().equals("U")));
                break;
            case KANAMYCIN_R: predicates.add(predicate.and(g -> g.getKanamycin().equals("R")));
                break;
            case PYRAZINAMIDE_R: predicates.add(predicate.and(g -> g.getPyrazinamide().equals("R")));
                break;
            case PYRAZINAMIDE_S: predicates.add(predicate.and(g -> g.getPyrazinamide().equals("S")));
                break;
            default: break;
        }
    }

    /**
     * Check the filters.
     * @param f Filter to be checked.
     */
    private void checkFilter11(Filter f) {
        switch (f) {
            case PYRAZINAMIDE_U: predicates.add(predicate.and(g -> g.getPyrazinamide().equals("U")));
                break;
            case OFLOXACIN_R: predicates.add(predicate.and(g -> g.getOfloxacin().equals("R")));
                break;
            case OFLOXACIN_S: predicates.add(predicate.and(g -> g.getOfloxacin().equals("S")));
                break;
            case OFLOXACIN_U: predicates.add(predicate.and(g -> g.getOfloxacin().equals("U")));
                break;
            case RIFAMPIN_R: predicates.add(predicate.and(g -> g.getRifampin().equals("R")));
                break;
            default: break;
        }
    }

    /**
     * Check the filters.
     * @param f Filter to be checked.
     */
    private void checkFilter12(Filter f) {
        switch (f) {
            case RIFAMPIN_S: predicates.add(predicate.and(g -> g.getRifampin().equals("S")));
                break;
            case RIFAMPIN_U: predicates.add(predicate.and(g -> g.getRifampin().equals("U")));
                break;
            case STREPTOMYCIN_R: predicates.add(predicate.and(g -> g.getStreptomycin().equals("R")));
                break;
            case STREPTOMYCIN_S: predicates.add(predicate.and(g -> g.getStreptomycin().equals("S")));
                break;
            case STREPTOMYCIN_U: predicates.add(predicate.and(g -> g.getStreptomycin().equals("U")));
                break;
            default: break;
        }
    }

    private void checkFilter13(Filter f) {
        switch (f) {
            case SPOLIGOTYPE_CAS1_DELHI: predicates.add(predicate.and(g -> g.getSpoligotype().equals("CAS1-Delhi")));
                break;
            case SPOLIGOTYPE_CAS1_KILI: predicates.add(predicate.and(g -> g.getSpoligotype().equals("CAS1-Kili")));
                break;
            case SPOLIGOTYPE_EAI1_SOM: predicates.add(predicate.and(g -> g.getSpoligotype().equals("EAI1-SOM")));
                break;
            case SPOLIGOTYPE_H1: predicates.add(predicate.and(g -> g.getSpoligotype().equals("H1")));
                break;
            default: break;
        }
    }

    private void checkFilter14(Filter f) {
        switch (f) {
            case SPOLIGOTYPE_H37RV: predicates.add(predicate.and(g -> g.getSpoligotype().equals("H37Rv")));
                break;
            case SPOLIGOTYPE_LAM11_ZWE: predicates.add(predicate.and(g -> g.getSpoligotype().equals("LAM11_ZWE")));
                break;
            case SPOLIGOTYPE_LAM3: predicates.add(predicate.and(g -> g.getSpoligotype().equals("LAM3")));
                break;
            case SPOLIGOTYPE_LAM4: predicates.add(predicate.and(g -> g.getSpoligotype().equals("LAM4")));
                break;
            case SPOLIGOTYPE_LAM5: predicates.add(predicate.and(g -> g.getSpoligotype().equals("LAM5")));
                break;
            default: break;
        }
    }

    private void checkFilter15(Filter f) {
        switch (f) {
            case SPOLIGOTYPE_LAM6: predicates.add(predicate.and(g -> g.getSpoligotype().equals("LAM6")));
                break;
            case SPOLIGOTYPE_LAM9: predicates.add(predicate.and(g -> g.getSpoligotype().equals("LAM9")));
                break;
            case SPOLIGOTYPE_S: predicates.add(predicate.and(g -> g.getSpoligotype().equals("S")));
                break;
            case SPOLIGOTYPE_T3: predicates.add(predicate.and(g -> g.getSpoligotype().equals("T3")));
                break;
            case SPOLIGOTYPE_T5_RUS1: predicates.add(predicate.and(g -> g.getSpoligotype().equals("T5-RUS1")));
                break;
            default: break;
        }
    }

    private void checkFilter16(Filter f) {
        switch (f) {
            case SPOLIGOTYPE_X2: predicates.add(predicate.and(g -> g.getSpoligotype().equals("X2")));
                break;
            case SPOLIGOTYPE_X3: predicates.add(predicate.and(g -> g.getSpoligotype().equals("X3")));
                break;
            case GENO_DRUG_RESIST: predicates.add(predicate.and(g -> g.getGenoDST().equals("Drug-resistant (other)")));
                break;
            case GENO_MDR: predicates.add(predicate.and(g -> g.getGenoDST().equals("MDR")));
                break;
            case GENO_SUSCEPTIBLE: predicates.add(predicate.and(g -> g.getGenoDST().equals("susceptible")));
                break;
            default: break;
        }
    }

    private void checkFilter17(Filter f) {
        switch (f) {
            case GENO_XDR: predicates.add(predicate.and(g -> g.getGenoDST().equals("XDR")));
                break;
            case TF: predicates.add(predicate.and(Genome::isTf));
                break;
            case NTF: predicates.add(predicate.and(g -> !g.isTf()));
                break;
            case SPOLIGOTYPE_T1: predicates.add(predicate.and(g -> g.getSpoligotype().equals("T1")));
                break;
            case SPOLIGOTYPE_T2: predicates.add(predicate.and(g -> g.getSpoligotype().equals("T2")));
                break;
            default: break;
        }
    }

    private void checkFilter18(Filter f) {
        switch (f) {
            case KZNSUR: predicates.add(predicate.and(g -> g.getCohort().equals("KZNSUR")));
                break;
            case PROX: predicates.add(predicate.and(g -> g.getCohort().equals("PROX")));
                break;
            case UTHUNGULU: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("uThungulu")));
                break;
            case ZULULAND: predicates.add(predicate.and(g -> g.getStudyDistrict().equals("Zululand")));
                break;
            case PHENO_XDR: predicates.add(predicate.and(g -> g.getPhenoDST().equals("XDR")));
                break;
            default: break;
        }
    }

    private void checkFilter19(Filter f) {
        switch (f) {
            case CAPREOMYCIN_R: predicates.add(predicate.and(g -> g.getCapreomycin().equals("R")));
                break;
            case KANAMYCIN_S: predicates.add(predicate.and(g -> g.getKanamycin().equals("S")));
                break;
            case KANAMYCIN_U: predicates.add(predicate.and(g -> g.getKanamycin().equals("U")));
                break;
            case SPOLIGOTYPE_BEJING: predicates.add(predicate.and(g -> g.getSpoligotype().equals("Beijing")));
                break;
            case SPOLIGOTYPE_CAS: predicates.add(predicate.and(g -> g.getSpoligotype().equals("CAS")));
                break;
            default: break;
        }
    }
}
