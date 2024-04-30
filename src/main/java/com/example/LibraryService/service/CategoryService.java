package com.example.LibraryService.service;


import com.example.LibraryService.entity.Category;
import com.example.LibraryService.payload.Result;

public interface CategoryService {
    Result saveCategory(String name);
    Result deleteCategory(Long categoryId);
    Result updateCategory(Long categoryId, String text);
    Result getCategoryList();

    Result getCategoryById(long id);
}
