package com.connect.system.utils;

import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.exception.ProfileRoleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ProfileRole.class, new ProfileRoleException());
        objectMapper.registerModule(simpleModule);

        return objectMapper;
    }
}