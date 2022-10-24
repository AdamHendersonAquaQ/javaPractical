package com.aquaq.training.javaPractical.errorHandling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnrollmentErrorResponseTest {
    @Test
    public void statusTest()
    {
        EnrollmentErrorResponse enrollmentErrorResponse = new EnrollmentErrorResponse();
        enrollmentErrorResponse.setStatus(1);
        assertEquals(enrollmentErrorResponse.getStatus(),1);
    }

    @Test
    public void messageTest()
    {
        EnrollmentErrorResponse enrollmentErrorResponse = new EnrollmentErrorResponse();
        enrollmentErrorResponse.setMessage("test");
        assertEquals(enrollmentErrorResponse.getMessage(),"test");
    }

    @Test
    public void timestampTest()
    {
        EnrollmentErrorResponse enrollmentErrorResponse = new EnrollmentErrorResponse();
        enrollmentErrorResponse.setTimeStamp(1);
        assertEquals(enrollmentErrorResponse.getTimeStamp(),1);
    }

    @Test
    public void constructorTest()
    {
        EnrollmentErrorResponse enrollmentErrorResponse = new EnrollmentErrorResponse(1,"test",2);
        assertEquals(enrollmentErrorResponse.getStatus(),1);
        assertEquals(enrollmentErrorResponse.getMessage(),"test");
        assertEquals(enrollmentErrorResponse.getTimeStamp(),2);
    }
}
