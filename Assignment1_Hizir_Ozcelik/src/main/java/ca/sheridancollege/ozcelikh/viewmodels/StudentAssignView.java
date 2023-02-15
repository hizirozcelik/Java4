package ca.sheridancollege.ozcelikh.viewmodels;

import java.util.List;

import ca.sheridancollege.ozcelikh.beans.Course;
import ca.sheridancollege.ozcelikh.beans.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAssignView {

    private Student student;
    private Course course;
    private List<Student> studentList;
    private List<Course> courseList;



}
