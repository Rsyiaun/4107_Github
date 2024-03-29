package SLC;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;

import SLC.LockerDriver.LockerDriver;
import SLC.SLC.SLC;
import SLC.BarcodeReaderDriver.BarcodeReaderDriver;
import SLC.OctopusCardReaderDriver.OctopusCardReaderDriver;
import SLC.TouchDisplayHandler.TouchDisplayHandler;
import SLC.SLSvrHandler.SLSvrHandler;

import javafx.application.Platform;


//======================================================================
// SLCStarter
public class SLCStarter extends AppKickstarter {
    protected Timer timer;
    protected SLC slc;
    protected BarcodeReaderDriver barcodeReaderDriver;
    protected OctopusCardReaderDriver octopusReaderDriver;
    protected TouchDisplayHandler touchDisplayHandler;
	protected LockerDriver lockerDriver;
	protected SLSvrHandler slsvrHandler;

    //------------------------------------------------------------
    // main
    public static void main(String [] args) {
        new SLCStarter().startApp();
    } // main


    //------------------------------------------------------------
    // SLCStart
    public SLCStarter() {
	super("SLCStarter", "etc/SLC.cfg");
    } // SLCStart


    //------------------------------------------------------------
    // startApp
    protected void startApp() {
	// start our application
	log.info("");
	log.info("");
	log.info("============================================================");
	log.info(id + ": Application Starting...");

	startHandlers();
    } // startApp


    //------------------------------------------------------------
    // startHandlers
    protected void startHandlers() {
	// create handlers
	try {
	    timer = new Timer("timer", this);
	    slc = new SLC("SLC", this);
	    barcodeReaderDriver = new BarcodeReaderDriver("BarcodeReaderDriver", this);
	    octopusReaderDriver = new OctopusCardReaderDriver("OctopusCardReaderDriver",this);
	    touchDisplayHandler = new TouchDisplayHandler("TouchDisplayHandler", this);
	    lockerDriver = new LockerDriver("LockerDriver", this);
		slsvrHandler = new SLSvrHandler("SLSvrHandler",this);
	} catch (Exception e) {
	    System.out.println("AppKickstarter: startApp failed");
	    e.printStackTrace();
	    Platform.exit();
	}

	// start threads
	new Thread(timer).start();
	new Thread(slc).start();
	new Thread(barcodeReaderDriver).start();
	new Thread(octopusReaderDriver).start();
	new Thread(touchDisplayHandler).start();
	new Thread(lockerDriver).start();
	new Thread(slsvrHandler).start();
    } // startHandlers


    //------------------------------------------------------------
    // stopApp
    public void stopApp() {
//	log.info("");
//	log.info("");
//	log.info("============================================================");
//	log.info(id + ": Application Stopping...");
//
//	barcodeReaderDriver.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
//	octopusReaderDriver.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
//	touchDisplayHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
//	lockerDriver.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
//	timer.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
//	slsvrHandler.getMBox().send(new Msg(id,null,Msg.Type.Terminate,"Terminate now!"));
    } // stopApp
} // SLCStarter
