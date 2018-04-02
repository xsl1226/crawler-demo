package com.example.controller;

import com.example.SearchVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Grated by xushaolin on 2018/3/30
 * 爬虫抓取页面绝对链接
 */
@Controller
public class SearchController {

    @RequestMapping(value = "/searchPage")
    @ResponseBody
    public Object search(String content) {
        //判断请求如果没加http自动补充
        if (!content.startsWith("http")){
            content = "http:"+content;
        }
        List<SearchVo> result = new ArrayList();
        String capture = capture(content);//抓取网页的内容
        //正则表达式
        String regex = "((http://|ftp://|https://|//))(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        Pattern _pattern = Pattern.compile(regex);
        Matcher _match = _pattern.matcher(capture);
        while (_match.find()) {
            String group = _match.group();
            //判断js或者css文件中是否还包含绝对路径
            if (group.contains(".css") || group.contains(".js")) {
                //如果以//开头则拼接一个http:
                if (!group.startsWith("http")) {
                    group = "http:" + group;
                }
                capture = capture(group);//抓取网页的内容
                Matcher matcher = _pattern.matcher(capture);
                List list = new ArrayList();
                while (matcher.find()) {
                    if (matcher.group().startsWith("http")) {
                        list.add(matcher.group());
                    }
                }
                SearchVo map = new SearchVo();
                map.setName(group);
                map.setList(list);
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 功能：输入网址抓取内容
     * 菜单：
     * 思路：
     * 描述：
     * @Date： 2018/03/30
     * @author：xushaolin
     */
    private String capture(String url){
        // 定义一个字符串用来存储网页内容
        String str = "";
        // 定义一个缓冲字符输入流
        BufferedReader in = null;
        try {
            // 将string转成url对象
            URL realUrl = new URL(url);
            // 初始化一个链接到那个url的连接
            URLConnection connection = realUrl.openConnection();
            // 开始实际的连接
            connection.connect();
            // 初始化 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // 用来临时存储抓取到的每一行的数据
            String line;
            while ((line = in.readLine()) != null) {
                // 遍历抓取到的每一行并将其存储到result里面
                str += line + "\n";
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } // 使用finally来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return str;
    }

}
