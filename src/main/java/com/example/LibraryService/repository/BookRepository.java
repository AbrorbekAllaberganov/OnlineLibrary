package com.example.LibraryService.repository;

import com.example.LibraryService.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(nativeQuery = true, value = "select * from book where category_id=?1")
    List<Book> getBookListByCategoryId(long categoryId);

    @Query(nativeQuery = true, value = "select * from book where author_id=?1")
    List<Book> getBookListByAuthorId(long authorId);


}
