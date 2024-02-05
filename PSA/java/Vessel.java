public class Vessel {
    private String lName;
    private String sName;
    private String inVoyN;
    private String outVoyN;

    public Vessel(String lName, String sName, String inVoyN, String outVoyN) {
        this.lName = lName;
        this.sName = sName;
        this.inVoyN = inVoyN;
        this.outVoyN = outVoyN;
    }

    public String getlName() {
        return lName;
    }

    public String getsName() {
        return sName;
    }

    public String getInVoyN() {
        return inVoyN;
    }

    public String getOutVoyN() {
        return outVoyN;
    }

    @Override
    public String toString() {
        return "Vessel [inVoyN=" + inVoyN + ", lName=" + lName + ", outVoyN=" + outVoyN + ", sName=" + sName + "]";
    }
}
    
    
          
           
    

