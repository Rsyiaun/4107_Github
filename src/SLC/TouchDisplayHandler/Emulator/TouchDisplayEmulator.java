package SLC.TouchDisplayHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import SLC.SLCStarter;
import SLC.TouchDisplayHandler.TouchDisplayHandler;
import AppKickstarter.misc.Msg;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// TouchDisplayEmulator
public class TouchDisplayEmulator extends TouchDisplayHandler {


    private final int WIDTH = 680;
    private final int HEIGHT = 570;
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private TouchDisplayEmulatorController touchDisplayEmulatorController;

    //------------------------------------------------------------
    // TouchDisplayEmulator
    public TouchDisplayEmulator(String id, SLCStarter slcStarter) throws Exception {
	super(id, slcStarter);
	this.slcStarter = slcStarter;
	this.id = id;

    } // TouchDisplayEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
	// Parent root;
	myStage = new Stage();
	reloadStage("TouchDisplayEmulator.fxml");
	myStage.setTitle("Touch Display");
	myStage.setResizable(false);
	myStage.setOnCloseRequest((WindowEvent event) -> {
	    slcStarter.stopApp();
	    Platform.exit();
	});
	myStage.show();
    } // TouchDisplayEmulator

    //------------------------------------------------------------
    // reloadStage
    private void reloadStage(String fxmlFName) {
        TouchDisplayEmulator touchDisplayEmulator = this;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info(id + ": loading fxml: " + fxmlFName);

                    // get the latest pollResp string, default to "ACK"
                    String pollResp = "ACK";
                    if (touchDisplayEmulatorController != null) {
                        pollResp = touchDisplayEmulatorController.getPollResp();
                    }

                    Parent root;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(TouchDisplayEmulator.class.getResource(fxmlFName));
                    root = loader.load();
                    touchDisplayEmulatorController = (TouchDisplayEmulatorController) loader.getController();
                    touchDisplayEmulatorController.initialize(id, slcStarter, log, touchDisplayEmulator, pollResp);
                    myStage.setScene(new Scene(root, WIDTH, HEIGHT));
                } catch (Exception e) {
                    log.severe(id + ": failed to load " + fxmlFName);
                    e.printStackTrace();
                }
            }
        });
    } // reloadStage



    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateDisplay(Msg msg) {
        log.info(id + ": update display -- " + msg.getDetails());

        switch (msg.getDetails()) {
            case "BlankScreen":
                reloadStage("TouchDisplayEmulator.fxml");
                break;

            case "MainMenu":
                reloadStage("TouchDisplayMainMenu.fxml");
                break;

            case "Confirmation":
                reloadStage("TouchDisplayConfirmation.fxml");
                break;

            default:
                log.severe(id + ": update display with unknown display type -- " + msg.getDetails());
                break;
        }
    } // handleUpdateDisplay


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();

        switch (touchDisplayEmulatorController.getPollResp()) {
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
    // if Barcode is right, this method will be called
    @FXML
    public void td_StoreBtnClick(String barcode) {
        String BtnMsg = "Request StoreParcel,"+barcode;
        log.fine(id + ": Button clicked: -- (" + BtnMsg + ")");
        MBox touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_ButtonClicked, BtnMsg));
        //Msg msg = touchDisplayMBox.receive();
    }

    @FXML public void ShowPickUpCodeMsg(Msg msg){
        if (msg.getType() == Msg.Type.PickupCodeMsg) {
            if (!msg.getDetails().equals("full")) {
                log.info(id + ": " + msg.getDetails());
                System.out.println("Your pickupcode: "+msg.getDetails());
                System.out.println("Here's the pickup code please remember!");
                System.out.println("Door is open, please put the package into it");
               // GeneratedCode.setText(msg.getDetails());
                //CodeGenerateMsg.setText("Here's the pickup code please remember!");
               // DoorOpenRemind.setText("Door is open, please put the package into it");
            } else if (msg.getDetails().equals("full")) {
                System.out.println("Sorry, the locker is full...");
                //CodeGenerateMsg.setText("Sorry, the locker is full...");
            }
        }
    }
    // if Barcode is right, this method will be called

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
       // Msg msg = touchDisplayMBox.receive();
    }

    @FXML public void ShowCodeVerifyResult(Msg msg){
        if (msg.getType() == Msg.Type.CodeVerifyResult) {
            System.out.println(msg.getDetails());
           // CodeVerifyResult.setText(msg.getDetails());
        }
    }

    //to check the Barcode
    @FXML private Label NeedBarcodeMsg;
    @FXML public void td_ClickStore(){
        System.out.println("Please scan the Barcode of the package first!");
       // NeedBarcodeMsg.setText("Please scan the Barcode of the package first!");
       // Msg msg = touchDisplayMBox.receive();
    }
    @FXML public void GetBarcodeChecked(Msg msg){
        if (msg.getType() == Msg.Type.BarcodeVerify){
            if(msg.getDetails().contains("Correct Barcode")){
                String[] msgArray = msg.getDetails().split(",");
                td_StoreBtnClick(msgArray[1]);
                System.out.println("Correct Barcode!");
               // NeedBarcodeMsg.setText("Correct Barcode!");
            }else if(msg.getDetails().equals("wrong barcode")){
                System.out.println("Can't find barcode in System!");
               // NeedBarcodeMsg.setText("Can't find barcode in System!");
            }
        }
    }



} // TouchDisplayEmulator
