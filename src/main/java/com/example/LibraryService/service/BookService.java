package com.example.LibraryService.service;

import com.example.LibraryService.entity.Book;
import com.example.LibraryService.payload.BookPayload;
import com.example.LibraryService.payload.Result;

public interface BookService {
    Result saveBook(BookPayload bookPayload);
    Result editBook(Long bookId, BookPayload bookPayload);
    Result deleteBook(Long bookId);
    Result getBookList();
    Result getBookListByCategoryId(Long categoryId);
    Result getBookListByAuthorId(Long userId);

    Book findById(Long id);

    Result createBook(BookPayload bookPayload);
}
