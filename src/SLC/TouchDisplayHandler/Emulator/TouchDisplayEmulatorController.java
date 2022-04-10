package SLC.TouchDisplayHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;


//======================================================================
// TouchDisplayEmulatorController
public class TouchDisplayEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private TouchDisplayEmulator touchDisplayEmulator;
    private MBox touchDisplayMBox;
    private String selectedScreen;
    private String pollResp;
    public ChoiceBox screenSwitcherCBox;
    public ChoiceBox pollRespCBox;



    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, TouchDisplayEmulator touchDisplayEmulator, String pollRespParam) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.touchDisplayEmulator = touchDisplayEmulator;
        this.touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
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
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
                        break;

                    case "Main Menu":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                        break;

                    case "Confirmation":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "Confirmation"));
                        break;
                }
            }
        });
        this.selectedScreen = screenSwitcherCBox.getValue().toString();

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
    // td_mouseClick
    public void td_mouseClick(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        log.fine(id + ": mouse clicked: -- (" + x + ", " + y + ")");
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_MouseClicked, x + " " + y));
    } // td_mouseClick

    // td_ButtonClick
    @FXML
    private Label GeneratedCode;
    @FXML
    private Label DoorOpenRemind;
    @FXML
    private Label CodeGenerateMsg;

    @FXML
    public void td_StoreBtnClick() {
        String BtnMsg = "Request StoreParcel!";
        log.fine(id + ": Button clicked: -- (" + BtnMsg + ")");
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_ButtonClicked, BtnMsg));
        System.out.println("alalallaalalal");
        Msg msg = touchDisplayMBox.receive();
        if (msg.getType() == Msg.Type.PickupCodeMsg) {
            if (!msg.getDetails().equals("full")) {
                log.info(id + ": " + msg.getDetails());
                GeneratedCode.setText(msg.getDetails());
                CodeGenerateMsg.setText("Here's the pickup code please remember!");
                DoorOpenRemind.setText("Door is open, please put the package into it");
            } else if (msg.getDetails().equals("full")) {
                CodeGenerateMsg.setText("Sorry, the locker is full...");
            }
        }
    } // td_ButtonClick

    // td_ButtonClick
    public void td_PickBtnClick() {
        String BtnMsg = "Request PickParcel!";
        log.fine(id + ": Button clicked: -- (" + BtnMsg + ")");
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_ButtonClicked, BtnMsg));
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
    } // td_ButtonClick

    //td_getPickupCode

    @FXML
    private TextField InputPickupCode;
    @FXML
    private Label CodeVerifyResult;
    //td_CodeInputOK, check if the pickup Code that customer input is correct
    @FXML
    public void td_CodeInputOK() {
        String code = InputPickupCode.getText();
        System.out.println(code);
        log.info(id + " sending pickup code: " + code + " to verify...");
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.CodeToVerify, code));
        log.info(id+": verify message sent...");
        Msg msg = touchDisplayMBox.receive();
        if (msg.getType() == Msg.Type.CodeVerifyResult) {
            CodeVerifyResult.setText(msg.getDetails());
        }
    }


} // TouchDisplayEmulatorController
