package com.example.LibraryService.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookPayload {
    String name;
    String description;
    Long userId;
    String hashId;
    List<Long> categoryIds;
    String text;
}
