package com.challenge.reactivemongo.bootstrap;

import com.challenge.reactivemongo.domain.Beer;
import com.challenge.reactivemongo.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        beerRepository.deleteAll()
                .doOnSuccess(success -> {
                    loadBeerData();
                }).subscribe();
    }

    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                        .name("Corona Extra")
                        .beerStyle("Lager")
                        .upc("11111")
                        .price(new BigDecimal("9.99"))
                        .quantityOnHand(122)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer2 = Beer.builder()
                        .name("Estrella Damm")
                        .beerStyle("Pilsner")
                        .upc("22222")
                        .price(new BigDecimal("1.50"))
                        .quantityOnHand(392)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer3 = Beer.builder()
                        .name("Modelo Especial")
                        .beerStyle("Pale Lager")
                        .upc("33333")
                        .price(new BigDecimal("10.99"))
                        .quantityOnHand(144)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                beerRepository.save(beer1).subscribe(
                        beer -> System.out.println(beer.toString()));
                beerRepository.save(beer2).subscribe(
                        beer -> System.out.println(beer.toString()));
                beerRepository.save(beer3).subscribe(
                        beer -> System.out.println(beer.toString()));
            }
        });
    }
}
