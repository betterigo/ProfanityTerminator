package cloud.troila.profanity.policy;

import java.util.List;

import cloud.troila.profanity.dictionary.ProfanityDictionary;

public interface ProfanityFilterConfiguration {
	public void configuration(ProfanityPolicy profanityPolicy);
	public String getReplaceWord();
	
	public List<ProfanityDictionary> getDictionaries();
	
	public List<String> getCommonIngoreFields();
}