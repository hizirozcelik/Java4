package ca.sheridancollege.ozcelikh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.ozcelikh.beans.Professor;

public interface ProfessorRepo extends JpaRepository<Professor, Long>{

    List<Professor> findByNameLikeIgnoreCase(String string);

    List<Professor> findAllByOrderByIdAsc();

    List<Professor> findAllByOrderByIdDesc();

    List<Professor> findAllByOrderByNameAsc();

    List<Professor> findAllByOrderByNameDesc();

}
