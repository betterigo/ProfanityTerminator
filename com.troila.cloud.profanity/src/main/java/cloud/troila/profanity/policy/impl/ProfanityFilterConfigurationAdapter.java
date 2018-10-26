package cloud.troila.profanity.policy.impl;

import javax.annotation.PostConstruct;

import cloud.troila.profanity.dictionary.ProfanityDictionary;
import cloud.troila.profanity.policy.DictionarySettings;

public class ProfanityFilterConfigurationAdapter extends DefaultProfanityConfiguration implements DictionarySettings{
	
	@Override
	public DictionarySettings addDictionary(ProfanityDictionary dictionary) {
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
	
	@PostConstruct
	public void init() {
		System.out.println("配置configadapter");
		this.configuration();
	}
}
