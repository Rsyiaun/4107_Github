package SLC.SLSvrHandler.Emulator;


import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import SLC.BarcodeReaderDriver.Emulator.BarcodeReaderEmulator;
import SLC.BarcodeReaderDriver.Emulator.BarcodeReaderEmulatorController;
import SLC.SLCStarter;
import SLC.BarcodeReaderDriver.BarcodeReaderDriver;
import  SLC.SLSvrHandler.Emulator.SLSvrEmulator;
import  SLC.SLSvrHandler.Emulator.SLSvrEmulatorController;
import SLC.SLSvrHandler.SLSvrHandler;
import SLC.TouchDisplayHandler.Emulator.TouchDisplayEmulator;
import SLC.TouchDisplayHandler.Emulator.TouchDisplayEmulatorController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// SLSvrEmulator
public class SLSvrEmulator extends SLSvrHandler {
    private final int WIDTH = 680;
    private final int HEIGHT = 570;
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private SLSvrEmulatorController SLSvrEmulatorController;

    //------------------------------------------------------------
    // SLSvrEmulator
    public SLSvrEmulator(String id, SLCStarter slcStarter) {
        super(id, slcStarter);
        this.slcStarter = slcStarter;
        this.id = id;
    } // SLSvrEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
        // Parent root;
        myStage = new Stage();
        reloadStage("SLSvrEmulator.fxml");
        myStage.setTitle("SLSvr");
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
       SLSvrEmulator SLSvrEmulator = this;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info(id + ": loading fxml: " + fxmlFName);

                    // get the latest pollResp string, default to "ACK"
                    String pollResp = "ACK";
                    if (SLSvrEmulatorController != null) {
                        pollResp = SLSvrEmulatorController.getPollResp();
                    }

                    Parent root;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(SLSvrEmulator.class.getResource(fxmlFName));
                    root = loader.load();
                    SLSvrEmulatorController = (SLSvrEmulatorController) loader.getController();
                    SLSvrEmulatorController.initialize(id, slcStarter, log, SLSvrEmulator, pollResp);
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

        switch (SLSvrEmulatorController.getPollResp()) {
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

}
