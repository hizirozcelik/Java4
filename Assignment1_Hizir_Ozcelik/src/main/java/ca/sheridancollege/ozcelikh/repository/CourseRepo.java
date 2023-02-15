package ca.sheridancollege.ozcelikh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.ozcelikh.beans.Course;
import ca.sheridancollege.ozcelikh.beans.Professor;

public interface CourseRepo extends JpaRepository<Course, Long>{

    List<Course> findAllByOrderByIdAsc();

    List<Course> findAllByOrderByIdDesc();

    List<Course> findAllByOrderByNameDesc();

    List<Course> findAllByOrderByNameAsc();

    List<Course> findAllByOrderByCodeAsc();

    List<Course> findAllByOrderByCodeDesc();

    List<Course> findByNameLikeIgnoreCase(String name);

    List<Course> findByCodeLikeIgnoreCase(String string);

    List<Course> findAllByProfessor(Professor professor);


}
