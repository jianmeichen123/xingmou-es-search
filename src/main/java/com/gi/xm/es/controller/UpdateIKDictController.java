package com.gi.xm.es.controller;

import com.gi.xm.es.pojo.*;
import com.gi.xm.es.repository.InvestFirmRepository;
import com.gi.xm.es.repository.InvestorRepository;
import com.gi.xm.es.repository.OriginatorRepository;
import com.gi.xm.es.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RestController
@RequestMapping(value = "/updateDict/")
public class UpdateIKDictController {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateIKDictController.class);



    @Autowired
    private ProjectRepository dictRepository;

    @Autowired
    private InvestFirmRepository investFirmRepository;

    @Autowired
    private OriginatorRepository originatorRepository;

    @Autowired
    private InvestorRepository investorRepository;
    /**
     * 更新ik自定义词典 @author zhangchunyuan
     */
    @RequestMapping(value="updateProject")
    @ResponseBody
    public  String  updateProject(HttpServletResponse response){
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (ProjectDict dict:dictRepository.findAll()) {
            sb.append(dict.getTitle()+"\n");
            i++;
        }
        response.setHeader("Last-Modified",i+"");
        response.setHeader("ETag",i+"");
        return sb.toString();
    }

    /**
     * 更新ik自定义词典 @author zhangchunyuan
     */
    @RequestMapping(value="updateInvestfirm")
    @ResponseBody
    public  String  updateInvestfirm(HttpServletResponse response){
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (InvestFirmDict dict:investFirmRepository.findAll()) {
            sb.append(dict.getName()+"\n");
            i++;
        }
        response.setHeader("Last-Modified",i+"");
        response.setHeader("ETag",i+"");
        return sb.toString();
    }

    /**
     * 更新ik自定义词典 @author zhangchunyuan
     */
    @RequestMapping(value="updateInvestor")
    @ResponseBody
    public  String  updateInvestor(HttpServletResponse response){
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (InvestorDict dict:investorRepository.findAll()) {
            sb.append(dict.getName()+"\n");
            i++;
        }
        response.setHeader("Last-Modified",i+"");
        response.setHeader("ETag",i+"");
        return sb.toString();
    }

    /**
     * 更新ik自定义词典 @author zhangchunyuan
     */
    @RequestMapping(value="updateOriginator")
    @ResponseBody
    public  String  updateOriginator(HttpServletResponse response){
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (OriginatorDict dict:originatorRepository.findAll()) {
            sb.append(dict.getName()+"\n");
            i++;
        }
        response.setHeader("Last-Modified",i+"");
        response.setHeader("ETag",i+"");
        return sb.toString();
    }
}