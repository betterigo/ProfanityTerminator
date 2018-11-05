package cloud.troila.profanity.config.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="profanity.policy")
public class IgnoreSettings {
	
	private String ignoreUrls="";

	private String ignoreFields="";
	
	public void setIgnoreUrls(String ignoreUrls) {
		this.ignoreUrls = ignoreUrls;
	}

	public void setIgnoreFields(String ignoreFields) {
		this.ignoreFields = ignoreFields;
	}


	public List<String> getIgnoreUrlsList() {
		String[] arrayStr = ignoreUrls.split(";");
		List<String> result = new ArrayList<>();
		for(String o:arrayStr) {
			if(o!=null && !"".equals(o))
			result.add(o);
		}
		return result;
	}
	
	public List<String> getIgnoreFieldsList(){
		String[] arrayStr = ignoreFields.split(";");
		List<String> result = new ArrayList<>();
		for(String o:arrayStr) {
			if(o!=null && !"".equals(o))
			result.add(o);
		}
		return result;
	}
}
