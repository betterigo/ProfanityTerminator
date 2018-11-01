package cloud.troila.profanity.policy;

import java.util.List;

import cloud.troila.profanity.dictionary.WordDictionary;

public interface ProfanityFilterConfiguration {
	public String getReplaceWord();
	
	public List<WordDictionary> getDictionaries();
	
	public List<String> getCommonIngoreFields();
	
	public List <String> getIngoreUriPatterns();
	
}
