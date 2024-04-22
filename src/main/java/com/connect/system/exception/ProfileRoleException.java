package com.connect.system.exception;

import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ProfileRoleException extends JsonDeserializer<ProfileRole> {

    @Override
    public ProfileRole deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        try {
            return ProfileRole.valueOf(node.asText());
        } catch (IllegalArgumentException e) {
            throw new RegraNegocioException("A role fornecida não é válida para criação de usuário.", HttpStatus.BAD_REQUEST);
        }
    }
}
