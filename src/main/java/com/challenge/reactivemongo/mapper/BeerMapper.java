package com.challenge.reactivemongo.mapper;

import com.challenge.reactivemongo.domain.Beer;
import com.challenge.reactivemongo.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);
    Beer beerDtoToBeer(BeerDto beerDto);
}
