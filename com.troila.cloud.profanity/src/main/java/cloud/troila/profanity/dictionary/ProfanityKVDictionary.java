package cloud.troila.profanity.dictionary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cloud.troila.profanity.PatternUtil;

public class ProfanityKVDictionary implements WordDictionary{

	private List<RegxEntry> profanities = new ArrayList<>();
	
	private String defaultReplaceWord = null;

	private String urlPattern="/**";
	
	private int order;
	
	private List<String> ignoreFields = new ArrayList<>();
	
	

	public String getDefaultReplaceWord() {
		return defaultReplaceWord;
	}

	public void setDefaultReplaceWord(String defaultReplaceWord) {
		this.defaultReplaceWord = defaultReplaceWord;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public List<String> getIgnoreFields() {
		return ignoreFields;
	}

	public void setIgnoreFields(List<String> ignoreFields) {
		this.ignoreFields = ignoreFields;
	}

	public ProfanityKVDictionary(List<RegxEntry> profanities, String defaultReplaceWord, String urlPattern,
			List<String> ignoreFields) {
		super();
		this.profanities = profanities;
		this.defaultReplaceWord = defaultReplaceWord;
		this.urlPattern = urlPattern;
		this.ignoreFields = ignoreFields;
		this.profanities = this.profanities.stream().distinct().sorted(Comparator.comparing(RegxEntry::getRegx).reversed()).collect(Collectors.toList());
	}

	public ProfanityKVDictionary(List<RegxEntry> profanities, String urlPattern, List<String> ignoreFields) {
		super();
		this.profanities = profanities;
		this.urlPattern = urlPattern;
		this.ignoreFields = ignoreFields;
		this.profanities = this.profanities.stream().distinct().sorted(Comparator.comparing(RegxEntry::getRegx).reversed()).collect(Collectors.toList());
	}

	public ProfanityKVDictionary(List<RegxEntry> profanities, String urlPattern) {
		super();
		this.profanities = profanities;
		this.urlPattern = urlPattern;
		this.profanities = this.profanities.stream().distinct().sorted(Comparator.comparing(RegxEntry::getRegx).reversed()).collect(Collectors.toList());
	}

	@Override
	public String selectAndReplace(String uri, String key, String value) {
		boolean flag = false;
		if(PatternUtil.match(urlPattern, uri) && !ignoreFields.contains(key)) {			
			for(RegxEntry entry : this.profanities) {
				Pattern p = Pattern.compile(entry.getRegx(),entry.getFlag());
				Matcher m = p.matcher(value);
				if(m.find()) {
					if(!flag) {
						flag = true;
					}
					if(entry.getReplaceWord() == null) {					
						value = m.replaceAll(defaultReplaceWord);
					}else {
						value = m.replaceAll(entry.getReplaceWord());
					}
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
