package com.example.LibraryService.service.impl;

import com.example.LibraryService.entity.Book;
import com.example.LibraryService.exceptions.ResourceNotFound;
import com.example.LibraryService.payload.BookPayload;
import com.example.LibraryService.payload.Result;
import com.example.LibraryService.repository.AttachmentRepository;
import com.example.LibraryService.repository.BookRepository;
import com.example.LibraryService.service.BookService;
import com.example.LibraryService.service.CategoryService;
import com.example.LibraryService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final PdfService pdfService;


    @Value("${upload}")
    String upload;

    @Override
    public Result saveBook(BookPayload bookPayload) {
        try {
            Book book = new Book();
            book.setName(bookPayload.getName());
            book.setDescription(bookPayload.getDescription());
            book.setAuthor(userService.findUserById(bookPayload.getUserId()));
            book.setFile(attachmentRepository.findByHashId(bookPayload.getHashId()));
            book.setCategory(categoryService.findCategoryById(bookPayload.getCategoryId()));

            bookRepository.save(book);
            return Result.success(book);
        } catch (Exception e) {
            return Result.exception(e);
        }
    }

    @Override
    public Result editBook(Long bookId, BookPayload bookPayload) {
        try {
            Book book = bookRepository.findById(bookId).orElseThrow(
                    () -> new ResourceNotFound("book", "id", bookId)
            );
            book.setName(bookPayload.getName());
            book.setDescription(bookPayload.getDescription());
            book.setAuthor(userService.findUserById(bookPayload.getUserId()));
            book.setFile(attachmentRepository.findByHashId(bookPayload.getHashId()));
            book.setCategory(categoryService.findCategoryById(bookPayload.getCategoryId()));

            bookRepository.save(book);
            return Result.success(book);
        } catch (Exception e) {
            return Result.exception(e);
        }
    }

    @Override
    public Result deleteBook(Long bookId) {
        try {
            bookRepository.deleteById(bookId);
            return Result.message("Book has been deleted", true);
        } catch (Exception e) {
            return Result.exception(e);
        }
    }

    @Override
    public Result getBookList() {
        return Result.success(bookRepository.findAll(Sort.by("createdAt")));
    }

    @Override
    public Result getBookListByCategoryId(Long categoryId) {
        return Result.success(bookRepository.getBookListByCategoryId(categoryId));
    }

    @Override
    public Result getBookListByAuthorId(Long userId) {
        return Result.success(bookRepository.getBookListByAuthorId(userId));
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("book", "id", id)
        );
    }

    @Override
    public Result createBook(BookPayload bookPayload) {
        try {
            String hashId=pdfService.uploadFile(bookPayload.getName(), bookPayload.getText());

            bookPayload.setHashId(hashId);
            saveBook(bookPayload);

            return Result.success(bookPayload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
