package hhh.appstudy.entities;

/**
 * Created by hhh on 2016/4/22.
 */
public class Feedback {
    private int fid;
    private String content;
    private String token;

    public Feedback(int fid, String content, String token) {
        this.fid = fid;
        this.content = content;
        this.token = token;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
