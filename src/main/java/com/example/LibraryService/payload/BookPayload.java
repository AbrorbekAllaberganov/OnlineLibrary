package com.example.LibraryService.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookPayload {
    String name;
    String description;
    Long userId;
    String hashId;
    Long categoryId;
    String text;
}
