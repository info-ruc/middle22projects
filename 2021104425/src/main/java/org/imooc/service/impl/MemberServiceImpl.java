package org.imooc.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.imooc.bean.Member;
import org.imooc.cache.CodeCache;
import org.imooc.cache.TokenCache;
import org.imooc.dao.MemberDao;
import org.imooc.service.MemberService;
import org.imooc.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class MemberServiceImpl implements MemberService {

	@Resource
	private MemberDao memberDao;

	@Autowired
	private RedisTemplate redisTemplate;

	private final static Logger logger = LoggerFactory
			.getLogger(MemberService.class);

	@Override
	public boolean exists(Long phone) {
		Member member = new Member();
		member.setPhone(phone);
		List<Member> list = memberDao.select(member);
		return list != null && list.size() == 1;
	}

	@Override
	public boolean saveCode(Long phone, String code) {
		//将短信验证码存入redis
		ValueOperations valueOperations = redisTemplate.opsForValue();
		String key = String.valueOf(phone);
		Object o = valueOperations.get(key);
		if(o == null){
			valueOperations.set(key, MD5Util.getMD5(code),60, TimeUnit.SECONDS);
			return true;
		}
		return false;
		/*CodeCache codeCache = CodeCache.getInstance();
		return codeCache.save(phone, MD5Util.getMD5(code));*/
	}

	@Override
	public boolean sendCode(Long phone, String content) {
		logger.info(phone + "|" + content);
		return true;
	}

	@Override
	public String getCode(Long phone) {
		String key = String.valueOf(phone);
		Object o = redisTemplate.opsForValue().get(key);
		return String.valueOf(o);
		/*// TODO 在真实环境中，改成借助第三方实现
		CodeCache codeCache = CodeCache.getInstance();
		return codeCache.getCode(phone);*/
	}

	@Override
	public void saveToken(String token, Long phone) {
		TokenCache tokenCache = TokenCache.getInstance();
		tokenCache.save(token, phone);
	}

	@Override
	public Long getPhone(String token) {
		TokenCache tokenCache = TokenCache.getInstance();
		return tokenCache.getPhone(token);
	}

	@Override
	public Long getIdByPhone(Long phone) {
		Member member = new Member();
		member.setPhone(phone);
		List<Member> list = memberDao.select(member);
		if (list != null && list.size() == 1) {
			return list.get(0).getId();
		}
		return null;
	}
}
