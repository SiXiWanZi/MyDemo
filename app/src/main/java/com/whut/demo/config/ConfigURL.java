package com.whut.demo.config;

/**
 * <pre>
 *  desc: 一些网络地址
 *  Created by 忘尘无憾 on 2018/01/20.
 *  version:
 * </pre>
 */

public class ConfigURL {

    /*
        服务器配置信息
     */

    /*
     * 后台响应的基本配置
     */
    // 现场部署到
    public static final String IP = "59.69.104.197";
    public static final String BASE_URL = "https://" + IP + ":4435/Handler";
    public static final String INNER_VEHICLE_EXTERNAL_BASE_URL = BASE_URL + "/InnerVehicleExternal/";


    //服务器配置文件地址路径、名称
    //服务器装载机外港车预约系统后台基地址：主要使用这个*****************************
    public static final String SERVER_CONFIG_PATH = BASE_URL + "CYCTOS_Android/ServerConfig.xml";
    public static final String SERVER_CONGIG_NAME = "ServerConfig.xml";


    /*
     * 更新相关基地址
     */
    public static final String BASE_URL_UPDATE = "http://" + IP + ":8099/CYCTOS_Android";

    /*******yanglijin*************/
    //一般处理程序页面名称
    public static final String LOGIN = "Login.ashx";
    // 内转车排班记录
    public static final String INNER_VEHICLE_SHCEDULE_RECORD = "InnerVehSchedule.ashx";
    // apk版本检测（8099端口号）
    public static final String VERSION_URL = BASE_URL_UPDATE + "/version.xml";
    // 测试页面
    public static final String MY_TEST_URL = "Test.ashx";
    // 查询页面
    public static final String GET_QUERY_DATA = "Query.ashx";
    /*************************/


}
