package com.troila.cloud.profanity.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.troila.cloud.profanity.ProfanityProcesserFilter;
import com.troila.cloud.profanity.filter.ProfanityFilter;

@Configuration
public class ProfanityFilterConfiguration {
	
	@Autowired
	private ProfanityFilter profanityFilter;
	
	@Bean
	public FilterRegistrationBean<Filter> createFilter(){
		ProfanityProcesserFilter filter = new ProfanityProcesserFilter(profanityFilter);
		FilterRegistrationBean<Filter> register = new FilterRegistrationBean<Filter>();
		register.setFilter(filter);
		register.setOrder(Ordered.LOWEST_PRECEDENCE);
		return register;
	}
}
