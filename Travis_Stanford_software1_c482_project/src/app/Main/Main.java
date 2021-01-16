package app.Main;

import app.Model.*;
import app.Model.InHousePart;
import app.Model.Inventory;
import app.Model.OutsourcedPart;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String [] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Inventory inv = new Inventory();
        addTestData(inv);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View_Controller/MainScreen.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }
    //adds sample test data to the inventory
    void addTestData(Inventory inv) {
        //Sample in house parts
        InHousePart motherboardM1 = new InHousePart(Inventory.getPartIDInc(), "Motherboard M1", 20, 150.00, 3, 50, 1111);
        InHousePart processorP1 = new InHousePart(Inventory.getPartIDInc(), "Processor P1", 20, 200.00, 3, 50, 1112);
        InHousePart storageS1 = new InHousePart(Inventory.getPartIDInc(), "Storage S1", 50, 80.00, 20, 100, 1113);
        inv.addPart(motherboardM1);
        inv.addPart(processorP1);
        inv.addPart(storageS1);

        //Sample outsourced parts
        OutsourcedPart daleMotherboardM2 = new OutsourcedPart(Inventory.getPartIDInc(), "Dale Motherboard", 10, 100.00, 2, 20, "Dale Computers");
        OutsourcedPart antelProcessorP2 = new OutsourcedPart(Inventory.getPartIDInc(), "Antel Processor", 10, 200.00, 2, 20, "Antel processors");
        OutsourcedPart sasungStorageS2 = new OutsourcedPart(Inventory.getPartIDInc(), "Sasung Storage", 10, 100.00, 2, 20, "Sansung Electronics");
        inv.addPart(daleMotherboardM2);
        inv.addPart(antelProcessorP2);
        inv.addPart(sasungStorageS2);

        //sample products
        Product computer1 = new Product(Inventory.getProdIDInc(), "Razor Computer", 10, 700.99, 2, 50);
        //adds associated parts to the product
        computer1.addAssociatedPart(motherboardM1);
        computer1.addAssociatedPart(processorP1);
        inv.addProduct(computer1);
        Product computer2 = new Product(Inventory.getProdIDInc(), "Alien Computer", 10, 900.99, 2, 50);
        computer2.addAssociatedPart(motherboardM1);
        computer2.addAssociatedPart(antelProcessorP2);
        computer2.addAssociatedPart(storageS1);
        inv.addProduct(computer2);
        Product computer3 = new Product(Inventory.getProdIDInc(), "Dragon Computer", 10, 1299.99, 2, 50);
        computer3.addAssociatedPart(daleMotherboardM2);
        computer3.addAssociatedPart(antelProcessorP2);
        computer3.addAssociatedPart(storageS1);
        inv.addProduct(computer3);
    }
}