package com.example.spiderman.link;

/**
 * @author YHW
 * @ClassName: Links
 * @Description:
 * @date 2019/1/25 11:19
 */
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/*
 *  Link主要功能;
 *  1: 存储已经访问过的URL路径 和 待访问的URL 路径;
 *
 *
 * */
public class Links {

    //已访问的 url 集合  已经访问过的 主要考虑 不能再重复了 使用set来保证不重复;
    private static Set visitedUrlSet = new ConcurrentSkipListSet();

    //待访问的 url 集合  待访问的主要考虑 1:规定访问顺序;2:保证不提供重复的带访问地址;
    private static ConcurrentLinkedQueue  unVisitedUrlQueue = new ConcurrentLinkedQueue();

    //获得已经访问的 URL 数目
    public static int getVisitedUrlNum() {
        return visitedUrlSet.size();
    }

    //添加到访问过的 URL
    public static void addVisitedUrlSet(String url) {
        visitedUrlSet.add(url);
    }

    //移除访问过的 URL
    public static void removeVisitedUrlSet(String url) {
        visitedUrlSet.remove(url);
    }



    //获得 待访问的 url 集合
    public static ConcurrentLinkedQueue getUnVisitedUrlQueue() {
        return unVisitedUrlQueue;
    }

    // 添加到待访问的集合中  保证每个 URL 只被访问一次
    public static void addUnvisitedUrlQueue(String url) {
        if (url != null && !url.trim().equals("")  && !visitedUrlSet.contains(url)  && !unVisitedUrlQueue.contains(url)){
            unVisitedUrlQueue.offer(url);
        }
    }

    //删除 待访问的url
    public static Object removeHeadOfUnVisitedUrlQueue() {
        return unVisitedUrlQueue.poll();
    }

    //判断未访问的 URL 队列中是否为空
    public static boolean unVisitedUrlQueueIsEmpty() {
        return unVisitedUrlQueue.isEmpty();
    }

}
