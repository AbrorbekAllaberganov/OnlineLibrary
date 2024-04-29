package com.example.LibraryService.controller;

import com.example.LibraryService.payload.Result;
import com.example.LibraryService.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/controller/{name}")
    public ResponseEntity<Result> addCategory(@PathVariable String name) {
        Result result=categoryService.saveCategory(name);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

    @PutMapping("/admin/controller")
    public ResponseEntity<Result> editCategory(@RequestParam("name") String name,
                                               @RequestParam("id") long id) {
        Result result=categoryService.updateCategory(id,name);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

    @DeleteMapping("/admin/controller/{id}")
    public ResponseEntity<Result> deleteCategory(@PathVariable long id) {
        Result result=categoryService.deleteCategory(id);
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

    @GetMapping("/user/controller")
    public ResponseEntity<Result> getCategories() {
        Result result=categoryService.getCategoryList();
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

}
