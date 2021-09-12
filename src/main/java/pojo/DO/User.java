package pojo.DO;

import it.unisa.dia.gas.jpbc.Element;

public class User {
    private int uid;
    private String uname;
    private String password;
    private String PID;
    private Element sk_user;

    public User() {
    }

    public User(int uid, String uname, String password) {
        this.uid = uid;
        this.uname = uname;
        this.password = password;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public Element getSk_user() {
        return sk_user;
    }

    public void setSk_user(Element sk_user) {
        this.sk_user = sk_user;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", password='" + password + '\'' +
                ", PID='" + PID + '\'' +
                ", sk_user=" + sk_user +
                '}';
    }
}
