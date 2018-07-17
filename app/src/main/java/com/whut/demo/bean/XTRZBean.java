package com.whut.demo.bean;

/**
 * <pre>
 *  desc: 系统日志实体类
 *  Created by 忘尘无憾 on 2018/01/20.
 *  version:
 * </pre>
 */

public class XTRZBean {
    String YHZH;// 用户账户，实际应该保存的是用户编号：0001
    String IPDZ;// IP地址
    String MACDZ;// MAC地址
    String YHMC;// 用户名称

    public String getYHZH() {
        return YHZH;
    }

    public void setYHZH(String YHZH) {
        this.YHZH = YHZH;
    }

    public String getIPDZ() {
        return IPDZ;
    }

    public void setIPDZ(String IPDZ) {
        this.IPDZ = IPDZ;
    }

    public String getMACDZ() {
        return MACDZ;
    }

    public void setMACDZ(String MACDZ) {
        this.MACDZ = MACDZ;
    }

    public String getYHMC() {
        return YHMC;
    }

    public void setYHMC(String YHMC) {
        this.YHMC = YHMC;
    }

    @Override
    public String toString() {
        return "XTRZBean{" +
                "YHZH='" + YHZH + '\'' +
                ", IPDZ='" + IPDZ + '\'' +
                ", MACDZ='" + MACDZ + '\'' +
                ", YHMC='" + YHMC + '\'' +
                '}';
    }
}
