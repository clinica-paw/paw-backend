package com.clinicapaw.backend_clinicapaw.presentation.advice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.authorization.method.MethodAuthorizationDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityErrorHandler implements MethodAuthorizationDeniedHandler {

    @Override
    public Object handleDeniedInvocation(MethodInvocation methodInvocation, AuthorizationResult authorizationResult) {
        log.info("\n\n\n");
        log.info(String.format("Method info -> %s", methodInvocation.toString()));
        log.info(String.format("Is authorized? %s", authorizationResult.isGranted()));

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("status", 401);
        jsonNode.put("message", "Not authorized");

        try {
            return mapper.writeValueAsString(jsonNode);

        }catch (JsonProcessingException e) {
            throw new RuntimeException("Json processing exception",e);
        }
    }
}
