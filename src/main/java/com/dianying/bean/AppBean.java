package com.dianying.bean;

import com.avatar.db.annotation.Column;
import com.avatar.db.annotation.GeneratorType;
import com.avatar.db.annotation.Table;

/**
 * Created by ff135 on 2017/8/4.
 */
@Table(name = "app_icon")
public class AppBean {

    @Column(name = "id", primaryKey = true, generatorType = GeneratorType.AUTO_INCREMENT)
    private Long id;

    @Column(name = "appcode")
    private String appcode;

    @Column(name = "type")
    private int type;

    @Column(name = "startx")
    private int startx;

    @Column(name = "starty")
    private int starty;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @Column(name = "dir")
    private String dir;

    @Column(name = "log_time")
    private long logTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStartx() {
        return startx;
    }

    public void setStartx(int startx) {
        this.startx = startx;
    }

    public int getStarty() {
        return starty;
    }

    public void setStarty(int starty) {
        this.starty = starty;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public long getLogTime() {
        return logTime;
    }

    public void setLogTime(long logTime) {
        this.logTime = logTime;
    }

    @Override
    public String toString() {
        return "AppBean{" +
                "id=" + id +
                ", appcode='" + appcode + '\'' +
                ", type=" + type +
                ", startx=" + startx +
                ", starty=" + starty +
                ", width=" + width +
                ", height=" + height +
                ", dir='" + dir + '\'' +
                '}';
    }
}
