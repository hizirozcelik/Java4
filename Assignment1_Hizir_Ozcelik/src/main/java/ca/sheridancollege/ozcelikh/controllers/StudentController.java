package ca.sheridancollege.ozcelikh.controllers;
/*
 * Author: Hizir Ozcelik
 * Date: 2023-02-24
 * Description: Student Controller
 * This controller is responsible for handling the requests from the student page.
 * It is responsible for adding, deleting, and updating students.
 * It is also responsible for adding and dropping students from courses.
 *  
 */

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ca.sheridancollege.ozcelikh.beans.Course;
import ca.sheridancollege.ozcelikh.beans.Student;
import ca.sheridancollege.ozcelikh.repository.CourseRepo;
import ca.sheridancollege.ozcelikh.repository.StudentRepo;
import ca.sheridancollege.ozcelikh.viewmodels.StudentAssignView;

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
	@GetMapping(value = { "/", "" })
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
		model.addAttribute("message", model.getAttribute("message"));

		return "/student/add.html";
	}

	// Processing add
	@PostMapping("/add")
	public ModelAndView add(@ModelAttribute Student student, RedirectAttributes attr) {
		try {
			studentRepo.save(student);
			attr.addFlashAttribute("student", new Student());
			attr.addFlashAttribute("message", "Student added successfully!");
			return new ModelAndView("redirect:/student/add");
		} catch (Exception e) {
			attr.addFlashAttribute("student", student);
			attr.addFlashAttribute("error", "Student could not be added!");
			return new ModelAndView("redirect:/student/add");
		}
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
	public ModelAndView processEdit(@ModelAttribute Student student, RedirectAttributes attr) {

		studentRepo.save(student);
		attr.addFlashAttribute("message", "Student updated successfully!");
		return new ModelAndView("redirect:/student");

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
	public ModelAndView processDelete(@ModelAttribute Student student, RedirectAttributes attr) {

		if (student == null) {
			attr.addFlashAttribute("error", "Student cannot be found!");
			return new ModelAndView("redirect:/student");
		}
		// get all courses from database
		List<Course> courseListDB = courseRepo.findAll();

		// drop student from all courses
		for (Course course : courseListDB) {
			course.getStudentList().removeIf(e -> e.getId().equals(student.getId()));
			courseRepo.save(course);
		}

		studentRepo.delete(student);

		attr.addFlashAttribute("message", "Student deleted!");
		return new ModelAndView("redirect:/student");
	}

	// Navigate a details with studentById
	@GetMapping("/details/{id}")
	public String details(Model model, @PathVariable Long id) {

		Optional<Student> student = studentRepo.findById(id);
		List<Course> courseList = courseRepo.findAll();

		if (student.isPresent()) {
			// create a list of courses that student is not registered
			courseList.removeIf(e -> student.get().getCourseList().contains(e));

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

	// Navigate to my assign course page
	@GetMapping("/assign")
	public String assignCourse(Model model) {

		List<Student> studentList = studentRepo.findAll();
		List<Course> courseList = courseRepo.findAll();
		StudentAssignView listViewModel = new StudentAssignView();

		// set student name as name + last name
		for (Student student : studentList) {
			student.setName(student.getName() + " " + student.getLastName());
		}

		// set course name as code + name + professor name
		for (Course course : courseList) {
			if(course.getProfessor() == null) {
				course.setName(course.getCode() + " " + course.getName() + " - no professor");
			} else{
				course.setName(course.getCode() + " " + course.getName() + " - " + course.getProfessor().getName());
			}
		}

		listViewModel.setStudentList(studentList);
		listViewModel.setCourseList(courseList);

		model.addAttribute("listViewModel", listViewModel);

		return "student/assign.html";
	}

	// Assign course to student
	@PostMapping("/assign")
	public ModelAndView assignCourse(@ModelAttribute StudentAssignView listViewModel, RedirectAttributes attr) {

		Optional<Student> student = studentRepo.findById(listViewModel.getStudent().getId());
		Optional<Course> course = courseRepo.findById(listViewModel.getCourse().getId());

		if (student.isPresent() && course.isPresent()) {

			// check if student is already registered to course
			if (student.get().getCourseList().contains(course.get())) {
				attr.addFlashAttribute("listViewModel", listViewModel);
				attr.addFlashAttribute("error", "Student is already registered to this course!");
				return new ModelAndView("redirect:/student/assign");
			} else {
				// add course to student
				student.get().getCourseList().add(course.get());
				studentRepo.save(student.get());

				// add student to course
				course.get().getStudentList().add(student.get());
				courseRepo.save(course.get());

				attr.addFlashAttribute("listViewModel", listViewModel);
				attr.addFlashAttribute("message", "Student assigned successfully!");
				return new ModelAndView("redirect:/student/assign");
			}

		}
		attr.addFlashAttribute("listViewModel", listViewModel);
		attr.addFlashAttribute("message", "Something went wrong. Please try again!");
		return new ModelAndView("redirect:/student/assign");
	}

	// Navigate to my search page
	@GetMapping("/search")
	public String search(Model model) {

		model.addAttribute("message", model.getAttribute("message"));
		return "/student/search.html";
	}

	// search by id
	@GetMapping("/searchById")
	public ModelAndView search(@RequestParam Long id, RedirectAttributes attr) {

		Optional<Student> studentList = studentRepo.findById(id);
		if (!studentList.isEmpty()) {
			attr.addFlashAttribute("studentList", studentList.get());
			return new ModelAndView("redirect:/student/search");
		} else {
			attr.addFlashAttribute("error", "Student cannot be found on database!");
			return new ModelAndView("redirect:/student/search");
		}
	}

	// search by name
	@GetMapping("/searchByName")
	public ModelAndView searchByName(@RequestParam String name, RedirectAttributes attr) {

		List<Student> studentList = studentRepo.findByNameLikeIgnoreCase("%"+name+"%");
		if (!studentList.isEmpty()) {
			attr.addFlashAttribute("studentList", studentList);
			return new ModelAndView("redirect:/student/search");
		} else {
			attr.addFlashAttribute("error", "Student cannot be found on database!");
			return new ModelAndView("redirect:/student/search");
		}
	}

	// search by last name
	@GetMapping("/searchByLastName")
	public ModelAndView searchByLastName(@RequestParam String lastName, RedirectAttributes attr) {

		List<Student> studentList = studentRepo.findByLastNameLikeIgnoreCase("%"+lastName+"%");
		if (!studentList.isEmpty()) {
			attr.addFlashAttribute("studentList", studentList);
			return new ModelAndView("redirect:/student/search");
		} else {
			attr.addFlashAttribute("error", "Student cannot be found on database!");
			return new ModelAndView("redirect:/student/search");
		}
	}

	@GetMapping("/sortById")
	public String sortById(Model model) {

		model.addAttribute("studentList", studentRepo.findAllByOrderByIdAsc());
		return "/student/all.html";
	}

	@GetMapping("/sortByIdDesc")
	public String sortByIdDesc(Model model) {

		model.addAttribute("studentList", studentRepo.findAllByOrderByIdDesc());
		return "/student/all.html";
	}

	@GetMapping("/sortByName")
	public String sortByName(Model model) {
		model.addAttribute("studentList", studentRepo.findAllByOrderByNameAsc());
		return "/student/all.html";
	}

	@GetMapping("/sortByNameDesc")
	public String sortByNameDesc(Model model) {
		model.addAttribute("studentList", studentRepo.findAllByOrderByNameDesc());
		return "/student/all.html";
	}

	@GetMapping("/sortByLastName")
	public String sortByLastName(Model model) {
		model.addAttribute("studentList", studentRepo.findAllByOrderByLastNameAsc());
		return "/student/all.html";

	}

	@GetMapping("/sortByLastNameDesc")
	public String sortByLastNameDesc(Model model) {
		model.addAttribute("studentList", studentRepo.findAllByOrderByLastNameDesc());
		return "/student/all.html";

	}

}
