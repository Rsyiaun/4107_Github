package SLC.TouchDisplayHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

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
        sendPickupCode.setText("");
        log.fine(id + ": mouse clicked: -- (" + x + ", " + y + ")");
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_MouseClicked, x + " " + y));
    } // td_mouseClick


    // td_ButtonClick
    public void td_PickBtnClick() {
        String BtnMsg = "Request PickParcel!";
        log.fine(id + ": Button clicked: -- (" + BtnMsg + ")");
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_ButtonClicked, BtnMsg));
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
    } // td_ButtonClick

    @FXML
    private TextField InputPickupCode;
    @FXML
    private Label sendPickupCode;
    //td_CodeInputOK, check if the pickup Code that customer input is correct
    @FXML
    public void td_CodeInputOK() {
        String code = InputPickupCode.getText();
        System.out.println(code);
        log.info(id + " sending pickup code: " + code + " to verify...");
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.CodeToVerify, code));
        log.info(id+": verify message sent...");
        sendPickupCode.setText("verifying pickup code,please wait...");
        // Msg msg = touchDisplayMBox.receive();
    }

    //to check the Barcode
    @FXML private Label NeedBarcodeMsg;
    @FXML public void td_ClickStore(){
        NeedBarcodeMsg.setText("Please scan the Barcode of the package first!");
        // Msg msg = touchDisplayMBox.receive();
    }
    @FXML private TextArea infoShow;
    @FXML private TextField AdminName;
    @FXML private TextField AdminPassword;
    @FXML public void td_LoginClicked(){
        touchDisplayMBox.send(new Msg(id,touchDisplayMBox,Msg.Type.Login,AdminName.getText()+","+AdminPassword.getText()));
        MBox touchDisplayMBox1 = appKickstarter.getThread("TouchDisplayHandler").getMBox();
        //Msg msg = touchDisplayMBox1.receive();
       // if(msg.getType().equals(Msg.Type.lockerinfo)){
            //if(msg.getDetails().equals("true Admin")) {
              //  infoShow.setText(msg.getDetails());
          //  }else if(msg.getDetails().equals("Fake Admin")){
            //   infoShow.setText("visit deny");
         //   }
      //  }
   }

    @FXML public void td_LogoutClicked(){
        infoShow.setText("");
    }




} // TouchDisplayEmulatorController
