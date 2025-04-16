package com.spark.lms.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spark.lms.model.Book;
import com.spark.lms.model.Category;
import com.spark.lms.service.BookService;
import com.spark.lms.service.CategoryService;

@RestController
@RequestMapping(value = "/rest/book")
public class BookRestController {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/{categoryId}/available")
    public List<Book> getAvailableBooksByCategory(@PathVariable Long categoryId) {
        Category category = categoryService.get(categoryId);
        if (category != null) {
            return bookService.geAvailabletByCategory(category);
        }
        return null;
    }
}
