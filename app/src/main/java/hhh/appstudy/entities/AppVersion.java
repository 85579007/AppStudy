package hhh.appstudy.entities;

/**
 * Created by hhh on 2016/4/22.
 */
public class AppVersion {
    private int id;
    private int code;
    private String name;
    private String url;

    public AppVersion(int ver_id, int ver_code, String ver_name, String ver_url) {
        this.id = ver_id;
        this.code = ver_code;
        this.name = ver_name;
        this.url = ver_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
