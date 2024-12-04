package com.challenge.reactivemongo.repositories;

import com.challenge.reactivemongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
    Mono<Beer> findFirstByName(String name);
    Flux<Beer> findByBeerStyle(String beerStyle);
}
