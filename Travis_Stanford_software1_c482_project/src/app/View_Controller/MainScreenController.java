package app.View_Controller;

import app.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import app.Model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static app.Model.Inventory.getPartInventory;
import static app.Model.Inventory.getProductInventory;


public class MainScreenController implements Initializable {
    @FXML
    private TextField searchBarPartsText;
    @FXML
    private TextField searchBarProdText;
    @FXML
    private TableView<Part> partsTableView;
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private static Product productToNextScreen;

    //Takes the product that is selected on the main screen and makes it accessible to the modify
    //product screen
    public static Product getProductToNextScreen() {
        return productToNextScreen;
    }


    @FXML
    private TableView<Product> prodTableView;

    //constructor for main screen controller
    //takes the inventory as a parameter

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //calls the display parts table function
        displayPartsTable();
        //calls the display products table function
        displayProductsTable();
    }
    //gets the part inventory and sets the table view to display the inventory.
    private void displayPartsTable() {
        partsTableView.refresh();
        partInventory.setAll(getPartInventory());
        partsTableView.setItems(partInventory);

    }
    //gets the product inventory and sets the table view to display the inventory.
    private void displayProductsTable() {
        productInventory.setAll(getProductInventory());
        prodTableView.setItems(productInventory);
        partsTableView.refresh();
    }

    private ObservableList<Part> searchPartName(String partialName) {
        ObservableList<Part> foundPartsList = FXCollections.observableArrayList();

        ObservableList<Part> partInventory = Inventory.getPartInventory();


        for(Part part : partInventory) {
            if(part.getPartName().contains(partialName)) {
                foundPartsList.add(part);
            }
        }
        return foundPartsList;
    }

    private Part getPartWithFoundID(int id) {
        ObservableList<Part> partInventory = Inventory.getPartInventory();

        for(Part part : partInventory) {
            if(part.getPartID() == id) {
                return part;
            }
        }
        return null;
    }
    //searches the part table for either an ID or a name including partial name
    //removes list items that do not match search
    //throws an error if no search is found
    public void searchHandler(ActionEvent event) throws IOException {
        String searchText = searchBarPartsText.getText();

        ObservableList<Part> parts = searchPartName(searchText);

        if(parts.size() == 0) {
            try {
                int id = Integer.parseInt(searchText);
                Part part = getPartWithFoundID(id);
                if (part != null) {
                    parts.add(part);
                }
            }
            catch (NumberFormatException exception){
                //ignore
            }
        }
        partsTableView.setItems(parts);
        System.out.println("Press the search button with the searchbar empty to return the full list ");
        if(parts.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error in search");
            alert.setContentText("No matching parts in search");
            alert.showAndWait();
        }
        searchBarPartsText.clear();
    }
    //moves to the add part screen
    public void addBtnHandler(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View_Controller/AddPart.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    //takes a selected item and moves to the modify part screen to modify it
    //throws an error if no item is selected
    public void modBtnHandler(ActionEvent event) throws IOException {
        Part part  = partsTableView.getSelectionModel().getSelectedItem();
        if(partsTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error modifying part");
            alert.setContentText("You did not select a part to modify");
            alert.showAndWait();
        }
        else {
            Stage stage;
            Parent root;
            stage=(Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource(
                    "../View_Controller/ModifyPart.fxml"));
            root =loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ModifyPartController controller = loader.getController();
            controller.setPart(part);
        }
    }
    //asks for confirmation and then deletes a selected item from the part list
    //throws an error if no part is selected
    public void deleteBtnHandler(ActionEvent event) {
        Part part = partsTableView.getSelectionModel().getSelectedItem();
        if(partsTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error deleting part");
            alert.setContentText("You did not select a part to delete");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("DELETE Confirmation");
            alert.setHeaderText("DELETE Confirmation");
            alert.setContentText("ARE YOU SURE YOU WOULD LIKE TO DELETE?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                partInventory.remove(part);
                Inventory.partInventory.remove(part);
            }
            else {
                System.out.println("No longer deleting.");
            }
        }
    }

    private ObservableList<Product> searchProdName(String partialName) {
        ObservableList<Product> foundProdsList = FXCollections.observableArrayList();

        ObservableList<Product> productInventory = Inventory.getProductInventory();


        for(Product product : productInventory) {
            if(product.getProdName().contains(partialName)) {
                foundProdsList.add(product);
            }
        }
        return foundProdsList;
    }

    private Product getProdWithFoundID(int id) {
        ObservableList<Product> productInventory = Inventory.getProductInventory();

        for(Product product : productInventory) {
            if(product.getProdID() == id) {
                return product;
            }
        }
        return null;
    }
    //same functionality as the part search bar
    public void searchHandler1(ActionEvent event) {
        String searchText = searchBarProdText.getText();
        ObservableList<Product> products = searchProdName(searchText);

        if(products.size() == 0) {
            try {
                int id = Integer.parseInt(searchText);
                Product product = getProdWithFoundID(id);
                if (product != null) {
                    products.add(product);
                }
            }
            catch (NumberFormatException exception){
                //ignore
            }
        }
        prodTableView.setItems(products);
        System.out.println("Press the search button with the searchbar empty to return the full list ");
        if(products.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error in search");
            alert.setContentText("No matching products in search");
            alert.showAndWait();
        }
        searchBarProdText.clear();
    }
    //goes to the add product screen
    public void addBtnHandler1(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;
        stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View_Controller/AddProduct.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //takes a selected item from the table and goes to the modify product screen to modify it
    //throws an error if no product is selected
    public void modBtnHandler1(ActionEvent event) throws IOException {
        Product product=prodTableView.getSelectionModel().getSelectedItem();
        productToNextScreen = product;

        if (prodTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error modifying product");
            alert.setContentText("You did not select a product to modify");
            alert.showAndWait();
        }
        else {
            Stage stage;
            Parent root;
            stage=(Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader=new FXMLLoader(getClass().getResource(
                    "../View_Controller/ModifyProduct.fxml"));
            root =loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }
    //asks for confirmation and deletes a selected item from the product list
    //throws an error if no item is selected
    public void deleteBtnHandler1(ActionEvent event) {
        Product product = prodTableView.getSelectionModel().getSelectedItem();
        if(prodTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error deleting product");
            alert.setContentText("You did not select a product to delete");
            alert.showAndWait();
        }
        else {
            if (!product.getAssociatedParts().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error deleting product");
                alert.setContentText("Cannot delete a product that has associated parts");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("DELETE Confirmation");
                alert.setHeaderText("DELETE Confirmation");
                alert.setContentText("ARE YOU SURE YOU WOULD LIKE TO DELETE?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    productInventory.remove(product);
                    Inventory.productInventory.remove(product);
                } else {
                    System.out.println("No longer deleting.");
                }
            }
        }
    }
    //asks for confirmation and then exits the program
    public void exitBtnHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Exit Confirmation");
        alert.setContentText("Are you sure you want to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        else {
            System.out.println("No longer exiting.");
        }
    }
}
