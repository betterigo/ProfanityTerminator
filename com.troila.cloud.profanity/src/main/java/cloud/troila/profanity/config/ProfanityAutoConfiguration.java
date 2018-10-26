package cloud.troila.profanity.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import cloud.troila.profanity.ProfanityProcesserFilter;
import cloud.troila.profanity.filter.ProfanityFilter;
import cloud.troila.profanity.filter.impl.DefaultProfanityFilter;
import cloud.troila.profanity.policy.ProfanityFilterConfiguration;
import cloud.troila.profanity.policy.ProfanityPolicy;
import cloud.troila.profanity.policy.impl.DefaultProfanityConfiguration;
import cloud.troila.profanity.policy.impl.DefaultProfanityPolicy;
import cloud.troila.profanity.policy.impl.ProfanityFilterConfigurationAdapter;

@Configuration
@ConditionalOnClass(ProfanityFilter.class)
public class ProfanityAutoConfiguration {
	
	/*
	 * 配置config
	 */
	@Configuration
	@ConditionalOnMissingBean(value=ProfanityFilterConfigurationAdapter.class)
	static class InnerConfig1{
		
		@Bean
		public ProfanityFilterConfiguration createConfigss() {
			System.out.println("创建configuration");
			return new DefaultProfanityConfiguration();
			
		}
		
	}
	/*
	 * 配置policy
	 */
	@Configuration
	static class InnerConfig2{
		
		@Autowired
		private ProfanityFilterConfiguration config;
		
		@Bean
		@ConditionalOnMissingBean(ProfanityPolicy.class)
		public ProfanityPolicy createPolicy() {
			ProfanityPolicy policy = new DefaultProfanityPolicy();
			policy.setConfig(config);
			System.out.println("创建policy");
			return policy;
		}
		
	}
	/*
	 * 配置filter
	 */
	@Configuration
	static class InnerConfig3{

		@Autowired
		private ProfanityPolicy policy;
		
		@Bean
		@ConditionalOnMissingBean(ProfanityFilter.class)
		public ProfanityFilter create() {
			System.out.println("创建filter");
			return new DefaultProfanityFilter(policy);
		}
		
	}
	/*
	 * 配置processor
	 */
	@Configuration
	static class InnerConfig4{
		
		@Autowired
		private ProfanityFilter profanityFilter;

		@Bean
		public FilterRegistrationBean<Filter> createFilter(){
			System.out.println("创建processer");
			ProfanityProcesserFilter filter = new ProfanityProcesserFilter(profanityFilter);
			FilterRegistrationBean<Filter> register = new FilterRegistrationBean<Filter>();
			register.setFilter(filter);
			register.setOrder(Ordered.LOWEST_PRECEDENCE);
			return register;
		}
		
	}
	
}
