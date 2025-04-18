package com.spark.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spark.lms.model.Book;
import com.spark.lms.model.Category;

/**
 * Repository for Book entity
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    /**
     * Find a book by its tag
     * 
     * @param tag the book tag
     * @return the book
     */
    Book findByTag(String tag);
    
    /**
     * Find books by category
     * 
     * @param category the category
     * @return list of books in the category
     */
    List<Book> findByCategory(Category category);
    
    /**
     * Find books by category and status
     * 
     * @param category the category
     * @param status the status
     * @return list of books in the category with the status
     */
    List<Book> findByCategoryAndStatus(Category category, Integer status);
    
    /**
     * Count books by status
     * 
     * @param status the status
     * @return count of books with the status
     */
    Long countByStatus(Integer status);
    
    /**
     * Search books by title, authors, or ISBN
     * 
     * @param title part of the title
     * @param authors part of the authors
     * @param isbn part of the ISBN
     * @return list of matching books
     */
    List<Book> findByTitleContainingOrAuthorsContainingOrIsbnContaining(String title, String authors, String isbn);
    
    /**
     * Find recently added books
     * 
     * @param limit maximum number of books to return
     * @return list of recently added books
     */
    @Query(value = "SELECT * FROM book ORDER BY created_date DESC LIMIT :limit", nativeQuery = true)
    List<Book> findRecentlyAdded(@Param("limit") int limit);
}
