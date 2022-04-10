package SLC.SLC;

import java.util.ArrayList;

public class CabinetGroup {
    private ArrayList<Cabinet> CabGroup = new ArrayList<Cabinet>();

    public CabinetGroup(){
        this.CabGroup.add(new Cabinet("A1",true,""));
        this.CabGroup.add(new Cabinet("A2",true,""));
        this.CabGroup.add(new Cabinet("A3",true,""));
        this.CabGroup.add(new Cabinet("A4",true,""));
        this.CabGroup.add(new Cabinet("A5",true,""));
        this.CabGroup.add(new Cabinet("A6",true,""));
    }


    public String getEmptyID(){
        for(int i=0;i<this.CabGroup.size();i++){
            if(this.CabGroup.get(i).getEmptyStatus()){
                return this.CabGroup.get(i).getID();
            }
        }
        return null;
    }

    public String findMatchCabinet(String code){
        for(int i=0;i<this.CabGroup.size();i++){
            if((!this.CabGroup.get(i).getEmptyStatus()) && this.CabGroup.get(i).getOpenCode().equals(code)){
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

