package cloud.troila.profanity.filter;

import cloud.troila.profanity.policy.ProfanityPolicy;

/**
 * 敏感词过滤器
 * @author haodonglei
 *
 */
public interface ProfanityFilter {
	public String filter(String uri,String key,String t);
	
	public ProfanityPolicy getPolicy();
}
