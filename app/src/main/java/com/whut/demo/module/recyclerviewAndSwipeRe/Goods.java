package com.whut.demo.module.recyclerviewAndSwipeRe;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/12
 *     desc   : 商品类
 * </pre>
 */
public class Goods {
    // ROWNUm
    private int ROWNUM;

    public int getROWNUM() {
        return ROWNUM;
    }

    public void setROWNUM(int ROWNUM) {
        this.ROWNUM = ROWNUM;
    }

    // 名字
    private String NAME;
    // 说明
    private String DESCRIPTION;

    public String getNAME() {
        return NAME == null ? "" : NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION == null ? "" : DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "ROWNUM=" + ROWNUM +
                ", NAME='" + NAME + '\'' +
                ", DESCRIPTION='" + DESCRIPTION + '\'' +
                '}';
    }
}
