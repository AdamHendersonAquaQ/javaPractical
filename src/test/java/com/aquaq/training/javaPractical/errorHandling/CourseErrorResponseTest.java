package com.aquaq.training.javaPractical.errorHandling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseErrorResponseTest {

    @Test
    public void statusTest()
    {
        CourseErrorResponse courseErrorResponse = new CourseErrorResponse();
        courseErrorResponse.setStatus(1);
        assertEquals(courseErrorResponse.getStatus(),1);
    }

    @Test
    public void messageTest()
    {
        CourseErrorResponse courseErrorResponse = new CourseErrorResponse();
        courseErrorResponse.setMessage("test");
        assertEquals(courseErrorResponse.getMessage(),"test");
    }

    @Test
    public void timestampTest()
    {
        CourseErrorResponse courseErrorResponse = new CourseErrorResponse();
        courseErrorResponse.setTimeStamp(1);
        assertEquals(courseErrorResponse.getTimeStamp(),1);
    }

    @Test
    public void constructorTest()
    {
        CourseErrorResponse courseErrorResponse = new CourseErrorResponse(1,"test",2);
        assertEquals(courseErrorResponse.getStatus(),1);
        assertEquals(courseErrorResponse.getMessage(),"test");
        assertEquals(courseErrorResponse.getTimeStamp(),2);
    }
}
