package com.troila.cloud.profanity;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.troila.cloud.profanity.filter.ProfanityFilter;

public class ProfanityProcesserFilter implements Filter{

	ObjectMapper mapper = new ObjectMapper();
	
	private ProfanityFilter profanityfilter;
	
	public ProfanityProcesserFilter(ProfanityFilter profanityfilter) {
		super();
		this.profanityfilter = profanityfilter;
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
			ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse)response);
			chain.doFilter(request, wrapper);
			String result = wrapper.getResponseData("UTF-8");
			JsonNode root = mapper.readTree(result);
			selectJsonNodeFields(root);
			response.getOutputStream().write(root.toString().getBytes());
			String parameter = mapper.writeValueAsString(request.getParameterMap());
			System.out.println(parameter);
			System.out.println(root);
	}

	public void init(FilterConfig config) throws ServletException {
		System.out.println("初始化ProfanityProcesserFilter...");
	}

	private void selectJsonNodeFields(JsonNode jsonNode) {
		System.out.println(jsonNode.getNodeType());
		if(jsonNode.getNodeType().equals(JsonNodeType.ARRAY)) {
			Iterator<JsonNode> iterator = jsonNode.elements();
			while(iterator.hasNext()) {					
				selectJsonNodeFields(iterator.next());
			}
		}
		if(jsonNode.getNodeType().equals(JsonNodeType.OBJECT)) {			
			Iterator<String> it = jsonNode.fieldNames();
			while(it.hasNext()) {
				String field = it.next();
				JsonNode node = jsonNode.get(field);
				if(node.getNodeType().equals(JsonNodeType.STRING)) {
					ObjectNode target = (ObjectNode) jsonNode;
					target.put(field, profanityfilter.filter(node.asText()));
					System.out.println(field+"字段是string类型");
				}
				if(node.getNodeType().equals(JsonNodeType.ARRAY)) {
					Iterator<JsonNode> iterator = node.elements();
					while(iterator.hasNext()) {					
						selectJsonNodeFields(iterator.next());
					}
				}
				if(node.getNodeType().equals(JsonNodeType.OBJECT)) {
					selectJsonNodeFields(node);
				}
			}
		}
	}
}
