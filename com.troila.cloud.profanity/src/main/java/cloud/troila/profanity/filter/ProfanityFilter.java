package cloud.troila.profanity.filter;

/**
 * 敏感词过滤器
 * @author haodonglei
 *
 */
public interface ProfanityFilter {
	public String filter(String uri,String key,String t);
}
