package app.Model;


import static javafx.application.Application.launch;

//have to use getters and setters due to part being abstract
//can't be instantiated
public abstract class Part {
    protected int partID;
    protected String partName;
    protected double partPrice;
    protected int partStock;
    protected int partMin;
    protected int partMax;

    public Part() {}

    public Part(int partID, String partName, int partStock, double partPrice, int partMin, int partMax) {
        this.partID = partID;
        this.partName = partName;
        this.partStock = partStock;
        this.partPrice = partPrice;
        this.partMin = partMin;
        this.partMax = partMax;
    }

    public static void main(String[] args) {
        launch(args);
    }

    //getter part ID
    public int getPartID() {
        return partID;
    }

    //setter part ID
    public void setPartID(int partID) {
        this.partID = partID;
    }

    //getter part name
    public String getPartName() {
        return partName;
    }

    //setter part name
    public void setPartName(String partName) {
        this.partName = partName;
    }

    //getter part price
    public double getPartPrice() {
        return partPrice;
    }

    //setter for part price
    public void setPartPrice(double partPrice) {
        this.partPrice = partPrice;
    }

    //getter for parts in stock
    public int getPartStock() {
        return partStock;
    }

    //setter amount of parts in stock
    public void setPartStock(int partStock) {
        this.partStock = partStock;
    }

    //getter for minimum allowed parts
    public int getPartMin() {
        return partMin;
    }

    //sets minimum allowed parts
    public void setPartMin(int partMin) {
        this.partMin = partMin;
    }

    //getter for max allowed parts
    public int getPartMax() {
        return partMax;
    }

    //sets maximum allowed parts
    public void setPartMax(int partMax) {
        this.partMax = partMax;
    }

}
