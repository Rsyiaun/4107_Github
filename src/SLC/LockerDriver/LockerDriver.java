package SLC.LockerDriver;

import SLC.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import SLC.LockerDriver.Emulator.LockerEmulator;
import SLC.OctopusCardReaderDriver.Emulator.OctopusCardReaderEmulator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


//======================================================================
// LockerDriver
public class LockerDriver extends HWHandler {
    //------------------------------------------------------------
    // LockerDriver
    public LockerDriver(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    } // LockerDriver


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        SLSvrMBox = appKickstarter.getThread("SLSvrHandler").getMBox();
        switch (msg.getType()) {
            case SysDiagnostic:
                Properties prop = new Properties();
                String fileName = "SLC.config"; //lockerFile
                try (FileInputStream fis = new FileInputStream(fileName)) {
                    prop.load(fis);
                } catch (FileNotFoundException ex) {
                        // FileNotFoundException catch is optional and can be collapsed
                } catch (IOException ex) {

                }

                System.out.println(prop.getProperty("app.name"));
                System.out.println(prop.getProperty("app.version"));


                SLSvrMBox.send(new Msg(id, mbox, Msg.Type.SysDiagnostic, "Locker " + System.lineSeparator() +  LockerEmulator.handleDiagnostic() + System.lineSeparator() +
                "Locker status" + System.lineSeparator()

                ));

                break;

            case Terminate:
                break;
            case LK_GoActive:
                handleGoActive();
                break;

            case LK_GoStandby:
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
} // BarcodeReaderDriver
