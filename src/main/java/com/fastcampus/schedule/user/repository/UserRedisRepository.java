package com.fastcampus.schedule.user.repository;

import com.fastcampus.schedule.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRedisRepository {
	private final RedisTemplate<String, User> userDtoRedisTemplate;

	private final static Duration USER_CACHE_TTL = Duration.ofDays(2);

	public void setRedisUser (User user){
		String key = getKey(user.getUsername());
		log.info("Set User to Redis {}({})", key, user);
		userDtoRedisTemplate.opsForValue().set(getKey(user.getUsername()),user);
	}



	public Optional<User>  getRedisUser(String userName ) {
		User data = userDtoRedisTemplate.opsForValue().get(getKey(userName));
		log.info("Get User from Redis {}", data);
		return Optional.ofNullable(data);
	}


	private String getKey(String userName){
		return "USER:" + userName;
	}

}
