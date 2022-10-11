CREATE TABLE Student
(
    studentId INT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255),
    graduationYear INT,
    PRIMARY KEY(studentId)
);

CREATE TABLE Course
(
    courseId INT NOT NULL AUTO_INCREMENT,
    courseName VARCHAR(255) NOT NULL,
    subjectArea VARCHAR(255),
    creditAmount INT NOT NULL,
    studentCapacity INT,
    semesterCode VARCHAR(255),
    PRIMARY KEY(courseId)
);

CREATE TABLE StudentCourse
(
    studentId INT NOT NULL,
    courseId INT NOT NULL,
    FOREIGN KEY (studentId) REFERENCES Student(studentId),
    FOREIGN KEY (courseId) REFERENCES Course(courseId),
    PRIMARY KEY (studentId, courseId)
);

INSERT INTO Student (firstName, lastName, graduationYear)
VALUES ('Scott', 'Summers', '2024');
INSERT INTO Student (firstName, lastName, graduationYear)
VALUES ('Jean', 'Grey', '2024');
INSERT INTO Student (firstName, lastName, graduationYear)
VALUES ('Hank', 'McCoy', '2023');
INSERT INTO Student (firstName, lastName, graduationYear)
VALUES ('Robert', 'Drake', '2025');
INSERT INTO Student (firstName, lastName, graduationYear)
VALUES ('Warren', 'Worthington', '2024');

INSERT INTO Course (courseName, subjectArea, creditAmount, studentCapacity, semesterCode)
VALUES ('English', 'English', 5, 3, 'AUTUMN2022');
INSERT INTO Course (courseName, subjectArea, creditAmount, studentCapacity, semesterCode)
VALUES ('English Literature', 'English', 5, 5, 'WINTER2022');
INSERT INTO Course (courseName, subjectArea, creditAmount, studentCapacity, semesterCode)
VALUES ('Maths', 'Maths', 7, 3, 'AUTUMN2022');
INSERT INTO Course (courseName, subjectArea, creditAmount, studentCapacity, semesterCode)
VALUES ('Physics', 'Science', 5, 5, 'WINTER2022');
INSERT INTO Course (courseName, subjectArea, creditAmount, studentCapacity, semesterCode)
VALUES ('Chemistry', 'Science', 5, 5, 'WINTER2022');

INSERT INTO StudentCourse (studentId, courseId)
VALUES (1,1);
INSERT INTO StudentCourse (studentId, courseId)
VALUES (2,1);
INSERT INTO StudentCourse (studentId, courseId)
VALUES (3,2);
INSERT INTO StudentCourse (studentId, courseId)
VALUES (4,3);
INSERT INTO StudentCourse (studentId, courseId)
VALUES (5,3);
INSERT INTO StudentCourse (studentId, courseId)
VALUES (1,4);
INSERT INTO StudentCourse (studentId, courseId)
VALUES (3,4);
INSERT INTO StudentCourse (studentId, courseId)
VALUES (3,5);
INSERT INTO StudentCourse (studentId, courseId)
VALUES (4,5);