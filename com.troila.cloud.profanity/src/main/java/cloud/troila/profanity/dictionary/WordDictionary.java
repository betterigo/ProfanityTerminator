package cloud.troila.profanity.dictionary;

public interface WordDictionary {
	public String selectAndReplace(String uri,String key,String value);
	
	public int getOrder();
}
