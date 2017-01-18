package com.gi.xm.es.repository;

import com.gi.xm.es.pojo.InvestorDict;
import com.gi.xm.es.pojo.ProjectDict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zcy on 17-1-17.
 */

public interface InvestorRepository extends JpaRepository<InvestorDict,Long> {

    List<InvestorDict> findAll();
}
