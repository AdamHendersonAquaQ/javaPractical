CREATE TABLE Student
(
    id INT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255),
    graduationYear DATE,
    PRIMARY KEY(id)
);

CREATE TABLE Course
(
    id INT NOT NULL AUTO_INCREMENT,
    courseName VARCHAR(255) NOT NULL,
    subjectArea VARCHAR(255),
    creditAmount INT NOT NULL,
    studentCapacity INT,
    semesterCode VARCHAR(255),
    PRIMARY KEY(id)
);

CREATE TABLE StudentCourse
(
    studentId INT NOT NULL,
    courseId INT NOT NULL,
    FOREIGN KEY (studentId) REFERENCES Student(id),
    FOREIGN KEY (courseId) REFERENCES Course(id),
    PRIMARY KEY (studentId, courseId)
);