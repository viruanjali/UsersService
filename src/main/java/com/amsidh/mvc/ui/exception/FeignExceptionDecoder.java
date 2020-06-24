package com.amsidh.mvc.ui.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignExceptionDecoder implements ErrorDecoder {

    private final Environment environment;

    @Autowired
    public FeignExceptionDecoder(Environment environment) {
        this.environment = environment;
    }


    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()) {
            case 404:
                if (methodKey.contains(environment.getProperty("get.users.albums.method.name")))
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), environment.getProperty("albums.exception.albums.not.found"));
                break;
            case 400:
                return new ResponseStatusException(HttpStatus.valueOf(response.status()),response.reason());
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
