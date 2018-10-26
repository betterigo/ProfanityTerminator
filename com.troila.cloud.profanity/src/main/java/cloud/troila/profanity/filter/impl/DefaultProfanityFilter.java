package cloud.troila.profanity.filter.impl;

import cloud.troila.profanity.filter.ProfanityFilter;
import cloud.troila.profanity.policy.ProfanityPolicy;

public class DefaultProfanityFilter implements ProfanityFilter {

	private ProfanityPolicy profanityPolicy;
	
	public DefaultProfanityFilter(ProfanityPolicy profanityPolicy) {
		super();
		this.profanityPolicy = profanityPolicy;
	}

	public String filter(String uri,String key,String t) {
		if(t instanceof String) {
			String result = profanityPolicy.match(uri, key, (String) t);
			if(result == null) {
				return null;
			}else {
				return result;
			}
		}
		return t;
	}

}
