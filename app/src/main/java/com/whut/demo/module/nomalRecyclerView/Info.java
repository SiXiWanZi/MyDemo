package com.whut.demo.module.nomalRecyclerView;

/**
 * 作者：鲁翩
 * 时间：2018/01/31
 * 功能：
 */

public class Info {

    // 姓名
    private String name;
    // 年龄
    private int age;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Info{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
