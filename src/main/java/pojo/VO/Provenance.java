package pojo.VO;

import java.io.Serializable;
import java.util.List;

public class Provenance implements Serializable {
    public String ehrHash;
    public List<String> viewHash;
    public String pidD;
    public String depName;//
    public String depID;//
    public String idP;
    public String HName;//
    public String HID;//
    //    public String block;//TODO 上一个溯源记录的区块哈希
    public long startCreateTime;
    public long endCreateTime;
    public long startViewTime;//TODO
    public long endViewTime;//TODO
    public int stageNumber;

    @Override
    public String toString() {
        return "Provenance{" +
                "ehrHash='" + ehrHash + '\'' +
                ", viewHash=" + viewHash +
                ", pidD='" + pidD + '\'' +
                ", depName='" + depName + '\'' +
                ", depID='" + depID + '\'' +
                ", idP='" + idP + '\'' +
                ", HName='" + HName + '\'' +
                ", HID='" + HID + '\'' +
//                ", block='" + block + '\'' +
                ", startCreateTime=" + startCreateTime +
                ", endCreateTime=" + endCreateTime +
                ", startViewTime=" + startViewTime +
                ", endViewTime=" + endViewTime +
                ", stageNumber=" + stageNumber +
                '}';
    }

    public String getEhrHash() {
        return ehrHash;
    }

    public void setEhrHash(String ehrHash) {
        this.ehrHash = ehrHash;
    }

    public List<String> getViewHash() {
        return viewHash;
    }

    public void setViewHash(List<String> viewHash) {
        this.viewHash = viewHash;
    }

    public String getPidD() {
        return pidD;
    }

    public void setPidD(String pidD) {
        this.pidD = pidD;
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

//    public String getBlock() {
//        return block;
//    }
//
//    public void setBlock(String block) {
//        this.block = block;
//    }

    public long getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(long startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public long getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(long endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public long getStartViewTime() {
        return startViewTime;
    }

    public void setStartViewTime(long startViewTime) {
        this.startViewTime = startViewTime;
    }

    public long getEndViewTime() {
        return endViewTime;
    }

    public void setEndViewTime(long endViewTime) {
        this.endViewTime = endViewTime;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public void setStageNumber(int stageNumber) {
        this.stageNumber = stageNumber;
    }
}
