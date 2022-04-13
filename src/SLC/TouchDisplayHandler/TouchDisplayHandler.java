package SLC.TouchDisplayHandler;

import SLC.BarcodeReaderDriver.Emulator.BarcodeReaderEmulator;
import SLC.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import SLC.TouchDisplayHandler.Emulator.TouchDisplayEmulator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


//======================================================================
// TouchDisplayHandler
public class TouchDisplayHandler extends HWHandler {
    //------------------------------------------------------------
    // TouchDisplayHandler
    public TouchDisplayHandler(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);
    } // TouchDisplayHandler


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        SLSvrMBox = appKickstarter.getThread("SLSvrHandler").getMBox();
        switch (msg.getType()) {
            case SysDiagnostic:

                SLSvrMBox.send(new Msg(id, mbox, Msg.Type.SysDiagnostic, "TouchDisplay " + System.lineSeparator()+TouchDisplayEmulator.handleDiagnostic()));

                break;
            case TD_MouseClicked:
                slc.send(new Msg(id, mbox, Msg.Type.TD_MouseClicked, msg.getDetails()));
                break;
            case TD_ButtonClicked:
                slc.send(new Msg(id,mbox,Msg.Type.TD_ButtonClicked,msg.getDetails()));
                break;
            case PickupCode:
                handlePickupCodeDisplay(msg);
                break;
            case TD_UpdateDisplay:
                handleUpdateDisplay(msg);
                break;
            case CodeToVerify:
                slc.send(new Msg(id,mbox,Msg.Type.CodeToVerify,msg.getDetails()));
                break;
            case CodeVerifyResult:
               ShowCodeVerifyResult(msg);
                break;
            case PickupCodeMsg:
               ShowPickUpCodeMsg(msg);
                break;
            case BarcodeVerify:
                GetBarcodeChecked(msg);
                break;
            case SysShutdown:
                appKickstarter.unregThread(this);
                log.info(id + ": terminating...");
                break;
            case Login:
                slc.send(msg);
                log.info("try to login...");
                break;
            case AdminVerify:
                System.out.println(msg.getDetails());
                break;
            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg




    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateDisplay(Msg msg) {
        log.info(id + ": update display -- " + msg.getDetails());
    } // handleUpdateDisplay


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        log.info(id + ": Handle Poll");
    } // handlePoll


    //------------------------------------------------------------
    // handlePickupCodeDisplay
    public void handlePickupCodeDisplay(Msg msg) {
        log.info(id + ": pickupCode display : " + msg.getDetails());
    } // handlePickupCodeDisplay

    public void GetBarcodeChecked(Msg msg) {
log.info(id+":SLSvr already checked the barcode:"+msg.getDetails());
    }

    public void ShowPickUpCodeMsg(Msg msg) {
    }
    void ShowCodeVerifyResult(Msg msg) {

   System.out.println("receive Barcode checking result from server: "+msg.getDetails());
    }



} // TouchDisplayHandler
