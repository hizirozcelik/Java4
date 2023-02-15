package ca.sheridancollege.ozcelikh.viewmodels;

import java.util.List;

import ca.sheridancollege.ozcelikh.beans.Course;
import ca.sheridancollege.ozcelikh.beans.Professor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfAssignView {

	private Professor professor;
	private Course course;
	private List<Professor> profList;
	private List<Course> courseList;

}
