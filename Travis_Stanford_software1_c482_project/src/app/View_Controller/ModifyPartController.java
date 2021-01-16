package app.View_Controller;

import app.Model.InHousePart;
import app.Model.Inventory;
import app.Model.OutsourcedPart;
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
import app.Model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static app.Model.Inventory.partInventory;

public class ModifyPartController implements Initializable {
    @FXML private RadioButton modPartRadBtnIn;
    @FXML private RadioButton modPartRadBtnOut;
    @FXML
    private TextField modPartIDTextField;
    @FXML
    private TextField modPartNameTextField;
    @FXML
    private TextField modPartInvTextField;
    @FXML
    private TextField modPartPriceTextField;
    @FXML
    private TextField modPartMaxTextField;
    @FXML
    private TextField modPartMinTextField;
    @FXML
    private TextField modPartCNTextField;
    @FXML
    private TextField modPartMacIDTextField;
    @FXML
    Label lblInHouseOutsourcedSwap;
    Inventory inv;
    Part part;
    private String exceptionMessage = new String();
    private boolean outsourced;

    public ModifyPartController(Inventory inv, Part part) {
        this.inv = inv;
        this.part = part;
    }
    public ModifyPartController(){}
    //used to ensure that the company name is not a number
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    //asks for confirmation and then goes to the main screen
    private void cancelBtnHandler(ActionEvent event) throws IOException {

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

    //sets the text field values to the part that was selected on the main screen
    //also sets the label and selects the radio button depending on if the part is
    //in house or outsourced
    public void setPart(Part part) {
        this.part = part;
        if (part instanceof InHousePart) {
            modPartMacIDTextField.setText(Integer.toString(((InHousePart) part).getMachineID()));
            lblInHouseOutsourcedSwap.setText("Machine ID");
            outsourced = false;
            modPartRadBtnIn.setSelected(true);
        }
        else if (part instanceof OutsourcedPart) {
            modPartMacIDTextField.setText(((OutsourcedPart) part).getCompanyName());
            lblInHouseOutsourcedSwap.setText("Company Name");
            outsourced = true;
            modPartRadBtnOut.setSelected(true);
        }
        modPartIDTextField.setText(Integer.toString(part.getPartID()));
        modPartNameTextField.setText(part.getPartName());
        modPartInvTextField.setText(Integer.toString(part.getPartStock()));
        modPartPriceTextField.setText(Double.toString(part.getPartPrice()));
        modPartMinTextField.setText(Integer.toString(part.getPartMin()));
        modPartMaxTextField.setText(Integer.toString(part.getPartMax()));
    }
    //created a new in house or outsourced part depending on which radio button is selected
    //sets the attributes of the part
    //performs validation
    public void saveBtnHandler (ActionEvent event) throws IOException {
        int partID;
        String partName;
        int partStock;
        double partPrice;
        int partMin;
        int partMax;
        int machineID;
        String companyName;

        try {
            partID = Integer.parseInt(modPartIDTextField.getText());
            partName = modPartNameTextField.getText();
            partStock = Integer.parseInt(modPartInvTextField.getText());
            partPrice = Double.parseDouble(modPartPriceTextField.getText());
            partMin = Integer.parseInt(modPartMinTextField.getText());
            partMax = Integer.parseInt(modPartMaxTextField.getText());

            //ensures that a name is entered
            if (modPartNameTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error modifying part");
                alert.setContentText("Please enter a name");
                alert.showAndWait();
            }
            else {
                if (outsourced == false) {
                    machineID = Integer.parseInt(modPartMacIDTextField.getText());
                    System.out.println("You are editing in house " + partName);
                    //validation
                    exceptionMessage = InHousePart.isInPartValid(Integer.parseInt(String.valueOf(partMin)), Integer.parseInt(String.valueOf(partMax)), Integer.parseInt(String.valueOf(partStock)), Double.parseDouble(String.valueOf(partPrice)), exceptionMessage);
                    if (exceptionMessage.length() > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error modifying part");
                        alert.setContentText(exceptionMessage);
                        alert.showAndWait();
                        exceptionMessage = "";
                        //creates a new part and sets the attributes if validation is passed
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
                    //creates a new outsourced part
                } else if (outsourced == true) {
                    companyName = modPartMacIDTextField.getText();
                    System.out.println("You are editing outsourced " + partName);
                    exceptionMessage = OutsourcedPart.isOutPartValid(Integer.parseInt(String.valueOf(partMin)), Integer.parseInt(String.valueOf(partMax)), Integer.parseInt(String.valueOf(partStock)), Double.parseDouble(String.valueOf(partPrice)), exceptionMessage);
                    if (exceptionMessage.length() > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error modifying part");
                        alert.setContentText(exceptionMessage);
                        alert.showAndWait();
                        exceptionMessage = "";
                    }
                    else if (exceptionMessage.length() == 0){
                        if (isNumeric(companyName) == true) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error modifying part");
                            alert.setContentText("Company name not allowed to be a number");
                            alert.showAndWait();
                        }
                        else {
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
        }


            catch (NumberFormatException exception) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error modifying part");
                alert.setContentText("One or more fields is blank or incorrect type for machine ID");
                alert.showAndWait();
            }
    }

    public void inHouseBtnRadioHandler(ActionEvent event) {
        outsourced = false;
        lblInHouseOutsourcedSwap.setText("Machine ID");
        modPartMacIDTextField.setPromptText("Machine ID");
        modPartRadBtnOut.setSelected(false);
    }

    public void outSourceBtnRadioHandler(ActionEvent event) {
        outsourced = true;
        lblInHouseOutsourcedSwap.setText("Company Name");
        modPartMacIDTextField.setPromptText("Company Name");
        modPartRadBtnIn.setSelected(false);
    }
}
