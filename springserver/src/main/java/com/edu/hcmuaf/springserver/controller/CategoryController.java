package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.entity.Category;
import com.edu.hcmuaf.springserver.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("")
    public ResponseEntity<?> getListCategory() {
        List<Category> listCategory = categoryService.getAllCategory();
        if(listCategory != null) {
            return ResponseEntity.ok(listCategory);
        }
        return ResponseEntity.badRequest().body(null);
    }
}
