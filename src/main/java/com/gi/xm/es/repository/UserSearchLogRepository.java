package com.gi.xm.es.repository;

import com.gi.xm.es.pojo.UserSearchLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by vincent on 16-12-19.
 */
@Repository
public interface UserSearchLogRepository extends MongoRepository<UserSearchLog, String> {

}
