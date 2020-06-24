package com.amsidh.mvc.repository;

import com.amsidh.mvc.ui.model.AlbumResponseModel;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumServiceClientFactory.class)
public interface AlbumServiceClient {

    @GetMapping(value = "/users/{userId}/albums")
    List<AlbumResponseModel> getAlbumsByUserId(@PathVariable(name = "userId", required = true) String userId);
}

@Component
class AlbumServiceClientFactory implements FallbackFactory<AlbumServiceClient> {

    @Override
    public AlbumServiceClient create(Throwable clause) {
        return new AlbumServiceClientFallBack(clause);
    }
}

@Slf4j
class AlbumServiceClientFallBack implements AlbumServiceClient {

    private final Throwable clause;

    public AlbumServiceClientFallBack(Throwable clause) {
        this.clause = clause;
    }
    @Override
    public List<AlbumResponseModel> getAlbumsByUserId(String userId) {
        if (clause instanceof FeignException && ((FeignException) clause).status() == 404) {
            log.error(format("404 error took place when getAlbumsByUserId was called with userId:%s. Error message %s", userId, clause.getLocalizedMessage()));
        } else {
            log.error("Other error took place: "+ clause.getLocalizedMessage());
        }
        return emptyList();
    }
}
