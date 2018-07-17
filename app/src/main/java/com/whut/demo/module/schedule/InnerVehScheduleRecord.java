package com.whut.demo.module.schedule;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/06
 *     desc   : 内转车排班记录
 * </pre>
 */
public class InnerVehScheduleRecord implements Serializable{
    // 车牌号
    private String CPH;

    // RFID卡
    private String RFIDKH;

    // 关联时间
    private Date GLSJ;

    // 所属公司
    private String SSGS;

    // 排班时间
    private Date PBSJ;

    // 排班班次:1，早班；2：中班；3：晚班
    private int PBBC;

    // 是否已排班:0，未排班；1:，已排班
    private int ISSCHEDULED=0;

    public String getCPH() {
        return CPH == null ? "" : CPH;
    }

    public void setCPH(String CPH) {
        this.CPH = CPH;
    }

    public String getRFIDKH() {
        return RFIDKH == null ? "" : RFIDKH;
    }

    public void setRFIDKH(String RFIDKH) {
        this.RFIDKH = RFIDKH;
    }

    public Date getGLSJ() {
        return GLSJ;
    }

    public void setGLSJ(Date GLSJ) {
        this.GLSJ = GLSJ;
    }

    public String getSSGS() {
        return SSGS == null ? "" : SSGS;
    }

    public void setSSGS(String SSGS) {
        this.SSGS = SSGS;
    }

    public Date getPBSJ() {
        return PBSJ;
    }

    public void setPBSJ(Date PBSJ) {
        this.PBSJ = PBSJ;
    }

    public int getPBBC() {
        return PBBC;
    }

    public void setPBBC(int PBBC) {
        this.PBBC = PBBC;
    }

    public int ISSCHEDULED() {
        return ISSCHEDULED;
    }

    public void setISSCHEDULED(int ISSCHEDULED) {
        this.ISSCHEDULED = ISSCHEDULED;
    }

    @Override
    public String toString() {
        return "InnerVehScheduleRecord{" +
                "CPH='" + CPH + '\'' +
                ", RFIDKH='" + RFIDKH + '\'' +
                ", GLSJ=" + GLSJ +
                ", SSGS='" + SSGS + '\'' +
                ", PBSJ=" + PBSJ +
                ", PBBC=" + PBBC +
                ", isSchedule=" + ISSCHEDULED +
                '}';
    }
}
