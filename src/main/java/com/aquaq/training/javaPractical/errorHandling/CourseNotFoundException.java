package com.aquaq.training.javaPractical.errorHandling;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(String message) {
        super(message);
    }
}
