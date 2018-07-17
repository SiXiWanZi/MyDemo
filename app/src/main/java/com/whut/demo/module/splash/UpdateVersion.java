package com.whut.demo.module.splash;

import android.support.annotation.NonNull;


import com.whut.demo.bean.ServerVersionBean;
import com.whut.demo.config.ConfigURL;
import com.whut.demo.utils.LogUtil;
import com.whut.demo.utils.RegexUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yx on 2018/02/01.
 * 获取version.xml版本号和资源地址
 * 参数：传入apk名称
 */

public class UpdateVersion {

    private static Map<String, ServerVersionBean> map_version = null;
    private static final String TAG = "UpdateVersion";

    /**
     * 获取服务器配置文件，并进行遍历<br />
     * 将各个apk的名称，版本号以及地址存放在Map集合中
     */
    private static void getServerVersions() {
        ServerVersionBean versionBean = null;// 保存一项配置
        String name_apk = null;// apk名称
        String version_apk = null;
        String url_apk = null;
        try {
            //"http://59.69.75.251:8099/CYCTOS_Android/version.xml"
            LogUtil.e(ConfigURL.VERSION_URL);
            URL urlstr = new URL(ConfigURL.VERSION_URL);
            InputStream is = urlstr.openStream();
            if (is != null) {
                XmlPullParserFactory factory = XmlPullParserFactory
                        .newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            // 文档开始，进行集合的初始化
                            map_version = new HashMap<>();
                            break;
                        case XmlPullParser.START_TAG:
                            // 标签开始
                            String qname = parser.getName().trim();
                            if ("item".equals(qname)) {
                                versionBean = new ServerVersionBean();
                            } else if ("name".equals(qname)) {
                                name_apk = parser.nextText();
                                versionBean.setName(name_apk);
                            } else if ("version".equals(qname)) {
                                version_apk = parser.nextText();
                                versionBean.setVersion(version_apk);
                            } else if ("url".equals(qname)) {
                                url_apk = parser.nextText();
                                versionBean.setUrl(url_apk);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            String qname1 = parser.getName().trim();
                            if ("item".equals(qname1)) {
                                map_version.put(name_apk, versionBean);
                                versionBean = null;
                            }
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        default:
                            break;
                    }
                    eventType = parser.next();
                }
            }
        } catch (MalformedURLException e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 获取服务器配置文件配置信息<br />
     * 从静态变量中获取，默认第一次会从服务器获取
     *
     * @return
     */
    public static Map<String, ServerVersionBean> getAllVersion() {
        return getAllVersion(true);
    }

    /**
     * 获取服务器配置文件配置信息
     *
     * @param isRead 是否从服务器获取<br />
     *               true 代表强制从服务器重新获取 <br />
     *               false 代表从静态变量中获取，默认第一次会从服务器获取
     * @return
     */
    public static Map<String, ServerVersionBean> getAllVersion(boolean isRead) {
        if (isRead || map_version == null) {
            map_version = null;
            getServerVersions();
        }
        return map_version;
    }

    /**
     * 获取服务器版本实体类
     *
     * @param VersionName
     * @return
     */
    public static ServerVersionBean getAuthorities(@NonNull String VersionName) {
        try {
            if (RegexUtil.isNullOrEmpty(VersionName)) {
                return null;
            }
            //获取配置的集合
            Map<String, ServerVersionBean> allVersions = getAllVersion();
            if (allVersions.get(VersionName) == null) {
                LogUtil.d("没有找到该apk");
            }
            if (allVersions.get(VersionName) != null) {
                return allVersions.get(VersionName);// 不为空则返回该apk名称
            }

        } catch (Exception e) {
            LogUtil.e("根据已知apk名称获取该apk存放Bean，获取失败，默认返回null");
        }
        return null;
    }

    /**
     * 根据权限名获取对应的版本和资源地址值
     *
     * @param versionName apk名称
     * @return
     */
    public static int getXMLVersion(@NonNull String versionName) {
        ServerVersionBean serverVersionBean = getAuthorities(versionName);
        if (serverVersionBean != null && serverVersionBean.getVersion() != null) {
            LogUtil.e(TAG,serverVersionBean.toString());
            return Integer.parseInt(serverVersionBean.getVersion());// 返回该权限名对应的值
        }
        return 0;
    }

    /**
     * 根据App名称获取服务器对应的URL
     *
     * @param versionName APP名称
     * @return
     */
    public static String getURL(@NonNull String versionName) {
        ServerVersionBean serverVersionBean = getAuthorities(versionName);
        if (serverVersionBean != null && serverVersionBean.getVersion() != null) {
            return serverVersionBean.getUrl();// 返回该权限名对应的值
        }
        return null;
    }

}