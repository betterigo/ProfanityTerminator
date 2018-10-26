package cloud.troila.profanity.policy;

import cloud.troila.profanity.dictionary.ProfanityDictionary;

public interface DictionarySettings {
	
	public DictionarySettings changeReplaceWord(String word);
	
	public DictionarySettings addDictionary(ProfanityDictionary dictionary);
}
