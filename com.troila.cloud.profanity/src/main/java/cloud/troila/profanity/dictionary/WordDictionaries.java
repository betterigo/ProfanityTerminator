package cloud.troila.profanity.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cloud.troila.profanity.dictionary.node.WordNode;

public class WordDictionaries {
	

	private static final Logger logger = LoggerFactory.getLogger(WordDictionaries.class);
	/**
	 * 返回一个默认的Xss攻击词典，该词典采用正则表达式匹配，在大文件中效率不好
	 * @return
	 */
	public static WordDictionary xssDictionary() {
		List<RegxEntry> profanities = new ArrayList<>();
		RegxEntry re1 = new RegxEntry("<(no)?script[^>]*>.*?</(no)?script>", "",Pattern.CASE_INSENSITIVE);
		RegxEntry re2 = new RegxEntry("eval\\((.*?)\\)","",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		RegxEntry re3 = new RegxEntry("expression\\((.*?)\\)","",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		RegxEntry re4 = new RegxEntry("(javascript:|vbscript:|view-source:)*","",Pattern.CASE_INSENSITIVE);
//		RegxEntry re5 = new RegxEntry("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>","",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		RegxEntry re6 = new RegxEntry("(window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()*","",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		RegxEntry re7 = new RegxEntry("(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+\\s*=\\\"?[^\\\"\\s]*\\\"?","$1_",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		profanities.add(re1);
		profanities.add(re2);
		profanities.add(re3);
		profanities.add(re4);
//		profanities.add(re5);
		profanities.add(re6);
		profanities.add(re7);
		ProfanityKVDictionary dictionary = new ProfanityKVDictionary(profanities, "", "/**",new ArrayList<>());
		dictionary.setOrder(2);
		return dictionary;
	}
	
	/**
	 * 返回一个使用Dfa算法的词典，该词典效率非常高，尤其在词典中的条目多的时候
	 * @param profanityWordInput 敏感词词典的inputStream 词典中的条目需要utf-8编码，用回车符分割
	 * @param skipWordInput 忽略字符词典，例如：白痴 白*痴 那么“*” 就可以列入文本中，用回车符分割
	 * @return
	 */
	public static WordDictionary DfaProfanityDictionary(InputStream profanityWordInput,InputStream skipWordInput) {
		BufferedReader br = null;
		BufferedReader sbr = null;
		Map<Integer, WordNode> profanities = new HashMap<>();
		List<Integer> skipChar = new ArrayList<>();
		List<Integer> fChar = new ArrayList<>();
		logger.info("加载敏感词库...开始");
		if(profanityWordInput == null) {
			br = new BufferedReader(new InputStreamReader(WordDictionaries.class.getClassLoader().getResourceAsStream("profanitywords.dic")));
		}else {
			br = new BufferedReader(new InputStreamReader(profanityWordInput));
		}
		if(skipWordInput == null) {
			sbr = new BufferedReader(new InputStreamReader(WordDictionaries.class.getClassLoader().getResourceAsStream("skipword.dic")));
		}else {
			sbr = new BufferedReader(new InputStreamReader(skipWordInput));
		}
		List<String> words = new ArrayList<>();
		//加载敏感词字符集
		try {
			String word = "";
			while((word = br.readLine())!=null) {
				if(word==null || word.trim().equals("")) {
					continue;
				}
				words.add(word);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("加载敏感词库...完成");
		//加载忽略字符集
		logger.info("加载敏感词库忽略字符集...开始");
		try {
			String sw ="";
			while((sw = sbr.readLine())!=null) {
				if(sw.length()>0) {
					char w = sw.charAt(0);
					skipChar.add((int) w);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				sbr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("加载敏感词库忽略字符集...完成");
		DfaKVDictionary dictionary = new DfaKVDictionary();
		for(String w : words) {
			char[] chs = w.toCharArray();
			int length = chs.length;
			int cur;
			int lastIndex;
			WordNode fWordNode = null;
			for(int i =0; i<length; i++) {
				cur = chs[i];
				if(i == 0) { //首字母
					if(!fChar.contains(cur)) {
						fChar.add(cur);
						fWordNode = new WordNode(cur, length == 1);
						profanities.put(cur, fWordNode);
					}else {
						fWordNode = profanities.get(cur);
						if(!fWordNode.isLast() && length == 1) {
							fWordNode.setLast(true);
						}
						
					}
				}
				lastIndex = length - 1;
				for(int j = 1; j<length ;j++) {					
					fWordNode = fWordNode.addSubNodeIfNoExist(chs[j], lastIndex == j);
				}
			}
		}
		dictionary.setSkipwords(skipChar);
		dictionary.setProfanities(profanities);
		dictionary.setOrder(1);
		logger.info("配置敏感词库完成！");
		return dictionary;
	}
}
