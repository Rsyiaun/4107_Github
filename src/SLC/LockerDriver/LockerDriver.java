package SLC.LockerDriver;

import SLC.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import SLC.LockerDriver.Emulator.LockerEmulator;
import SLC.OctopusCardReaderDriver.Emulator.OctopusCardReaderEmulator;
import SLC.SLC.SLC;
import SLC.SLCEmulatorStarter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
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
                String fileName = "etc/Locker.cfg"; //lockerFile
                try (FileInputStream fis = new FileInputStream(fileName)) {
                    prop.load(fis);
                } catch (FileNotFoundException ex) {
                        // FileNotFoundException catch is optional and can be collapsed
                } catch (IOException ex) {

                }
                String lockerStatus="";
                for(int i= 1 ; i<= Integer.parseInt(prop.getProperty("Lockers.NumOfLocker")); i++){
                    String [] value = prop.getProperty("Lockers.Locker"+String.valueOf(i)).split("-");

                    if (value[2].equals("null") ){
                        lockerStatus += i + "                        " + value[0] + "           " + value[1]  +"               " + "null" + System.lineSeparator() ;

                    }else{
                        Timestamp storageTime = new Timestamp(Long.valueOf(value[2]));
                        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                        Long hours = (currentTime.getTime()-storageTime.getTime()  ) / 1000/60/60 ;
                        lockerStatus += i + "                        " + value[0] + "           " + value[1]  +"               " + hours + System.lineSeparator() ;
                    }

                }



                SLSvrMBox.send(new Msg(id, mbox, Msg.Type.SysDiagnostic, "Locker " + System.lineSeparator() +  LockerEmulator.handleDiagnostic() + System.lineSeparator() +
                "Locker status" + System.lineSeparator()+
                "Locker ID              PickUp Code          Status        Storage Time(Hour/s)" + System.lineSeparator()
                +lockerStatus
                ));

                break;

            case SysShutdown:
                appKickstarter.unregThread(this);

                log.info(id + ": terminating...");

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
