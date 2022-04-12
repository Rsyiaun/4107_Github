package SLC.HWHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;

import java.io.IOException;


//======================================================================
// HWHandler
public abstract class HWHandler extends AppThread {
    protected MBox slc = null;
    protected  MBox touchDisplayMBox=null;
    protected  MBox SLSvrMBox = null;

    //------------------------------------------------------------
    // HWHandler
    public HWHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    } // HWHandler


    //------------------------------------------------------------
    // run
    public void run() {
        slc = appKickstarter.getThread("SLC").getMBox();
        log.info(id + ": starting...");

        for (boolean quit = false; !quit;) {
            Msg msg = mbox.receive();
            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case Poll:
                    handlePoll();
                    break;

                case Terminate:
                    quit = true;
                    break;

                default:
                    try {
                        processMsg(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    } // run


    //------------------------------------------------------------
    // abstract methods
    protected abstract void processMsg(Msg msg) throws IOException;
    protected abstract void handlePoll();
} // HWHandler
