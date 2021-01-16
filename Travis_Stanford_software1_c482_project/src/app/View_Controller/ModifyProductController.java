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
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static app.Model.Inventory.*;

public class ModifyProductController implements Initializable {

    @FXML
    private TableView<Part> partsTableView;
    @FXML
    private TableView<Part> associatedPartsTableView;
    @FXML
    private TextField modProdIDTextField;
    @FXML
    private TextField modProdNameTextField;
    @FXML
    private TextField modProdInvTextField;
    @FXML
    private TextField modProdPriceTextField;
    @FXML
    private TextField modProdMaxTextField;
    @FXML
    private TextField modProdMinTextField;
    public Button modProdAddBtn;
    public Button modProdDelBtn;
    public Button modProdCancelBtn;
    public Button modProdSaveBtn;
    private String exceptionMessage = new String();
    public TextField modProdSearchTextField;
    public Button modProdSearchBtn;
    Product modifiedProduct;
    Inventory inventory;
    ObservableList<Part> modProdAssociatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();


    public void setInventory(Inventory inv) {
        this.inventory = inv;
    }

    //sets the text fields when called
    public void setProduct() {
        modProdIDTextField.setText(Integer.toString(modifiedProduct.getProdID()));
        modProdNameTextField.setText(modifiedProduct.getProdName());
        modProdInvTextField.setText(Integer.toString(modifiedProduct.getProdStock()));
        modProdPriceTextField.setText(Double.toString(modifiedProduct.getProdPrice()));
        modProdMinTextField.setText(Integer.toString(modifiedProduct.getProdMin()));
        modProdMaxTextField.setText(Integer.toString(modifiedProduct.getProdMax()));

    }

    @Override
    //gets the selected product from the main screen, calls set product, and displays
    //the parts/associated parts for that screen.
    //Displays the associated parts for the specific product selected
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifiedProduct = MainScreenController.getProductToNextScreen();
        setProduct();
        displayPartsTable();
        displayAssociatedPartsTable();
    }

    private void displayPartsTable () {
        partInventory.setAll(getPartInventory());
        partsTableView.setItems(partInventory);
        partsTableView.refresh();
    }

    private void displayAssociatedPartsTable () {
        associatedPartsTableView.setItems(modifiedProduct.getAssociatedParts());
        modProdAssociatedParts.setAll(modifiedProduct.getAssociatedParts());
        associatedPartsTableView.setItems(modProdAssociatedParts);
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
    //same functionality as the main screen search
    public void modProdSearchBtnHandler(ActionEvent event) {
        String searchText = modProdSearchTextField.getText();
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
        modProdSearchTextField.clear();
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
    //adds a part that is selected to the associated parts table
    //throws error if no part is selected to add when the button is pressed
    public void modProdAddBtnHandler(ActionEvent event) {
        Part part = partsTableView.getSelectionModel().getSelectedItem();
        if (partsTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error adding associated part");
            alert.setContentText("Please select an associated part to add");
            alert.showAndWait();
        }
        else {
            modProdAssociatedParts.add(part);
        }
    }
    //asks for confirmation and deletes an associated part from the associated parts table
    public void modProdDelBtnHandler(ActionEvent event) {
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
                modProdAssociatedParts.remove(selectedPart);
            }
            else {
                System.out.println("No longer deleting.");
            }
        }
    }
    //asks for confirmation and then goes back to the main screen
    public void modProdCancelBtnHandler(ActionEvent event) throws IOException {
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
            System.out.println("Continuing to modify...");
        }
    }
    //creates a new product and sets the attributes from the product that was selected
    //performs validation and makes sure all fields are entered.
    //adds associated parts that are selected to the list for the product
    public void modProdSaveBtnHandler(ActionEvent event) throws IOException {
        int prodID;
        String prodName;
        int prodStock;
        double prodPrice;
        int prodMin;
        int prodMax;

        try {
            prodID = Integer.parseInt(modProdIDTextField.getText());
            prodName = modProdNameTextField.getText();
            prodStock = Integer.parseInt(modProdInvTextField.getText());
            prodPrice = Double.parseDouble(modProdPriceTextField.getText());
            prodMin = Integer.parseInt(modProdMinTextField.getText());
            prodMax = Integer.parseInt(modProdMaxTextField.getText());

            //ensures that the name is entered
            if (modProdNameTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error modifying product");
                alert.setContentText("Please enter a name");
                alert.showAndWait();
            }
            else {
                //ensures that there is at least 1 associated part
                if (modProdAssociatedParts.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error adding product");
                    alert.setContentText("There must be at least 1 associated part");
                    alert.showAndWait();
                }
                else {
                    System.out.println("You are editing product " + prodName);
                    //validation for the product
                    exceptionMessage = Product.isProductValid(Integer.parseInt(String.valueOf(prodMin)), Integer.parseInt(String.valueOf(prodMax)), Integer.parseInt(String.valueOf(prodStock)), Double.parseDouble(String.valueOf(prodPrice)), exceptionMessage);
                    if (exceptionMessage.length() > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error modifying product");
                        alert.setContentText(exceptionMessage);
                        alert.showAndWait();
                        exceptionMessage = "";
                    } else if (exceptionMessage.length() == 0) {
                        productInventory.remove(modifiedProduct);
                        Product product = new Product(prodID, prodName, prodStock, prodPrice, prodMin, prodMax);
                        product.setProdID(prodID);
                        product.setProdName(prodName);
                        product.setProdStock(prodStock);
                        product.setProdPrice(prodPrice);
                        product.setProdMin(prodMin);
                        product.setProdMax(prodMax);
                        product.setAssociatedParts(modProdAssociatedParts);
                        productInventory.add(product);

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


        catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error modifying product");
            alert.setContentText("One or more fields is blank.");
            alert.showAndWait();
        }
    }
}
