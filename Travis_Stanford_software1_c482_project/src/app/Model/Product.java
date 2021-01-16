package app.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int prodID;
    private String prodName;
    private double prodPrice;
    private int prodStock;
    private int prodMin;
    private int prodMax;

    //Constructor for Product
    public Product(int prodID, String prodName, int prodStock, double prodPrice, int prodMin, int prodMax) {
        setProdID(prodID);
        setProdName(prodName);
        setProdPrice(prodPrice);
        setProdStock(prodStock);
        setProdMin(prodMin);
        setProdMax(prodMax);

    }
    //validation for products
    public static String isProductValid(int prodMin, int prodMax, int prodStock, double prodPrice, String errorMessage) {
        if (prodPrice <= 0) {
            errorMessage = errorMessage + "The price of product must be greater than $0. ";
        }
        if (prodMax < prodMin) {
            errorMessage = errorMessage + "The maximum inventory must be more than the minimum. ";
        }
        if (prodStock < prodMin || prodStock > prodMax) {
            errorMessage = errorMessage + "Error! Inventory not between Min and Max. Try adding more stock or less stock. ";
        }
        return errorMessage;
    }

    //getter for product ID
    public int getProdID() {
        return prodID;
    }
    //setter for product ID
    public void setProdID(int prodID) {
        this.prodID = prodID;
    }


    //getter for product name
    public String getProdName() {
        return prodName;
    }
    //setter for product name
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    //getter for products in stock
    public int getProdStock() {
        return prodStock;
    }
    //setter for products in stock
    public void setProdStock(int prodStock) {
        this.prodStock = prodStock;
    }

    //getter for product price
    public double getProdPrice() {
        return prodPrice;
    }
    //setter for product price
    public void setProdPrice(double prodPrice) {
        this.prodPrice = prodPrice;
    }

    //getter for min allowed products
    public int getProdMin() {
        return prodMin;
    }
    //setter for min allowed products
    public void setProdMin(int prodMin) {
        this.prodMin = prodMin;
    }

    //getter for max allowed products
    public int getProdMax() {
        return prodMax;
    }
    //setter for max allowed products
    public void setProdMax(int prodMax) {
        this.prodMax = prodMax;
    }


    //adds a part to the associated parts when called
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    //gets the list of associated parts when called
    public ObservableList getAssociatedParts() {
        return associatedParts;
    }
    //sets the associated parts when called
    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

}

