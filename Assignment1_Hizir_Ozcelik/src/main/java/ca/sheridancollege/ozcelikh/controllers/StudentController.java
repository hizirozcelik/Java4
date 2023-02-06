package ca.sheridancollege.ozcelikh.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.ozcelikh.beans.Course;
import ca.sheridancollege.ozcelikh.beans.Student;
import ca.sheridancollege.ozcelikh.repository.CourseRepo;
import ca.sheridancollege.ozcelikh.repository.StudentRepo;

@Controller
@RequestMapping("/student")
public class StudentController {

	private StudentRepo studentRepo;
	private CourseRepo courseRepo;

	public StudentController(StudentRepo studentRepo, CourseRepo courseRepo) {
		this.studentRepo = studentRepo;
		this.courseRepo = courseRepo;
	}
	
	// Navigate to my all students page
	@GetMapping()
	public String index(Model model) {

		model.addAttribute("studentList", studentRepo.findAll());
		return "/student/all.html";
	}
	
	// Navigate to detailed view
	@GetMapping("/detailedView")
	public String detailedView(Model model) {

		model.addAttribute("studentList", studentRepo.findAll());
		return "/student/detailedViewAll.html";
	}

	// Navigate to my add page
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("student", new Student());
		return "/student/add.html";
	}

	// Processing add 
	@PostMapping("/add")
	public String add(Student student) {
		studentRepo.save(student);
		return "redirect:/student/add";
	}

	// Navigate to my edit page
	@GetMapping("/edit/{id}")
	public String navigateEdit(@PathVariable Long id, Model model) {
		
		Optional<Student> student = studentRepo.findById(id);
		if (student.isPresent()) {
			model.addAttribute("student", student.get());
			return "student/edit.html";
		} else
			return "redirect:/student/add";
	}

	// Processing edit
	@PostMapping("/edit")
	public String processEdit(@ModelAttribute Student student) {

		studentRepo.save(student);
		return "redirect:/student";

	}
	
	// Navigate to my delete page
	@GetMapping("/delete/{id}")
    public String navigateDelete(Model model, @PathVariable Long id) {
		
        Optional<Student> student = studentRepo.findById(id);
		if (student.isPresent()) {
			model.addAttribute("student", student.get());
			return "student/delete.html";
		} else
			return "redirect:/student";
	}

    @PostMapping("/delete")
    public String processDelete(@ModelAttribute Student student) {
    	    	
		// get all courses from database
		List<Course> courseListDB = courseRepo.findAll();

		// drop student from all courses
		for (Course course : courseListDB) {
			course.getStudentList().removeIf(e -> e.getId().equals(student.getId()));
			courseRepo.save(course);
		}		 	
    	
        studentRepo.delete(student);
        
        return "redirect:/student";
    }
	
	// Navigate a details with studentById
	@GetMapping("/details/{id}")
	public String details(Model model, @PathVariable Long id) {
		
		Optional<Student> student = studentRepo.findById(id);
		List<Course> courseList = courseRepo.findAll();

		// create a list of courses that student is not registered
		courseList.removeIf(e -> student.get().getCourseList().contains(e));
		
		if (student.isPresent()) {
			model.addAttribute("student", student.get());			
			model.addAttribute("courseList", courseList);
			
			return "student/details.html";
		} else
			return "error.html";
	}


	// Drop course from student
	@GetMapping("/dropCourse/{id}/{courseId}")
	public String dropCourse(Model model, @PathVariable Long id, @PathVariable Long courseId) {
		
		Optional<Student> student = studentRepo.findById(id);
		Optional<Course> course = courseRepo.findById(courseId);
		
		if (student.isPresent() && course.isPresent()) {
			
			student.get().getCourseList().removeIf(e -> e.getId().equals(courseId));
			studentRepo.save(student.get());
		
			course.get().getStudentList().removeIf(e -> e.getId().equals(id));
			courseRepo.save(course.get());	
			
			model.addAttribute("student", student);			
			model.addAttribute("courseList", student.get().getCourseList());
			
			return "redirect:/student/details/{id}";
		} else {
			return "error.html";
		}

	}

	// Add course to student
	@GetMapping("/addCourse/{id}/{courseId}")
	public String addCourse(Model model, @PathVariable Long id, @PathVariable Long courseId) {
		
		Optional<Student> student = studentRepo.findById(id);
		Optional<Course> course = courseRepo.findById(courseId);
		
		if (student.isPresent() && course.isPresent()) {
			
			student.get().getCourseList().add(course.get());
			studentRepo.save(student.get());
		
			course.get().getStudentList().add(student.get());
			courseRepo.save(course.get());	
			
			model.addAttribute("student", student);			
			model.addAttribute("courseList", student.get().getCourseList());
			
			return "redirect:/student/details/{id}";
		} else {
			return "error.html";
		}

	}

	// Navigate to my search page
	@GetMapping("/search")
	public String search(Model model) {
		return "/student/search.html";
	}

	// search by id
	@GetMapping("/searchById")
	public String search(Model model, @RequestParam Long id) {

		Optional<Student> studentList = studentRepo.findById(id);
		List<Course> courseList = courseRepo.findAll();
		if(!studentList.isEmpty()) {
		// create a list of courses that student is not registered
		courseList.removeIf(e -> studentList.get().getCourseList().contains(e));

		model.addAttribute("studentList", studentList.get());
		model.addAttribute("courseList", courseList);
		}
		return "/student/search.html";

	}

	// search by name
	@GetMapping("/searchByName")
	public String searchByName(Model model, @RequestParam String name) {

		List<Student> studentList = studentRepo.findByNameIgnoreCase(name);
		List<Course> courseList = courseRepo.findAll();

		// create a list of courses that student is not registered
		courseList.removeIf(e -> studentList.get(0).getCourseList().contains(e));

		model.addAttribute("studentList", studentList);
		model.addAttribute("courseList", courseList);
		return "/student/search.html";
	}
	
	// search by last name
	@GetMapping("/searchByLastName")
	public String searchByLastName(Model model, @RequestParam String lastName) {

		List<Student> studentList = studentRepo.findByLastNameIgnoreCase(lastName);
		List<Course> courseList = courseRepo.findAll();

		// create a list of courses that student is not registered
		courseList.removeIf(e -> studentList.get(0).getCourseList().contains(e));

		model.addAttribute("studentList", studentList);
		model.addAttribute("courseList", courseList);
		return "/student/search.html";

	}	

	@GetMapping("/sortById")
    public String sortById(Model model) {
		
        model.addAttribute("studentList", studentRepo.findAllByOrderByIdAsc());
        return "/student/all.html";
    }

	@GetMapping("/sortByName")
    public String sortByName(Model model) {
        model.addAttribute("studentList", studentRepo.findAllByOrderByNameAsc());
        return "/student/all.html";
    }

    @GetMapping("/sortByLastName")
    public String sortByClass(Model model) {
        model.addAttribute("studentList", studentRepo.findAllByOrderByLastNameAsc());
        return "/student/all.html";

    }
	
}
