package com.troila.cloud.profanity.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.troila.cloud.profanity.filter.ProfanityFilter;
import com.troila.cloud.profanity.filter.impl.DefaultProfanityFilter;

@Configuration
@AutoConfigureBefore(value=ProfanityFilterConfiguration.class)
public class ProfanityAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ProfanityFilter.class)
	public ProfanityFilter create() {
		return new DefaultProfanityFilter();
	}
}
