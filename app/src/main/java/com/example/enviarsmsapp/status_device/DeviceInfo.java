package com.example.enviarsmsapp.status_device;

public class DeviceInfo {
    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    private String numberPhone;
    private String imei;

    public DeviceInfo(String numberPhone, String imei) {
        this.numberPhone = numberPhone;
        this.imei = imei;
    }
}
