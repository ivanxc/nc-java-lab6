package com.ivanxc.netcracker.bookshop.mapper;

import com.ivanxc.netcracker.bookshop.dto.BookCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.BookReadDto;
import com.ivanxc.netcracker.bookshop.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookCreateEditMapper implements Mapper<BookCreateEditDto, Book> {

    @Override
    public Book map(BookCreateEditDto from) {
        Book book = new Book();
        copy(from, book);
        return book;
    }

    public BookCreateEditDto map(Book from) {
        return new BookCreateEditDto(
            from.getTitle(),
            from.getPrice(),
            from.getRepo(),
            from.getQuantity()
        );
    }

    public BookCreateEditDto map(BookReadDto from) {
        return new BookCreateEditDto(
            from.getTitle(),
            from.getPrice(),
            from.getRepo(),
            from.getQuantity()
        );
    }

    @Override
    public Book map(BookCreateEditDto from, Book to) {
        copy(from, to);
        return to;
    }

    private void copy(BookCreateEditDto from, Book to) {
        to.setTitle(from.getTitle());
        to.setPrice(from.getPrice());
        to.setRepo(from.getRepo());
        to.setQuantity(from.getQuantity());
    }
}
