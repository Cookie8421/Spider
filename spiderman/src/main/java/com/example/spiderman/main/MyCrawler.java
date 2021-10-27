package com.example.spiderman.main;

/**
 * @author YHW
 * @ClassName: MyCrawler
 * @Description:
 * @date 2021/10/20 11:23
 */
import com.alibaba.excel.EasyExcel;
import com.example.spiderman.domain.Region;
import com.example.spiderman.link.Links;
import com.example.spiderman.page.Page;
import com.example.spiderman.page.PageParserTool;
import com.example.spiderman.page.RequestAndResponseTool;
import com.example.spiderman.utils.RegexRule;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

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

        Date startTime = new Date();

        //初始化 URL 队列
        initCrawlerWithSeeds(seeds);

        /*final String baseUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/32/3205.html";

        //定义过滤器，提取以 变量url 开头的链接
        LinkFilter filter = new LinkFilter() {
            @Override
            public boolean accept(String url) {
                if (url.startsWith(baseUrl))
                    return true;
                else
                    return false;
            }
        };*/

        List<Region> regionList = new ArrayList<>();

        //循环条件：待抓取的链接不空
        while (!Links.unVisitedUrlQueueIsEmpty()) {

            //先从待访问的序列中取出第一个；
            String visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();
            if (visitUrl == null){
                continue;
            }

            //根据URL得到page;
            Page page = null;
            try{
                page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);
                System.out.println("当前URL为：：" + visitUrl);
            } catch (Exception e){
                e.printStackTrace();
                //如果超时就会继续把url放进队列
                System.out.println("发生超时+++++++++++++++++++++++++++++++++++++++++++++++++重新放入队列！");
                Links.addUnvisitedUrlQueue(visitUrl);
            }
            if(page == null){
                continue;
            }
            page.setCharset("gbk");
            Page finalPage = page;

            //对page中的元素进行处理： 访问DOM的某个标签
            Elements villagetr = PageParserTool.select(finalPage, ".villagetr");
            Elements citytr = PageParserTool.select(finalPage, ".citytr");
            Elements provincetr = PageParserTool.select(finalPage, ".provincetr");
            Elements countytr = PageParserTool.select(finalPage, ".countytr");
            Elements towntr = PageParserTool.select(finalPage, ".towntr");
            Set<String> esSet = PageParserTool.getLinks(finalPage,"a");

            Iterator villagetri = villagetr.iterator();
            Iterator citytri = citytr.iterator();
            Iterator provincetri = provincetr.iterator();
            Iterator countytri = countytr.iterator();
            Iterator towntri = towntr.iterator();

            //该页面的新爬取路径入队
            RegexRule regexRule = new RegexRule();
            regexRule.addPositive("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/.*");
            if(CollectionUtils.isNotEmpty(esSet)){
                Iterator i = esSet.iterator();
                while(i.hasNext()){
                    String newUrl = (String)i.next();
                    if(regexRule.satisfy(newUrl)){
                        Links.addUnvisitedUrlQueue(newUrl);
                        System.out.println("新增爬取路径：：" + newUrl);
                    }
                }
            }

            //根据当前不同的页面采取不同的爬取方式
            if(!provincetr.isEmpty()){
                while(provincetri.hasNext()){
                    Element tr = (Element)provincetri.next();
                    Elements tds = tr.children();
                    Iterator tdi = tds.iterator();
                    while(tdi.hasNext()){
                        Element td = (Element)tdi.next();
                        String cityNameTmp = td.getElementsByTag("a").text();
                        if(Strings.isBlank(cityNameTmp)){
                            continue;
                        }
                        regionList.add(new Region(td.getElementsByTag("a").attr("href").substring(0, 2) + "0000000000", cityNameTmp, "province", 1));
                    }
                }
            } else if(!citytr.isEmpty()){
                /*if(citytr.get(0).child(1).text().equals("市辖区")){
                    String provinceCode = citytr.get(0).children().get(0).text();
                    String provinceName = provinceMap.get(provinceCode.substring(0,2)).toString();
                    System.out.println("省份直辖市填入map：" + provinceCode + "：：" + provinceName);
                    continue;
                }*/
                //城市一级的td放入结果集
                while(citytri.hasNext()){
                    Element tr = (Element)citytri.next();
                    Elements tds = tr.children();
                    if(tds.get(0).getElementsByTag("a").isEmpty()){
                        regionList.add(new Region(tds.get(0).text(), tds.get(1).text(), "city", 0));
                        System.out.println("城市填入list：：" + tds.get(0).text() + "：：" + tds.get(1).text());
                    } else {
                        regionList.add(new Region(tds.get(0).getElementsByTag("a").text(), tds.get(1).getElementsByTag("a").text(), "city", 1));
                        System.out.println("城市填入list：：" + tds.get(0).getElementsByTag("a").text() + "：：" + tds.get(1).getElementsByTag("a").text());
                    }
                }
            } else if(!countytr.isEmpty()){
                //地区一级的td放入结果集
                while(countytri.hasNext()){
                    Element tr = (Element)countytri.next();
                    Elements tds = tr.children();
                    if(tds.get(0).getElementsByTag("a").isEmpty()){
                        regionList.add(new Region(tds.get(0).text(), tds.get(1).text(), "county", 0));
                        System.out.println("county填入list：" + tds.get(0).text() + "：：" + tds.get(1).text());
                    } else {
                        regionList.add(new Region(tds.get(0).getElementsByTag("a").text(), tds.get(1).getElementsByTag("a").text(), "county", 1));
                        System.out.println("county填入list：：" + tds.get(0).getElementsByTag("a").text() + "：：" + tds.get(1).getElementsByTag("a").text());
                    }
                }
            } else if(!towntr.isEmpty()){
                //地区一级的td放入结果集
                while(towntri.hasNext()){
                    Element tr = (Element)towntri.next();
                    Elements tds = tr.children();
                    if(tds.get(0).getElementsByTag("a").isEmpty()){
                        regionList.add(new Region(tds.get(0).text(), tds.get(1).text(), "town", 0));
                        System.out.println("county填入list：" + tds.get(0).text() + "：：" + tds.get(1).text());
                    } else {
                        regionList.add(new Region(tds.get(0).getElementsByTag("a").text(), tds.get(1).getElementsByTag("a").text(), "town", 1));
                        System.out.println("town填入list：：" + tds.get(0).getElementsByTag("a").text() + "：：" + tds.get(1).getElementsByTag("a").text());
                    }
                }
            } else if(!villagetr.isEmpty()){
                //居委会一级的td放入结果集
                while(villagetri.hasNext()){
                    Element tr = (Element)villagetri.next();
                    Elements tds = tr.children();
                    regionList.add(new Region(tds.get(0).text(), tds.get(2).text(), "village", 0));
                    System.out.println("village填入map：" + tds.get(0).text() + "：：" + tds.get(2).text());
                }
            }


            System.out.println("结果集当前数量：：" + regionList.size());
                //将保存文件
                //FileTool.saveToLocal(page);

                //将已经访问过的链接放入已访问的链接中；
                Links.addVisitedUrlSet(visitUrl);

        }

        for(Region region : regionList){
            System.out.println("区域编码结果集：：" + region.getRegionCode() + "::" + region.getRegionName());
        }
        System.out.println("结果集总数：：" + regionList.size() + "：：" + "开始写入表格！");
        System.out.println("任务开始时间：：" + startTime + "：：" + "任务结束时间：：" + new Date());

        try{
            EasyExcel.write(new FileOutputStream("D:\\tmp\\Files\\最新区划2020(国家统计局).xls"), Region.class).sheet("模板").doWrite(regionList);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        //exportExcel(regionList, "D:\\tmp\\Files\\最新区划2020(国家统计局)", 1);

    }

    @Deprecated
    public static void exportExcel(Queue<Region> regionQueue, String filepath, int fileNum){

        // 创建excel
        XSSFWorkbook wb = new XSSFWorkbook();

        List regionRemainList = new ArrayList();

        // 创建工作表
        XSSFSheet sheet = wb.createSheet();
        // 产生一行
        Row row = sheet.createRow(0);
        // 标题样式
        CellStyle style = wb.createCellStyle();
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
        style.setAlignment(HorizontalAlignment.CENTER);// 水平
        // 表头
        XSSFCell hssfCell = (XSSFCell) row.createCell((short) 0);

        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("区域编号");
        hssfCell = (XSSFCell) row.createCell((short) 1);

        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("区域名称");
        hssfCell = (XSSFCell) row.createCell((short) 2);

        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("父级区域编号");
        hssfCell = (XSSFCell) row.createCell((short) 3);

        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("区域等级");
        hssfCell = (XSSFCell) row.createCell((short) 4);

        hssfCell.setCellStyle(style);
        hssfCell.setCellValue("状态码");
        hssfCell = (XSSFCell) row.createCell((short) 5);


        // 设置列宽
        sheet.setColumnWidth(0, "区域编号".getBytes().length * 4 * 256);
        sheet.setColumnWidth(1, "区域名称".getBytes().length * 2 * 256);
        sheet.setColumnWidth(1, "父级区域编号".getBytes().length * 4 * 256);
        sheet.setColumnWidth(1, "区域等级".getBytes().length * 2 * 256);
        sheet.setColumnWidth(1, "状态码".getBytes().length * 1 * 256);

        // 动态数据样式
        CellStyle rowStyle = wb.createCellStyle();
        rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
        rowStyle.setAlignment(HorizontalAlignment.CENTER);// 水平
        rowStyle.setWrapText(true);  // 自动换行

        // 创建第一行
        int rowCount = 1;
        //int rowLimit = 1000000;
        while(!regionQueue.isEmpty()){
            row = sheet.createRow(rowCount);

            Region tmp = regionQueue.poll();
            row.createCell(0).setCellValue(tmp.getRegionCode());
            row.getCell(0).setCellStyle(rowStyle);

            row.createCell(1).setCellValue(tmp.getRegionName());
            row.getCell(1).setCellStyle(rowStyle);

            row.createCell(2).setCellValue(tmp.getParentCode());
            row.getCell(2).setCellStyle(rowStyle);

            row.createCell(3).setCellValue(tmp.getGrade());
            row.getCell(3).setCellStyle(rowStyle);

            row.createCell(4).setCellValue(tmp.getStatus());
            row.getCell(4).setCellStyle(rowStyle);

            rowCount++;
        }
        /*for (Region region : regionList) {
            //如果行数超过预设值，剩余的数据另外生成一个excel
            *//*if(rowCount == rowLimit){
                regionRemainList.add(region);
            } else {*//*
                // Excel行数计数器
                row = sheet.createRow(rowCount);

                row.createCell(0).setCellValue(region.getRegionCode());
                row.getCell(0).setCellStyle(rowStyle);

                row.createCell(1).setCellValue(region.getRegionName());
                row.getCell(1).setCellStyle(rowStyle);

                row.createCell(2).setCellValue(region.getParentCode());
                row.getCell(2).setCellStyle(rowStyle);

                row.createCell(3).setCellValue(region.getGrade());
                row.getCell(3).setCellStyle(rowStyle);

                row.createCell(4).setCellValue(region.getStatus());
                row.getCell(4).setCellStyle(rowStyle);

                rowCount++;
            //}
        }*/
        /*if(regionRemainList.size() > 0){
            exportExcel(regionRemainList, filepath, fileNum+1);
        }*/
        try {
            OutputStream out = null;
            String fileName = filepath + fileNum + ".xlsx";
            File desc = new File(fileName);
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            out = new FileOutputStream(fileName);
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //main 方法入口
    public static void main(String[] args) {
        MyCrawler crawler = new MyCrawler();
        crawler.crawling(new String[]{"http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/index.html"});
    }
}