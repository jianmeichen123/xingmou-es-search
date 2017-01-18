package com.gi.xm.es.pojo;

import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zcy on 17-1-17.
 */
@Component
@Entity
@Table(name = "dm_es_project")
public class ProjectDict implements Serializable{

    @Id
    private Long id;
    @Column(name = "title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
