package com.example.LibraryService.service;

import com.example.LibraryService.payload.CommentPayload;
import com.example.LibraryService.payload.Result;

public interface CommentService {
    Result saveComment(CommentPayload commentPayload);
    Result editComment(Long commentId, CommentPayload commentPayload);
    Result deleteComment(Long commentId);
    Result getCommentListByBookId(Long id);
}
