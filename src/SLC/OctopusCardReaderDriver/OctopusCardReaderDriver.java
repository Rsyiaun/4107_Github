package SLC.OctopusCardReaderDriver;

import SLC.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import SLC.OctopusCardReaderDriver.Emulator.OctopusCardReaderEmulator;

import java.util.logging.Handler;

public class OctopusCardReaderDriver extends HWHandler {
    //------------------------------------------------------------
    // OctopusCardReaderDriver
    public OctopusCardReaderDriver(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    } // OctopusCardReaderDriver


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        SLSvrMBox = appKickstarter.getThread("SLSvrHandler").getMBox();
        switch (msg.getType()) {
            case SysShutdown:
                appKickstarter.unregThread(this);
                log.info(id + ": terminating...");
                break;
            case SysDiagnostic:
                SLSvrMBox.send(new Msg(id, mbox, Msg.Type.SysDiagnostic, "Octopus Card Reader "+ System.lineSeparator() +OctopusCardReaderEmulator.handleDiagnostic()));

                break;

            case OC_OctopusCardRead:
                slc.send(new Msg(id, mbox, Msg.Type.OC_OctopusCardRead, msg.getDetails()));
                break;

            case OC_OctopusCardPaid:
                slc.send(new Msg(id, mbox, Msg.Type.OC_OctopusCardPaid, msg.getDetails()));
                break;

            case OC_GoActive:
                handleGoActive();
                break;

            case OC_GoStandby:
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
} // OctopusCardReaderDriver