package cloud.troila.profanity.policy;

/**
 * 
 * @author haodonglei
 *
 */
public interface ProfanityPolicy {
	public void setConfig(ProfanityFilterConfiguration config);
	
	public ProfanityFilterConfiguration getConfig();
	/**
	 * 返回值为null证明没有发生替换
	 * @param uri
	 * @param key
	 * @param value
	 * @return
	 */
	public String match(String uri,String key,String value);
}
