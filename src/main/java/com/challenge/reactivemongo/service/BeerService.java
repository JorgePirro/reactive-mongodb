package com.challenge.reactivemongo.service;


import com.challenge.reactivemongo.domain.Beer;
import com.challenge.reactivemongo.model.BeerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {

    Flux<BeerDto> listAllBeers();
    Mono<BeerDto> saveBeer(Mono<BeerDto> beer);
    Mono<BeerDto> saveBeer(BeerDto beer);
    Mono<BeerDto> getById(String id);
    Mono<BeerDto> updateBeer(String id, BeerDto beer);
    Mono<BeerDto> patchBeer(String id, BeerDto beer);
    Mono<Void> deleteBeerById(String id);
    Mono<BeerDto>findFirstByName(String name);
    Flux<BeerDto> findByBeerStyle(String beerStyle);
}
