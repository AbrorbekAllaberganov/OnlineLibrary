package com.example.LibraryService.service.impl;

import com.example.LibraryService.entity.Category;
import com.example.LibraryService.exceptions.ResourceNotFound;
import com.example.LibraryService.payload.Result;
import com.example.LibraryService.repository.BookRepository;
import com.example.LibraryService.repository.CategoryRepository;
import com.example.LibraryService.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Override
    public Result saveCategory(String name) {
        try {
            Category category = new Category();
            category.setName(name);

            categoryRepository.save(category);
            return Result.success(category);
        } catch (Exception e) {
            return Result.exception(e);
        }
    }

    @Override
    public Result deleteCategory(Long categoryId) {
        try {
            if (bookRepository.getBookListByCategoryId(categoryId).size()==0){
                categoryRepository.deleteById(categoryId);
                return Result.success("Category has been deleted");
            }else {
                return Result.message("The category has some books, so you cannot delete the category", false);
            }
        }catch (Exception e){
            return Result.exception(e);
        }
    }

    @Override
    public Result updateCategory(Long categoryId, String name) {
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFound("category", "id", categoryId));
            category.setName(name);

            categoryRepository.save(category);
            return Result.success(category);
        } catch (Exception e) {
            return Result.exception(e);
        }
    }

    @Override
    public Result getCategoryList() {
        return Result.success(categoryRepository.findAll(Sort.by("createdAt")));
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("user", "id", id)
        );
    }
}
