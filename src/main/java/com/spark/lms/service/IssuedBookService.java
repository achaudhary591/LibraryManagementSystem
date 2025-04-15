package com.spark.lms.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spark.lms.common.Constants;
import com.spark.lms.model.Book;
import com.spark.lms.model.IssuedBook;
import com.spark.lms.repository.IssuedBookRepository;

@Service
public class IssuedBookService {

    @Autowired
    private IssuedBookRepository issuedBookRepository;
    
    public IssuedBook get(Long id) {
        return issuedBookRepository.findById(id).orElse(null);
    }
    
    public IssuedBook save(IssuedBook issuedBook) {
        return issuedBookRepository.save(issuedBook);
    }
    
    public IssuedBook addNew(IssuedBook issuedBook) {
        issuedBook.setReturned(Constants.BOOK_NOT_RETURNED);
        return issuedBookRepository.save(issuedBook);
    }
    
    public IssuedBook returnBook(IssuedBook issuedBook) {
        issuedBook.setReturned(Constants.BOOK_RETURNED);
        issuedBook.setReturnDate(new Date());
        return issuedBookRepository.save(issuedBook);
    }
    
    public Long getCountByBook(Book book) {
        return issuedBookRepository.countByBook(book);
    }
}
