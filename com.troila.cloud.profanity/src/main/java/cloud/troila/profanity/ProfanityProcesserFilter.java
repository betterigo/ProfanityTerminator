package cloud.troila.profanity;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cloud.troila.profanity.filter.ProfanityFilter;

public class ProfanityProcesserFilter implements Filter{

	ObjectMapper mapper = new ObjectMapper();
	
	private ProfanityFilter profanityfilter;
	
	public ProfanityProcesserFilter(ProfanityFilter profanityfilter) {
		super();
		this.profanityfilter = profanityfilter;
	}

	public void destroy() {
	}

	/*
	 * ResponseWrapper 会代理response方法，对于需要直接输出到页面的内容，例如下载的数据，不要进行包装，不然会把数据先读到wrapper里面，造成页面响应速度很慢
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//是否在屏蔽列表里面
		String uri = ((HttpServletRequest)request).getRequestURI();
		if(!matchUri(uri)) {			
			ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse)response);
			chain.doFilter(request, wrapper);
			if(response.getContentType() != null && response.getContentType().contains("application/json")) {				
				String result = new String(wrapper.getBytes(),"utf-8");
				JsonNode root = mapper.readTree(result);
				selectJsonNodeFields(uri,root);
				response.getOutputStream().write(root.toString().getBytes());
			}else {
				byte[] b = wrapper.getBytes();
				response.setContentLength(b.length);
				response.getOutputStream().write(b);
			}
		}else {
			chain.doFilter(request, response);
		}
				
	}

	public void init(FilterConfig config) throws ServletException {
		System.out.println("初始化ProfanityProcesserFilter...");
	}

	private void selectJsonNodeFields(String uri,JsonNode jsonNode) {
		if(jsonNode.getNodeType().equals(JsonNodeType.ARRAY)) {
			Iterator<JsonNode> iterator = jsonNode.elements();
			while(iterator.hasNext()) {					
				selectJsonNodeFields(uri,iterator.next());
			}
		}
		if(jsonNode.getNodeType().equals(JsonNodeType.OBJECT)) {			
			Iterator<String> it = jsonNode.fieldNames();
			while(it.hasNext()) {
				String field = it.next();
				JsonNode node = jsonNode.get(field);
				if(node.getNodeType().equals(JsonNodeType.STRING)) {
					ObjectNode target = (ObjectNode) jsonNode;
					String result = null;
					if((result = profanityfilter.filter(uri,field,node.asText()))!=null) {						
						target.put(field, result);
					}
				}
				if(node.getNodeType().equals(JsonNodeType.ARRAY)) {
					Iterator<JsonNode> iterator = node.elements();
					while(iterator.hasNext()) {					
						selectJsonNodeFields(uri,iterator.next());
					}
				}
				if(node.getNodeType().equals(JsonNodeType.OBJECT)) {
					selectJsonNodeFields(uri,node);
				}
			}
		}
	}
	
	private boolean matchUri(String uri) {
		for(String uriPattern : profanityfilter.getPolicy().getConfig().getIngoreUriPatterns()) {
			if(PatternUtil.match(uriPattern, uri)) {
				return true;
			}
		}
		return false;
	}
}
