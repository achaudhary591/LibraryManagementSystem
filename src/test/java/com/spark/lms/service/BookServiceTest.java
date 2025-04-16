package com.spark.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spark.lms.common.Constants;
import com.spark.lms.model.Book;
import com.spark.lms.model.Category;
import com.spark.lms.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    
    @InjectMocks
    private BookService bookService;
    
    private Book testBook;
    private Category testCategory;
    
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
    }
    
    @Test
    public void testGetAllBooks() {
        // Arrange
        List<Book> books = new ArrayList<>();
        books.add(testBook);
        when(bookRepository.findAll()).thenReturn(books);
        
        // Act
        List<Book> result = bookService.getAll();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
    }
    
    @Test
    public void testGetBookById() {
        // Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(testBook));
        
        // Act
        Book result = bookService.get(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
    }
    
    @Test
    public void testGetBookByIdNotFound() {
        // Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // Act
        Book result = bookService.get(1L);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testAddNewBook() {
        // Arrange
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        
        // Act
        Book result = bookService.addNew(testBook);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        assertEquals(Constants.BOOK_STATUS_AVAILABLE, result.getStatus());
        assertNotNull(result.getCreatedDate());
    }
    
    @Test
    public void testSaveBook() {
        // Arrange
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        
        // Act
        Book result = bookService.save(testBook);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
    }
    
    @Test
    public void testDeleteBook() {
        // Act
        bookService.delete(1L);
        
        // Assert
        verify(bookRepository, times(1)).deleteById(1L);
    }
    
    @Test
    public void testGetBooksByCategory() {
        // Arrange
        List<Book> books = new ArrayList<>();
        books.add(testBook);
        when(bookRepository.findByCategory(any(Category.class))).thenReturn(books);
        
        // Act
        List<Book> result = bookService.getByCategory(testCategory);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
    }
}
