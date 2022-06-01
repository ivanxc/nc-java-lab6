package com.ivanxc.netcracker.bookshop.mapper;

import com.ivanxc.netcracker.bookshop.dto.BookCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.BookReadDto;
import com.ivanxc.netcracker.bookshop.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookReadMapper implements Mapper<Book, BookReadDto> {

    @Override
    public BookReadDto map(Book from) {
        return new BookReadDto(
            from.getId(),
            from.getTitle(),
            from.getPrice(),
            from.getRepo(),
            from.getQuantity()
        );
    }

    public BookReadDto map(BookCreateEditDto from, long id) {
        return new BookReadDto(
            id,
            from.getTitle(),
            from.getPrice(),
            from.getRepo(),
            from.getQuantity()
        );
    }
}
