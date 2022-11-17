package com.aquaq.training.javaPractical.controllers;

import com.aquaq.training.javaPractical.errorHandling.CourseEnrollmentException;
import com.aquaq.training.javaPractical.errorHandling.CourseNotFoundException;
import com.aquaq.training.javaPractical.errorHandling.EnrollmentErrorResponse;
import com.aquaq.training.javaPractical.errorHandling.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<EnrollmentErrorResponse> handleCourseException(CourseNotFoundException exc) {
        return new ResponseEntity<>(new EnrollmentErrorResponse(HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),System.currentTimeMillis()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<EnrollmentErrorResponse> handleCourseException(CourseEnrollmentException exc) {
        return new ResponseEntity<>(new EnrollmentErrorResponse(HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),System.currentTimeMillis()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<EnrollmentErrorResponse> handleStudentException(StudentNotFoundException exc) {
        return new ResponseEntity<>(new EnrollmentErrorResponse(HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),System.currentTimeMillis()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<EnrollmentErrorResponse> handleCourseException(HttpMessageNotReadableException exc) {
        return new ResponseEntity<>(new EnrollmentErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "This is not a valid format",System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<EnrollmentErrorResponse> handleException(Exception exc) {
        return new ResponseEntity<>(new EnrollmentErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }
}
