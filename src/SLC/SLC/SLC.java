package SLC.SLC;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;
import SLC.LockerDriver.Emulator.LockerEmulatorController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;
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
    private MBox SLSvrMBox;
    private MBox octCardReaderMBox;

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
        SLSvrMBox = appKickstarter.getThread("SLSvrHandler").getMBox();
        octCardReaderMBox = appKickstarter.getThread("OctopusCardReaderDriver").getMBox();


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
                    octCardReaderMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    lockerMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    SLSvrMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    break;

                case SysDiagnostic:

                    barcodeReaderMBox.send(new Msg(id, mbox, Msg.Type.SysDiagnostic, ""));
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.SysDiagnostic, ""));
                    lockerMBox.send(new Msg(id, mbox, Msg.Type.SysDiagnostic, ""));
                    octCardReaderMBox.send(new Msg(id, mbox, Msg.Type.SysDiagnostic, ""));

                    break;

                case PollAck:
                    log.info("PollAck: " + msg.getDetails());
                    break;

                case PollNak:
                    log.info("PollNak: " + msg.getDetails() + " Broken hardware need to be checked!");
                    break;

                case Terminate:
                    quit = true;

                    break;

                case TD_ButtonClicked:
                    log.info("ButtonCLicked: " + msg.getDetails());
                    try {
                        processButtonClicked(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CodeToVerify:
                    log.info("SLC receive customer's pickup code,start verify...");
                    try {
                        processCodeVerify(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case BR_BarcodeRead:
                    System.out.println("SLC got the Barcode form emulator,sending to SLSvr...");
                    SLSvrMBox.send(new Msg(id, mbox, Msg.Type.BR_BarcodeRead, msg.getDetails()));
                    break;

                case BarcodeVerify:
                    touchDisplayMBox.send(msg);
                    break;

                case OC_OctopusCardPaid:
                    octCardReaderMBox.send(msg);
                    break;
                case Login:
                    SLSvrMBox.send(msg);
                    break;
                case AdminVerify:
                    touchDisplayMBox.send(new Msg(id,mbox,Msg.Type.lockerinfo,msg.getDetails()));
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
    private void processButtonClicked(Msg msg) throws IOException {
        if (msg.getDetails().contains("Correct Barcode")) {
            String str = "0123456789";
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 6; i++) {
                int number = random.nextInt(10);
                sb.append(str.charAt(number));
            }
            String pickUpCode = sb.toString();
            String[] msgArray = msg.getDetails().split(",");
            String EmptyCabinetID = CabinetGroup1.getEmptyID(msgArray[2]);

            if (EmptyCabinetID != null) {
                CabinetGroup1.getCabinet(EmptyCabinetID).setOpenCode(pickUpCode);
                CabinetGroup1.getCabinet(EmptyCabinetID).setOpenStatus("open");
                CabinetGroup1.getCabinet(EmptyCabinetID).setBarcode(msgArray[1]);
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.PickupCodeMsg, "pickup code: " + pickUpCode + ", LockerID:" + EmptyCabinetID));
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                String StoreTime = String.valueOf(currentTime.getTime());
                CabinetGroup1.getCabinet(EmptyCabinetID).setStoreTime(StoreTime);
                setLockerProperty(EmptyCabinetID, StoreTime, pickUpCode, "open", msgArray[2]);
                log.info(id + ": success generate a pickup code, please put your parcel in the door:" + EmptyCabinetID);
            } else {
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.PickupCodeMsg, "full"));
                log.info(id + ": Sorry,all the cabinets are full...");
            }
        }
    } // processButtonClicked

    public void setLockerProperty(String lockerID, String time, String pickupCode, String openStatus, String size) throws IOException {
        Properties cfgProps1 = null;
        cfgProps1 = new Properties();
        FileInputStream in = new FileInputStream("etc/Locker.cfg");
        cfgProps1.load(in);
        in.close();
        String lockerKey = "Lockers.Locker" + lockerID;
        String refreshProperty = pickupCode + "-" + openStatus + "-" + time + "-" + size;
        System.out.println(refreshProperty);
        Object s = cfgProps1.setProperty(lockerKey, refreshProperty);
        FileOutputStream out = new FileOutputStream("etc/Locker.cfg");
        cfgProps1.store(out, "update locker data");
        if (s == null) {
            log.severe(id + ": getProperty(" + s + ") failed.  Check the config file etc/SLSvr.cfg!");
        }
    } // setProperty

    private void processCodeVerify(Msg msg) throws IOException {
        String MatchCabID = CabinetGroup1.findMatchCabinet(msg.getDetails());

        if (MatchCabID != null) {
            Cabinet cabinet = CabinetGroup1.getCabinet(MatchCabID);//match cabinet
            Timestamp storageTime = new Timestamp(Long.valueOf(cabinet.getStoreTime()));//get cabinet storage tme
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            Long hours = (currentTime.getTime() - storageTime.getTime()) / 1000 / 60 / 60;//calculate storage hours

            String OctopusPaid = String.valueOf(octCardReaderMBox.receive());

            log.info(id + ": pick up code correct! Verifying storage hours...");
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.CodeVerifyResult, "pick up code correct! Verifying storage hours..." + MatchCabID));
            if (hours > 24) {
                if (OctopusPaid.contains("Paid")) {
                    log.info(id + "please pick up your parcel at door:" + MatchCabID);
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.CodeVerifyResult, "Successfully Paid! please pick up your parcel at door:" + MatchCabID));
                    CabinetGroup1.getCabinet(MatchCabID).setOpenStatus("open");
                    CabinetGroup1.getCabinet(MatchCabID).setOpenCode("null");
                    CabinetGroup1.getCabinet(MatchCabID).setBarcode("null-null");
                    String size = CabinetGroup1.getCabinet(MatchCabID).getSize();
                    String PickTime = String.valueOf(currentTime.getTime());
                    setLockerProperty(MatchCabID, "null", "null", "open", size);

                    Properties cfgProps1 = null;
                    cfgProps1 = new Properties();
                    FileInputStream in = new FileInputStream("etc/SLSVr.cfg");
                    cfgProps1.load(in);
                    in.close();
                    int Num = Integer.parseInt(cfgProps1.getProperty("SLSvr.NumOfBarcode"));
                    for (int i = 1; i <= Num; i++) {
                        String[] array = cfgProps1.getProperty("SLSvr.Barcode" + i).split("-");
                        if ((array[0] + "-" + array[1]).equals(msg.getDetails())) {
                            String BarcodeToDelete = "SLSvr.Barcode" + i;
                            Object s = cfgProps1.setProperty(BarcodeToDelete, "null-" + array[2]);
                            if (s == null) {
                                log.severe(id + ": getProperty(" + s + ") failed.  Check the config file etc/SLSvr.cfg!");
                            }
                            return;
                        }
                    }
                    log.info(id + ": Please pay the overtime fee by Octopus Card!");
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.OC_OctopusCardPaid, "Please pay the overtime fee by Octopus Card!" + MatchCabID));
                }
            } else {
                log.info(id + "please pick up your parcel at door:" + MatchCabID);
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.CodeVerifyResult, "Successfully Paid! please pick up your parcel at door:" + MatchCabID));
                CabinetGroup1.getCabinet(MatchCabID).setOpenStatus("open");
                CabinetGroup1.getCabinet(MatchCabID).setOpenCode("null");
                CabinetGroup1.getCabinet(MatchCabID).setBarcode("null-null");
                String size = CabinetGroup1.getCabinet(MatchCabID).getSize();
                String PickTime = String.valueOf(currentTime.getTime());
                setLockerProperty(MatchCabID, "null", "null", "open", size);
                Properties cfgProps1 = null;
                cfgProps1 = new Properties();
                FileInputStream in = new FileInputStream("etc/SLSVr.cfg");
                cfgProps1.load(in);
                in.close();
                int Num = Integer.parseInt(cfgProps1.getProperty("SLSvr.NumOfBarcode"));
                for (int i = 1; i <= Num; i++) {
                    String[] array = cfgProps1.getProperty("SLSvr.Barcode" + i).split("-");
                    if ((array[0] + "-" + array[1]).equals(msg.getDetails())) {
                        String BarcodeToDelete = "SLSvr.Barcode" + i;
                        Object s = cfgProps1.setProperty(BarcodeToDelete, "null-" + array[2]);
                        if (s == null) {
                            log.severe(id + ": getProperty(" + s + ") failed.  Check the config file etc/SLSvr.cfg!");
                        }
                        return;
                    }
                }
            }

        } else {
            log.info(id + ":Wrong pick up code, please try again!");
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.CodeVerifyResult, "Wrong pick up code, please try again!"));
        }
    }

    private String getInfo() throws IOException {
        Properties cfgProps1 = null;
        cfgProps1 = new Properties();
        FileInputStream in = new FileInputStream("etc/Locker.cfg");
        cfgProps1.load(in);
        in.close();
        String info = "";
        for(int i=1;i<=40;i++){
            info = info+"Lockers.Locker"+i+" : "+cfgProps1.getProperty("Lockers.Locker"+i)+"/r/n";
        }
         return  info;
    }


} // SLC