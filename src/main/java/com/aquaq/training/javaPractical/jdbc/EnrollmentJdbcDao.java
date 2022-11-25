package com.aquaq.training.javaPractical.jdbc;

import com.aquaq.training.javaPractical.classes.Course;
import com.aquaq.training.javaPractical.classes.Enrollment;
import com.aquaq.training.javaPractical.errorHandling.CourseEnrollmentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class EnrollmentJdbcDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CourseJdbcDao courseJdbcDao;

    @Autowired
    StudentJdbcDao studentJdbcDao;

    private static final Logger logger = Logger.getLogger(CourseJdbcDao.class.getName());

    public List<Enrollment> findAll() {
        logger.log(Level.INFO,"Finding all enrollment records");
        List<Enrollment> records = jdbcTemplate.query("SELECT " +
                        "StudentCourse.studentId, firstName, lastName, StudentCourse.courseId, courseName " +
                        "FROM StudentCourse " +
                        "LEFT JOIN Student ON Student.studentId = StudentCourse.studentId " +
                        "LEFT JOIN Course ON Course.courseId = StudentCourse.courseId",
                new BeanPropertyRowMapper<>(Enrollment.class));
        if (records.size() == 0)
            throw throwEnrollmentError("No enrollments found");
        else
            return records;
    }

    public List<Enrollment> findByStudent(int id) {
        logger.log(Level.INFO,"Finding all enrollment records for student " + id);
        List<Enrollment> records = jdbcTemplate.query("SELECT " +
                        "StudentCourse.studentId, firstName, lastName, StudentCourse.courseId, courseName " +
                        "FROM StudentCourse " +
                        "LEFT JOIN Student ON Student.studentId = StudentCourse.studentId " +
                        "LEFT JOIN Course ON Course.courseId = StudentCourse.courseId " +
                        "WHERE StudentCourse.studentId = ?",
                new BeanPropertyRowMapper<>(Enrollment.class), id);
        if (records.size() == 0)
            throw throwEnrollmentError("No enrollments found for Student ID: " +id);
        else
            return records;
    }

    public List<Enrollment> findByCourse(int id) {
        logger.log(Level.INFO,"Finding all enrollment records for course " + id);
        List<Enrollment> records = jdbcTemplate.query("SELECT " +
                        "StudentCourse.studentId, firstName, lastName, StudentCourse.courseId, courseName " +
                        "FROM StudentCourse " +
                        "LEFT JOIN Student ON Student.studentId = StudentCourse.studentId " +
                        "LEFT JOIN Course ON Course.courseId = StudentCourse.courseId " +
                        "WHERE StudentCourse.courseId = ?",
                new BeanPropertyRowMapper<>(Enrollment.class), id);
        if (records.size() == 0)
            throw throwEnrollmentError("No enrollments found for Course ID: " +id);
        else
            return records;
    }

    public List<Enrollment> findEnrollment(int studentId, int courseId) {
        logger.log(Level.INFO,"Finding all enrollment record for course " + courseId + "and student " + studentId);
        List<Enrollment> records = jdbcTemplate.query("SELECT " +
                        "StudentCourse.studentId, firstName, lastName, StudentCourse.courseId, courseName " +
                        "FROM StudentCourse " +
                        "LEFT JOIN Student ON Student.studentId = StudentCourse.studentId " +
                        "LEFT JOIN Course ON Course.courseId = StudentCourse.courseId " +
                        "WHERE StudentCourse.courseId = ? AND StudentCourse.studentId = ?",
                new BeanPropertyRowMapper<>(Enrollment.class), courseId, studentId);
        if (records.size() == 0)
            throw throwEnrollmentError("No enrollments found for Course ID: " + courseId + " and Student ID: " + studentId);
        else
            return records;

    }

    public String enrollStudent(int studentId, int courseId) {
        if(!checkIfEnrolled(studentId,courseId)) {
            studentJdbcDao.findById(studentId);
            Course course = courseJdbcDao.findById(courseId).get(0);
            int semesterCredits = getStudentCredits(course.getSemesterCode(), studentId);
            int newSemesterCredits = semesterCredits + course.getCreditAmount();
            if (newSemesterCredits <= 20) {
                int availableSpaces = course.getStudentCapacity() - getCurrentCourseCapacity(courseId);
                if (availableSpaces > 0)
                    return enrollStudentInCourse(studentId, courseId);
                else
                    throw throwEnrollmentError("Student could not be added, course capacity has been reached. ");
            } else
                throw throwEnrollmentError("Adding student would exceed their maximum course credits by "
                        + (newSemesterCredits - 20));
        }
        else
            throw throwEnrollmentError("Student is already enrolled in this course");
    }

    public boolean checkIfEnrolled(int studentId, int courseId)
    {
        logger.log(Level.INFO,"Checking if student already enrolled");
        Integer courseCountVar = jdbcTemplate.queryForObject("SELECT Count(studentId) " +
                        "FROM studentCourse WHERE studentId = ? AND courseId = ?",
                Integer.class, studentId, courseId );
        return courseCountVar != null && courseCountVar != 0;
    }

    public int getCurrentCourseCapacity(int courseId) {
        logger.log(Level.INFO,"Getting current course capacity");
        Integer courseCountVar = jdbcTemplate.queryForObject("SELECT COUNT(studentId) " +
                        "AS capacityCount FROM StudentCourse WHERE courseId = ?",
                Integer.class, courseId );
        if(courseCountVar==null)
            courseCountVar=0;
        return courseCountVar;
    }

    public int getStudentCredits(String semesterCode, int studentId)
    {
        if (semesterCode.matches("^[A-Z]+[0-9]{4}$")) {
            logger.log(Level.INFO,"Getting current student credits");
            Integer courseCountVar = jdbcTemplate.queryForObject("SELECT Sum(creditAmount) as courseCount" +
                            " FROM Course LEFT JOIN StudentCourse ON StudentCourse.courseId = Course.courseId" +
                            " WHERE Course.SemesterCode = ? AND StudentId=?",
                    Integer.class, semesterCode, studentId );
            if(courseCountVar==null)
                courseCountVar=0;
            return courseCountVar;
        } else
            throw throwEnrollmentError("Semester not found - " + semesterCode);
    }

    public String enrollStudentInCourse(int studentId, int courseId)
    {
        logger.log(Level.INFO,"Enrolling student "+studentId+" in course "+courseId);
        String sql = ("INSERT INTO StudentCourse (studentId, courseId) " +
                "VALUES (?,?)");
        int returnVal = jdbcTemplate.update(sql,studentId, courseId);
        if (returnVal == 1)
            return "Student has been successfully registered";
        else
            throw throwEnrollmentError("Student "+studentId+" could not be registered in course "+courseId);
    }

    public String unEnrollStudent(int courseId, int studentId) {
        String sql = "DELETE FROM studentCourse WHERE studentId = ? AND courseId = ?";
        logger.log(Level.INFO,"Unenrolling student " + studentId + " from course "+courseId);
        int returnVal = jdbcTemplate.update(sql,studentId,courseId);
        if(returnVal>0)
            return "Student has been successfully unenrolled.";
        else
            throw throwEnrollmentError("Student was not enrolled in this course.");
    }

    private CourseEnrollmentException throwEnrollmentError(String errorMsg)
    {
        logger.log(Level.WARNING, errorMsg);
        return new CourseEnrollmentException(errorMsg);
    }

}
