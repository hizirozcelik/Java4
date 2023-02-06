package ca.sheridancollege.ozcelikh.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.ozcelikh.beans.Student;

public interface StudentRepo extends JpaRepository<Student, Long>{

	List<Student> findByNameIgnoreCase(String name);

	List<Student> findByLastNameIgnoreCase(String lastName);

    List<Student> findAllByOrderByIdAsc();

    List<Student> findAllByOrderByNameAsc();

    List<Student> findAllByOrderByLastNameAsc();




}
