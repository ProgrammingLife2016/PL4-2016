package core.genome;

/**
 * Created by Arthur on 5/30/16.
 */
public class Genome {
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
    public Genome() {
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean isHiv() {
        return hiv;
    }

    public void setHiv(boolean hiv) {
        this.hiv = hiv;
    }

    public void setHiv(String hiv) {
        if(hiv.equals("Positive")) {
            this.hiv = true;
        }
        else if(hiv.equals("Negative")) {
            this.hiv = false;
        }
    }

    public String getCohort() {
        return cohort;
    }

    public void setCohort(String cohort) {
        this.cohort = cohort;
    }

    public String getStudyDistrict() {
        return studyDistrict;
    }

    public void setStudyDistrict(String studyDistrict) {
        this.studyDistrict = studyDistrict;
    }

    public String getSpecimenType() {
        return specimenType;
    }

    public void setSpecimenType(String specimenType) {
        this.specimenType = specimenType;
    }

    public String getSmearStatus() {
        return smearStatus;
    }

    public void setSmearStatus(String smearStatus) {
        this.smearStatus = smearStatus;
    }

    public String getIsolation() {
        return isolation;
    }

    public void setIsolation(String isolation) {
        this.isolation = isolation;
    }

    public String getPhenoDST() {
        return phenoDST;
    }

    public void setPhenoDST(String phenoDST) {
        this.phenoDST = phenoDST;
    }

    public String getCapreomycin() {
        return capreomycin;
    }

    public void setCapreomycin(String capreomycin) {
        this.capreomycin = capreomycin;
    }

    public String getEthambutol() {
        return ethambutol;
    }

    public void setEthambutol(String ethambutol) {
        this.ethambutol = ethambutol;
    }

    public String getEthionamide() {
        return ethionamide;
    }

    public void setEthionamide(String ethionamide) {
        this.ethionamide = ethionamide;
    }

    public String getIsoniazid() {
        return isoniazid;
    }

    public void setIsoniazid(String isoniazid) {
        this.isoniazid = isoniazid;
    }

    public String getKanamycin() {
        return kanamycin;
    }

    public void setKanamycin(String kanamycin) {
        this.kanamycin = kanamycin;
    }

    public String getPyrazinamide() {
        return pyrazinamide;
    }

    public void setPyrazinamide(String pyrazinamide) {
        this.pyrazinamide = pyrazinamide;
    }

    public String getOfloxacin() {
        return ofloxacin;
    }

    public void setOfloxacin(String ofloxacin) {
        this.ofloxacin = ofloxacin;
    }

    public String getRifampin() {
        return rifampin;
    }

    public void setRifampin(String rifampin) {
        this.rifampin = rifampin;
    }

    public String getStreptomycin() {
        return streptomycin;
    }

    public void setStreptomycin(String streptomycin) {
        this.streptomycin = streptomycin;
    }

    public String getSpoligotype() {
        return spoligotype;
    }

    public void setSpoligotype(String spoligotype) {
        this.spoligotype = spoligotype;
    }

    public String getGenoDST() {
        return genoDST;
    }

    public void setGenoDST(String genoDST) {
        this.genoDST = genoDST;
    }

    public int getLineage() {
        return lineage;
    }

    public void setLineage(int lineage) {
        this.lineage = lineage;
    }

    public String getTf() {
        return tf;
    }

    public void setTf(String tf) {
        this.tf = tf;
    }
}
