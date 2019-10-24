public class TaxReport {
    public boolean single;
    public double AGI;
    public int dependents;
    public double tax;

    public TaxReport(boolean single, double AGI, int dependents, double tax) {
        this.single = single;
        this.AGI = AGI;
        this.dependents = dependents;
        this.tax = tax;
    }


    public boolean getSingle() {
        return single;
    }

    public double getAGI() {
        return AGI;
    }

    public int getDependents() {
        return dependents;
    }

    public double getTax() {
        return tax;
    }
}
