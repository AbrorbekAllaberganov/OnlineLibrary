package com.example.LibraryService.controller;

import com.example.LibraryService.payload.CommentPayload;
import com.example.LibraryService.payload.Result;
import com.example.LibraryService.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Result> saveComment(@RequestBody CommentPayload commentPayload){
        Result result=commentService.saveComment(commentPayload);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> updateComment(@PathVariable Long id,@RequestBody CommentPayload comment){
        Result result=commentService.editComment(id,comment);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> deleteComment(@PathVariable Long id){
        Result result=commentService.deleteComment(id);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getCommentListByBookId(@PathVariable Long id){
        Result result=commentService.getCommentListByBookId(id);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

}
