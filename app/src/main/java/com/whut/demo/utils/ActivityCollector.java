package com.whut.demo.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/06/25
 *     desc   : 用专门的集合类对所有的活动进行管理
 * </pre>
 */
public class ActivityCollector {
    public static List<Activity> sActivities = new ArrayList<>();

    /**
     * 向集合中添加元素
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (activity == null)
            throw new NullPointerException();
        if (!sActivities.contains(activity)) {
            sActivities.add(activity);
        }
    }

    /**
     * 从集合中删除元素
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        sActivities.remove(activity);
    }

    /**
     * 销毁所有活动
     */
    public static void finishAll() {
        for (Activity activity : sActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
