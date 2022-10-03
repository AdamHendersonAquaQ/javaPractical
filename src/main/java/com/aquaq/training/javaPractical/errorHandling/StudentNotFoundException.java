package com.aquaq.training.javaPractical.errorHandling;

public class StudentNotFoundException extends RuntimeException{

    public StudentNotFoundException(String message) {
        super(message);
    }
}
