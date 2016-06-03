package core.genome;

/**
 * Created by Arthur on 5/30/16.
 */
public class Genome {
    private String name;
    private int age;
    private String sex;
    private boolean hiv;
    private String cohort;
    private String studyDistrict;
    private String specimenType;
    private String smearStatus;
    private String isolation;
    private String phenoDST;
    private String capreomycin;
    private String ethambutol;
    private String ethionamide;
    private String isoniazid;
    private String kanamycin;
    private String pyrazinamide;
    private String ofloxacin;
    private String rifampin;
    private String streptomycin;
    private String spoligotype;
    private String genoDST;
    private int lineage;
    private String tf;

    /**
     * Construct the genome.
     */
    public Genome(String name) {
        this.name = name;
    }

    /**
     * Getter method for the name of the genome.
     * @return the name of the genome.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for the age of the genome.
     * @return the age of the genome.
     */
    public int getAge() {
        return age;
    }

    /**
     * Setter method for tha age of the genome.
     * @param age the age of the genome.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Getter method for the sex of the genome.
     * @return the sex of the genome.
     */
    public String getSex() {
        return sex;
    }

    /**
     * Setter method for the sex of the genome.
     * @param sex the sex of the genome.
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Check whether genome is HIV positive.
     * @return HIV positive.
     */
    public boolean isHiv() {
        return hiv;
    }

    /**
     * Set whether genome is HIV positive.
     * @param hiv HIV positive.
     */
    public void setHiv(boolean hiv) {
        this.hiv = hiv;
    }

    /**
     * Set whether genome is HIV positive.
     * @param hiv HIV positive.
     */
    public void setHiv(String hiv) {
        if (hiv.equals("Positive")) {
            this.hiv = true;
        }
        else if (hiv.equals("Negative")) {
            this.hiv = false;
        }
    }

    /**
     * Getter method for the cohort of the genome.
     * @return the cohort of the genome.
     */
    public String getCohort() {
        return cohort;
    }

    /**
     * Setter method for the cohort of the genome.
     * @param cohort the cohort of the genome.
     */
    public void setCohort(String cohort) {
        this.cohort = cohort;
    }

    /**
     * Getter method for the study district of the genome.
     * @return the study district.
     */
    public String getStudyDistrict() {
        return studyDistrict;
    }

    /**
     * Setter method for the study district of the genome.
     * @param studyDistrict the study district.
     */
    public void setStudyDistrict(String studyDistrict) {
        this.studyDistrict = studyDistrict;
    }

    /**
     * Getter method for the specimen type of the genome.
     * @return the specimen type of the genome.
     */
    public String getSpecimenType() {
        return specimenType;
    }

    /**
     * Setter method for the specimen type of the genome.
     * @param specimenType the specimen type of the genome.
     */
    public void setSpecimenType(String specimenType) {
        this.specimenType = specimenType;
    }

    /**
     * Getter method for the microscopic smear status of the genome.
     * @return the microscopic smear status.
     */
    public String getSmearStatus() {
        return smearStatus;
    }

    /**
     * Setter method for the microscopic smear status of the genome.
     * @param smearStatus the microscopic smear status.
     */
    public void setSmearStatus(String smearStatus) {
        this.smearStatus = smearStatus;
    }

    /**
     * Getter method for the isolation status of the genome.
     * @return the isolation status of the genome.
     */
    public String getIsolation() {
        return isolation;
    }

    /**
     * Setter method for the isolation status of the genome.
     * @param isolation the isolation status of the genome.
     */
    public void setIsolation(String isolation) {
        this.isolation = isolation;
    }

    /**
     * Getter method for the phenotypic DST of the genome.
     * @return the phenotypic DST of the genome.
     */
    public String getPhenoDST() {
        return phenoDST;
    }

    /**
     * Setter method for the phenotypic DST of the genome.
     * @param phenoDST the phenotypic DST of the genome.
     */
    public void setPhenoDST(String phenoDST) {
        this.phenoDST = phenoDST;
    }

    /**
     * Getter method for the Capreomycin value of the genome.
     * @return the Capreomycin value of the genome.
     */
    public String getCapreomycin() {
        return capreomycin;
    }

    /**
     * Setter method for the capreomycin value of the genome.
     * @param capreomycin the capreomycin value of the genome.
     */
    public void setCapreomycin(String capreomycin) {
        this.capreomycin = capreomycin;
    }

    /**
     * Getter method for the Ethambutol value of the genome.
     * @return the Ethambutol value of the genome.
     */
    public String getEthambutol() {
        return ethambutol;
    }

