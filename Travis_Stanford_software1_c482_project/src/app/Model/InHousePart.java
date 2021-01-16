package app.Model;

public class InHousePart extends Part {
    private int machineID;
    public InHousePart(int partID, String partName, int partStock, double partPrice, int partMin, int partMax, int machineID) {
        super(partID, partName, partStock, partPrice, partMin, partMax);
        setPartID(partID);
        setPartName(partName);
        setPartStock(partStock);
        setPartPrice(partPrice);
        setPartMin(partMin);
        setPartMax(partMax);
        setMachineID(machineID);
    }
    //getter for machine id
    public int getMachineID() {
        return machineID;
    }
    //setter for machine id
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    //validation for the in house parts
    public static String isInPartValid(int partMin, int partMax, int partStock, double partPrice, String errorMessage) {
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
