package SLC.SLSvrHandler;

import SLC.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

//======================================================================
// SLSvrHandler
public class SLSvrHandler extends HWHandler{
    //------------------------------------------------------------
    // SLSvrHandler
    public SLSvrHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    } // SLSvrHandler

    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) throws IOException {
        switch (msg.getType()) {
            case SysDiagnostic:

                System.out.println(msg.getDetails());
                break;
            case BR_BarcodeRead:
                CheckBarcode(msg);
                break;

            case BR_GoActive:
                handleGoActive();
                break;

            case BR_GoStandby:
                handleGoStandby();
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg

    //------------------------------------------------------------
    // handleGoActive
    protected void handleGoActive() {
        log.info(id + ": Go Active");
    } // handleGoActive


    //------------------------------------------------------------
    // handleGoStandby
    protected void handleGoStandby() {
        log.info(id + ": Go Standby");
    } // handleGoStandby


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        log.info(id + ": Handle Poll");
    } // handlePoll

    protected void CheckBarcode(Msg msg) throws IOException {
        System.out.println("This is SLSvr, now start verify barcode...");
        int Num = Integer.parseInt(SLSvrGetProperty("SLSvr.NumOfBarcode"));
        Properties cfgProps1 = null;
        cfgProps1 = new Properties();
        FileInputStream in = new FileInputStream("etc/SLSVr.cfg");
        cfgProps1.load(in);
        in.close();
        for(int i=1;i<=Num;i++){
            String[] array = SLSvrGetProperty("SLSvr.Barcode"+i).split("-");
            if((array[0]+"-"+array[1]).equals(msg.getDetails())){
                String BarcodeToDelete = "SLSvr.Barcode"+i;
                slc.send(new Msg(id, mbox, Msg.Type.BarcodeVerify, ("Correct Barcode,"+array[0]+"-"+array[1]+","+array[2])));
                Object s = cfgProps1.setProperty(BarcodeToDelete,"null-null"+array[2]);
                FileOutputStream out = new FileOutputStream("etc/SLSvr.cfg");
                cfgProps1.store(out,"update barcode data");
                if (s == null) {
                    log.severe(id + ": getProperty(" + s + ") failed.  Check the config file etc/SLSvr.cfg!");
                }
                return;
            }
        }
        MBox touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
        slc.send(new Msg(id, mbox, Msg.Type.BarcodeVerify, "wrong barcode"));
    }

    public String SLSvrGetProperty(String property) throws IOException {
        Properties cfgProps1 = null;
        cfgProps1 = new Properties();
        FileInputStream in = new FileInputStream("etc/SLSvr.cfg");
        cfgProps1.load(in);
        in.close();
        String s = cfgProps1.getProperty(property);
        if (s == null) {
            log.severe(id + ": getProperty(" + property + ") failed.  Check the config file etc/SLSvr.cfg!");
        }
        return s;
    } // getProperty

}
