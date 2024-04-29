package com.example.LibraryService.service.impl;

import com.example.LibraryService.entity.Comment;
import com.example.LibraryService.exceptions.ResourceNotFound;
import com.example.LibraryService.payload.CommentPayload;
import com.example.LibraryService.payload.Result;
import com.example.LibraryService.repository.CommentRepository;
import com.example.LibraryService.service.BookService;
import com.example.LibraryService.service.CommentService;
import com.example.LibraryService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final BookService bookService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Override
    public Result saveComment(CommentPayload commentPayload) {
        try {
            Comment comment=new Comment();
            comment.setText(commentPayload.getText());
            comment.setBook(bookService.findById(commentPayload.getBookId()));
            comment.setUser(userService.findUserById(commentPayload.getUserId()));

            commentRepository.save(comment);
            return Result.success(comment);
        }catch (Exception e){
            return Result.exception(e);
        }
    }

    @Override
    public Result editComment(Long commentId, CommentPayload commentPayload) {
        try {
            Comment comment=commentRepository.findById(commentId).orElseThrow(
                    ()->new ResourceNotFound("comment","id", commentId)
            );
            comment.setText(commentPayload.getText());
            comment.setBook(bookService.findById(commentPayload.getBookId()));
            comment.setUser(userService.findUserById(commentPayload.getUserId()));

            commentRepository.save(comment);
            return Result.success(comment);
        }catch (Exception e){
            return Result.exception(e);
        }
    }

    @Override
    public Result deleteComment(Long commentId) {
        try {
            commentRepository.deleteById(commentId);
            return Result.success("Comment has been deleted");
        }catch (Exception e){
            return Result.exception(e);
        }
    }

    @Override
    public Result getCommentListByBookId(Long id) {
        return Result.success(commentRepository.getCommentListByBookId(id));
    }
}
