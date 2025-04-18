package com.spark.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not found.
 * This will trigger a 404 HTTP response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    /**
     * Constructs a new resource not found exception
     * 
     * @param resourceName the name of the resource that wasn't found
     * @param fieldName the field name used in the search
     * @param fieldValue the field value used in the search
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Get the name of the resource that wasn't found
     * 
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Get the field name used in the search
     * 
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Get the field value used in the search
     * 
     * @return the field value
     */
    public Object getFieldValue() {
        return fieldValue;
    }
}
