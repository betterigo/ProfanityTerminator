package cloud.troila.profanity.policy.impl;

import java.util.ArrayList;
import java.util.List;

import cloud.troila.profanity.dictionary.ProfanityDictionary;
import cloud.troila.profanity.policy.ProfanityFilterConfiguration;

public class DefaultProfanityConfiguration implements ProfanityFilterConfiguration{

	List<ProfanityDictionary> dictionaries = new ArrayList<ProfanityDictionary>();
	
	List<String> commonIngoreFields = new ArrayList<>();
	
	protected String replaceWord = "******";
	
	@Override
	public String getReplaceWord() {
		return replaceWord;
	}
	
	public void setReplaceWord(String replaceWord) {
		this.replaceWord = replaceWord;
	}

	@Override
	public List<ProfanityDictionary> getDictionaries() {
		return dictionaries;
	}
	
	@Override
	public List<String> getCommonIngoreFields() {
		commonIngoreFields.add("gmtCreate");
		commonIngoreFields.add("gmtModify");
		commonIngoreFields.add("gmtDelete");
		return commonIngoreFields;
	}

}
