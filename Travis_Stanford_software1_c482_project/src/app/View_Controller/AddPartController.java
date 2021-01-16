package app.View_Controller;

import app.Model.InHousePart;
import app.Model.Inventory;
import app.Model.OutsourcedPart;
import app.Model.Part;
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

import static app.View_Controller.ModifyPartController.isNumeric;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static app.Model.Inventory.partInventory;

public class AddPartController implements Initializable {
    private int partID;
    private String exceptionMessage = new String();
    @FXML private TextField addPartIDTextField;
    @FXML private TextField addPartNameTextField;
    @FXML private TextField addPartInvTextField;
    @FXML private TextField addPartPriceTextField;
    @FXML private TextField addPartMinTextField;
    @FXML private TextField addPartMaxTextField;
    @FXML private TextField addPartMacIDTextField;
    @FXML private RadioButton addPartRadBtnIn;
    @FXML private RadioButton addPartRadBtnOut;
    Part part;
    private boolean outsourced;
    @FXML
    //this is the text label that is changed when in house or outsourced it selected
    Label lblInHouseOutsourcedSwap;
    @Override
    //increments the part ID and sets the part ID when the screen is initialized
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partID = Inventory.getPartIDInc();
    }

    //Creates a new part, sets the attributes of the part, and does the validation
    public void saveBtnHandler(ActionEvent event) throws IOException {
        String partName;
        int partStock;
        double partPrice;
        int partMin;
        int partMax;
        int machineID;
        String companyName;

        try {
            partName = addPartNameTextField.getText();
            partStock = Integer.parseInt(addPartInvTextField.getText());
            partPrice = Double.parseDouble(addPartPriceTextField.getText());
            partMin = Integer.parseInt(addPartMinTextField.getText());
            partMax = Integer.parseInt(addPartMaxTextField.getText());

            //Ensures that a part name is entered
            if (addPartNameTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error modifying part");
                alert.setContentText("Please enter a name");
                alert.showAndWait();
            } else {
                if (outsourced == false) {
                    machineID = Integer.parseInt(addPartMacIDTextField.getText());
                    System.out.println("You are editing in house " + partName);

                    //validation for the part
                    exceptionMessage = InHousePart.isInPartValid(Integer.parseInt(String.valueOf(partMin)), Integer.parseInt(String.valueOf(partMax)), Integer.parseInt(String.valueOf(partStock)), Double.parseDouble(String.valueOf(partPrice)), exceptionMessage);

                    //displays an error message if the validation is not passed
                    if (exceptionMessage.length() > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error modifying part");
                        alert.setContentText(exceptionMessage);
                        alert.showAndWait();
                        exceptionMessage = "";

                        //creates the part, sets the values, and goes to the main screen if the validation is passed
                    } else if (exceptionMessage.length() == 0) {
                        partInventory.remove(part);
                        InHousePart inHousePart = new InHousePart(partID, partName, partStock, partPrice, partMin, partMax, machineID);
                        partInventory.add(inHousePart);
                        inHousePart.setPartID(partID);
                        inHousePart.setPartName(partName);
                        inHousePart.setPartStock(partStock);
                        inHousePart.setPartPrice(partPrice);
                        inHousePart.setPartMin(partMin);
                        inHousePart.setPartMax(partMax);
                        inHousePart.setMachineID(machineID);
                        System.out.println("Part has machine ID/Company name: " + machineID);

                        Stage stage;
                        Parent root;
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View_Controller/MainScreen.fxml"));

                        root = loader.load();

                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                    //creates an outsourced part and sets the attributes if the outsourced button is selected
                    //validates the attributes
                } else if (outsourced == true) {
                    companyName = addPartMacIDTextField.getText();
                    System.out.println("You are editing outsourced " + partName);
                    //checks to make sure that the company name is not a number
                    if (isNumeric(companyName) == true) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error modifying part");
                        alert.setContentText("Company name not allowed to be a number");
                        alert.showAndWait();
                    } else {
                        //creates a new outsourced part and sets the attributes if the outsourced button is selected
                        //goes to the main screen on save
                        partInventory.remove(part);
                        OutsourcedPart outsourcedPart = new OutsourcedPart(partID, partName, partStock, partPrice, partMin, partMax, companyName);
                        partInventory.add(outsourcedPart);
                        outsourcedPart.setPartName(partName);
                        outsourcedPart.setPartStock(partStock);
                        outsourcedPart.setPartPrice(partPrice);
                        outsourcedPart.setPartMin(partMin);
                        outsourcedPart.setPartMax(partMax);
                        outsourcedPart.setCompanyName(companyName);

                        System.out.println("Part has machine ID/Company name: " + companyName);

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
        //ensures that all fields are filled and the machine ID is a number
        catch (NumberFormatException exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error modifying part");
            alert.setContentText("One or more fields is blank or incorrect type for machine ID");
            alert.showAndWait();
        }
    }
    //asks for confirmation and then goes back to the main screen
    public void cancelBtnHandler(ActionEvent event) throws IOException {
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
    //changes the label and prompt text if the in house radio button is selected
    //also deselects the outsourced button
    public void inHouseBtnRadioHandler(ActionEvent event) {
        outsourced = false;
        lblInHouseOutsourcedSwap.setText("Machine ID");
        addPartMacIDTextField.setPromptText("Machine ID");
        addPartRadBtnOut.setSelected(false);
    }
    //changes the label and prompt text if the outsourced radio button is selected
    //also deselects the in house button
    public void outSourceBtnRadioHandler(ActionEvent event) {
        outsourced = true;
        lblInHouseOutsourcedSwap.setText("Company Name");
        addPartMacIDTextField.setPromptText("Company Name");
        addPartRadBtnIn.setSelected(false);
    }
}
