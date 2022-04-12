package SLC.OctopusCardReaderDriver;

import SLC.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import javafx.scene.control.ChoiceBox;

import java.util.logging.Handler;

public class OctopusCardReaderDriver extends HWHandler {

    public ChoiceBox activationRespCBox;
    //------------------------------------------------------------
    // OctopusCardReaderDriver
    public OctopusCardReaderDriver(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    } // OctopusCardReaderDriver


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case SysDiagnostic:
                System.out.println();
                break;

            case OC_OctopusCardRead:
                slc.send(new Msg(id, mbox, Msg.Type.OC_OctopusCardRead, msg.getDetails()));
                break;

            case OC_OctopusCardPaid:
                octopusCardPaid(msg);
                //slc.send(new Msg(id, mbox, Msg.Type.OC_OctopusCardPaid, msg.getDetails()));
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

    //------------------------------------------------------------
    // Octopus Card Paid
    protected void octopusCardPaid(Msg msg){log.info(id+":Octopus payment status:"+msg.getDetails());}

} // OctopusCardReaderDriver