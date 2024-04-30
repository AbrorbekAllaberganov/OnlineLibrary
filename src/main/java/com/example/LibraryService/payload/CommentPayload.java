package com.example.LibraryService.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentPayload {
    String text;
    List<Long> booksId;
    List<Long> usersId;

}
