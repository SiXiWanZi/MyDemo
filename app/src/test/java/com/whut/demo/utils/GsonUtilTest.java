package com.whut.demo.utils;


import org.junit.Test;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/06/27
 *     desc   :
 * </pre>
 */
public class GsonUtilTest {
    @Test
    public void gsonToBean() throws Exception {
        String data="{\r\n  \"yhbh\": \"0001\",\r\n  \"yhmc\": \"果园港\"\r\n}";
        UserInfo userInfo = GsonUtil.GsonToBean(data, UserInfo.class);
        System.out.println(userInfo.toString());
    }

    private static class UserInfo {
        String yhbh;
        String yhmc;

        @Override
        public String toString() {
            return "UserInfo{" +
                    "yhbh='" + yhbh + '\'' +
                    ", yhmc='" + yhmc + '\'' +
                    '}';
        }
    }

}