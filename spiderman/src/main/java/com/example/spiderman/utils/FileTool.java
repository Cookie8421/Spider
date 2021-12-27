package com.example.spiderman.utils;

/**
 * @author YHW
 * @ClassName: FileTool
 * @Description:
 * @date 2019/1/25 11:21
 */
import com.example.spiderman.page.Page;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/*  本类主要是 下载那些已经访问过的文件*/
public class FileTool {

    private static String dirPath;
    private static int BUFFER_SIZE = 10240; // 缓冲区大小(缓冲区越大下载的越快,但是要根据自己的服务器配置)
    private Vector vDownLoad = new Vector(); // URL列表
    private Vector vFileList = new Vector(); // 下载后的保存文件名列表


    /**
     * getMethod.getResponseHeader("Content-Type").getValue()
     * 根据 URL 和网页类型生成需要保存的网页的文件名，去除 URL 中的非文件名字符
     */
    private static String getFileNameByUrl(String url, String contentType) {
        //去除 http://
        url = url.substring(7);
        //text/html 类型
        if (contentType.indexOf("html") != -1) {
            url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
            return url;
        }
        //如 application/pdf 类型
        else {
            return url.replaceAll("[\\?/:*|<>\"]", "_") + "." +
                    contentType.substring(contentType.lastIndexOf("/") + 1);
        }
    }

    /*
     *  生成目录
     * */
    private static void mkdir() {
        if (dirPath == null) {
            dirPath = Class.class.getClass().getResource("/").getPath() + "temp\\";
        }
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
    }

    /**
     * 保存网页字节数组到本地文件，filePath 为要保存的文件的相对地址
     */

    public static void saveToLocal(Page page) {
        mkdir();
        String fileName =  getFileNameByUrl(page.getUrl(), page.getContentType()) ;
        String filePath = dirPath + fileName ;
        byte[] data = page.getContent();
        try {
            //Files.lines(Paths.get("D:\\jd.txt"), StandardCharsets.UTF_8).forEach(System.out::println);
            DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
            for (int i = 0; i < data.length; i++) {
                out.write(data[i]);
            }
            out.flush();
            out.close();
            System.out.println("文件："+ fileName + "已经被存储在"+ filePath  );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除下载列表
     */
    public void resetList() {
        vDownLoad.clear();
        vFileList.clear();
    }

    /**
     * 增加下载列表项
     *
     * @param url
     *            String
     * @param filename
     *            String
     */

    public void addItem(String url, String filename) {
        vDownLoad.add(url);
        vFileList.add(filename);
    }

    /**
     * 根据列表下载资源
     */
    public void downLoadByList() {
        String url = null;
        String filename = null;

        // 按列表顺序保存资源
        for (int i = 0; i < vDownLoad.size(); i++) {
            url = (String) vDownLoad.get(i);
            filename = (String) vFileList.get(i);

            try {
                saveToFile(url, filename);
            } catch (IOException err) {
                System.out.println("资源[" + url + "]下载失败!!!");
            }
        }
        System.out.println("下载完成!!!");
    }

    /**
     * 将HTTP资源另存为文件
     *
     * @param destUrl
     *            String
     * @param fileName
     *            String
     * @throws Exception
     */
    public void saveToFile(String destUrl, String fileName) throws IOException {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;

        // 建立链接
        url = new URL(destUrl);
        httpUrl = (HttpURLConnection) url.openConnection();
        // 连接指定的资源
        httpUrl.connect();
        // 获取网络输入流
        bis = new BufferedInputStream(httpUrl.getInputStream());
        // 建立文件
        fos = new FileOutputStream(fileName);

        System.out.println("正在获取链接[" + destUrl + "]的内容...\n将其保存为文件["
                    + fileName + "]");
        // 保存文件
        while ((size = bis.read(buf)) != -1)
            fos.write(buf, 0, size);

        fos.close();
        bis.close();
        httpUrl.disconnect();
    }

}
