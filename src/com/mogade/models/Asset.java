package com.mogade.models;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: darrenkopp
 * Date: 10/6/11
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Asset {
    private String name;
    private String file;
    private String meta;
    private int type;
    private Date dated;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDated() {
        return dated;
    }

    public void setDated(Date dated) {
        this.dated = dated;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
