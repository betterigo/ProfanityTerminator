package cloud.troila.profanity;

import java.util.Random;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class PatternUtil {
	private static PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
	private static char[] chars = "~@#%^&*()-+÷√θω┐".toCharArray();
	public static boolean match(String pattern,String uri) {
		return resourceLoader.getPathMatcher().match(pattern, uri);
	}
	
	/**
	 * 全角转半角字符
	 * @param fullWidthStr
	 * @return
	 */
	public static String fullWidth2halfWidth(String fullWidthStr) {
        if (null == fullWidthStr || fullWidthStr.length() <= 0) {
            return "";
        }
        char[] charArray = fullWidthStr.toCharArray();
        //对全角字符转换的char数组遍历
        for (int i = 0; i < charArray.length; ++i) {
            int charIntValue = (int) charArray[i];
            //如果符合转换关系,将对应下标之间减掉偏移量65248;如果是空格的话,直接做转换
            if (charIntValue >= 65281 && charIntValue <= 65374) {
                charArray[i] = (char) (charIntValue - 65248);
            } else if (charIntValue == 12288) {
                charArray[i] = (char) 32;
            }
        }
        return new String(charArray);
    } 
	
	public static char getIngoreWord() {
		        Random random=new Random();  
		        int number=random.nextInt(15);
		        return chars[number];
	}
}
