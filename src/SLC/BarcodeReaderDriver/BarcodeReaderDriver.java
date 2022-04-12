package SLC.BarcodeReaderDriver;

import SLC.BarcodeReaderDriver.Emulator.BarcodeReaderEmulator;
import SLC.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import SLC.OctopusCardReaderDriver.Emulator.OctopusCardReaderEmulator;


//======================================================================
// BarcodeReaderDriver
public class BarcodeReaderDriver extends HWHandler {
    //------------------------------------------------------------
    // BarcodeReaderDriver
    public BarcodeReaderDriver(String id, AppKickstarter appKickstarter) {
	super(id, appKickstarter);
    } // BarcodeReaderDriver


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        SLSvrMBox = appKickstarter.getThread("SLSvrHandler").getMBox();
        switch (msg.getType()) {
            case SysDiagnostic:

                SLSvrMBox.send(new Msg(id, mbox, Msg.Type.SysDiagnostic, "Barcode Reader " + System.lineSeparator() +  BarcodeReaderEmulator.handleDiagnostic()));

                break;
            case BR_BarcodeRead:
                slc.send(new Msg(id, mbox, Msg.Type.BR_BarcodeRead, msg.getDetails()));
                break;

            case BR_GoActive:
                handleGoActive();
                break;

            case BR_GoStandby:
                handleGoStandby();
                break;
            case SysShutdown:
                appKickstarter.unregThread(this);
                log.info(id + ": terminating...");
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
} // BarcodeReaderDriver
