package cloud.troila.profanity.policy;

import cloud.troila.profanity.dictionary.WordDictionary;

public interface DictionarySettings {
	
	public DictionarySettings changeReplaceWord(String word);
	
	public DictionarySettings addDictionary(WordDictionary dictionary);
}
