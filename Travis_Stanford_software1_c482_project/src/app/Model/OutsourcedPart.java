package app.Model;

public class OutsourcedPart extends Part {
    private String companyName;
    public OutsourcedPart(int partID, String partName, int partStock, double partPrice, int partMin, int partMax, String companyName) {
        super(partID, partName, partStock, partPrice, partMin, partMax);
        setPartID(partID);
        setPartName(partName);
        setPartStock(partStock);
        setPartPrice(partPrice);
        setPartMin(partMin);
        setPartMax(partMax);
        setCompanyName(companyName);

    }
    //Getter for company name
    public String getCompanyName() {
        return companyName;
    }
    //setter for company name
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    //validation for outsourced part
    public static String isOutPartValid(int partMin, int partMax, int partStock, double partPrice, String errorMessage) {
        if (partStock < 1) {
            errorMessage = errorMessage + "The inventory count must be greater than 0. ";
        }
        if (partPrice <= 0) {
            errorMessage = errorMessage + "The price of parts must be greater than $0. ";
        }
        if (partMax < partMin) {
            errorMessage = errorMessage + "The maximum inventory must be more than the minimum. ";
        }
        if (partStock < partMin || partStock > partMax) {
            errorMessage = errorMessage + "Error! Inventory not between Min and Max. Try adding more stock or less stock. ";
        }
        return errorMessage;
    }
}


