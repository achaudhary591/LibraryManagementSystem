package com.spark.lms.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotEmpty(message = "*Please enter title")
    @NotNull(message = "*Please enter title")
    @Column(name = "title")
    private String title;
    
    @NotEmpty(message = "*Please enter tag")
    @NotNull(message = "*Please enter tag")
    @Column(name = "tag")
    private String tag;
    
    @NotEmpty(message = "*Please enter authors")
    @NotNull(message = "*Please enter authors")
    @Column(name = "authors")
    private String authors;
    
    @Column(name = "publisher")
    private String publisher;
    
    @Column(name = "isbn")
    private String isbn;
    
    @Column(name = "year_of_publication")
    private Integer yearOfPublication;
    
    @Column(name = "num_of_copies")
    private Integer numOfCopies;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Column(name = "created_date")
    private Date createdDate;
    
    @Column(name = "status")
    private Integer status;
    
    public Book() {}

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
