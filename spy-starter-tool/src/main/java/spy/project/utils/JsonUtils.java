package spy.project.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class JsonUtils {

    public static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        //在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        //在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss
        objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS,false);
        //在序列化时忽略值为 null 的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat(DateUtils.Date_Format_YMDHMS));
        objectMapper.registerModule(new JavaTimeModule());

    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T toObject(String jsonStr, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(jsonStr, typeReference);
    }

    public static <T> T toObject(String jsonStr, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonStr, clazz);
    }


}
