package cloud.troila.profanity.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cloud.troila.profanity.PatternUtil;
import cloud.troila.profanity.dictionary.node.WordNode;

public class DfaKVDictionary implements WordDictionary{
	private Map<Integer, WordNode> profanities = new HashMap<>(); //敏感词库
	
	private List<Integer> skipwords = new ArrayList<>();
	
	private List<String> ignoreFields = new ArrayList<>();
	
	private int order;
	
	public List<String> getIgnoreFields() {
		return ignoreFields;
	}

	public void setIgnoreFields(List<String> ignoreFields) {
		this.ignoreFields = ignoreFields;
	}

	public void setProfanities(Map<Integer, WordNode> profanities) {
		this.profanities = profanities;
	}

	public void setSkipwords(List<Integer> skipwords) {
		this.skipwords = skipwords;
	}

	@Override
	public String selectAndReplace(String uri, String key, String value) {
		if(ignoreFields.contains(key)) {//忽略屏蔽词
			return null;
		}
		value = PatternUtil.fullWidth2halfWidth(value);
		char[] chs = value.toCharArray();
		int length = chs.length;
		int cur;
		int k;
		int seed;
		WordNode node;
		for(int i=0;i<length;i++) {
			seed = 0;
			cur = charConvert(chs[i]);//转为小写
			if(!this.profanities.containsKey(cur)) {
				continue;
			}
			node = this.profanities.get(cur);
			if(node == null) {
				continue;
			}
			boolean isMark = false;
			int markNum = -1;
			if(node.isLast()) {
				isMark = true;
				markNum = 0;
			}
			seed = i;
			for(k=i+1;k<length;k++) {
				int curNext = charConvert(chs[k]);
				if(skipwords.contains(curNext)) {
					continue;
				}
				node = node.searchSubNode(curNext);
				if(node == null) {
					break; //到头了
				}
				if(node.isLast()) {
					isMark = true;
					markNum = k-seed;
				}
			}
			if(isMark) {
				for(k = 0;k<=markNum;k++) {
					chs[k+i] = PatternUtil.getIngoreWord();
				}
				i = i + markNum;
			}
		}
		return new String(chs);
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return order;
	}
	private int charConvert(char r) {
		return (r >= 'A' && r <= 'Z') ? r + 32 : r;
	}

}
