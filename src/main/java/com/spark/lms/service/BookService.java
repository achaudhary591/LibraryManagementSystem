package com.spark.lms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spark.lms.common.Constants;
import com.spark.lms.exception.ResourceNotFoundException;
import com.spark.lms.model.Book;
import com.spark.lms.model.Category;
import com.spark.lms.model.IssuedBook;
import com.spark.lms.repository.BookRepository;
import com.spark.lms.repository.IssuedBookRepository;

/**
 * Service for managing books
 */
@Service
public class BookService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private IssuedBookRepository issuedBookRepository;
    
    /**
     * Get all books
     * 
     * @return list of all books
     */
    public List<Book> getAll() {
        logger.debug("Fetching all books");
        return bookRepository.findAll();
    }
    
    /**
     * Get all books by category
     * 
     * @param category the category to filter by
     * @return list of books in the category
     */
    public List<Book> getByCategory(Category category) {
        logger.debug("Fetching books by category: {}", category.getName());
        return bookRepository.findByCategory(category);
    }
    
    /**
     * Get available books by category
     * 
     * @param category the category to filter by
     * @return list of available books in the category
     */
    public List<Book> geAvailabletByCategory(Category category) {
        logger.debug("Fetching available books by category: {}", category.getName());
        return bookRepository.findByCategoryAndStatus(category, Constants.BOOK_STATUS_AVAILABLE);
    }
    
    /**
     * Get a book by ID
     * 
     * @param id the book ID
     * @return the book
     * @throws ResourceNotFoundException if book not found
     */
    public Book get(Long id) {
        logger.debug("Fetching book by ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", id);
                    return new ResourceNotFoundException("Book", "id", id);
                });
    }
    
    /**
     * Get a book by tag
     * 
     * @param tag the book tag
     * @return the book
     */
    public Book getByTag(String tag) {
        logger.debug("Fetching book by tag: {}", tag);
        return bookRepository.findByTag(tag);
    }
    
    /**
     * Get books by IDs
     * 
     * @param ids list of book IDs
     * @return list of books
     */
    public List<Book> get(List<Long> ids) {
        logger.debug("Fetching books by IDs: {}", ids);
        List<Book> books = new ArrayList<>();
        for(Long id : ids) {
            books.add(get(id));
        }
        return books;
    }
    
    /**
     * Save a book
     * 
     * @param book the book to save
     * @return the saved book
     */
    public Book save(Book book) {
        if (book.getId() == null) {
            book.setCreatedDate(new Date());
            book.setStatus(Constants.BOOK_STATUS_AVAILABLE);
            logger.info("Creating new book: {}", book.getTitle());
        } else {
            logger.info("Updating book: {}", book.getTitle());
        }
        return bookRepository.save(book);
    }
    
    /**
     * Add a new book
     * 
     * @param book the book to add
     * @return the added book
     */
    public Book addNew(Book book) {
        book.setCreatedDate(new Date());
        book.setStatus(Constants.BOOK_STATUS_AVAILABLE);
        logger.info("Adding new book: {}", book.getTitle());
        return bookRepository.save(book);
    }
    
    /**
     * Delete a book by ID
     * 
     * @param id the book ID
     */
    public void delete(Long id) {
        Book book = get(id);
        logger.info("Deleting book: {} (ID: {})", book.getTitle(), id);
        bookRepository.deleteById(id);
    }
    
    /**
     * Check if a book has been used in any issues
     * 
     * @param book the book to check
     * @return true if the book has been used, false otherwise
     */
    public boolean hasUsage(Book book) {
        logger.debug("Checking if book has usage: {}", book.getTitle());
        List<IssuedBook> issuedBooks = issuedBookRepository.findByBook(book);
        return issuedBooks != null && issuedBooks.size() > 0;
    }
    
    /**
     * Search for books by title, author, or ISBN
     * 
     * @param keyword the search keyword
     * @return list of matching books
     */
    public List<Book> search(String keyword) {
        logger.debug("Searching books with keyword: {}", keyword);
        return bookRepository.findByTitleContainingOrAuthorsContainingOrIsbnContaining(keyword, keyword, keyword);
    }
    
    /**
     * Get total count of books
     * 
     * @return total count of books
     */
    public Long getTotalCount() {
        logger.debug("Getting total count of books");
        return bookRepository.count();
    }
    
    /**
     * Get total count of issued books
     * 
     * @return total count of issued books
     */
    public Long getTotalIssuedBooks() {
        logger.debug("Getting total count of issued books");
        return bookRepository.countByStatus(Constants.BOOK_STATUS_ISSUED);
    }
    
    /**
     * Get recently added books
     * 
     * @param limit maximum number of books to return
     * @return list of recently added books
     */
    public List<Book> getRecentlyAdded(int limit) {
        logger.debug("Getting recently added books (limit: {})", limit);
        return bookRepository.findRecentlyAdded(limit);
    }
}
