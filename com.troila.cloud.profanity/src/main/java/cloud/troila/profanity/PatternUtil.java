package cloud.troila.profanity;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class PatternUtil {
	private static PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
	
	public static boolean match(String pattern,String uri) {
		return resourceLoader.getPathMatcher().match(pattern, uri);
	}
}
