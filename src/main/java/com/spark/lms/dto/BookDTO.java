package com.spark.lms.dto;

import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for Book entity
 * Used to transfer book data between processes or layers
 */
public class BookDTO {
    
    private Long id;
    
    @NotEmpty(message = "*Please enter title")
    private String title;
    
    @NotEmpty(message = "*Please enter tag")
    private String tag;
    
    @NotEmpty(message = "*Please enter authors")
    private String authors;
    
    private String publisher;
    
    private String isbn;
    
    private Integer yearOfPublication;
    
    @Min(value = 1, message = "*Must have at least 1 copy")
    private Integer numOfCopies;
    
    @NotNull(message = "*Please select a category")
    private Long categoryId;
    
    private String categoryName;
    
    private Date createdDate;
    
    private Integer status;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(Integer yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public Integer getNumOfCopies() {
        return numOfCopies;
    }

    public void setNumOfCopies(Integer numOfCopies) {
        this.numOfCopies = numOfCopies;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
