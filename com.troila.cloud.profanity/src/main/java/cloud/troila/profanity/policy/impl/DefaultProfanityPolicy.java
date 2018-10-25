package cloud.troila.profanity.policy.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import cloud.troila.profanity.dictionary.ProfanityDictionary;
import cloud.troila.profanity.policy.ProfanityFilterConfiguration;
import cloud.troila.profanity.policy.ProfanityPolicy;

public class DefaultProfanityPolicy implements ProfanityPolicy{

	private ProfanityFilterConfiguration config;
	
	private List<ProfanityDictionary> dicrionaries = null;

	public void setConfig(ProfanityFilterConfiguration config) {
		this.config = config;
	}

	/*
	 * 初始化敏感词策略，通过配置项获取词典
	 */
	@PostConstruct
	private void init() {
		if(this.config == null) {
			return;
		}
		this.dicrionaries = new ArrayList<ProfanityDictionary>();
		for(ProfanityDictionary pd:config.getDictionaries()) {
			if(pd.getReplaceWord() == null) {
				pd.setReplaceWord(config.getReplaceWord());//设置通用的替换字符
			}
			//处理屏蔽字段
			if(pd.getIgnoreFields() == null) {
				pd.setIgnoreFields(config.getCommonIngoreFields());
			}else {
				pd.getIgnoreFields().addAll(config.getCommonIngoreFields());
			}
			this.dicrionaries.add(pd);
		}
	}
	
	@Override
	public String match(String uri, String key, String value) {
		String result1 = null;
		String result2 = null;
		if(this.dicrionaries!=null) {
			for(ProfanityDictionary pd: this.dicrionaries) {
				result1 = pd.selectAndReplace(uri, key, value);
				if(result1 != null) {
					result2 = result1;
					value = result2;//value值发生了变化
				}
			}
		}
		return result2;
	}
}
