package pojo.DO;

import it.unisa.dia.gas.jpbc.Element;

public class Doctor {
    private String ID;
    private Element tk;

    public Doctor(String id) {
        ID = id;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Element getTk() {
        return tk;
    }

    public void setTk(Element tk) {
        this.tk = tk;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "ID='" + ID + '\'' +
                ", tk=" + tk +
                '}';
    }
}