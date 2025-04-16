package com.spark.lms.dto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Data Transfer Object for Member entity
 * Used to transfer member data between processes or layers
 */
public class MemberDTO {
    
    private Long id;
    
    @NotEmpty(message = "*Please enter first name")
    private String firstName;
    
    private String middleName;
    
    private String lastName;
    
    @Pattern(regexp = "^\\d{10}$", message = "*Please enter a valid 10 digit mobile number")
    @NotEmpty(message = "*Please enter mobile number")
    private String mobile;
    
    @Email(message = "*Please enter a valid email")
    @NotEmpty(message = "*Please enter email")
    private String email;
    
    @NotNull(message = "*Please select gender")
    private String gender;
    
    @NotNull(message = "*Please select type")
    private String type;
    
    private Date joiningDate;
    
    private Date dateOfBirth;
    
    private String createAccountFlag;
    
    private String username;
    
    private String password;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + (middleName != null ? middleName + " " : "") + (lastName != null ? lastName : "");
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCreateAccountFlag() {
        return createAccountFlag;
    }

    public void setCreateAccountFlag(String createAccountFlag) {
        this.createAccountFlag = createAccountFlag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
