package ca.sheridancollege.ozcelikh.controllers;

/*
 * Author: Ozcelik Hizir
 * Date: 2023-02-15
 * Description: This is a controller class for Course entity
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

@Controller
@RequestMapping("/course")
public class CourseController {
	private StudentRepo studentRepo;
	private CourseRepo courseRepo;

	public CourseController(StudentRepo studentRepo, CourseRepo courseRepo) {
		this.studentRepo = studentRepo;
		this.courseRepo = courseRepo;
	}

	// Navigate to my all students page
	@GetMapping(value = { "/", "" })
	public String index(Model model) {

		model.addAttribute("courseList", courseRepo.findAll());

		return "/course/all.html";
	}

	// Navigate to detailed view
	@GetMapping("/detailedView")
	public String detailedView(Model model) {

		model.addAttribute("courseList", courseRepo.findAll());
		return "/course/detailedViewAll.html";
	}

	// Navigate to my add page
	@GetMapping("/add")
	public String add(Model model) {

		model.addAttribute("course", new Course());
		model.addAttribute("message", model.getAttribute("message"));

		return "/course/add.html";
	}

	// Processing add
	@PostMapping("/add")
	public ModelAndView add(@ModelAttribute Course course, RedirectAttributes attr) {
		try {
			courseRepo.save(course);
			attr.addFlashAttribute("course", new Course());
			attr.addFlashAttribute("message", "Course added successfully!");
			return new ModelAndView("redirect:/course/add");
		} catch (Exception e) {
			attr.addFlashAttribute("course", course);
			attr.addFlashAttribute("error", "Course could not be added!");
			return new ModelAndView("redirect:/course/add");
		}
	}

	// Navigate to my edit page
	@GetMapping("/edit/{id}")
	public String navigateEdit(@PathVariable Long id, Model model) {

		Optional<Course> course = courseRepo.findById(id);
		if (course.isPresent()) {
			model.addAttribute("course", course.get());
			return "course/edit.html";
		} else
			return "redirect:/course/add";
	}

	// Processing edit
	@PostMapping("/edit")
	public ModelAndView processEdit(@ModelAttribute Course course, RedirectAttributes attr) {
		try {
			courseRepo.save(course);
			attr.addFlashAttribute("message", "Course updated successfully!");
			return new ModelAndView("redirect:/course");

		} catch (Exception e) {

			attr.addFlashAttribute("error", "Course could not be added!");
			return new ModelAndView("redirect:/course");
		}

	}

	// Navigate to my delete page
	@GetMapping("/delete/{id}")
	public String navigateDelete(Model model, @PathVariable Long id) {

		Optional<Course> course = courseRepo.findById(id);
		if (course.isPresent()) {
			model.addAttribute("course", course.get());
			return "course/delete.html";
		} else
			return "redirect:/course";
	}

	@PostMapping("/delete")
	public ModelAndView processDelete(@ModelAttribute Course course, RedirectAttributes attr) {

		if (course == null) {
			attr.addFlashAttribute("error", "Course cannot be found!");
			return new ModelAndView("redirect:/course");
		}

		try {
			// get all students from database
			List<Student> studentListDB = studentRepo.findAll();

			// drop student from all courses
			for (Student student : studentListDB) {
				student.getCourseList().removeIf(e -> e.getId().equals(course.getId()));
				studentRepo.save(student);
			}

			courseRepo.delete(course);

			attr.addFlashAttribute("message", "Course deleted!");
			return new ModelAndView("redirect:/course");

		} catch (Exception e) {

			attr.addFlashAttribute("error", "Course could not be deleted!");
			return new ModelAndView("redirect:/course");
		}
	}

	// Navigate a details with courseById
	@GetMapping("/details/{id}")
	public String details(Model model, @PathVariable Long id) {

		Optional<Course> course = courseRepo.findById(id);
		List<Student> studentList = studentRepo.findAll();

		if (course.isPresent()) {
			// create a list of students that courses is not assigned
			studentList.removeIf(e -> course.get().getStudentList().contains(e));

			model.addAttribute("course", course.get());
			model.addAttribute("studentList", studentList);

			return "course/details.html";
		} else
			return "error.html";
	}

	// Drop student from course
	@GetMapping("/dropStudent/{id}/{studentId}")
	public String dropStudent(Model model, @PathVariable Long id, @PathVariable Long studentId) {

		Optional<Student> student = studentRepo.findById(studentId);
		Optional<Course> course = courseRepo.findById(id);

		if (student.isPresent() && course.isPresent()) {

			course.get().getStudentList().removeIf(e -> e.getId().equals(studentId));
			courseRepo.save(course.get());

			student.get().getCourseList().removeIf(e -> e.getId().equals(id));
			studentRepo.save(student.get());

			model.addAttribute("course", course);
			model.addAttribute("studentList", course.get().getStudentList());

			return "redirect:/course/details/{id}";
		} else {
			return "error.html";
		}

	}

	// Add student to course
	@GetMapping("/addStudent/{id}/{studentId}")
	public String addStudent(Model model, @PathVariable Long id, @PathVariable Long studentId) {

		Optional<Student> student = studentRepo.findById(studentId);
		Optional<Course> course = courseRepo.findById(id);

		if (student.isPresent() && course.isPresent()) {

			course.get().getStudentList().add(student.get());
			courseRepo.save(course.get());

			student.get().getCourseList().add(course.get());
			studentRepo.save(student.get());

			model.addAttribute("course", course);
			model.addAttribute("studentList", course.get().getStudentList());

			return "redirect:/course/details/{id}";
		} else {
			return "error.html";
		}

	}

	// Navigate to my search page
	@GetMapping("/search")
	public String search(Model model) {

		model.addAttribute("message", model.getAttribute("message"));
		return "/course/search.html";
	}

	// search by id
	@GetMapping("/searchById")
	public ModelAndView search(@RequestParam Long id, RedirectAttributes attr) {

		Optional<Course> courseList = courseRepo.findById(id);

		if (!courseList.isEmpty()) {

			attr.addFlashAttribute("courseList", courseList.get());
			return new ModelAndView("redirect:/course/search");
		} else {
			attr.addFlashAttribute("error", "Course cannot be found on database!");
			return new ModelAndView("redirect:/course/search");
		}
	}

	// search by name
	@GetMapping("/searchByName")
	public ModelAndView searchByName(@RequestParam String name, RedirectAttributes attr) {

		List<Course> courseList = courseRepo.findByNameLikeIgnoreCase("%" + name + "%");
		if (!courseList.isEmpty()) {
			attr.addFlashAttribute("courseList", courseList);
			return new ModelAndView("redirect:/course/search");
		} else {
			attr.addFlashAttribute("error", "Course cannot be found on database!");
			return new ModelAndView("redirect:/course/search");
		}
	}

	// search by last name
	@GetMapping("/searchByCode")
	public ModelAndView searchByCode(@RequestParam String code, RedirectAttributes attr) {

		List<Course> courseList = courseRepo.findByCodeLikeIgnoreCase("%" + code + "%");
		if (!courseList.isEmpty()) {
			attr.addFlashAttribute("courseList", courseList);
			return new ModelAndView("redirect:/course/search");
		} else {
			attr.addFlashAttribute("error", "Course cannot be found on database!");
			return new ModelAndView("redirect:/course/search");
		}
	}

	// sort by id
	@GetMapping("/sortById")
	public String sortById(Model model) {

		model.addAttribute("courseList", courseRepo.findAllByOrderByIdAsc());
		return "/course/all.html";
	}

	@GetMapping("/sortByIdDesc")
	public String sortByIdDesc(Model model) {

		model.addAttribute("courseList", courseRepo.findAllByOrderByIdDesc());
		return "/course/all.html";
	}

	// sort by name
	@GetMapping("/sortByName")
	public String sortByName(Model model) {
		model.addAttribute("courseList", courseRepo.findAllByOrderByNameAsc());
		return "/course/all.html";
	}

	@GetMapping("/sortByNameDesc")
	public String sortByNameDesc(Model model) {
		model.addAttribute("courseList", courseRepo.findAllByOrderByNameDesc());
		return "/course/all.html";
	}

	@GetMapping("/sortByCode")
	public String sortByCodeAsc(Model model) {
		model.addAttribute("courseList", courseRepo.findAllByOrderByCodeAsc());
		return "/course/all.html";

	}

	@GetMapping("/sortByCodeDesc")
	public String sortByCodeDesc(Model model) {
		model.addAttribute("courseList", courseRepo.findAllByOrderByCodeDesc());
		return "/course/all.html";

	}

}
