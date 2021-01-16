package app.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    public static ObservableList<Part> partInventory = FXCollections.observableArrayList();
    public static ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private static int partIDInc;
    private static int prodIDInc;

    public static ObservableList<Part> getPartInventory() {
        return partInventory;
    }
    public static ObservableList<Product> getProductInventory() {
        return productInventory;
    }

    //increments the partID
    public static int getPartIDInc() {
        partIDInc++;
        return partIDInc;
    }
    //increments the product ID
    public static int getProdIDInc() {
        prodIDInc++;
        return prodIDInc;
    }
    //adds a new part to the part inventory when called
    public void addPart (Part newPart) {
        this.partInventory.add(newPart);
    }
    //adds a new product to the product inventory when called
    public void addProduct(Product newProduct){
        this.productInventory.add(newProduct);
    }
}
