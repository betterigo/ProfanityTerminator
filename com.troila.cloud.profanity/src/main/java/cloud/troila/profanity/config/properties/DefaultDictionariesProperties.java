package cloud.troila.profanity.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="profanity.policy.dictionary.default")
public class DefaultDictionariesProperties {
	
	private boolean xss = true;
	
	private boolean sensitive = true;
	
	public boolean isXss() {
		return xss;
	}
	public void setXss(boolean xss) {
		this.xss = xss;
	}
	public boolean isSensitive() {
		return sensitive;
	}
	public void setSensitive(boolean sensitive) {
		this.sensitive = sensitive;
	}
	
}
