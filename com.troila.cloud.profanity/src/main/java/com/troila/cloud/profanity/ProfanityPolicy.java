package com.troila.cloud.profanity;

/**
 * 
 * @author haodonglei
 *
 */
public interface ProfanityPolicy {
	public <T> String match(T t);
}
