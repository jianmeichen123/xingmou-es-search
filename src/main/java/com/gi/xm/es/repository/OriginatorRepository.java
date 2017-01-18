package com.gi.xm.es.repository;

import com.gi.xm.es.pojo.OriginatorDict;
import com.gi.xm.es.pojo.ProjectDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zcy on 17-1-17.
 */

public interface OriginatorRepository extends JpaRepository<OriginatorDict,Long> {

    @Query("select o from OriginatorDict o where o.isCoreMember = 0")
    List<OriginatorDict>  findAll();
}
