package com.chenshinan.exercises.rocketchat.rest.dto;

/**
 * @author shinan.chen
 * @since 2019/7/31
 */
public class Discussion {
    private String prid;
    private String t_name;

    public Discussion() {
    }

    public Discussion(String prid, String t_name) {
        this.prid = prid;
        this.t_name = t_name;
    }

    public String getPrid() {
        return prid;
    }

    public void setPrid(String prid) {
        this.prid = prid;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }
}
