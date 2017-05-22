//package com.gi.xm.es.index;
//
//import com.gi.xm.es.util.Mysql2ES;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//
///**
// * Created by zcy on 17-2-8.
// * 定时执行创建索引程序
// */
//@Component
//public class Monitor {
//    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
//    @Scheduled(cron="0 0 23  * * ?")
//    public static void createprojectIndex(){
//        //ProjectIndex projectIndex = new ProjectIndex(HOST,CLUSTERNAME);
//        Mysql2ES.importProjects();
//    }
//    @Scheduled(cron="0 0 23 * * ?")
//    public static void createInvestfirmIndex(){
//       // InvestfirmsIndex investfirmIndex = new InvestfirmsIndex(HOST,CLUSTERNAME);
//        Mysql2ES.importInvestfirms();
//    }
//    @Scheduled(cron="0 0 23  * * ?")
//    public static void createInvestEventIndex(){
//        //InvestEventIndex investorIndex = new InvestEventIndex(HOST,CLUSTERNAME);
//        Mysql2ES.importInvestEvent();
//    }
//    @Scheduled(cron="0 0 23 * * ?")
//    public static void createMergeEventIndex(){
//        //MergeEventIndex mergeEventIndex = new MergeEventIndex(HOST,CLUSTERNAME);
//        Mysql2ES.importMergeEvent();
//    }
//    @Scheduled(cron="0 0 23 * * ?")
//    public static void createQuitEventIndex(){
//        //QuitEventIndex quitEventIndex = new QuitEventIndex(HOST,CLUSTERNAME);
//        //Mysql2ES.importQuitEvent();
//    }
//    @Scheduled(cron="0 0 23 * * ?")
//    public static void createLanuchEventIndex(){
//        //LaunchEventIndex quitEventIndex = new LaunchEventIndex(HOST,CLUSTERNAME);
//        //Mysql2ES.importLaunchIndex();
//    }
//}
