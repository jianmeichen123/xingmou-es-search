//package com.gi.xm.es.index;
//
//import com.gi.xm.es.util.ESDataUtil;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by zcy on 17-2-8.
// * 定时执行创建索引程序
// */
//@Component
//public class Monitor {
//    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
//    private static final String HOST = "10.9.130.135";
//    private static final String CLUSTERNAME = "elasticsearch";
//    @Scheduled(cron="0 0 23  * * ?")
//    public static void createprojectIndex(){
//        ProjectIndex projectIndex = new ProjectIndex(HOST,CLUSTERNAME);
//        projectIndex.importProjects();
//    }
//    @Scheduled(cron="0 0 23 * * ?")
//    public static void createInvestfirmIndex(){
//        InvestfirmsIndex investfirmIndex = new InvestfirmsIndex(HOST,CLUSTERNAME);
//        investfirmIndex.importInvestfirms();
//    }
//    @Scheduled(cron="0 0 23  * * ?")
//    public static void createInvestEventIndex(){
//        InvestEventIndex investorIndex = new InvestEventIndex(HOST,CLUSTERNAME);
//        investorIndex.importInvestEvent();
//    }
//    @Scheduled(cron="0 0 23 * * ?")
//    public static void createMergeEventIndex(){
//        MergeEventIndex mergeEventIndex = new MergeEventIndex(HOST,CLUSTERNAME);
//        mergeEventIndex.importMergeEvent();
//    }
//    @Scheduled(cron="0 0 23 * * ?")
//    public static void createQuitEventIndex(){
//        QuitEventIndex quitEventIndex = new QuitEventIndex(HOST,CLUSTERNAME);
//        quitEventIndex.importQuitEvent();
//    }
//}
