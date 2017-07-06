package be.fgov.caamihziv.services.quickmon.intrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gs on 26.04.17.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public BeanPostProcessor processObjectMappers() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof ObjectMapper) {
                    ObjectMapper mapper = (ObjectMapper) bean;
                    mapper.registerModule(new JavaTimeModule());
                    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                    mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
                }
                return bean;
            }
        };

    }

}
