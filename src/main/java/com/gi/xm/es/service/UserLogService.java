package com.gi.xm.es.service;

import com.gi.xm.es.pojo.UserSearchLog;
import com.gi.xm.es.repository.UserSearchLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by vincent on 16-12-20.
 */
@Service
public class UserLogService {

    @Autowired
    private UserSearchLogRepository userSearchLogRepository;

    public void addUserSearchLog(UserSearchLog userSearchLog){
        userSearchLogRepository.save(userSearchLog);
    }
}
