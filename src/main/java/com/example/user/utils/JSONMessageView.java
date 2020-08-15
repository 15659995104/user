package com.example.user.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.web.servlet.View;

public class JSONMessageView implements View {
	/**
	 *  消息码
	 */
	private int code;
	/**
	 *  消息内容
	 */
	private String message;
	/**
	 *  数据内容
	 */
	private Object content;

	private String contentType = "application/json";

	public JSONMessageView() {
	};

	public JSONMessageView(int code, String message, Object content) {
		super();
		this.code = code;
		this.message = message;
		this.content = content;
	}

	public JSONMessageView(long code, String message, Object content) {
		this((int) code, message, content);
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void render(Map<String, ?> arg0, HttpServletRequest arg1, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Type", getContentType());
		response.getWriter().write(toString());
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {  
            @Override  
            public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {  
                arg1.writeString("");  
            }  
        });  
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw, this);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(new JSONMessageView(-1, "错误", ""));
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
}
