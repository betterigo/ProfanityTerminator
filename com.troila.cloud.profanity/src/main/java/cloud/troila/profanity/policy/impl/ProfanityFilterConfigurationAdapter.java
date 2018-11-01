package cloud.troila.profanity.policy.impl;

import javax.annotation.PostConstruct;

import cloud.troila.profanity.dictionary.WordDictionary;
import cloud.troila.profanity.policy.DictionarySettings;

public class ProfanityFilterConfigurationAdapter extends DefaultProfanityConfiguration implements DictionarySettings{
	
	@Override
	public DictionarySettings addDictionary(WordDictionary dictionary) {
		super.getDictionaries().add(dictionary);
		return this;
	}
	
	public void configuration() {
		
	}

	@Override
	public DictionarySettings changeReplaceWord(String word) {
		super.setReplaceWord(word);
		return this;
	}
	
	public ProfanityFilterConfigurationAdapter setIngoreUriPatterns(String...patterns) {
		super.setIngoreUrlPatterns(patterns);
		return this;
	}
	
	public ProfanityFilterConfigurationAdapter setIngoreFields(String...fields) {
		super.setCommonIngoreFields(fields);
		return this;
	}
	@PostConstruct
	public void init() {
		System.out.println("配置configadapter");
		this.configuration();
	}
}
