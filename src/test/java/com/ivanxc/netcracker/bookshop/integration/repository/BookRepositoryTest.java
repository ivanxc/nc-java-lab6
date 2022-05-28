package com.ivanxc.netcracker.bookshop.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.ivanxc.netcracker.bookshop.dto.BookTitlePriceDto;
import com.ivanxc.netcracker.bookshop.entity.Book;
import com.ivanxc.netcracker.bookshop.integration.IntegrationTestBase;
import com.ivanxc.netcracker.bookshop.repository.BookRepository;
import java.util.List;
import java.util.stream.Collectors;
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
class BookRepositoryTest extends IntegrationTestBase {

    private final BookRepository repository;

    @Test
    void sizeFindDistinctTitlesAndPrices() {
        Book book1 = Book.builder().title("Java").price(10).repo("Repo1").quantity(10).build();
        Book book2 = Book.builder().title("Java").price(10).repo("Repo2").quantity(10).build();
        Book book3 = Book.builder().title("Spring").price(10).repo("Repo2").quantity(10).build();
        Book book4 = Book.builder().title("Java").price(20).repo("Repo2").quantity(10).build();

        repository.save(book1);
        repository.save(book2);
        repository.save(book3);
        repository.save(book4);

        List<BookTitlePriceDto> actual = repository.findDistinctTitlesAndPrices();
        List<String> actualTitles = actual.stream()
            .map(BookTitlePriceDto::getTitle)
            .collect(Collectors.toList());

        assertThat(actual).hasSize(3);
        assertThat(actualTitles).hasSameElementsAs(List.of("Java", "Java", "Spring"));
    }

    @Test
    void findBooksContainingWordOrCostsMoreThan() {
        Book book1 = Book.builder().title("Java for professionals").price(2000).repo("Repo1").quantity(10).build();
        Book book2 = Book.builder().title("Effective Java").price(3000).repo("Repo1").quantity(10).build();
        Book book3 = Book.builder().title("Spring in Action").price(2500).repo("Repo2").quantity(10).build();
        Book book4 = Book.builder().title("Docker in Action").price(1500).repo("Repo3").quantity(10).build();

        repository.save(book1);
        repository.save(book2);
        repository.save(book3);
        repository.save(book4);

        List<BookTitlePriceDto> actual = repository.findBooksContainingWordOrCostsMoreThan("Java", 2200);
        List<String> actualTitles = actual
            .stream()
            .map(BookTitlePriceDto::getTitle)
            .collect(Collectors.toList());
        List<String> expectedTitles = List.of(book1.getTitle(), book2.getTitle(), book3.getTitle());

        assertThat(actual).hasSize(3);
        assertThat(actualTitles).hasSameElementsAs(expectedTitles);
    }

    @Test
    void alwaysPass() {
        assertTrue(true);
    }

}