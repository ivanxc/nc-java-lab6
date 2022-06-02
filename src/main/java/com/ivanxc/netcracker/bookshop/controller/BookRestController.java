package com.ivanxc.netcracker.bookshop.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.ivanxc.netcracker.bookshop.dto.BookCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.BookReadDto;
import com.ivanxc.netcracker.bookshop.dto.BookTitlePriceDto;
import com.ivanxc.netcracker.bookshop.exception.ParameterFormatException;
import com.ivanxc.netcracker.bookshop.exception.ResourceNotFoundException;
import com.ivanxc.netcracker.bookshop.response.DeleteResponse;
import com.ivanxc.netcracker.bookshop.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping
    public List<BookReadDto> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookReadDto findById(@PathVariable Long id) {
        return bookService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No book with ID = " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookReadDto create(@RequestBody BookCreateEditDto book) {
        return bookService.create(book);
    }

    @PutMapping("/{id}")
    public BookReadDto updateById(@PathVariable Long id, @RequestBody BookCreateEditDto book) {
        return bookService.update(id, book)
                    .orElseThrow(() -> new ResourceNotFoundException("No book with ID = " + id));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public BookReadDto patchById(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return bookService.patch(id, patch);
    }

    @DeleteMapping("/{id}")
    public DeleteResponse delete(@PathVariable Long id) {
        if (bookService.delete(id)) {
            return new DeleteResponse("Book with ID = " + id + " was deleted");
        } else {
            throw new ResourceNotFoundException("No book with ID = " + id);
        }
    }

    @GetMapping("/distinct-titles-and-prices")
    public List<BookTitlePriceDto> findDistinctTitlesAndPrices() {
        return bookService.findDistinctTitlesAndPrices();
    }

    @GetMapping("/contains-word-or-costs-more-than")
    public List<BookTitlePriceDto> findBooksContainingWordOrCostsMoreThan(@RequestParam String word,
        @RequestParam Integer price) {
        if (word.length() == 0) {
            throw new ParameterFormatException("Parameter word cannot be empty.");
        }
        return bookService.findBooksContainingWordOrCostsMoreThan(word, price);
    }
}
