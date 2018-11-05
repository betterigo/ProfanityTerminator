package cloud.troila.profanity.config;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import cloud.troila.profanity.ProfanityProcesserFilter;
import cloud.troila.profanity.config.properties.DefaultDictionariesProperties;
import cloud.troila.profanity.config.properties.IgnoreSettings;
import cloud.troila.profanity.dictionary.WordDictionaries;
import cloud.troila.profanity.filter.ProfanityFilter;
import cloud.troila.profanity.filter.impl.DefaultProfanityFilter;
import cloud.troila.profanity.policy.ProfanityFilterConfiguration;
import cloud.troila.profanity.policy.ProfanityPolicy;
import cloud.troila.profanity.policy.impl.DefaultProfanityConfiguration;
import cloud.troila.profanity.policy.impl.DefaultProfanityPolicy;
import cloud.troila.profanity.policy.impl.ProfanityFilterConfigurationAdapter;

@Configuration
@EnableConfigurationProperties({DefaultDictionariesProperties.class,IgnoreSettings.class})
@ConditionalOnClass(ProfanityFilter.class)
@ConditionalOnProperty(name="profanity.filter",havingValue="true",matchIfMissing=true)
public class ProfanityAutoConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(ProfanityAutoConfiguration.class);
	/*
	 * 配置config
	 */
	@Configuration
	@ConditionalOnMissingBean(value=ProfanityFilterConfigurationAdapter.class)
	static class InnerConfig1{
		

		
		@Autowired
		private DefaultDictionariesProperties defaultDictionariesProperties;
		
		@Autowired
		private IgnoreSettings ignoreSettings;
		
		@Bean
		public ProfanityFilterConfiguration createConfig() {
			logger.info("创建configuration");
			DefaultProfanityConfiguration config = new DefaultProfanityConfiguration();
			if(defaultDictionariesProperties.isXss()) {
				config.getDictionaries().add(WordDictionaries.xssDictionary());
			}
			if(defaultDictionariesProperties.isSensitive()) {
				config.getDictionaries().add(WordDictionaries.DfaProfanityDictionary(null, null));
			}
			if(ignoreSettings.getIgnoreUrlsList()!=null) {
				config.setIngoreUrlPatterns(ignoreSettings.getIgnoreUrlsList());
			}
			if(ignoreSettings.getIgnoreFieldsList()!=null) {
				config.setCommonIngoreFields(ignoreSettings.getIgnoreFieldsList());
			}
			return config;
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
			logger.info("创建policy");
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
			logger.info("创建filter");
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
			logger.info("创建processer");
			ProfanityProcesserFilter filter = new ProfanityProcesserFilter(profanityFilter);
			FilterRegistrationBean<Filter> register = new FilterRegistrationBean<Filter>();
			register.setFilter(filter);
			register.setOrder(Ordered.LOWEST_PRECEDENCE);
			return register;
		}
		
	}
	
}
