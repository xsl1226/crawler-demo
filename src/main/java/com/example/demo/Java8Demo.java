package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Java8Demo {

    public static void main(String[] args) {
        List<String> names2 = new ArrayList<String>();
        names2.add("Google ");
        names2.add("Runoob ");
        names2.add("Taobao ");
        names2.add("Baidu ");
        names2.add("Sina ");
        Collections.sort(names2, (s1, s2) -> s1.compareTo(s2));
        System.out.println(names2);
    }
}
