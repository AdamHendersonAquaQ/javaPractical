package com.aquaq.training.javaPractical.errorHandling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentErrorResponseTest {

    @Test
    public void statusTest()
    {
        StudentErrorResponse studentErrorResponse = new StudentErrorResponse();
        studentErrorResponse.setStatus(1);
        assertEquals(studentErrorResponse.getStatus(),1);
    }

    @Test
    public void messageTest()
    {
        StudentErrorResponse studentErrorResponse = new StudentErrorResponse();
        studentErrorResponse.setMessage("test");
        assertEquals(studentErrorResponse.getMessage(),"test");
    }

    @Test
    public void timestampTest()
    {
        StudentErrorResponse studentErrorResponse = new StudentErrorResponse();
        studentErrorResponse.setTimeStamp(1);
        assertEquals(studentErrorResponse.getTimeStamp(),1);
    }

    @Test
    public void constructorTest()
    {
        StudentErrorResponse studentErrorResponse = new StudentErrorResponse(1,"test",2);
        assertEquals(studentErrorResponse.getStatus(),1);
        assertEquals(studentErrorResponse.getMessage(),"test");
        assertEquals(studentErrorResponse.getTimeStamp(),2);
    }
}
