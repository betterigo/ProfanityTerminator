package cloud.troila.profanity.filter.impl;

import cloud.troila.profanity.filter.ProfanityFilter;
import cloud.troila.profanity.policy.ProfanityPolicy;

public class DefaultProfanityFilter implements ProfanityFilter {

	private ProfanityPolicy profanityPolicy;
	
	public DefaultProfanityFilter(ProfanityPolicy profanityPolicy) {
		super();
		this.profanityPolicy = profanityPolicy;
	}

	@SuppressWarnings("unchecked")
	public <T> T filter(String uri,String key,T t) {
		if(t instanceof String) {
			Object result = profanityPolicy.match(uri, key, (String) t);
			if(result == null) {
				return null;
			}else {
				return (T) result;
			}
		}
		return t;
	}

}