    /**
     * Setter method for the Ethambutol value of the genome.
     * @param ethambutol the Ethambutol value of the genome.
     */
    public void setEthambutol(String ethambutol) {
        this.ethambutol = ethambutol;
    }

    /**
     * Getter method for the Ethionamide value of the genome.
     * @return the Ethionamide value of the genome.
     */
    public String getEthionamide() {
        return ethionamide;
    }

    /**
     * Setter method for the Ethionamide value of the genome.
     * @param ethionamide the Ethionamide value of the genome.
     */
    public void setEthionamide(String ethionamide) {
        this.ethionamide = ethionamide;
    }

    /**
     * Getter method for the Isoniazid value of the genome.
     * @return the Isoniazid value of the genome.
     */
    public String getIsoniazid() {
        return isoniazid;
    }

    /**
     * Setter method for the Isoniazid value of the genome.
     * @param isoniazid the Isoniazid value of the genome.
     */
    public void setIsoniazid(String isoniazid) {
        this.isoniazid = isoniazid;
    }

    /**
     * Getter method for the Kanamycin value of the genome.
     * @return the Kanamycin value of the genome.
     */
    public String getKanamycin() {
        return kanamycin;
    }

    /**
     * Setter method for the Kanamycin value of the genome.
     * @param kanamycin the Kanamycin value of the genome.
     */
    public void setKanamycin(String kanamycin) {
        this.kanamycin = kanamycin;
    }

    /**
     * Getter method for the Pyrazinamide value of the genome.
     * @return the Pyrazinamide value of the genome.
     */
    public String getPyrazinamide() {
        return pyrazinamide;
    }

    /**
     * Setter method for the Pyrazinamide value of the genome.
     * @param pyrazinamide the Pyrazinamide value of the genome.
     */
    public void setPyrazinamide(String pyrazinamide) {
        this.pyrazinamide = pyrazinamide;
    }

    /**
     * Getter method for the Ofloxacin value of the genome.
     * @return the Ofloxacin value of the genome.
     */
    public String getOfloxacin() {
        return ofloxacin;
    }

    /**
     * Setter method for the Ofloxacin value of the genome.
     * @param ofloxacin the Ofloxacin value of the genome.
     */
    public void setOfloxacin(String ofloxacin) {
        this.ofloxacin = ofloxacin;
    }

    /**
     * Getter method for the Rifampin value of the genome.
     * @return the Rifampin value of the genome.
     */
    public String getRifampin() {
        return rifampin;
    }

    /**
     * Setter method for the Rifampin value of the genome.
     * @param rifampin the Rifampin value of the genome.
     */
    public void setRifampin(String rifampin) {
        this.rifampin = rifampin;
    }

    /**
     * Getter method for the Streptomycin value of the genome.
     * @return the Streptomycin value of the genome.
     */
    public String getStreptomycin() {
        return streptomycin;
    }

    /**
     * Setter method for the Streptomycin value of the genome.
     * @param streptomycin the Streptomycin value of the genome.
     */
    public void setStreptomycin(String streptomycin) {
        this.streptomycin = streptomycin;
    }

    /**
     * Getter method for the Spoligotype of the genome.
     * @return the Spoligotype of the genome.
     */
    public String getSpoligotype() {
        return spoligotype;
    }

    /**
     * Setter method for the Spoligotype of the genome.
     * @param spoligotype the Spoligotype of the genome.
     */
    public void setSpoligotype(String spoligotype) {
        this.spoligotype = spoligotype;
    }

    /**
     * Getter method of the Genotypic DST of the genome.
     * @return the Genotypic DST of the genome.
     */
    public String getGenoDST() {
        return genoDST;
    }

    /**
     * Setter method for the Genotypic DST of the genome.
     * @param genoDST the Genotypic DST of the genome.
     */
    public void setGenoDST(String genoDST) {
        this.genoDST = genoDST;
    }

    /**
     * Getter method for the lineage of the genome.
     * @return the lineage of the genome.
     */
    public int getLineage() {
        return lineage;
    }

    /**
     * Setter method for the lineage of the genome.
     * @param lineage the lineage of the genome.
     */
    public void setLineage(int lineage) {
        this.lineage = lineage;
    }

    /**
     * Getter method for the Tugela Ferry value of the genome.
     * @return the Tugela Ferry value of the genome.
     */
    public String getTf() {
        return tf;
    }

    /**
     * Setter method for the Tugela Ferry value of the genome.
     * @param tf the Tugela Ferry value of the genome.
     */
    public void setTf(String tf) {
        this.tf = tf;
    }
}
