package utils.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class JsonUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public JsonUtils() {
	}

	public static ObjectMapper getMapper() {
		return MAPPER;
	}

	public static void writeValue(OutputStream out, Object value) {
		try {
			MAPPER.writeValue(out, value);
		} catch (IOException var3) {
			LOGGER.error("write:{}", value.toString(), var3);
		}

	}

	public static <T> T readValue(String content, Class<T> objectType) {
		try {
			return MAPPER.readValue(content, objectType);
		} catch (IOException var3) {
			LOGGER.error("read:{}", content, var3);
			return null;
		}
	}

	public static String writeValue(Object value) {
		try {
			return MAPPER.writeValueAsString(value);
		} catch (JsonProcessingException var2) {
			LOGGER.error("{}", value.toString(), var2);
			return null;
		}
	}

	static {
		MAPPER.setSerializationInclusion(Include.NON_NULL);
		MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		MAPPER.registerModule(new JavaTimeModule());
	}
}
