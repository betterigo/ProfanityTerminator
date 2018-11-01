package cloud.troila.profanity.dictionary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cloud.troila.profanity.PatternUtil;

public class ProfanityDictionary implements WordDictionary {
	
	private List<String> profanityWords = null;
	
	private List<Pattern> patterns = null;
	
	private String urlPattern;

	private String replaceWord;
	
	private int order;
	
	private List<String> ignoreFields = null;
	
	public ProfanityDictionary(String urlPattern,String... profanityWords) {
		super();
		this.urlPattern = urlPattern;
		this.profanityWords = new ArrayList<String>();
		this.patterns = new ArrayList<>();
		for(String pw:profanityWords) {
			this.profanityWords.add(pw);
		}
		this.profanityWords = this.profanityWords.stream().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList()); //默认排序
		for(String pw : this.profanityWords) {
			Pattern p = Pattern.compile(pw);
			this.patterns.add(p);
		}
	}
	
	public ProfanityDictionary(String urlPattern,String replaceWord,String... profanityWords) {
		super();
		this.urlPattern = urlPattern;
		this.replaceWord = replaceWord;
		this.profanityWords = new ArrayList<String>();
		this.patterns = new ArrayList<>();
		for(String pw:profanityWords) {
			this.profanityWords.add(pw);
		}
		this.profanityWords = this.profanityWords.stream().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList()); //默认排序
		for(String pw : this.profanityWords) {
			Pattern p = Pattern.compile(pw);
			this.patterns.add(p);
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
		boolean flag = false;
		if(PatternUtil.match(urlPattern, uri) && !ignoreFields.contains(key)) {
			for(Pattern p:patterns) {
				Matcher m = p.matcher(value);
				if(m.find()) {
					if(!flag) {
						flag = true;
					}
					value = m.replaceAll(replaceWord);
				}
			}
		}
		return flag?value:null;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}
}
