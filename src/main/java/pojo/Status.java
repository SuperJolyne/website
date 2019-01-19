package pojo;

import java.util.ArrayList;
import java.util.List;

public class Status {
    private String rs;
    private Data d;
    private List<Data> da;

    public List<Data> getD() {
        return da;
    }

    public void newDa(){
        da= new ArrayList<>();
    }

    public void setDa(Data s) {
        da.add(s);
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public Data getD() {
        return d;
    }

    public void setD(Data d) {
        this.d = d;
    }
}
