package com.example.LibraryService.repository;

import com.example.LibraryService.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(nativeQuery = true,value = "select * from comment where book_id=?1")
    List<Comment> getCommentListByBookId(Long bookId);
}
