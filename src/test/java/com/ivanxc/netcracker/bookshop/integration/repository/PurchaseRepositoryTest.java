package com.ivanxc.netcracker.bookshop.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivanxc.netcracker.bookshop.dto.PurchaseCustomerSurnameShopNameDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseDateCustomerSurnameDistrictDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseDateQuantityCustomerSurnameDiscountBookTitleDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseIdDateCustomerSurnameDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseQuantityBookTitlePriceRepoDto;
import com.ivanxc.netcracker.bookshop.entity.Book;
import com.ivanxc.netcracker.bookshop.entity.Customer;
import com.ivanxc.netcracker.bookshop.entity.Purchase;
import com.ivanxc.netcracker.bookshop.entity.Shop;
import com.ivanxc.netcracker.bookshop.integration.IntegrationTestBase;
import com.ivanxc.netcracker.bookshop.repository.PurchaseRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
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
class PurchaseRepositoryTest extends IntegrationTestBase {

    private final PurchaseRepository repository;

    @Test
    void findDistinctMonths() {
        Book book1 = Book.builder().title("Война и мир").price(200).repo("Сормово").quantity(10).build();
        Book book2 = Book.builder().title("Отцы и дети").price(100).repo("Автозавод").quantity(20).build();

        Shop shop1 = Shop.builder().name("Книжный №1").district("Советский").commission((short)5).build();
        Shop shop2 = Shop.builder().name("Книжный №2").district("Сормовский").commission((short)6).build();

        Customer customer1 = Customer.builder().surname("Петров").district("Советский").discount((short)5).build();
        Customer customer2 = Customer.builder().surname("Иванов").district("Сормовский").discount((short)10).build();

        Purchase purchase1 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-25 10:00:00"))
            .shop(shop1)
            .customer(customer1)
            .book(book1)
            .quantity(2)
            .totalPrice(1000)
            .build();

        Purchase purchase2 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-25 10:00:00"))
            .shop(shop2)
            .customer(customer2)
            .book(book2)
            .quantity(2)
            .totalPrice(400)
            .build();

        Purchase purchase3 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-26 12:00:00"))
            .shop(shop1)
            .customer(customer2)
            .book(book2)
            .quantity(2)
            .totalPrice(400)
            .build();

        repository.save(purchase1);
        repository.save(purchase2);
        repository.save(purchase3);

        List<Timestamp> actual = repository.findDistinctDates();

        List<Timestamp> distinctTimestamps =
            List.of(purchase1.getDate(), purchase3.getDate());

        assertThat(actual).hasSize(2).hasSameElementsAs(distinctTimestamps);
    }

    @Test
    void findCustomerSurnameAndShopNameForEachPurchase() {
        Book book1 = Book.builder().title("Война и мир").price(200).repo("Сормово").quantity(10).build();
        Book book2 = Book.builder().title("Отцы и дети").price(100).repo("Автозавод").quantity(20).build();

        Shop shop1 = Shop.builder().name("Книжный №1").district("Советский").commission((short)5).build();
        Shop shop2 = Shop.builder().name("Книжный №2").district("Сормовский").commission((short)6).build();

        Customer customer1 = Customer.builder().surname("Петров").district("Советский").discount((short)5).build();
        Customer customer2 = Customer.builder().surname("Иванов").district("Сормовский").discount((short)10).build();

        Purchase purchase1 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-25 10:00:00"))
            .shop(shop1)
            .customer(customer1)
            .book(book1)
            .quantity(2)
            .totalPrice(1000)
            .build();

        Purchase purchase2 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-25 10:00:00"))
            .shop(shop2)
            .customer(customer2)
            .book(book2)
            .quantity(2)
            .totalPrice(400)
            .build();

        repository.save(purchase1);
        repository.save(purchase2);

        List<PurchaseCustomerSurnameShopNameDto> actual = repository
            .findCustomerSurnameAndShopNameForEachPurchase();

        Map<String, String> actualSurnameAndName = actual
            .stream()
            .collect(Collectors.toMap(
                PurchaseCustomerSurnameShopNameDto::getCustomerSurname,
                PurchaseCustomerSurnameShopNameDto::getShopName)
            );

        Map<String, String> expectedSurnameAndName = Map.of(
            "Петров", "Книжный №1", "Иванов", "Книжный №2");

        assertThat(actualSurnameAndName).hasSize(2);
        assertThat(actualSurnameAndName.get("Петров")).isEqualTo("Книжный №1");
        assertThat(actualSurnameAndName.get("Иванов")).isEqualTo("Книжный №2");
    }

