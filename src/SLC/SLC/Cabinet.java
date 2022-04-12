package SLC.SLC;

public class Cabinet {
    private String OpenStatus;
    private String CabinetID;
    private String OpenCode;
    private String Barcode;
    private String StoreTime;
    private String Size;

    public Cabinet(String ID, String OpenStatus, String OpenCode, String Barcode,String storeTime,String size) {
        this.CabinetID = ID;
        this.OpenStatus = OpenStatus;
        this.OpenCode = OpenCode;
        this.Barcode = Barcode;
        this.StoreTime = StoreTime;
        this.Size=size;
    }

    public String getID() {
        return this.CabinetID;
    }

    public String getOpenCode() {
        return this.OpenCode;
    }

    public void setOpenCode(String code) {
        this.OpenCode = code;
    }

    public String getOpenStatus() {
        return this.OpenStatus;
    }

    public void setOpenStatus(String status) {
        this.OpenStatus = status;
    }

    public void setBarcode(String code) {
        this.Barcode = code;
    }

    public String getBarcode() {
        return this.Barcode;
    }

    public void setStoreTime(String time) {
        this.StoreTime = time;
    }

    public String getStoreTime() {
        return this.StoreTime;
    }

    public String getSize(){
        return this.Size;
    }

}

