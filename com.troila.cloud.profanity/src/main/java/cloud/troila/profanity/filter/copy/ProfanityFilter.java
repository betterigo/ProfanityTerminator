package cloud.troila.profanity.filter.copy;

/**
 * 敏感词过滤器
 * @author haodonglei
 *
 */
public interface ProfanityFilter {
	public <T> T filter(String uri,String key,T t);
}
