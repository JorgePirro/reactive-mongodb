package com.challenge.reactivemongo.service;

import com.challenge.reactivemongo.domain.Beer;
import com.challenge.reactivemongo.mapper.BeerMapper;
import com.challenge.reactivemongo.model.BeerDto;
import com.challenge.reactivemongo.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;

    @Override
    public Flux<BeerDto> listAllBeers() {
        return beerRepository.findAll()
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> saveBeer(Mono<BeerDto> beerDto) {
        return beerDto.map(beerMapper::beerDtoToBeer)
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> saveBeer(BeerDto beer) {
        return beerRepository.save(beerMapper.beerDtoToBeer(beer))
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> getById(String id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> updateBeer(String id, BeerDto beer) {
        return beerRepository.findById(id)
                .map(foundBeer -> {
                    foundBeer.setName(beer.getName());
                    foundBeer.setBeerStyle(beer.getBeerStyle());
                    foundBeer.setPrice(beer.getPrice());
                    foundBeer.setUpc(beer.getUpc());
                    return foundBeer;
                }).flatMap(beerRepository::save)
                    .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> patchBeer(String beerId, BeerDto beerDto) {
        return beerRepository.findById(beerId)
                .map(foundBeer -> {
                    if(StringUtils.hasText(beerDto.getName())){
                        foundBeer.setName(beerDto.getName());
                    }

                    if(StringUtils.hasText(beerDto.getBeerStyle())){
                        foundBeer.setBeerStyle(beerDto.getBeerStyle());
                    }

                    if(beerDto.getPrice() != null){
                        foundBeer.setPrice(beerDto.getPrice());
                    }

                    if(StringUtils.hasText(beerDto.getUpc())){
                        foundBeer.setUpc(beerDto.getUpc());
                    }

                    if(beerDto.getQuantityOnHand() != null){
                        foundBeer.setQuantityOnHand(beerDto.getQuantityOnHand());
                    }
                    return foundBeer;
                }).flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<Void> deleteBeerById(String id) {
        return beerRepository.deleteById(id);
    }

    @Override
    public Mono<BeerDto>findFirstByName(String name) {
        return beerRepository.findFirstByName(name)
                .map(beerMapper::beerToBeerDto);
    }

    public Flux<BeerDto> findByBeerStyle(String beerStyle) {
        return beerRepository.findByBeerStyle(beerStyle)
                .map(beerMapper::beerToBeerDto);
    }
}
