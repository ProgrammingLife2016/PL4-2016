package core.filtering;

/**
 * Created by Arthur on 6/2/16.
 */
public enum Filter {
    LIN1 ("Lineage 1"),
    LIN2 ("Lineage 2"),
    LIN3 ("Lineage 3"),
    LIN4 ("Lineage 4"),
    LIN5 ("Lineage 5"),
    LIN6 ("Lineage 6"),
    LIN7 ("Lineage 7"),
    LIN8 ("Lineage B"),
    LIN9 ("Lineage animal"),
    LIN10 ("Lineage CANETTII"),
    HIVP ("HIV positive"),
    HIVN ("HIV negative"),
    KZNSUR ("KZNSUR"),
    PROX ("PROX"),
    NHLS ("NHLS"),
    CUBS ("CUBS"),
    PHAGE ("phage"),
    AMAJUBA ("Amajuba"),
    ETHEKWINI ("eThekwini"),
    ILEMBE ("iLembe"),
    SISONKE ("Sisonke"),
    UGU ("Ugu"),
    UMGUNGUNDLOVU ("uMgundlovu"),
    UMKHANYAKUDE ("uMkhanyakude"),
    UMZINYATHI ("uMzinyathi"),
    UTHUKELA ("uThukela"),
    UTHUNGULU ("uThungulu"),
    ZULULAND ("Zululand"),
    BLOOD ("blood"),
    CSF ("CSF"),
    PLEURA ("pleura"),
    PLEURAL_FLUID ("pleural fluid"),
    PUS ("pus"),
    SPUTUM ("sputum"),
    SINGLE_COLONY ("single-colony"),
    NON_SINGLE_COLONY ("non-single-colony"),
    PHENO_MDR ("Phenotypic MDR"),
    PHENO_MONO ("Phenotypic Mono"),
    PHENO_POLY ("Phenotypic Poly"),
    PHENO_SUSCEPTIBLE ("Phenotypic Susceptible"),
    PHENO_XDR ("Phenotypic XDR"),
    CAPREOMYCIN_R ("Capreomycin R"),
    CAPREOMYCIN_S ("Capreomycin S"),
    CAPREOMYCIN_U ("Capreomycin U"),
    ETHAMBUTOL_R ("Ethambutol R"),
    ETHAMBUTOL_S ("Ethambutol S"),
    ETHAMBUTOL_U ("Ethambutol U"),
    ETHIONAMIDE_R ("Ethionamide R"),
    ETHIONAMIDE_S ("Ethionamide S"),
    ETHIONAMIDE_U ("Ethionamide U"),
    ISONIAZID_R ("Isoniazid R"),
    ISONIAZID_S ("Isoniazid S"),
    ISONIAZID_U ("Isoniazid U"),
    KANAMYCIN_R ("Kanamycin R"),
    KANAMYCIN_S ("Kanamycin S"),
    KANAMYCIN_U ("Kanamycin U"),
    PYRAZINAMIDE_R ("Pyrazinamide R"),
    PYRAZINAMIDE_S ("Pyrazinamide S"),
    PYRAZINAMIDE_U ("Pyrazinamide U"),
    OFLOXACIN_R ("Ofloxacin R"),
    OFLOXACIN_S ("Ofloxacin S"),
    OFLOXACIN_U ("Ofloxacin U"),
    RIFAMPIN_R ("Rifampin R"),
    RIFAMPIN_S ("Rifampin S"),
    RIFAMPIN_U ("Rifampin U"),
    STREPTOMYCIN_R ("Streptomycin R"),
    STREPTOMYCIN_S ("Streptomycin S"),
    STREPTOMYCIN_U ("Streptomycin U"),
    SPOLIGOTYPE_BEJING ("Spoligotype Beijing"),
    SPOLIGOTYPE_CAS ("Spoligotype CAS"),
    SPOLIGOTYPE_CAS1_DELHI ("Spoligotype CAS1_Delhi"),
    SPOLIGOTYPE_CAS1_KILI ("Spoligotype CAS1_Kili"),
    SPOLIGOTYPE_EAI1_SOM ("Spoligotype EAI1_SOM"),
    SPOLIGOTYPE_H1 ("Spoligotype H1"),
    SPOLIGOTYPE_H37RV ("Spoligotype H37RV"),
    SPOLIGOTYPE_LAM11_ZWE ("Spoligotype LAM11_ZWE"),
    SPOLIGOTYPE_LAM3 ("Spoligotype LAM3"),
    SPOLIGOTYPE_LAM4 ("Spoligotype LAM4"),
    SPOLIGOTYPE_LAM5 ("Spoligotype LAM5"),
    SPOLIGOTYPE_LAM6 ("Spoligotype LAM6"),
    SPOLIGOTYPE_LAM9 ("Spoligotype LAM9"),
    SPOLIGOTYPE_S ("Spoligotype S"),
    SPOLIGOTYPE_T1 ("Spoligotype T1"),
    SPOLIGOTYPE_T2 ("Spoligotype T2"),
    SPOLIGOTYPE_T3 ("Spoligotype T3"),
    SPOLIGOTYPE_T5_RUS1 ("Spoligotype T5_RUS1"),
    SPOLIGOTYPE_X2 ("Spoligotype X2"),
    SPOLIGOTYPE_X3 ("Spoligotype X3"),
    GENO_DRUG_RESIST ("Genotypic drug resistant"),
    GENO_MDR ("Genotypic MDR"),
    GENO_SUSCEPTIBLE ("Genotypic Susceptible"),
    GENO_XDR ("Genotypic XDR"),
    TF ("Tugela Ferry"),
    NTF ("Non Tugela Ferry");

    private String filterName;

    /**
     * Constructor method.
     * @param filterName name of the Filter.
     */
    Filter(String filterName) {
        this.filterName = filterName;
    }

    /**
     * Get the name of a filter.
     * @return the name of the Filter.
     */
    public String getFilterName() {
        return filterName;
    }
}
