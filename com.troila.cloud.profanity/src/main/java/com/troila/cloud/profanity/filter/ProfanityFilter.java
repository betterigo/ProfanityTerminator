package com.troila.cloud.profanity.filter;

/**
 * 敏感词过滤器
 * @author haodonglei
 *
 */
public interface ProfanityFilter {
	public <T> T filter(T t);
}
