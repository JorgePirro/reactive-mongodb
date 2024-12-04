package com.challenge.reactivemongo.web;


import com.challenge.reactivemongo.domain.Beer;
import com.challenge.reactivemongo.model.BeerDto;
import com.challenge.reactivemongo.service.BeerServiceImplTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerHandlerTest {

    @Container
    @ServiceConnection
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testPatchIdNotFound() {
        webTestClient.patch()
                .uri(BeerRouterConfig.BEER_PATH_ID, 999)
                .body(Mono.just(BeerServiceImplTest.createBeer()), BeerDto.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testPatchIdFound() {
        BeerDto BeerDto = getSavedTestBeer();

        webTestClient.patch()
                .uri(BeerRouterConfig.BEER_PATH_ID, BeerDto.getId())
                .body(Mono.just(BeerDto), BeerDto.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteNotFound() {
        webTestClient.delete()
                .uri(BeerRouterConfig.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteBeer() {
        BeerDto BeerDto = getSavedTestBeer();

        webTestClient.delete()
                .uri(BeerRouterConfig.BEER_PATH_ID, BeerDto.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @Order(4)
    void testUpdateBeerBadRequest() {
        BeerDto testBeer = getSavedTestBeer();
        testBeer.setBeerStyle("");

        webTestClient.put()
                .uri(BeerRouterConfig.BEER_PATH_ID, testBeer)
                .body(Mono.just(testBeer), BeerDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateBeerNotFound() {
        webTestClient.put()
                .uri(BeerRouterConfig.BEER_PATH_ID, 999)
                .body(Mono.just(BeerServiceImplTest.createBeer()), BeerDto.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void testUpdateBeer() {

        BeerDto BeerDto = getSavedTestBeer();

        webTestClient.put()
                .uri(BeerRouterConfig.BEER_PATH_ID, BeerDto.getId())
                .body(Mono.just(BeerDto), BeerDto.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testCreateBeerBadData() {
        Beer testBeer = BeerServiceImplTest.createBeer();
        testBeer.setName("");

        webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(testBeer), BeerDto.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBeer() {
        webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(BeerServiceImplTest.createBeer()), BeerDto.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testGetByIdNotFound() {
        webTestClient.get().uri(BeerRouterConfig.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(1)
    void testGetById() {
        BeerDto BeerDto = getSavedTestBeer();

        webTestClient.get().uri(BeerRouterConfig.BEER_PATH_ID, BeerDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDto.class);
    }
    
    @Test
    @Order(2)
    void testListBeers() {
        webTestClient.get().uri(BeerRouterConfig.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }

    public BeerDto getSavedTestBeer(){
        FluxExchangeResult<BeerDto> BeerDtoFluxExchangeResult = webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(BeerServiceImplTest.createBeer()), BeerDto.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(BeerDto.class);

        List<String> location = BeerDtoFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient.get().uri(BeerRouterConfig.BEER_PATH)
                .exchange().returnResult(BeerDto.class).getResponseBody().blockFirst();
    }

}