package com.bridgelabz.googlekeep.repository;

import com.bridgelabz.googlekeep.model.RedisModel;

public interface IRedisRepository {
	void add(RedisModel model);
    long delete(String token);
    RedisModel findUser(String token);
}
