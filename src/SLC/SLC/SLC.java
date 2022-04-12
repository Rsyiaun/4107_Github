package SLC.SLC;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;

import java.util.Random;


//======================================================================
// SLC
public class SLC extends AppThread {
    private int pollingTime;
    private MBox barcodeReaderMBox;
    private MBox touchDisplayMBox;
    private MBox touchDisplayMBox1;
    private MBox lockerMBox;
    private CabinetGroup CabinetGroup1;

    //------------------------------------------------------------
    // SLC
    public SLC(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);
        pollingTime = Integer.parseInt(appKickstarter.getProperty("SLC.PollingTime"));
        CabinetGroup1 = new CabinetGroup();
    } // SLC


    //------------------------------------------------------------
    // run
    public void run() {
        Timer.setTimer(id, mbox, pollingTime);
        log.info(id + ": starting...");

        barcodeReaderMBox = appKickstarter.getThread("BarcodeReaderDriver").getMBox();
        touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
        touchDisplayMBox1 = appKickstarter.getThread("TouchDisplayHandler").getMBox();
        lockerMBox = appKickstarter.getThread("LockerDriver").getMBox();








        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case TD_MouseClicked:
                    log.info("MouseCLicked: " + msg.getDetails());
                    processMouseClicked(msg);
                    break;

                case TimesUp:
                    Timer.setTimer(id, mbox, pollingTime);
                    log.info("Poll: " + msg.getDetails());
                    barcodeReaderMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    lockerMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    break;
                case Diagnostic:

                    break;
                case PollAck:
                    log.info("PollAck: " + msg.getDetails());
                    break;

                case Terminate:
                    quit = true;

                    break;

                case TD_ButtonClicked:
                    log.info("ButtonCLicked: " + msg.getDetails());
                    processButtonClicked(msg);
                    break;
                case CodeToVerify:
                    log.info("SLC receive customer's pickup code,start verify...");
                    processCodeVerify(msg);
                    break;

                default:
                    log.warning(id + ": unknown message type: [" + msg + "]");
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    } // run


    //------------------------------------------------------------
    // processMouseClicked
    private void processMouseClicked(Msg msg) {
        // *** process mouse click here!!! ***
    } // processMouseClicked

    //------------------------------------------------------------
    // processButtonClicked
    private void processButtonClicked(Msg msg) {
        if (msg.getDetails().equals("Request StoreParcel!")) {
            String str = "0123456789";
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 6; i++) {
                int number = random.nextInt(10);
                sb.append(str.charAt(number));
            }
            String pickUpCode = sb.toString();
            String EmptyCabinetID = CabinetGroup1.getEmptyID();
            if (EmptyCabinetID != null) {
                CabinetGroup1.getCabinet(EmptyCabinetID).setOpenCode(pickUpCode);
                CabinetGroup1.getCabinet(EmptyCabinetID).setEmptyStatus(false);
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.PickupCodeMsg, "pickup code: "+pickUpCode+", LockerID:"+EmptyCabinetID));
                log.info(id + ": success generate a pickup code, please put your parcel in the door:" + EmptyCabinetID);
            } else {
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.PickupCodeMsg, "full"));
                log.info(id + ": Sorry,all the cabinets are full...");
            }
        }
    } // processButtonClicked

    private void processCodeVerify(Msg msg) {
        String MatchCabID = CabinetGroup1.findMatchCabinet(msg.getDetails());
        if (MatchCabID != null) {
            log.info(id + ": pick up code correct! please pick up your parcel at door:" + MatchCabID);
            CabinetGroup1.getCabinet(MatchCabID).setEmptyStatus(true);
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.CodeVerifyResult,  "pick up code correct! please pick up your parcel at door:" + MatchCabID));
        }else{
            log.info(id+":Wrong pick up code, please try again!");
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.CodeVerifyResult,"Wrong pick up code, please try again!"));
        }
    }



} // SLC
