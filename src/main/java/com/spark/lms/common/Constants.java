package com.spark.lms.common;

import java.util.Arrays;
import java.util.List;

public class Constants {
    
    // Book status constants
    public static final Integer BOOK_STATUS_AVAILABLE = 1;
    public static final Integer BOOK_STATUS_ISSUED = 2;
    
    // Book return status constants
    public static final Integer BOOK_RETURNED = 1;
    public static final Integer BOOK_NOT_RETURNED = 0;
    
    // Member types
    public static final String MEMBER_STUDENT = "Student";
    public static final String MEMBER_PARENT = "Parent";
    public static final String MEMBER_OTHER = "Other";
    
    public static final List<String> MEMBER_TYPES = Arrays.asList(MEMBER_STUDENT, MEMBER_PARENT, MEMBER_OTHER);
    
    // User roles
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_LIBRARIAN = "ROLE_LIBRARIAN";
    public static final String ROLE_STUDENT = "ROLE_STUDENT";
}
