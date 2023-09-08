package com.tinyhuman.tinyhumanapi.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

public class MockMvcUtil {

    public static ObjectMapper OBJECT_MAPPER = createObjectMapper();

    public static MockMvc createMockMvc(Object controller, Object[] controllerAdvices) {
        return MockMvcBuilders
                .standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(MockMvcResultHandlers.print())
                .setControllerAdvice(controllerAdvices)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(createObjectMapper()),
                        new ResourceHttpMessageConverter())
                .build();
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}

