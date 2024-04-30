package com.example.LibraryService.service.impl;

import com.example.LibraryService.entity.Book;
import com.example.LibraryService.entity.Comment;
import com.example.LibraryService.exceptions.ResourceNotFound;
import com.example.LibraryService.payload.BookPayload;
import com.example.LibraryService.payload.Result;
import com.example.LibraryService.repository.AttachmentRepository;
import com.example.LibraryService.repository.BookRepository;
import com.example.LibraryService.repository.CategoryRepository;
import com.example.LibraryService.repository.CommentRepository;
import com.example.LibraryService.service.BookService;
import com.example.LibraryService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final PdfService pdfService;
    private final CommentRepository commentRepository;

    @Override
    public Result saveBook(BookPayload bookPayload) {
        try {
            Book book = new Book();
            book.setName(bookPayload.getName());
            book.setDescription(bookPayload.getDescription());
            book.setAuthor(userService.findUserById(bookPayload.getUserId()));
            book.setFile(attachmentRepository.findByHashId(bookPayload.getHashId()));
            if (bookPayload.getCategoryIds()!=null)
                book.setCategories(
                    bookPayload.getCategoryIds().stream()
                            .map(id->categoryRepository.findById(id).orElseThrow(
                                    () -> new ResourceNotFound("category", "id", id)
                            ))
                            .collect(Collectors.toList())
                );

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
            if (bookPayload.getCategoryIds()!=null)
                book.setCategories(
                    bookPayload.getCategoryIds().stream()
                            .map(id->categoryRepository.findById(id).orElseThrow(
                                    () -> new ResourceNotFound("category", "id", id)
                            ))
                            .collect(Collectors.toList())
                );
            bookRepository.save(book);
            return Result.success(book);
        } catch (Exception e) {
            return Result.exception(e);
        }
    }

    @Override
    public Result deleteBook(Long bookId) {
        try {
            List<Comment> commentList=commentRepository.getCommentListByBookId(bookId);
            commentList.forEach(comment -> commentRepository.deleteById(comment.getId()));
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

            return saveBook(bookPayload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
