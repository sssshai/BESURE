package pojo.VO;

import java.io.Serializable;

public class EHR implements Serializable {
    public String HName;
    public String HID;
    public String depName;
    public String depID;
    public String idD;
    public String idP;
    public String content;

    @Override
    public String toString() {
        return "EHR{" +
                "depName='" + depName + '\'' +
                ", depID='" + depID + '\'' +
                ", idP='" + idP + '\'' +
                ", idD='" + idD + '\'' +
                ", HName='" + HName + '\'' +
                ", HID='" + HID + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepID() {
        return depID;
    }

    public void setDepID(String depID) {
        this.depID = depID;
    }

    public String getIdP() {
        return idP;
    }

    public void setIdP(String idP) {
        this.idP = idP;
    }

    public String getIdD() {
        return idD;
    }

    public void setIdD(String idD) {
        this.idD = idD;
    }

    public String getHName() {
        return HName;
    }

    public void setHName(String HName) {
        this.HName = HName;
    }

    public String getHID() {
        return HID;
    }

    public void setHID(String HID) {
        this.HID = HID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
