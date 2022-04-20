# COMP4107 Group2 SLC Project <br>
## 1. Project directory <br>
* README.md <br>
* .idea <br>
  * codeStyles <br>
    * codeStyleConfig.xml <br>
    * Project.xml <br>
  * .gitignore <br>
  * aws.xml <br>
  * misc.xml <br>
  * modules.xml <br>
  * vcs.xml <br>
  * workspace.xml <br>
* etc <br>
  * Locker.cfg <br>
  * SLC.cfg <br>
  * SLC.SLCEmulatorStarter.log <br>
  * SLSvr.cfg <br>
* slc <br>
  * AppKickstarter <br>
    * misc <br>
      * AppThread.java <br>
      * Lib.java <br>
      * LogFormatter.java <br>
      * MBox.java <br>
      * Msg.java <br>
    * timer <br>
      * Timer.java <br>
    * AppKickstarter.java <br>
  * SLC <br>
    * BarcodeReaderDriver <br>
      * Emulator <br>
        * BarcodeReaderEmulator.fxml <br>
        * BarcodeReaderEmulator.java <br>
        * BarcodeReaderEmulatorController.java <br>
      * BarcodeReaderDriver.java <br>
    * HWHandler <br>
      * HWHandler.java <br>
    * LockerDriver <br>
      * Emulator <br>
        * LockerEmulator.java <br>
        * LockerEmulator.fxml <br>
        * LockerEmulatorController.java <br>
      * LockerDriver.java <br>
    * OctopusCardReaderDriver <br>
      * Emulator <br>
        * OctopusCardReaderEmulator.java <br>
        * OctopusCardReaderEmulator.fxml <br>
        * OctopusCardReaderEmulatorController.java <br>
      * OctopusCardReaderDriver.java <br>
    * SLC <br>
      * Cabinet.java <br>
      * CabinetGroup.java <br>
      * SLC.java <br>
    * SLSvrHandler <br>
      * Emulator <br>
        * SLSvrEmulator.java <br>
        * SLSvrEmulator.fxml <br>
        * SLSvrEmulatorController.java <br>
      * SLSvrHandler.java <br>
    * TouchDisplayHandler <br>
      * Emulator <br>
        * TouchDisplayConfirmation.fxml <br>
        * TouchDisplayEmulator.java <br>
        * TouchDisplayEmulator.fxml <br>
        * TouchDisplayEmulatorController.java <br>
        * TouchDisplayMainMenu.fxml <br>
      * TouchDisplayHandler.java <br>
    * SLCEmulatorStarter.java <br>
    * SLCStarter.java <br>
* SLCStarter.iml <br>
* .gitignore <br>

## 2. Install, run and test <br>
* Download <br>
For our group project, first download from github or open the project through url on IntelliJ. <br>
URL: (https://github.com/Rsyiaun/4107_Github.git) <br>
* Copy <br>
After download the package, first please make a copy for the zip file of our project. <br>
Please copy the config file before testing and running. <br>
Then you can use the copy file to test the project. <br>
* Run and test <br>
Try to run or test our program on IntelliJ through right click SLCEmulatorStarter.java file and choose Run in the right click menu. <br> 
* Get the actual result. <br>
Get the run or test result and take screen shots. <br>

## 3. Simple Workflow Introduction <br>
First, the employee selects "Store Package" on the screen of the smart locker and aligns the barcode on the package with the barcode reader. The reader reads the barcode information and uploads it to the server for matching. If the match is successful, an empty express cabinet will be opened and a pickup code will be generated at the same time. The pickup code will be sent to the server. When the user receives the pickup code, he comes to pick up the item. The user first selects the "Pick Up Package" function on the touch screen, and then follows the prompts to enter the pickup code. When the pickup code is entered and checked, the express cabinet will perform a timeout detection. The real-time time when the user picks up the package is compared with the time when the employee deposits the package. If a certain time limit is exceeded, then the Octopus reader will be called first for overtime fines. The user needs to pay the fine with an Octopus card. After the fine is paid, the Octopus reader sends a message to the SLC that the payment was successful. After SLC receives the information of successful payment, it will open the corresponding courier locker and display the corresponding courier locker position on the screen. If there is no timeout, SLC will directly open the door of the express cabinet and give a prompt; if the fine payment fails, it will prompt the user to submit the timeout fine and ask the user to pay the fine again. The user completes the pickup and closes the door of the express cabinet to complete the entire operation of accessing the package. <br>

## 4. Author <br>
CHIANG Siu Yin	21210977@life.hkbu.edu.hk <br>
LU Lixin	17250633@life.hkbu.edu.hk <br>
SHU Xinyi	19250622@life.hkbu.edu.hk <br>
