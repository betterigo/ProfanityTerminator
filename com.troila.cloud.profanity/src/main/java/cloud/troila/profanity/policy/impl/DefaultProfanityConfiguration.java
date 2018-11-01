package cloud.troila.profanity.policy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cloud.troila.profanity.dictionary.WordDictionary;
import cloud.troila.profanity.policy.ProfanityFilterConfiguration;

public class DefaultProfanityConfiguration implements ProfanityFilterConfiguration{

	List<WordDictionary> dictionaries = new ArrayList<>();
	
	List<String> commonIngoreFields = new ArrayList<>();
	
	List<String> ingoreUrlPatterns = new ArrayList<>();
	
	protected String replaceWord = "******";
	
	@Override
	public String getReplaceWord() {
		return replaceWord;
	}
	
	public void setReplaceWord(String replaceWord) {
		this.replaceWord = replaceWord;
	}

	@Override
	public List<WordDictionary> getDictionaries() {
		return dictionaries;
	}
	
	@Override
	public List<String> getCommonIngoreFields() {
		return commonIngoreFields;
	}

	@Override
	public List<String> getIngoreUriPatterns() {
		return ingoreUrlPatterns;
	}

	void setIngoreUrlPatterns(String... patterns) {
		for(String p : patterns) {			
			this.ingoreUrlPatterns.add(p);
		}
		this.ingoreUrlPatterns = this.ingoreUrlPatterns.stream().distinct().collect(Collectors.toList());
	}
	
	void setCommonIngoreFields(String...fields) {
		for(String f : fields) {
			this.commonIngoreFields.add(f);
		}
		this.commonIngoreFields = this.commonIngoreFields.stream().distinct().collect(Collectors.toList());
	}
}
