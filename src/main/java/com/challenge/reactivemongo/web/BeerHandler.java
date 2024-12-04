package com.challenge.reactivemongo.web;

import com.challenge.reactivemongo.model.BeerDto;
import com.challenge.reactivemongo.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BeerHandler {

    private final BeerService beerService;
    private final Validator validator;

    private void validate(BeerDto beerDto) {
        Errors errors = new BeanPropertyBindingResult(beerDto, "beerDto");
        validator.validate(beerDto, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> createNewBeer(ServerRequest request) {
        return beerService.saveBeer(request.bodyToMono(BeerDto.class).doOnNext(this::validate))
                .flatMap(beer -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_PATH_ID)
                                .build(beer.getId())).build());
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
      return ServerResponse.ok()
                .body(beerService.getById(request.pathVariable("id"))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),
                        BeerDto.class);
    }

    public Mono<ServerResponse> listBeers(ServerRequest request) {
        return ServerResponse.ok()
                .body(beerService.listAllBeers(), BeerDto.class);
    }

    public Mono<ServerResponse> updateBeer(ServerRequest request) {
        return request.bodyToMono(BeerDto.class)
                .doOnNext(this::validate)
                .flatMap(beer -> beerService
                        .updateBeer(request.pathVariable("id"), beer))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(saveDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchBeerById(ServerRequest request) {
        return request.bodyToMono(BeerDto.class)
                .doOnNext(this::validate)
                .flatMap(beerDto -> beerService
                        .patchBeer(request.pathVariable("id"), beerDto))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(saveDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deleteBeerById(ServerRequest request){
        return beerService.getById(request.pathVariable("id"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(beerDTO -> beerService.deleteBeerById(beerDTO.getId()))
                .then(ServerResponse.noContent().build());
    }

}
