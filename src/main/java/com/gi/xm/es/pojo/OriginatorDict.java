package com.gi.xm.es.pojo;

import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zcy on 17-1-17.
 */
@Component
@Entity
@Table(name = "dm_project_person")
public class OriginatorDict {
    @Id
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "is_core_member")
    private String isCoreMember;

    public void setIsCoreMember(String isCoreMember) {
        this.isCoreMember = isCoreMember;
    }

    public String getIsCoreMember() {
        return isCoreMember;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
