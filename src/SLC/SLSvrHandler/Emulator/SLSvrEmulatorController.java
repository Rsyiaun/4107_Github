package SLC.SLSvrHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import SLC.BarcodeReaderDriver.Emulator.BarcodeReaderEmulator;
import SLC.LockerDriver.Emulator.LockerEmulatorController;
import SLC.SLC.SLC;
import SLC.TouchDisplayHandler.Emulator.TouchDisplayEmulator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.Timestamp;
import java.util.logging.Logger;

public class SLSvrEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private SLSvrEmulator SLSvrEmulator;
    private MBox SLSvrMBox;
    private String selectedScreen;
    private String pollResp;
    private MBox slc ;
    public ChoiceBox screenSwitcherCBox;
    public ChoiceBox pollRespCBox;



    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, SLSvrEmulator SLSvrEmulator, String pollRespParam) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.SLSvrEmulator = SLSvrEmulator;
        this.SLSvrMBox = appKickstarter.getThread("SLSvrHandler").getMBox();
        this.pollResp = pollRespParam;
        this.pollRespCBox.setValue(this.pollResp);
        this.pollRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pollResp = pollRespCBox.getItems().get(newValue.intValue()).toString();
            }
        });
        this.screenSwitcherCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                selectedScreen = screenSwitcherCBox.getItems().get(newValue.intValue()).toString();
                switch (selectedScreen) {
                    case "Blank":
                        SLSvrMBox.send(new Msg(id, SLSvrMBox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
                        break;

                    case "Main Menu":
                        SLSvrMBox.send(new Msg(id, SLSvrMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                        break;

                    case "Confirmation":
                        SLSvrMBox.send(new Msg(id,  SLSvrMBox, Msg.Type.TD_UpdateDisplay, "Confirmation"));
                        break;
                }
            }
        });
        this.selectedScreen = screenSwitcherCBox.getValue().toString();
        slc = appKickstarter.getThread("SLC").getMBox();


    } // initialize

    //------------------------------------------------------------
    // getSelectedScreen
    public String getSelectedScreen() {
        return selectedScreen;
    } // getSelectedScreen


    //------------------------------------------------------------
    // getPollResp
    public String getPollResp() {
        return pollResp;
    } // getPollResp

    //------------------------------------------------------------
    // buttonOnClickHandler
    public void buttonOnClickHandler(ActionEvent actionEvent){
        Button btn = (Button) actionEvent.getSource();
        switch (btn.getText()){
            case "System Diagnostic":
                slc.send(new Msg(id,SLSvrMBox, Msg.Type.SysDiagnostic, ""));
                break;
            case "System ShutDown":
                slc.send(new Msg(id,SLSvrMBox, Msg.Type.SysShutdown, ""));
                break;
            case "System Restart":
                slc.send(new Msg(id,SLSvrMBox, Msg.Type.SysRestart, ""));
                break;
        }



    }
    //------------------------------------------------------------
    // td_mouseClick
    public void td_mouseClick(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        log.fine(id + ": mouse clicked: -- (" + x + ", " + y + ")");
        SLSvrMBox.send(new Msg(id, SLSvrMBox, Msg.Type.TD_MouseClicked, x + " " + y));
    } // td_mouseClick
}
