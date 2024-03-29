package SLC.OctopusCardReaderDriver.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import java.util.logging.Logger;

import SLC.OctopusCardReaderDriver.Emulator.OctopusCardReaderEmulator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class OctopusCardReaderEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private OctopusCardReaderEmulator octopusCardReaderEmulator;
    private MBox octopusCardReaderMBox;
    private String activationResp;
    private String standbyResp;
    private String pollResp;
    public TextField octopusCardNumField;
    public TextField octopusCardCostField;
    public TextField octopusCardReaderStatusField;
    public TextArea octopusCardReaderTextArea;
    public ChoiceBox standbyRespCBox;
    public ChoiceBox activationRespCBox;
    public ChoiceBox pollRespCBox;


    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, OctopusCardReaderEmulator octopusCardReaderEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.octopusCardReaderEmulator = octopusCardReaderEmulator;
        this.octopusCardReaderMBox = appKickstarter.getThread("OctopusCardReaderDriver").getMBox();
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
    } // initialize


    //------------------------------------------------------------
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {
            case "OctopusCard 1":
                octopusCardNumField.setText(appKickstarter.getProperty("OctopusCardReader.OctopusCard1"));
                octopusCardCostField.setText(appKickstarter.getProperty("OctopusCardReader.OctopusCard1.cost1"));
                break;

            case "OctopusCard 2":
                octopusCardNumField.setText(appKickstarter.getProperty("OctopusCardReader.OctopusCard2"));
                octopusCardCostField.setText(appKickstarter.getProperty("OctopusCardReader.OctopusCard2.cost2"));
                break;

            case "OctopusCard 3":
                octopusCardNumField.setText(appKickstarter.getProperty("OctopusCardReader.OctopusCard3"));
                octopusCardCostField.setText(appKickstarter.getProperty("OctopusCardReader.OctopusCard3.cost3"));
                break;

            case "Reset":
                octopusCardNumField.setText("");
                octopusCardCostField.setText("");
                break;

            case "Send OctopusCard":
                octopusCardReaderMBox.send(new Msg(id, octopusCardReaderMBox, Msg.Type.OC_OctopusCardRead, octopusCardNumField.getText()));
                octopusCardReaderTextArea.appendText("Sending octopusCard " + octopusCardNumField.getText()+"\n");
                octopusCardReaderMBox.send(new Msg(id, octopusCardReaderMBox, Msg.Type.OC_OctopusCardRead, octopusCardCostField.getText()));
                octopusCardReaderTextArea.appendText("Sending octopusCard Cost" + octopusCardCostField.getText()+"\n");
                octopusCardReaderMBox.send(new Msg(id,octopusCardReaderMBox, Msg.Type.OC_OctopusCardPaid,id +"Paid!"));
                octopusCardReaderTextArea.appendText("Sending octopusCard Paid" +"\n");
                break;

            case "Activate/Standby":
                octopusCardReaderMBox.send(new Msg(id, octopusCardReaderMBox, Msg.Type.OC_GoActive, octopusCardNumField.getText()));
                octopusCardReaderMBox.send(new Msg(id, octopusCardReaderMBox, Msg.Type.OC_GoActive, octopusCardCostField.getText()));
                octopusCardReaderTextArea.appendText("Removing card\n");
                break;

            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    } // buttonPressed


    //------------------------------------------------------------
    // getters
    public String getActivationResp() { return activationResp; }
    public String getStandbyResp()    { return standbyResp; }
    public String getPollResp()       { return pollResp; }


    //------------------------------------------------------------
    // goActive
    public void goActive() {
        updateOctopusCardReaderStatus("Active");
    } // goActive


    //------------------------------------------------------------
    // goStandby
    public void goStandby() {
        updateOctopusCardReaderStatus("Standby");
    } // goStandby


    //------------------------------------------------------------
    // updateOctopusCardReaderStatus
    private void updateOctopusCardReaderStatus(String status) {
        octopusCardReaderStatusField.setText(status);
    } // updateOctopusCardReaderStatus


    //------------------------------------------------------------
    // appendTextArea
    public void appendTextArea(String status) {
        octopusCardReaderTextArea.appendText(status+"\n");
    } // appendTextArea

} // OctopusCardReaderEmulatorController