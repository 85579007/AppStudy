package hhh.appstudy.entities;

/**
 * Created by hhh on 2016/4/22.
 */
public class Role {
    private int rid;
    private String role;

    public Role(int rid, String role) {
        this.rid = rid;
        this.role = role;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
