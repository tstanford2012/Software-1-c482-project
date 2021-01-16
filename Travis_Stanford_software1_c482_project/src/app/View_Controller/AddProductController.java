package app.View_Controller;

import app.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static app.Model.Inventory.*;

public class AddProductController implements Initializable {
    @FXML
    private TextField addProdSearchTextField;
    @FXML
    private TextField addProdIDTextField;
    @FXML
    private TextField addProdNameTextField;
    @FXML
    private TextField addProdInvTextField;
    @FXML
    private TextField addProdPriceTextField;
    @FXML
    private TextField addProdMaxTextField;
    @FXML
    private TextField addProdMinTextField;
    @FXML
    private TableView<Part> partsTableView;
    @FXML
    private TableView<Part> associatedPartsTableView;
    private int prodID;
    private String exceptionMessage = new String();
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private static ObservableList<Part> addProdAssociatedParts = FXCollections.observableArrayList();


    @Override
    //increments and sets the product ID when the page in initialized
    //gets the part inventory to display on the table
    public void initialize(URL url, ResourceBundle resourceBundle) {

        prodID = Inventory.getProdIDInc();
        displayPartsTable();
        displayAssociatedParts();
    }

    private void displayPartsTable () {
        partInventory.setAll(getPartInventory());
        partsTableView.setItems(partInventory);
        partsTableView.refresh();
    }

    private void displayAssociatedParts() {
        addProdAssociatedParts.setAll();
        associatedPartsTableView.setItems(addProdAssociatedParts);
        associatedPartsTableView.refresh();
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
    //same functionality as the main screen searches
    public void addProdSearchBtnHandler(ActionEvent event) {
        String searchText = addProdSearchTextField.getText();
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
        addProdSearchTextField.clear();
    }
    //adds a part from the part table and places it in the associated part table
    //throws an error if no part selected when the button is pressed
    public void addProdAddBtnHandler(ActionEvent event) {
        Part part = partsTableView.getSelectionModel().getSelectedItem();
        if (partsTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error adding associated part");
            alert.setContentText("Please select an associated part to add");
            alert.showAndWait();
        } else {
            addProdAssociatedParts.add(part);
        }
    }
    //asks for confirmation and then removes a selected part from the associated parts table
    public void addProdDelBtnHandler(ActionEvent event) {
        Part selectedPart = associatedPartsTableView.getSelectionModel().getSelectedItem();
        if (associatedPartsTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error removing associated part");
            alert.setContentText("Please select an associated part to remove");
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
                addProdAssociatedParts.remove(selectedPart);
            }
            else {
                System.out.println("No longer deleting.");
            }
        }
    }
    //asks for confirmation and then goes back to the main screen
    public void addProdCancelBtnHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Confirmation");
        alert.setHeaderText("Cancel Confirmation");
        alert.setContentText("Are you sure you want to cancel? Changes will not be saved!");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Stage stage;
            Parent root;
            stage=(Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View_Controller/MainScreen.fxml"));

            root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            System.out.println("Continuing to add...");
        }
    }
    //creates a new product, sets the attributes, validates the attributes, and adds the associated parts to the list
    //for the product
    public void addProdSaveBtnHandler(ActionEvent event) throws IOException {
            String prodName;
            int prodStock;
            double prodPrice;
            int prodMin;
            int prodMax;

            try {
                //grabs the text and sets the attributes to the text
                prodName = addProdNameTextField.getText();
                prodStock = Integer.parseInt(addProdInvTextField.getText());
                prodPrice = Double.parseDouble(addProdPriceTextField.getText());
                prodMin = Integer.parseInt(addProdMinTextField.getText());
                prodMax = Integer.parseInt(addProdMaxTextField.getText());
                //ensures that a product name is entered
                if (addProdNameTextField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error adding product");
                    alert.setContentText("Please enter a product name");
                    alert.showAndWait();

                }
                else {
                        //ensures that the product has at least 1 associated part
                        if (addProdAssociatedParts.isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error adding product");
                            alert.setContentText("There must be at least 1 associated part");
                            alert.showAndWait();
                        }
                        else {
                            //creates new product, sets the attributes and validates
                            //then goes to main screen
                            System.out.println("You are adding product " + prodName);
                            exceptionMessage = Product.isProductValid(Integer.parseInt(String.valueOf(prodMin)), Integer.parseInt(String.valueOf(prodMax)), Integer.parseInt(String.valueOf(prodStock)), Double.parseDouble(String.valueOf(prodPrice)), exceptionMessage);
                            if (exceptionMessage.length() > 0) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Error");
                                alert.setHeaderText("Error adding product");
                                alert.setContentText(exceptionMessage);
                                alert.showAndWait();
                                exceptionMessage = "";
                            } else if (exceptionMessage.length() == 0) {
                                Product newProduct = new Product(prodID, prodName, prodStock, prodPrice, prodMin, prodMax);
                                newProduct.setProdID(prodID);
                                newProduct.setProdName(prodName);
                                newProduct.setProdStock(prodStock);
                                newProduct.setProdPrice(prodPrice);
                                newProduct.setProdMin(prodMin);
                                newProduct.setProdMax(prodMax);
                                newProduct.setAssociatedParts(addProdAssociatedParts);
                                productInventory.add(newProduct);

                                Stage stage;
                                Parent root;
                                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("../View_Controller/MainScreen.fxml"));

                                root = loader.load();

                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                            }
                        }
                    }
            }
            //ensures that all fields are filled
            catch (NumberFormatException exception) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error modifying product");
                alert.setContentText("One or more fields is blank");
                alert.showAndWait();
            }
        }
}
