package pojo;

import java.util.ArrayList;
import java.util.List;

public class Status {
    private String rs;
    private D d;
    private List<D> dd;

    public List<D> getDd() {
        return dd;
    }

    public void newDd(){
        dd = new ArrayList<>();
    }

    public void addDd(D s) {
        dd.add(s);
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public D getD() {
        return d;
    }

    public void setD(D d) {
        this.d = d;
    }
}
