package com.example.spiderman.main;

/**
 * @author YHW
 * @ClassName: MyCrawler
 * @Description:
 * @date 2019/1/25 11:23
 */
import com.example.spiderman.link.LinkFilter;
import com.example.spiderman.link.Links;
import com.example.spiderman.page.Page;
import com.example.spiderman.page.PageParserTool;
import com.example.spiderman.page.RequestAndResponseTool;
import com.example.spiderman.utils.FileTool;
import com.example.spiderman.utils.RegexRule;
import org.jsoup.select.Elements;

import java.util.Set;

public class MyCrawler {

    /**
     * 使用种子初始化 URL 队列
     *
     * @param seeds 种子 URL
     * @return
     */
    private void initCrawlerWithSeeds(String[] seeds) {
        for (int i = 0; i < seeds.length; i++){
            Links.addUnvisitedUrlQueue(seeds[i]);
        }
    }


    /**
     * 抓取过程
     *
     * @param seeds
     * @return
     */
    public void crawling(String[] seeds) {

        //初始化 URL 队列
        initCrawlerWithSeeds(seeds);

        //定义过滤器，提取以 变量url 开头的链接
        LinkFilter filter = new LinkFilter() {
            @Override
            public boolean accept(String url) {
                if (url.startsWith("https://www.cnblogs.com/Joey44/"))
                    return true;
                else
                    return false;
            }
        };

        //循环条件：待抓取的链接不空且抓取的网页不多于 1000
        while (!Links.unVisitedUrlQueueIsEmpty()  && Links.getVisitedUrlNum() <= 1000) {

            //先从待访问的序列中取出第一个；
            String visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();
            if (visitUrl == null){
                continue;
            }

            //根据URL得到page;
            Page page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);

            //对page进行处理： 访问DOM的某个标签
            Elements es = PageParserTool.select(page,"a");
            if(!es.isEmpty()){
                System.out.println("下面将打印所有a标签： ");
                System.out.println(es);
            }

            //将保存文件
            FileTool.saveToLocal(page);

            //将已经访问过的链接放入已访问的链接中；
            Links.addVisitedUrlSet(visitUrl);

            //得到超链接
            Set<String> links = PageParserTool.getLinks(page,"a");
            for (String link : links) {
                RegexRule regexRule = new RegexRule();
                regexRule.addPositive("http.*/Joey44/.*html.*");
                if(regexRule.satisfy(link)){
                    Links.addUnvisitedUrlQueue(link);
                    System.out.println("新增爬取路径: " + link);
                }

            }
        }
    }


    //main 方法入口
    public static void main(String[] args) {
        MyCrawler crawler = new MyCrawler();
        crawler.crawling(new String[]{"https://www.cnblogs.com/Joey44/"});
    }
}
