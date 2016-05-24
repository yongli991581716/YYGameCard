
package com.lordcard.ui.personal.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientName {
    public static List<String> allClientNames = null;

    public static List<String> getallClientNames() {
        allClientNames = new ArrayList<String>();
        allClientNames.add("机器人");
        allClientNames.add("机器猫");
        allClientNames.add("斗霸无痕");
        allClientNames.add("雪儿");
        allClientNames.add("兰兰");
        allClientNames.add("岚岚");
        allClientNames.add("花花");
        allClientNames.add("小白");
        allClientNames.add("罗刹");
        allClientNames.add("海尔");
        allClientNames.add("小明");
        allClientNames.add("十一郎");
        allClientNames.add("KK");
        allClientNames.add("Licey");
        allClientNames.add("伍子胥");
        allClientNames.add("荀彧");
        allClientNames.add("贾诩");
        allClientNames.add("司马懿");
        allClientNames.add("关羽");
        allClientNames.add("张飞");
        allClientNames.add("赵云");
        allClientNames.add("青龙");
        allClientNames.add("白虎");
        allClientNames.add("朱雀");
        allClientNames.add("炫舞");
        allClientNames.add("玄武");
        allClientNames.add("弑神");
        allClientNames.add("小龙女");
        allClientNames.add("白起");
        allClientNames.add("孙权");
        allClientNames.add("刘备");
        allClientNames.add("吕布");
        allClientNames.add("武松");
        allClientNames.add("宋江");
        allClientNames.add("世界之神");
        allClientNames.add("死神");
        allClientNames.add("太阳神");
        allClientNames.add("嫦娥");
        allClientNames.add("云里雾");
        allClientNames.add("雾里云");
        allClientNames.add("急如火");
        allClientNames.add("快如风");
        allClientNames.add("千里眼");
        allClientNames.add("顺风耳");
        List<String> ClientNames = new ArrayList<String>();

        for (int i = 0; i < 2; i++) {
            int nameSize = allClientNames.size();
            Random rd = new Random();
            int index = rd.nextInt(nameSize);
            ClientNames.add(allClientNames.get(index));
            allClientNames.remove(index);
        }

        return ClientNames;
    }
}
