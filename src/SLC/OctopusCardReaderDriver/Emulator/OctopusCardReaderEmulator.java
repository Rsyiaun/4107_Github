package SLC.OctopusCardReaderDriver.Emulator;

import AppKickstarter.misc.Msg;
import SLC.OctopusCardReaderDriver.Emulator.OctopusCardReaderEmulator;
import SLC.OctopusCardReaderDriver.Emulator.OctopusCardReaderEmulatorController;
import SLC.OctopusCardReaderDriver.OctopusCardReaderDriver;
import SLC.SLCStarter;
import SLC.OctopusCardReaderDriver.OctopusCardReaderDriver;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class OctopusCardReaderEmulator extends OctopusCardReaderDriver {
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private OctopusCardReaderEmulatorController octopusCardReaderEmulatorController;
    //Product Data
    private static String currentVersion = "3.14";
    private static String serialNumber = "PFAF0-99KMO-I0A5U-IJXHK-U1GPG";
    //------------------------------------------------------------
    // OctopusCardReaderEmulator
    public OctopusCardReaderEmulator(String id, SLCStarter slcStarter) {
        super(id, slcStarter);
        this.slcStarter = slcStarter;
        this.id = id;
    } // OctopusCardReaderEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "OctopusCardReaderEmulator.fxml";
        loader.setLocation(OctopusCardReaderEmulator.class.getResource(fxmlName));
        root = loader.load();
        octopusCardReaderEmulatorController = (OctopusCardReaderEmulatorController) loader.getController();
        octopusCardReaderEmulatorController.initialize(id, slcStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 350, 470));
        myStage.setTitle("OctopusCard Reader");
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            slcStarter.stopApp();
            Platform.exit();
        });
        myStage.show();
    } // OctopusCardReaderEmulator


    //------------------------------------------------------------
    // handleGoActive
    protected void handleGoActive() {
        // fixme
        super.handleGoActive();
        octopusCardReaderEmulatorController.appendTextArea("OctopusCard Reader Activated");

    } // handleGoActive


    //------------------------------------------------------------
    // handleGoStandby
    protected void handleGoStandby() {
        // fixme
        super.handleGoStandby();
        octopusCardReaderEmulatorController.appendTextArea("OctopusCard Reader Standby");
    } // handleGoStandby


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();

        switch (octopusCardReaderEmulatorController.getPollResp()) {
            case "ACK":
                slc.send(new Msg(id, mbox, Msg.Type.PollAck, id + " is up!"));
                break;

            case "NAK":
                slc.send(new Msg(id, mbox, Msg.Type.PollNak, id + " is down!"));
                break;

            case "Ignore":
                // Just ignore.  do nothing!!
                break;
        }
    } // handlePoll

    public void octopusCardPaid(Msg msg) {
        if (msg.getType() == Msg.Type.OC_OctopusCardPaid) {
            if (msg.getDetails().contains("Paid")) {
                System.out.println("Octopus Card Successfully Paid!");
                // NeedBarcodeMsg.setText("Correct Barcode!");
            } else {
                System.out.println("Payment Failed!");
                // NeedBarcodeMsg.setText("Can't find barcode in System!");
            }
        }
    }//Octopus Card Paid
    public static String handleDiagnostic() {
        return "Current Version : "+currentVersion + " SerialNumber : " + serialNumber;
    }
} // OctopusCardReaderEmulator

