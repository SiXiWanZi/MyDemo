package com.whut.demo.bean;

/**
 * Created by yx on 2018/02/01.
 * 服务器上version.xml的实体类
 */

public class ServerVersionBean {
    private String name;// apk名称
    private String version;// 版本
    private String url;// 资源地址

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Version{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
