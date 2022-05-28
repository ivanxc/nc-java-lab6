package com.ivanxc.netcracker.bookshop.repository;

import com.ivanxc.netcracker.bookshop.dto.BookTitlePriceDto;
import com.ivanxc.netcracker.bookshop.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select distinct b.title as title, b.price as price from Book b")
    List<BookTitlePriceDto> findDistinctTitlesAndPrices();

    @Query("select b.title as title, b.price as price from Book b"
        + " where b.title LIKE %:word% OR b.price > :price")
    List<BookTitlePriceDto> findBooksContainingWordOrCostsMoreThan(String word, int price);

}
