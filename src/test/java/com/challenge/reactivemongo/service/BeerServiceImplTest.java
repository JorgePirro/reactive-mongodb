package com.challenge.reactivemongo.service;

import com.challenge.reactivemongo.domain.Beer;
import com.challenge.reactivemongo.mapper.BeerMapper;
import com.challenge.reactivemongo.mapper.BeerMapperImpl;
import com.challenge.reactivemongo.model.BeerDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
public class BeerServiceImplTest {

    @Container
    @ServiceConnection
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    BeerDto BeerDto;

    @BeforeEach
    void setUp() {
        BeerDto = beerMapper.beerToBeerDto(createBeer());
    }

    @Test
    @DisplayName("Test Save Beer Using Subscriber")
    void saveBeerUseSubscriber() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDto> atomicDto = new AtomicReference<>();

        Mono<BeerDto> savedMono = beerService.saveBeer(Mono.just(BeerDto));

        savedMono.subscribe(savedDto -> {
            System.out.println(savedDto.getId());
            atomicBoolean.set(true);
            atomicDto.set(savedDto);
        });

        await().untilTrue(atomicBoolean);

        BeerDto persistedDto = atomicDto.get();
        assertThat(persistedDto).isNotNull();
        assertThat(persistedDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Save Beer Using Block")
    void testSaveBeerUseBlock() {
        BeerDto savedDto = beerService.saveBeer(Mono.just(createBeerDto())).block();
        assertThat(savedDto).isNotNull();
        assertThat(savedDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Update Beer Using Block")
    void testUpdateBlocking() {
        final String newName = "New Beer Name";  // use final so cannot mutate
        BeerDto savedBeerDto = getSavedBeerDto();
        savedBeerDto.setName(newName);

        BeerDto updatedDto = beerService.saveBeer(Mono.just(savedBeerDto)).block();

        //verify exists in db
        BeerDto fetchedDto = beerService.getById(updatedDto.getId()).block();
        assertThat(fetchedDto.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("Test Update Using Reactive Streams")
    void testUpdateStreaming() {
        final String newName = "New Beer Name";  // use final so cannot mutate

        AtomicReference<BeerDto> atomicDto = new AtomicReference<>();

        beerService.saveBeer(Mono.just(createBeerDto()))
                .map(savedBeerDto -> {
                    savedBeerDto.setName(newName);
                    return savedBeerDto;
                })
                .flatMap(beerService::saveBeer) // save updated beer
                .flatMap(savedUpdatedDto -> beerService.getById(savedUpdatedDto.getId())) // get from db
                .subscribe(dtoFromDb -> {
                    atomicDto.set(dtoFromDb);
                });

        await().until(() -> atomicDto.get() != null);
        assertThat(atomicDto.get().getName()).isEqualTo(newName);
    }

    @Test
    void testDeleteBeer() {
        BeerDto beerToDelete = getSavedBeerDto();

        beerService.deleteBeerById(beerToDelete.getId()).block();

        Mono<BeerDto> expectedEmptyBeerMono = beerService.getById(beerToDelete.getId());

        BeerDto emptyBeer = expectedEmptyBeerMono.block();

        assertThat(emptyBeer).isNull();

    }

    @Test
    void testFindFirstByBeerStyle() {
        BeerDto savedBeerDto = getSavedBeerDto();
        Flux<BeerDto> beerDto = beerService.findByBeerStyle(savedBeerDto.getBeerStyle());
        BeerDto foundBeerDto = beerDto.blockFirst();
        assertThat(foundBeerDto).isNotNull();
        assertThat(foundBeerDto.getBeerStyle()).isEqualTo(savedBeerDto.getBeerStyle());
    }

    @Test
    void testFindFirstByName() {
        BeerDto savedBeerDto = getSavedBeerDto();
        Mono<BeerDto> beerDtoMono = beerService.findFirstByName(savedBeerDto.getName());
        BeerDto foundBeerDto = beerDtoMono.block();
        assertThat(foundBeerDto).isNotNull();
    }

    public BeerDto getSavedBeerDto(){
        return beerService.saveBeer(Mono.just(createBeerDto())).block();
    }

    public static BeerDto createBeerDto(){
        return new BeerMapperImpl().beerToBeerDto(createBeer());
    }

    public static Beer createBeer() {
        return Beer.builder()
                .name("Corona")
                .beerStyle("Lager")
                .price(BigDecimal.ONE)
                .quantityOnHand(10)
                .upc("1234567890123")
                .build();
    }
}