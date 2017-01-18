package com.gi.xm.es.repository;

import com.gi.xm.es.pojo.InvestFirmDict;
import com.gi.xm.es.pojo.ProjectDict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zcy on 17-1-17.
 */

public interface InvestFirmRepository extends JpaRepository<InvestFirmDict,Long> {

    List<InvestFirmDict> findAll();
}
