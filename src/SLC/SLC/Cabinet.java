package SLC.SLC;

public class Cabinet {
    private Boolean EmptyStatus;
    private String CabinetID;
    private String OpenCode;
    private String Barcode;
    private String StoreTime;

    public Cabinet(String ID, Boolean EmptyStatus, String OpenCode, String Barcode, String storeTime) {
        this.CabinetID = ID;
        this.EmptyStatus = EmptyStatus;
        this.OpenCode = OpenCode;
        this.Barcode = Barcode;
        this.StoreTime = storeTime;
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

    public Boolean getEmptyStatus() {
        return this.EmptyStatus;
    }

    public void setEmptyStatus(Boolean status) {
        this.EmptyStatus = status;
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

}

