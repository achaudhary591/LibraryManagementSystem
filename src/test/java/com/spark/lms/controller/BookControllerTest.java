package com.spark.lms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.spark.lms.common.Constants;
import com.spark.lms.model.Book;
import com.spark.lms.model.Category;
import com.spark.lms.service.BookService;
import com.spark.lms.service.CategoryService;
import com.spark.lms.service.IssueService;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BookService bookService;
    
    @MockBean
    private CategoryService categoryService;
    
    @MockBean
    private IssueService issueService;
    
    private Book testBook;
    private Category testCategory;
    private List<Book> books;
    private List<Category> categories;
    
    @BeforeEach
    public void setup() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setTag("TB001");
        testBook.setAuthors("Test Author");
        testBook.setCategory(testCategory);
        testBook.setStatus(Constants.BOOK_STATUS_AVAILABLE);
        testBook.setCreatedDate(new Date());
        
        books = new ArrayList<>();
        books.add(testBook);
        
        categories = new ArrayList<>();
        categories.add(testCategory);
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testListBooks() throws Exception {
        // Arrange
        when(bookService.getAll()).thenReturn(books);
        
        // Act & Assert
        mockMvc.perform(get("/book/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("book/list"))
            .andExpect(model().attributeExists("books"))
            .andExpect(model().attribute("books", books));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAddBookForm() throws Exception {
        // Arrange
        when(categoryService.getAll()).thenReturn(categories);
        
        // Act & Assert
        mockMvc.perform(get("/book/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("book/form"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attributeExists("categories"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAddBook() throws Exception {
        // Arrange
        when(categoryService.get(anyLong())).thenReturn(testCategory);
        when(bookService.addNew(any(Book.class))).thenReturn(testBook);
        
        // Act & Assert
        mockMvc.perform(post("/book/add")
                .param("title", "Test Book")
                .param("tag", "TB001")
                .param("authors", "Test Author")
                .param("category.id", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/book/list"))
            .andExpect(flash().attributeExists("successMsg"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testEditBookForm() throws Exception {
        // Arrange
        when(bookService.get(anyLong())).thenReturn(testBook);
        when(categoryService.getAll()).thenReturn(categories);
        
        // Act & Assert
        mockMvc.perform(get("/book/edit/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("book/form"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attributeExists("categories"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteBook() throws Exception {
        // Arrange
        doNothing().when(bookService).delete(anyLong());
        
        // Act & Assert
        mockMvc.perform(get("/book/delete/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/book/list"))
            .andExpect(flash().attributeExists("successMsg"));
    }
}
