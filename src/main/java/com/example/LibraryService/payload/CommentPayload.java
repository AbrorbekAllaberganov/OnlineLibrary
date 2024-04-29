package com.example.LibraryService.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentPayload {
    String text;
    Long bookId;
    Long userId;

}
