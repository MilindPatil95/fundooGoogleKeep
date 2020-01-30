package com.bridgelabz.googlekeep.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.bridgelabz.googlekeep.model.RedisModel;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RedisRepositoryImpl implements IRedisRepository {

	final static private String KEY = "UserToken";
	private RedisTemplate<String, Object> redisTemplate;

	private HashOperations hashOperations;

	@Autowired
	public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public RedisModel findUser(String token) {
		return (RedisModel) hashOperations.get(token, KEY);
	}

	@Override
	public void add(RedisModel model) {
		hashOperations.put(model.getToken(), KEY, model);
	}

	@Override
	public long delete(String token) {
		return hashOperations.delete(token, KEY);
	}
}
