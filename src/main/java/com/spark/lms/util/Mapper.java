package com.spark.lms.util;

import org.springframework.stereotype.Component;

import com.spark.lms.dto.BookDTO;
import com.spark.lms.dto.MemberDTO;
import com.spark.lms.model.Book;
import com.spark.lms.model.Category;
import com.spark.lms.model.Member;

/**
 * Utility class for mapping between entities and DTOs
 */
@Component
public class Mapper {
    
    /**
     * Convert Book entity to BookDTO
     */
    public BookDTO toBookDTO(Book book) {
        if (book == null) {
            return null;
        }
        
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setTag(book.getTag());
        dto.setAuthors(book.getAuthors());
        dto.setPublisher(book.getPublisher());
        dto.setIsbn(book.getIsbn());
        dto.setYearOfPublication(book.getYearOfPublication());
        dto.setNumOfCopies(book.getNumOfCopies());
        dto.setCreatedDate(book.getCreatedDate());
        dto.setStatus(book.getStatus());
        
        if (book.getCategory() != null) {
            dto.setCategoryId(book.getCategory().getId());
            dto.setCategoryName(book.getCategory().getName());
        }
        
        return dto;
    }
    
    /**
     * Convert BookDTO to Book entity
     */
    public Book toBook(BookDTO dto, Category category) {
        if (dto == null) {
            return null;
        }
        
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setTag(dto.getTag());
        book.setAuthors(dto.getAuthors());
        book.setPublisher(dto.getPublisher());
        book.setIsbn(dto.getIsbn());
        book.setYearOfPublication(dto.getYearOfPublication());
        book.setNumOfCopies(dto.getNumOfCopies());
        book.setCreatedDate(dto.getCreatedDate());
        book.setStatus(dto.getStatus());
        book.setCategory(category);
        
        return book;
    }
    
    /**
     * Convert Member entity to MemberDTO
     */
    public MemberDTO toMemberDTO(Member member) {
        if (member == null) {
            return null;
        }
        
        MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setMobile(member.getContact()); // Using contact field instead of mobile
        dto.setEmail(member.getEmail());
        dto.setGender(member.getGender());
        dto.setType(member.getType());
        dto.setJoiningDate(member.getJoiningDate());
        
        return dto;
    }
    
    /**
     * Convert MemberDTO to Member entity
     */
    public Member toMember(MemberDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Member member = new Member();
        member.setId(dto.getId());
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setContact(dto.getMobile()); // Using contact field instead of mobile
        member.setEmail(dto.getEmail());
        member.setGender(dto.getGender());
        member.setType(dto.getType());
        member.setJoiningDate(dto.getJoiningDate());
        
        return member;
    }
}
