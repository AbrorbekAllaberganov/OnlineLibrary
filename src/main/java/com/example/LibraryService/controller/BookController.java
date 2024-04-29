package com.example.LibraryService.controller;

import com.example.LibraryService.payload.BookPayload;
import com.example.LibraryService.payload.Result;
import com.example.LibraryService.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Result> save(@RequestBody BookPayload bookPayload){
        Result result=bookService.saveBook(bookPayload);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

    @PostMapping("/upload")
    public ResponseEntity<Result> uploadBook(@RequestBody BookPayload bookPayload){
        Result result=bookService.createBook(bookPayload);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable long id, @RequestBody BookPayload bookPayload){
        Result result=bookService.editBook(id,bookPayload);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

    @GetMapping
    public ResponseEntity<Result> getBookList(){
        Result result=bookService.getBookList();
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

    @GetMapping("/author")
    public ResponseEntity<Result> getBookListByUserId(@RequestParam("authorId") long authorId){
        Result result=bookService.getBookListByAuthorId(authorId);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

    @GetMapping("/category")
    public ResponseEntity<Result> getBookListByCategoryId(@RequestParam("categoryId") long categoryId){
        Result result=bookService.getBookListByCategoryId(categoryId);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

}
