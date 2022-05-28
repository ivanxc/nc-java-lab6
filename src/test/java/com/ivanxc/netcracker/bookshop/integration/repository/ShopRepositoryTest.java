package com.ivanxc.netcracker.bookshop.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.ivanxc.netcracker.bookshop.entity.Book;
import com.ivanxc.netcracker.bookshop.entity.Customer;
import com.ivanxc.netcracker.bookshop.entity.Purchase;
import com.ivanxc.netcracker.bookshop.entity.Shop;
import com.ivanxc.netcracker.bookshop.integration.IntegrationTestBase;
import com.ivanxc.netcracker.bookshop.repository.PurchaseRepository;
import com.ivanxc.netcracker.bookshop.repository.ShopRepository;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
class ShopRepositoryTest extends IntegrationTestBase {

    private final ShopRepository repository;
    private final PurchaseRepository purchaseRepository;

    @Test
    void findShopsNameOfDistricts() {
        Shop shop1 = Shop.builder().name("Книжный №1").district("Ленинский").commission((short)5).build();
        Shop shop2 = Shop.builder().name("Книжный №2").district("Советский").commission((short)6).build();
        Shop shop3 = Shop.builder().name("Книжный №3").district("Советский").commission((short)7).build();
        Shop shop4 = Shop.builder().name("Книжный №4").district("Приокский").commission((short)8).build();

        repository.save(shop1);
        repository.save(shop2);
        repository.save(shop3);
        repository.save(shop4);

        List<String> actual = repository.findShopsNameOfDistricts(new String[]{"Советский", "Приокский"});
        List<String> expectedNames = List.of("Книжный №2", "Книжный №3", "Книжный №4");

        assertThat(actual).hasSize(3).hasSameElementsAs(expectedNames);
    }

    @Test
    void findShopsWhereCustomersHaveDiscountExcludingDistrict() {
        Book book = Book.builder().title("Война и мир").price(200).repo("Сормово").quantity(10).build();

        Shop shop1 = Shop.builder().name("Книжный №1").district("Автозаводский").commission((short)5).build();
        Shop shop2 = Shop.builder().name("Книжный №2").district("Сормовский").commission((short)6).build();
        Shop shop3 = Shop.builder().name("Книжный №3").district("Нижегородский").commission((short)7).build();

        Customer customer1 = Customer.builder().surname("Петров").district("Советский").discount((short)5).build();
        Customer customer2 = Customer.builder().surname("Иванов").district("Сормовский").discount((short)15).build();

        Purchase purchase1 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-25 10:00:00"))
            .shop(shop1)
            .customer(customer2)
            .book(book)
            .quantity(2)
            .totalPrice(1000)
            .build();

        Purchase purchase2 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-26 10:00:00"))
            .shop(shop2)
            .customer(customer1)
            .book(book)
            .quantity(2)
            .totalPrice(400)
            .build();

        Purchase purchase3 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-27 12:00:00"))
            .shop(shop3)
            .customer(customer2)
            .book(book)
            .quantity(2)
            .totalPrice(400)
            .build();

        purchaseRepository.save(purchase1);
        purchaseRepository.save(purchase2);
        purchaseRepository.save(purchase3);

        List<Shop> actual =
            repository.findShopsWhereCustomersHaveDiscountBetweenExcludingDistrict((short)10, (short)15, "Автозаводский");

        Shop actualShop = actual.get(0);

        assertThat(actual).hasSize(1);
        assertThat(actualShop).isEqualTo(shop3);
    }

}