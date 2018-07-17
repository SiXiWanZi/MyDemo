package com.whut.demo.bean;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/06
 *     desc   :
 * </pre>
 */
public class Company {
    private Company(){}

    // 公司名称
    private String companyName;

    public static Company getInstance(){
        return SingtonHolder.instance;
    }

    private static class  SingtonHolder{
        private static final Company instance = new Company();
    }

    public String getCompanyName() {
        return companyName == null ? "" : companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
