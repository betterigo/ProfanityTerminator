package cloud.troila.profanity.policy.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import cloud.troila.profanity.dictionary.DfaKVDictionary;
import cloud.troila.profanity.dictionary.ProfanityDictionary;
import cloud.troila.profanity.dictionary.ProfanityKVDictionary;
import cloud.troila.profanity.dictionary.WordDictionary;
import cloud.troila.profanity.policy.ProfanityFilterConfiguration;
import cloud.troila.profanity.policy.ProfanityPolicy;

public class DefaultProfanityPolicy implements ProfanityPolicy{

	private ProfanityFilterConfiguration config;
	
	private List<WordDictionary> dicrionaries = null;

	public void setConfig(ProfanityFilterConfiguration config) {
		this.config = config;
	}

	@Override
	public ProfanityFilterConfiguration getConfig() {
		return config;
	}
	/*
	 * 初始化敏感词策略，通过配置项获取词典
	 */
	@PostConstruct
	private void init() {
		if(this.config == null) {
			return;
		}
		this.dicrionaries = new ArrayList<>();
		for(WordDictionary pd:config.getDictionaries()) {
			if(pd instanceof ProfanityDictionary) {				
				if(((ProfanityDictionary) pd).getReplaceWord() == null) {
					((ProfanityDictionary) pd).setReplaceWord(config.getReplaceWord());//设置通用的替换字符
				}
				//处理屏蔽字段
				if(((ProfanityDictionary) pd).getIgnoreFields() == null) {
					((ProfanityDictionary) pd).setIgnoreFields(config.getCommonIngoreFields());
				}else {
					((ProfanityDictionary) pd).getIgnoreFields().addAll(config.getCommonIngoreFields());
				}
			}
			if(pd instanceof ProfanityKVDictionary) {
				if(((ProfanityKVDictionary) pd).getDefaultReplaceWord()==null) {
					((ProfanityKVDictionary) pd).setDefaultReplaceWord(config.getReplaceWord());
				}
				if(((ProfanityKVDictionary) pd).getIgnoreFields() == null) {
					((ProfanityKVDictionary) pd).setIgnoreFields(config.getCommonIngoreFields());
				}else {
					((ProfanityKVDictionary) pd).getIgnoreFields().addAll(config.getCommonIngoreFields());
				}
			}
			if(pd instanceof DfaKVDictionary) {
				((DfaKVDictionary) pd).setIgnoreFields(config.getCommonIngoreFields());
			}
			this.dicrionaries.add(pd);
			//根据order排序
			this.dicrionaries = this.dicrionaries.stream().sorted(Comparator.comparing(WordDictionary::getOrder)).collect(Collectors.toList());
		}
	}
	
	@Override
	public String match(String uri, String key, String value) {
		String result1 = null;
		String result2 = null;
		if(this.dicrionaries!=null) {
			for(WordDictionary pd: this.dicrionaries) {
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
