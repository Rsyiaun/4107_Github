package SLC.SLC;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class CabinetGroup {
    private ArrayList<Cabinet> CabGroup = new ArrayList<Cabinet>();

    public CabinetGroup() throws IOException {

        for(int i=1;i<=40;i++){
            Properties cfgProps1 = null;
            cfgProps1 = new Properties();
            FileInputStream in = new FileInputStream("etc/SLC.cfg");
            cfgProps1.load(in);
            in.close();
            String s = cfgProps1.getProperty("Lockers.Locker"+String.valueOf(i));
            String [] sArray = cfgProps1.getProperty("Lockers.Locker"+String.valueOf(i)).split("-");
            this.CabGroup.add((new Cabinet(String.valueOf(i),sArray[1],sArray[0],"",sArray[2])));
        }
    }


    public String getEmptyID(){
        for(int i=0;i<this.CabGroup.size();i++){
            if(this.CabGroup.get(i).getOpenCode().equals("null")){
                return this.CabGroup.get(i).getID();
            }
        }
        return null;
    }

    public String findMatchCabinet(String code){
        for(int i=0;i<this.CabGroup.size();i++){
            if(this.CabGroup.get(i).getOpenCode().equals(code)){
                return this.CabGroup.get(i).getID();
            }
        }
        return null;
    }

    public Cabinet getCabinet(String ID){
        for(int i=0;i<this.CabGroup.size();i++){
            if(this.CabGroup.get(i).getID().equals(ID)){
                return this.CabGroup.get(i);
            }
        }
        return null;
    }



}

