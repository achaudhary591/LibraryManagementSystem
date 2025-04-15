package com.spark.lms.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spark.lms.common.Constants;
import com.spark.lms.model.Issue;
import com.spark.lms.model.IssuedBook;
import com.spark.lms.model.Member;
import com.spark.lms.repository.IssueRepository;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;
    
    public List<Issue> getAll() {
        return issueRepository.findAll();
    }
    
    public Issue get(Long id) {
        return issueRepository.findById(id).orElse(null);
    }
    
    public List<Issue> findByMember(Member member) {
        return issueRepository.findByMember(member);
    }
    
    public Issue save(Issue issue) {
        return issueRepository.save(issue);
    }
    
    /**
     * Add a new issue with default values
     * @param issue The issue to add
     * @return The saved issue
     */
    public Issue addNew(Issue issue) {
        // Set issue date to current date
        issue.setIssueDate(new Date());
        
        // Set expected return date to 14 days from now
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issue.getIssueDate());
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        issue.setExpectedReturnDate(calendar.getTime());
        
        // Set status to not returned
        issue.setStatus(Constants.BOOK_NOT_RETURNED);
        
        return issueRepository.save(issue);
    }
    
    /**
     * Get all issues that have at least one book not returned
     * @return List of issues with unreturned books
     */
    public List<Issue> getAllUnreturned() {
        List<Issue> allIssues = issueRepository.findAll();
        
        // Filter issues that have at least one book not returned
        return allIssues.stream()
            .filter(issue -> issue.getIssuedBooks().stream()
                .anyMatch(book -> Constants.BOOK_NOT_RETURNED.equals(book.getReturned())))
            .collect(Collectors.toList());
    }
}
