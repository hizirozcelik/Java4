package ca.sheridancollege.ozcelikh.controllers;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
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
import ca.sheridancollege.ozcelikh.beans.Professor;
import ca.sheridancollege.ozcelikh.repository.CourseRepo;
import ca.sheridancollege.ozcelikh.repository.ProfessorRepo;
import ca.sheridancollege.ozcelikh.repository.StudentRepo;
import ca.sheridancollege.ozcelikh.viewmodels.ProfAssignView;
import ca.sheridancollege.ozcelikh.viewmodels.ProfDetailsView;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

	private StudentRepo studentRepo;
	private CourseRepo courseRepo;
	private ProfessorRepo profRepo;

	public ProfessorController(StudentRepo studentRepo, CourseRepo courseRepo, ProfessorRepo profRepo) {
		this.studentRepo = studentRepo;
		this.courseRepo = courseRepo;
		this.profRepo = profRepo;
	}
	
	// Navigate to my all students page
	@GetMapping(value = { "/", "" })
	public String index(Model model) {

		model.addAttribute("profList", profRepo.findAll());

		return "/professor/all.html";
	}

	// Navigate to detailed view
	@GetMapping("/detailedView")
	public String detailedView(Model model) {
		
		ProfDetailsView profDetailsView = new ProfDetailsView();
		List<ProfDetailsView> profDetailsViewList = new ArrayList<ProfDetailsView>();

		List<Professor> profList = profRepo.findAll();
		
		for (Professor professor : profList) {
			profDetailsView = new ProfDetailsView();
			profDetailsView.setProfessor(professor);
			profDetailsView.setCourseList(courseRepo.findAllByProfessor(professor));
			profDetailsViewList.add(profDetailsView);
		}

		model.addAttribute("profDetailsViewList", profDetailsViewList);

		return "/professor/detailedViewAll.html";
	}

	// Navigate to my add page
	@GetMapping("/add")
	public String add(Model model) {

		model.addAttribute("professor", new Professor());
		model.addAttribute("message", model.getAttribute("message"));

		return "/professor/add.html";
	}

	// Processing add
	@PostMapping("/add")
	public ModelAndView add(@ModelAttribute Professor professor, RedirectAttributes attr) {
		try {
			profRepo.save(professor);
			attr.addFlashAttribute("professor", new Professor());
			attr.addFlashAttribute("message", "Professor added successfully!");
			return new ModelAndView("redirect:/professor/add");
		} catch (Exception e) {
			attr.addFlashAttribute("professor", professor);
			attr.addFlashAttribute("error", "Professor could not be added!");
			return new ModelAndView("redirect:/professor/add");
		}
	}

	// Navigate to my edit page
	@GetMapping("/edit/{id}")
	public String navigateEdit(@PathVariable Long id, Model model) {

		Optional<Professor> professor = profRepo.findById(id);
		if (professor.isPresent()) {
			model.addAttribute("professor", professor.get());
			return "professor/edit.html";
		} else
			return "redirect:/professor/add";
	}

	// Processing edit
	@PostMapping("/edit")
	public ModelAndView processEdit(@ModelAttribute Professor professor, RedirectAttributes attr) {

		profRepo.save(professor);
		attr.addFlashAttribute("message", "Professor updated successfully!");
		return new ModelAndView("redirect:/professor");

	}

	// Navigate to my delete page
	@GetMapping("/delete/{id}")
	public String navigateDelete(Model model, @PathVariable Long id) {

		Optional<Professor> professor = profRepo.findById(id);
		if (professor.isPresent()) {
			model.addAttribute("professor", professor.get());
			return "professor/delete.html";
		} else
			return "redirect:/professor";
	}

	@PostMapping("/delete")
	public ModelAndView processDelete(@ModelAttribute Professor professor, RedirectAttributes attr) {

		if (professor == null) {
			attr.addFlashAttribute("error", "Professor cannot be found!");
			return new ModelAndView("redirect:/professor");
		}
		// get all courses from database
		List<Course> courseListDB = courseRepo.findAll();

		// drop prof from all courses
		for (Course course : courseListDB) {
			if (course.getProfessor() != null){
				if (course.getProfessor().getId() == professor.getId())
					course.setProfessor(null);
					courseRepo.save(course);
			} else
				continue;
		}

		profRepo.delete(professor);

		attr.addFlashAttribute("message", "Professor deleted!");
		return new ModelAndView("redirect:/professor");
	}

	

	// Navigate a details 
	@GetMapping("/details/{id}")
	public String details(Model model, @PathVariable Long id) {

		ProfDetailsView profDetailsView = new ProfDetailsView();
		List<ProfDetailsView> profDetailsViewList = new ArrayList<ProfDetailsView>();

		Optional<Professor> professor = profRepo.findById(id);
		profDetailsView.setProfessor(professor.get());

		// create al list find course that prof registered
		List<Course> courseListRegistered = new ArrayList<Course>();

		// get all courses from database
		List<Course> courseListDB = courseRepo.findAll();

		// find course that prof registered
		for (Course course : courseListDB) {
			if (course.getProfessor() != null) {
				if (course.getProfessor().getId() == id)
					courseListRegistered.add(course);
			} else
				continue;
		}
		profDetailsView.setCourseList(courseListRegistered);
		profDetailsViewList.add(profDetailsView);

		model.addAttribute("professor", professor.get());
		model.addAttribute("courseList", courseListRegistered);

			return "professor/details.html";
		} 

	// Drop course from Professor
	@GetMapping("/dropCourse/{id}/{courseId}")
	public String dropCourse(Model model, @PathVariable Long id, @PathVariable Long courseId) {

		Optional<Professor> professor = profRepo.findById(id);
		Optional<Course> course = courseRepo.findById(courseId);
		List<Course> courseList = courseRepo.findAll();

		if (professor.isPresent() && course.isPresent()) {

			// delete professor from course
			course.get().setProfessor(null);
			courseRepo.save(course.get());

			// delete course from professor
			professor.get().getCourseList().remove(course.get());
			profRepo.save(professor.get());

			// list of courses that any prof registered
			List<Course> courseListNotRegistered = new ArrayList<Course>();
			for (Course c : courseList) {
				if (c.getProfessor() == null)
					courseListNotRegistered.add(c);
			}

			model.addAttribute("professor", professor);
			model.addAttribute("courseList", courseListNotRegistered);

			return "redirect:/professor/details/{id}";
		} else
			return "error.html";
	}
		


	// Add course to professor
	@GetMapping("/addCourse/{id}/{courseId}")
	public String addCourse(Model model, @PathVariable Long id, @PathVariable Long courseId) {

		Optional<Professor> professor = profRepo.findById(id);
		Optional<Course> course = courseRepo.findById(courseId);

		if (professor.isPresent() && course.isPresent()) {

			professor.get().getCourseList().add(course.get());
			profRepo.save(professor.get());

			course.get().setProfessor(professor.get());
			courseRepo.save(course.get());

			model.addAttribute("professor", professor);
			model.addAttribute("courseList", professor.get().getCourseList());

			return "redirect:/professor/details/{id}";
		} else {
			return "error.html";
		}

	}

	// Navigate to my assign course page
	@GetMapping("/assign")
	public String assignCourse(Model model) {

		List<Professor> profList = profRepo.findAll();
		List<Course> avilableCourseList = courseRepo.findAllByProfessor(null);
		ProfAssignView listViewModel = new ProfAssignView();

		// set course name as code + name
		for (Course course : avilableCourseList) {
			course.setName(course.getCode() + " " + course.getName());
		}

		listViewModel.setProfList(profList);
		listViewModel.setCourseList(avilableCourseList);

		model.addAttribute("listViewModel", listViewModel);
		if(avilableCourseList.isEmpty())
			model.addAttribute("alertMessage", "No course available to assign!");

		return "professor/assign.html";
	}

	// Assign course to professor
	@PostMapping("/assign")
	public ModelAndView assignCourse(@ModelAttribute ProfAssignView listViewModel, RedirectAttributes attr) {

		Optional<Professor> professor = profRepo.findById(listViewModel.getProfessor().getId());
		Optional<Course> course = courseRepo.findById(listViewModel.getCourse().getId());

		if (professor.isPresent() && course.isPresent()) {

			// check if student is already registered to course
			if (professor.get().getCourseList().contains(course.get())) {
				attr.addFlashAttribute("listViewModel", listViewModel);
				attr.addFlashAttribute("error", "Professor is already registered to this course!");
				return new ModelAndView("redirect:/professor/assign");
			} else {
				// add course to professor
				professor.get().getCourseList().add(course.get());
				profRepo.save(professor.get());

				// add professor to course
				course.get().setProfessor(professor.get());
				courseRepo.save(course.get());

				attr.addFlashAttribute("listViewModel", listViewModel);
				attr.addFlashAttribute("message", "Professor assigned successfully!");
				return new ModelAndView("redirect:/professor/assign");
			}

		}
		attr.addFlashAttribute("listViewModel", listViewModel);
		attr.addFlashAttribute("message", "Something went wrong. Please try again!");
		return new ModelAndView("redirect:/professor/assign");
	}

	// Navigate to my search page
	@GetMapping("/search")
	public String search(Model model) {

		model.addAttribute("message", model.getAttribute("message"));
		return "/professor/search.html";
	}

	// search by id
	@GetMapping("/searchById")
	public ModelAndView search(@RequestParam Long id, RedirectAttributes attr) {

		Optional<Professor> profList = profRepo.findById(id);
		if (!profList.isEmpty()) {
			attr.addFlashAttribute("profList", profList.get());
			return new ModelAndView("redirect:/professor/search");
		} else {
			attr.addFlashAttribute("error", "Professor cannot be found on database!");
			return new ModelAndView("redirect:/professor/search");
		}
	}

	// search by name
	@GetMapping("/searchByName")
	public ModelAndView searchByName(@RequestParam String name, RedirectAttributes attr) {

		List<Professor> profList = profRepo.findByNameLikeIgnoreCase("%"+name+"%");
		if (!profList.isEmpty()) {
			attr.addFlashAttribute("profList", profList);
			return new ModelAndView("redirect:/professor/search");
		} else {
			attr.addFlashAttribute("error", "Professor cannot be found on database!");
			return new ModelAndView("redirect:/professor/search");
		}
	}

	@GetMapping("/sortById")
	public String sortById(Model model) {

		model.addAttribute("profList", profRepo.findAllByOrderByIdAsc());
		return "/professor/all.html";
	}

	@GetMapping("/sortByIdDesc")
	public String sortByIdDesc(Model model) {

		model.addAttribute("profList", profRepo.findAllByOrderByIdDesc());
		return "/professor/all.html";
	}

	@GetMapping("/sortByName")
	public String sortByName(Model model) {
		model.addAttribute("profList", profRepo.findAllByOrderByNameAsc());
		return "/professor/all.html";
	}

	@GetMapping("/sortByNameDesc")
	public String sortByNameDesc(Model model) {
		model.addAttribute("profList", profRepo.findAllByOrderByNameDesc());
		return "/professor/all.html";
	}
	
}
