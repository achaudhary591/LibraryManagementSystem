package com.spark.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spark.lms.model.Book;
import com.spark.lms.model.IssuedBook;

/**
 * Repository for IssuedBook entity
 */
@Repository
public interface IssuedBookRepository extends JpaRepository<IssuedBook, Long> {
    
    /**
     * Count issued books by book
     * 
     * @param book the book
     * @return count of issued books
     */
    Long countByBook(Book book);
    
    /**
     * Find issued books by book
     * 
     * @param book the book
     * @return list of issued books
     */
    List<IssuedBook> findByBook(Book book);
}
