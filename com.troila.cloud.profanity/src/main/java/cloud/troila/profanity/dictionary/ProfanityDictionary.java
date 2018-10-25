package cloud.troila.profanity.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cloud.troila.profanity.PatternUtil;

public class ProfanityDictionary {
	
	private List<String> profanityWords = null;
	
	private String urlPattern;

	private String replaceWord;
	
	private List<String> ignoreFields = null;
	
	public ProfanityDictionary(String urlPattern,String... profanityWords) {
		super();
		this.urlPattern = urlPattern;
		this.profanityWords = new ArrayList<String>();
		for(String pw:profanityWords) {
			this.profanityWords.add(pw);
		}
	}
	
	public ProfanityDictionary(String urlPattern,String replaceWord,String... profanityWords) {
		super();
		this.urlPattern = urlPattern;
		this.replaceWord = replaceWord;
		this.profanityWords = new ArrayList<String>();
		for(String pw:profanityWords) {
			this.profanityWords.add(pw);
		}
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public String getReplaceWord() {
		return replaceWord;
	}

	public void setReplaceWord(String replaceWord) {
		this.replaceWord = replaceWord;
	}
	
	public List<String> getIgnoreFields() {
		return ignoreFields;
	}

	public void setIgnoreFields(List<String> ignoreFields) {
		this.ignoreFields = ignoreFields;
	}

	/**
	 * 根据字典来查询和替换敏感词。返回值为null则证明没有发生替换
	 * @param uri
	 * @param key
	 * @param value
	 * @return
	 */
	public String selectAndReplace(String uri,String key,String value) {
		if(PatternUtil.match(urlPattern, uri) && !ignoreFields.contains(key)) {
			Optional<String> result = profanityWords.stream().filter(word->{
				return value.matches(word);
			}).map(word->{
				return value.replaceAll(word, this.replaceWord);
			}).findFirst();
			return result.orElse(null);
		}
		return null;
	}
}
