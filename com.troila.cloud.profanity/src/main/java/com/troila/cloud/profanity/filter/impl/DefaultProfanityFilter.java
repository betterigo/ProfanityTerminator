package com.troila.cloud.profanity.filter.impl;

import com.troila.cloud.profanity.filter.ProfanityFilter;

public class DefaultProfanityFilter implements ProfanityFilter {

	@SuppressWarnings("unchecked")
	public <T> T filter(T t) {
		if(t instanceof String) {
			t = (T) (t+"已经过滤");
		}
		return t;
	}

}
