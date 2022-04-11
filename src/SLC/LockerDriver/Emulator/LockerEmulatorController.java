package SLC.LockerDriver.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


//======================================================================
// BarcodeReaderEmulatorController
public class LockerEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private LockerEmulator lockerEmulator;
    private MBox barcodeReaderMBox;
    private String activationResp;
    private String standbyResp;
    private String pollResp;
    private ArrayList <Lockers> aL = new ArrayList<>();
    public TextField barcodeNumField;
    public TextField barcodeReaderStatusField;
    public TextArea barcodeReaderTextArea;
    public ChoiceBox standbyRespCBox;
    public ChoiceBox activationRespCBox;
    public ChoiceBox pollRespCBox;


    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, LockerEmulator lockerEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.lockerEmulator = lockerEmulator;
        this.barcodeReaderMBox = appKickstarter.getThread("LockerDriver").getMBox();
        this.activationRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                activationResp = activationRespCBox.getItems().get(newValue.intValue()).toString();
                appendTextArea("Activation Response set to " + activationRespCBox.getItems().get(newValue.intValue()).toString());
            }
        });
        this.standbyRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                standbyResp = standbyRespCBox.getItems().get(newValue.intValue()).toString();
                appendTextArea("Standby Response set to " + standbyRespCBox.getItems().get(newValue.intValue()).toString());
            }
        });
        this.pollRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pollResp = pollRespCBox.getItems().get(newValue.intValue()).toString();
                appendTextArea("Poll Response set to " + pollRespCBox.getItems().get(newValue.intValue()).toString());
            }
        });
        this.activationResp = activationRespCBox.getValue().toString();
        this.standbyResp = standbyRespCBox.getValue().toString();
        this.pollResp = pollRespCBox.getValue().toString();
        this.goStandby();
//
//        this.aL = (ArrayList<Lockers>) getLockerData(accessCode,SLid);

    } // initialize



    //------------------------------------------------------------
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {



            case "Reset":
                barcodeNumField.setText("");
                break;


            case "Activate/Standby":
                barcodeReaderMBox.send(new Msg(id, barcodeReaderMBox, Msg.Type.BR_GoActive, barcodeNumField.getText()));
                barcodeReaderTextArea.appendText("Removing card\n");
                break;

            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    } // buttonPressed

    public void lockerOnClickHandler(ActionEvent actionEvent){
        Button btn = (Button) actionEvent.getSource();
        int id  = Integer.parseInt(btn.getText());
        Lockers lk;
         lk = aL.get(id-1);
         appendTextArea("Locker: " + lk.id + "Access Code: "+lk.accessCode + "Status: " + lk.status + "Storage Time: " + lk.storageTime);
    }

    //------------------------------------------------------------
    // getters
    public String getActivationResp() { return activationResp; }
    public String getStandbyResp()    { return standbyResp; }
    public String getPollResp()       { return pollResp; }


    //------------------------------------------------------------
    // goActive
    public void goActive() {
        updateBarcodeReaderStatus("Active");
    } // goActive


    //------------------------------------------------------------
    // goStandby
    public void goStandby() {
        updateBarcodeReaderStatus("Standby");
    } // goStandby


    //------------------------------------------------------------
    // updateBarcodeReaderStatus
    private void updateBarcodeReaderStatus(String status) {
        barcodeReaderStatusField.setText(status);
    } // updateBarcodeReaderStatus

    //------------------------------------------------------------
    // LockerOpen-ChangeButtonColor
    public void changeLockerColor(){

    }

    //------------------------------------------------------------
    // appendTextArea
    public void appendTextArea(String status) {
        barcodeReaderTextArea.appendText(status+"\n");
    } // appendTextArea
    //------------------------------------------------------------
    // Create object of lockers
    public static class Lockers {
        String accessCode;
        String status;
        String storageTime;
        int id;

        Lockers(String accessCode, String status, String storageTime, int id){
            this.accessCode = accessCode;
            this.id = id;
            this.status = status;
            this.storageTime = storageTime;
        }
    }
    //------------------------------------------------------------
    // Generate a arraylist of locker var
    public  Object getLockerData(){
        String  accessCode ;
        String  storageTime;
        String  status = null;
        int  id;
        final int LockerSize = 40;
        ArrayList <Lockers> aL = new ArrayList<>();

        for(int i = 1 ; i <= LockerSize; i++){
            String lockerValue = "Lockers.Locker" + i;
            String lockerDetail = appKickstarter.getProperty(lockerValue);
            String [] detailSplit = lockerDetail.split("-");
            accessCode = detailSplit[0]; status = detailSplit[1]; storageTime = detailSplit[2];
            Lockers lk = new Lockers(accessCode, status,  storageTime, i-1 );
            aL.add(lk);
        }
        return aL;
    }


} // BarcodeReaderEmulatorController
