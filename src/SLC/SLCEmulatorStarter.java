package SLC;

import AppKickstarter.timer.Timer;

import SLC.LockerDriver.Emulator.LockerEmulator;
import SLC.LockerDriver.LockerDriver;
import SLC.SLC.SLC;
import SLC.BarcodeReaderDriver.BarcodeReaderDriver;
import SLC.BarcodeReaderDriver.Emulator.BarcodeReaderEmulator;
import SLC.OctopusCardReaderDriver.Emulator.OctopusCardReaderEmulator;
import SLC.OctopusCardReaderDriver.OctopusCardReaderDriver;
import SLC.TouchDisplayHandler.Emulator.TouchDisplayEmulator;
import SLC.TouchDisplayHandler.TouchDisplayHandler;
import SLC.SLSvrHandler.Emulator.SLSvrEmulator;
import SLC.SLSvrHandler.SLSvrHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


//======================================================================
// SLCEmulatorStarter
public class SLCEmulatorStarter extends SLCStarter {
    //------------------------------------------------------------
    // main
    public static void main(String [] args) {
	new SLCEmulatorStarter().startApp();
    } // main


    //------------------------------------------------------------
    // startHandlers
    @Override
    protected void startHandlers() {
        Emulators.slcEmulatorStarter = this;
        new Emulators().start();
    } // startHandlers


    //------------------------------------------------------------
    // Emulators
    public static class Emulators extends Application {
        private static SLCEmulatorStarter slcEmulatorStarter;

	//----------------------------------------
	// start
        public void start() {
            launch();
	} // start

	//----------------------------------------
	// start
        public void start(Stage primaryStage) {
	    Timer timer = null;
	    SLC slc = null;
	    BarcodeReaderEmulator barcodeReaderEmulator = null;
	    OctopusCardReaderEmulator octopusCardReaderEmulator = null;
	    TouchDisplayEmulator touchDisplayEmulator = null;
            LockerEmulator lockerEmulator = null;
          SLSvrEmulator SLSvrEmulator = null;
	    // create emulators
	    try {
	        timer = new Timer("timer", slcEmulatorStarter);
	        slc = new SLC("SLC", slcEmulatorStarter);
	        barcodeReaderEmulator = new BarcodeReaderEmulator("BarcodeReaderDriver", slcEmulatorStarter);
		octopusCardReaderEmulator = new OctopusCardReaderEmulator("OctopusCardReaderDriver", slcEmulatorStarter);
	        touchDisplayEmulator = new TouchDisplayEmulator("TouchDisplayHandler", slcEmulatorStarter);
            lockerEmulator = new LockerEmulator("LockerDriver", slcEmulatorStarter);
          SLSvrEmulator = new SLSvrEmulator("SLSvrHandler",slcEmulatorStarter);
		// start emulator GUIs
                barcodeReaderEmulator.start();
		octopusCardReaderEmulator.start();
		touchDisplayEmulator.start();
        lockerEmulator.start();
       SLSvrEmulator.start();
	    } catch (Exception e) {
		System.out.println("Emulators: start failed");
		e.printStackTrace();
		Platform.exit();
	    }
	    slcEmulatorStarter.setTimer(timer);
	    slcEmulatorStarter.setSLC(slc);
	    slcEmulatorStarter.setBarcodeReaderDriver(barcodeReaderEmulator);
	    slcEmulatorStarter.setOctopusCardReaderDriver(octopusCardReaderEmulator);
	    slcEmulatorStarter.setTouchDisplayHandler(touchDisplayEmulator);
        slcEmulatorStarter.setLockerDriver(lockerEmulator);
       slcEmulatorStarter.setSLSvrHandler(SLSvrEmulator);
	    // start threads
	    new Thread(timer).start();
	    new Thread(slc).start();
            new Thread(barcodeReaderEmulator).start();
	    new Thread(octopusCardReaderEmulator).start();
	    new Thread(touchDisplayEmulator).start();
            new Thread(lockerEmulator).start();
           new Thread(SLSvrEmulator).start();
	} // start
    } // Emulators


    //------------------------------------------------------------
    //  setters
    private void setTimer(Timer timer) {
        this.timer = timer;
    }
    private void setSLC(SLC slc) {
        this.slc = slc;
    }
    private void setLockerDriver(LockerDriver lockerEmulator) {
        this.lockerDriver = lockerEmulator;
    }
    private void setBarcodeReaderDriver(BarcodeReaderDriver barcodeReaderDriver) {
        this.barcodeReaderDriver = barcodeReaderDriver;
    }
    private void setOctopusCardReaderDriver(OctopusCardReaderDriver octopusCardReaderDriver){
        this.octopusReaderDriver = octopusCardReaderDriver;
    }
    private void setTouchDisplayHandler(TouchDisplayHandler touchDisplayHandler) {
        this.touchDisplayHandler = touchDisplayHandler;
    }
    private void setSLSvrHandler(SLSvrHandler SLSvrHandler) {
        this.slsvrHandler = SLSvrHandler;
    }

} // SLCEmulatorStarter
