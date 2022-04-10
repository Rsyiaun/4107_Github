package SLC.SLC;

public class Cabinet {
    private Boolean EmptyStatus;
    private String CabinetID;
    private String OpenCode;

    public Cabinet (String ID,Boolean EmptyStatus,String OpenCode ){
        this.CabinetID=ID;
        this.EmptyStatus=EmptyStatus;
        this.OpenCode=OpenCode;
    }

    public String getID(){
        return this.CabinetID;
    }
    public String getOpenCode(){
        return this.OpenCode;
    }
    public void setOpenCode(String code){
        this.OpenCode=code;
    }
    public Boolean getEmptyStatus(){
        return this.EmptyStatus;
    }
    public void setEmptyStatus(Boolean status){
        this.EmptyStatus=status;
    }

}

