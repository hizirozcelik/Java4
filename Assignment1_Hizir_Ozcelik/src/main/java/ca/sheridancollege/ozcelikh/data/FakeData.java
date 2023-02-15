package ca.sheridancollege.ozcelikh.data;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ca.sheridancollege.ozcelikh.beans.Course;
import ca.sheridancollege.ozcelikh.beans.Professor;
import ca.sheridancollege.ozcelikh.beans.Student;
import ca.sheridancollege.ozcelikh.repository.CourseRepo;
import ca.sheridancollege.ozcelikh.repository.ProfessorRepo;
import ca.sheridancollege.ozcelikh.repository.StudentRepo;

@Component
public class FakeData implements CommandLineRunner {
	private CourseRepo courseRepo;
	private StudentRepo studentRepo;
	private ProfessorRepo profRepo;

	public FakeData(CourseRepo courseRepo, StudentRepo studentRepo, ProfessorRepo profRepo) {
		super();
		this.courseRepo = courseRepo;
		this.studentRepo = studentRepo;
		this.profRepo = profRepo;
	}

	@Override
	public void run(String... args) throws Exception {

		// sample data
		String[] courseTitle = { "Introduction to Computer Science", "Data Structures and Algorithms", 
		"Programming in Java", "Operating Systems", "Computer Networks", "Database Systems", 
		"Web Development", "Artificial Intelligence", "Machine Learning", "Cryptography" };

		String[] profName = { "John", "Mike", "Mary", "Jane", "Peter", "Paul" };

		String[] firstName = { "James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph",
				"Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward",
				"Brian",
				"Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey" };

		String[] lastName = { "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson",
				"Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia",
				"Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen", "Young",
				"Hernandez",
				"King" };

		String[] courseCode = {"CSCI-101", "CSCI-205", "CSCI-310", "CSCI-400", "CSCI-415", "CSCI-450", "CSCI-480", "CSCI-510", "CSCI-520", "CSCI-540" };


		// Initialize professors with empty course list
		Professor prof1 = Professor.builder().name("Prof. " + profName[0]).courseList(new ArrayList<Course>()).build();
		Professor prof2 = Professor.builder().name("Prof. " + profName[1]).courseList(new ArrayList<Course>()).build();
		Professor prof3 = Professor.builder().name("Prof. " + profName[2]).courseList(new ArrayList<Course>()).build();
		Professor prof4 = Professor.builder().name("Prof. " + profName[3]).courseList(new ArrayList<Course>()).build();

		// save professors to the database
		profRepo.save(prof1);
		profRepo.save(prof2);
		profRepo.save(prof3);
		profRepo.save(prof4);

		// Initialize courses with empty student list and professors
		Course course1 = Course.builder().name(courseTitle[0]).code(courseCode[0])
				.studentList(new ArrayList<Student>()).professor(prof1).build();
		Course course2 = Course.builder().name(courseTitle[1]).code(courseCode[1])
				.studentList(new ArrayList<Student>()).professor(prof1).build();
		Course course3 = Course.builder().name(courseTitle[2]).code(courseCode[2])
				.studentList(new ArrayList<Student>()).professor(prof2).build();
		Course course4 = Course.builder().name(courseTitle[3]).code(courseCode[3])
				.studentList(new ArrayList<Student>()).professor(prof2).build();
		Course course5 = Course.builder().name(courseTitle[4]).code(courseCode[4])
				.studentList(new ArrayList<Student>()).professor(prof3).build();
		Course course6 = Course.builder().name(courseTitle[5]).code(courseCode[5])
				.studentList(new ArrayList<Student>()).professor(prof4).build();



		int num = 999;
		for (int i = 0; i < 6; i++) {
			
			num++;
			String stNumber = "99157" + num;
			Student student = Student.builder().name(firstName[i]).lastName(lastName[i])
					.email(firstName[i].toLowerCase() + lastName[i].toLowerCase() + "@sheridancollege.ca").studentNumber(stNumber)
					.courseList(new ArrayList<Course>()).build();
			studentRepo.save(student);

			// Assign students to courses
			course1.getStudentList().add(student);
			course2.getStudentList().add(student);
			course3.getStudentList().add(student);
			course6.getStudentList().add(student);

		}
		for(int i = 6; i < 12; i++) {
			num++;

			String stNumber = "99157" + num;
			Student student = Student.builder().name(firstName[i]).lastName(lastName[i])
					.email(firstName[i].toLowerCase() + lastName[i].toLowerCase() + "@sheridancollege.ca").studentNumber(stNumber)
					.courseList(new ArrayList<Course>()).build();
			studentRepo.save(student);

			// Assign students to courses
			course1.getStudentList().add(student);
			course2.getStudentList().add(student);
			course4.getStudentList().add(student);
			course5.getStudentList().add(student);

		}
		
		for(int i = 12; i < 18; i++) {
			num++;
			String stNumber = "99157" + num;
			Student student = Student.builder().name(firstName[i]).lastName(lastName[i])
					.email(firstName[i].toLowerCase() + lastName[i].toLowerCase() + "@sheridancollege.ca").studentNumber(stNumber)
					.courseList(new ArrayList<Course>()).build();
			studentRepo.save(student);

			// Assign students to courses
			course1.getStudentList().add(student);
			course3.getStudentList().add(student);
			course4.getStudentList().add(student);
			course6.getStudentList().add(student);		
		}
		
		for(int i = 18; i < 24; i++) {
			num++;
			String stNumber = "99157" + num;
			Student student = Student.builder().name(firstName[i]).lastName(lastName[i])
					.email(firstName[i].toLowerCase() + lastName[i].toLowerCase() + "@sheridancollege.ca").studentNumber(stNumber)
					.courseList(new ArrayList<Course>()).build();
			studentRepo.save(student);

			// Assign students to courses
			course2.getStudentList().add(student);
			course3.getStudentList().add(student);
			course4.getStudentList().add(student);
			course5.getStudentList().add(student);
		}
		
		for(int i = 24; i < 30; i++) {
			num++;
			String stNumber = "99157" + num;
			Student student = Student.builder().name(firstName[i]).lastName(lastName[i])
					.email(firstName[i].toLowerCase() + lastName[i].toLowerCase() + "@sheridancollege.ca").studentNumber(stNumber)
					.courseList(new ArrayList<Course>()).build();
			studentRepo.save(student);

			// Assign students to courses
			course3.getStudentList().add(student);
			course4.getStudentList().add(student);
			course5.getStudentList().add(student);
			course6.getStudentList().add(student);
		}

		courseRepo.save(course1);
		courseRepo.save(course2);
		courseRepo.save(course3);
		courseRepo.save(course4);
		courseRepo.save(course5);
		courseRepo.save(course6);

	}

}
