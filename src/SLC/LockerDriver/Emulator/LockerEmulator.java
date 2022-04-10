package SLC.LockerDriver.Emulator;

import AppKickstarter.misc.Msg;
import SLC.SLCStarter;
import SLC.LockerDriver.LockerDriver;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// LockerEmulator
public class LockerEmulator extends LockerDriver {
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private LockerEmulatorController lockerEmulatorController;

    //------------------------------------------------------------
    // LockerEmulator
    public LockerEmulator(String id, SLCStarter slcStarter) {
        super(id, slcStarter);
        this.slcStarter = slcStarter;
        this.id = id;
    } // BarcodeReaderEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "LockerEmulator.fxml";
        loader.setLocation(LockerEmulator.class.getResource(fxmlName));
        root = loader.load();
        lockerEmulatorController = (LockerEmulatorController) loader.getController();
        lockerEmulatorController.initialize(id, slcStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 700, 1000));
        myStage.setTitle("Locker");
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            slcStarter.stopApp();
            Platform.exit();
        });
        myStage.show();
    } // BarcodeReaderEmulator


    //------------------------------------------------------------
    // handleGoActive
    protected void handleGoActive() {
        // fixme
        super.handleGoActive();
        lockerEmulatorController.appendTextArea("Barcode Reader Activated");
    } // handleGoActive


    //------------------------------------------------------------
    // handleGoStandby
    protected void handleGoStandby() {
        // fixme
        super.handleGoStandby();
        lockerEmulatorController.appendTextArea("Barcode Reader Standby");
    } // handleGoStandby


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();

        switch (lockerEmulatorController.getPollResp()) {
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
} // BarcodeReaderEmulator