    @Test
    void findDateQuantityDiscountSurnameTitle() {
        Book book = Book.builder().title("Война и мир").price(200).repo("Сормово").quantity(10).build();

        Shop shop = Shop.builder().name("Книжный №1").district("Советский").commission((short)5).build();

        Customer customer = Customer.builder().surname("Петров").district("Советский").discount((short)5).build();

        Purchase purchase = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-25 10:00:00"))
            .shop(shop)
            .customer(customer)
            .book(book)
            .quantity(2)
            .totalPrice(1000)
            .build();

        repository.save(purchase);

        PurchaseDateQuantityCustomerSurnameDiscountBookTitleDto actual = repository
            .findDateQuantityDiscountSurnameTitle().get(0);

        assertThat(actual.getPurchaseDate()).isEqualTo(purchase.getDate());
        assertThat(actual.getCustomerSurname()).isEqualTo(customer.getSurname());
        assertThat(actual.getBookTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void findPurchasesIdDateCustomerSurnameWhereTotalPriceMoreThan() {
        Book book1 = Book.builder().title("Война и мир").price(200).repo("Сормово").quantity(10).build();
        Book book2 = Book.builder().title("Отцы и дети").price(100).repo("Автозавод").quantity(20).build();

        Shop shop1 = Shop.builder().name("Книжный №1").district("Советский").commission((short)5).build();
        Shop shop2 = Shop.builder().name("Книжный №2").district("Сормовский").commission((short)6).build();

        Customer customer1 = Customer.builder().surname("Петров").district("Советский").discount((short)5).build();
        Customer customer2 = Customer.builder().surname("Иванов").district("Сормовский").discount((short)10).build();

        Purchase purchase1 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-25 10:00:00"))
            .shop(shop1)
            .customer(customer1)
            .book(book1)
            .quantity(2)
            .totalPrice(1000)
            .build();

        Purchase purchase2 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-25 10:00:00"))
            .shop(shop2)
            .customer(customer2)
            .book(book2)
            .quantity(2)
            .totalPrice(400)
            .build();

        repository.save(purchase1);
        repository.save(purchase2);

        List<PurchaseIdDateCustomerSurnameDto> actual =
            repository.findPurchasesIdDateCustomerSurnameWhereTotalPriceMoreThan(500.0f);
        PurchaseIdDateCustomerSurnameDto purchase = actual.get(0);

        assertThat(actual).hasSize(1);
        assertThat(purchase.getPurchaseId()).isEqualTo(purchase1.getId());
    }

    @Test
    void findPurchasesInCustomerDistrictAfter() {
        Book book1 = Book.builder().title("Война и мир").price(200).repo("Сормово").quantity(10).build();
        Book book2 = Book.builder().title("Отцы и дети").price(100).repo("Автозавод").quantity(20).build();

        Shop shop1 = Shop.builder().name("Книжный №1").district("Советский").commission((short)5).build();
        Shop shop2 = Shop.builder().name("Книжный №2").district("Сормовский").commission((short)6).build();
        Shop shop3 = Shop.builder().name("Книжный №3").district("Нижегородский").commission((short)7).build();

        Customer customer1 = Customer.builder().surname("Петров").district("Советский").discount((short)5).build();
        Customer customer2 = Customer.builder().surname("Иванов").district("Сормовский").discount((short)10).build();

        Purchase purchase1 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-25 10:00:00"))
            .shop(shop1)
            .customer(customer1)
            .book(book1)
            .quantity(2)
            .totalPrice(1000)
            .build();

        Purchase purchase2 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-26 10:00:00"))
            .shop(shop2)
            .customer(customer2)
            .book(book2)
            .quantity(2)
            .totalPrice(400)
            .build();

        Purchase purchase3 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-27 12:00:00"))
            .shop(shop3)
            .customer(customer2)
            .book(book2)
            .quantity(2)
            .totalPrice(400)
            .build();

        repository.save(purchase1);
        repository.save(purchase2);
        repository.save(purchase3);

        List<PurchaseDateCustomerSurnameDistrictDto> actual =
            repository.findPurchasesInCustomerDistrictAfter(Timestamp.valueOf("2022-05-26 9:00:00"));

        PurchaseDateCustomerSurnameDistrictDto purchase = actual.get(0);

        assertThat(actual).hasSize(1);
        assertThat(purchase.getCustomerDistrict()).isEqualTo("Сормовский");
    }

    @Test
    void findBoughtBooksInSameDistrictAsRepoWhereStoredQuantityMoreThan() {
        Book book1 = Book.builder().title("Война и мир").price(100).repo("Сормовский").quantity(5).build();
        Book book2 = Book.builder().title("Отцы и дети").price(200).repo("Автозаводский").quantity(20).build();
        Book book3 = Book.builder().title("Щегол").price(300).repo("Сормовский").quantity(30).build();

        Shop shop1 = Shop.builder().name("Книжный №1").district("Советский").commission((short)5).build();
        Shop shop2 = Shop.builder().name("Книжный №2").district("Сормовский").commission((short)6).build();

        Customer customer = Customer.builder().surname("Петров").district("Советский").discount((short)5).build();

        Purchase purchase1 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-25 10:00:00"))
            .shop(shop2)
            .customer(customer)
            .book(book1)
            .quantity(2)
            .totalPrice(1000)
            .build();

        Purchase purchase2 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-26 11:00:00"))
            .shop(shop2)
            .customer(customer)
            .book(book3)
            .quantity(2)
            .totalPrice(400)
            .build();

        Purchase purchase3 = Purchase.builder()
            .date(Timestamp.valueOf("2022-05-27 12:00:00"))
            .shop(shop1)
            .customer(customer)
            .book(book3)
            .quantity(2)
            .totalPrice(400)
            .build();

        repository.save(purchase1);
        repository.save(purchase2);
        repository.save(purchase3);

        List<PurchaseQuantityBookTitlePriceRepoDto> actual = repository
            .findBoughtBooksInSameDistrictAsRepoWhereStoredQuantityMoreThan(10);

        PurchaseQuantityBookTitlePriceRepoDto actualBook = actual.get(0);

        assertThat(actual).hasSize(1);
        assertThat(actualBook.getBookTitle()).isEqualTo(book3.getTitle());
    }

}