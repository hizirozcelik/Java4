package ca.sheridancollege.ozcelikh.beans;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String code;
	

	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Course_Student",
        joinColumns = @JoinColumn(name = "Course_ID"),
        inverseJoinColumns = @JoinColumn(name = "Student_ID"))
	private List<Student> studentList;
	

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "Course_Professor",
        joinColumns = @JoinColumn(name = "Course_ID"),
        inverseJoinColumns = @JoinColumn(name = "Professor_ID"))
	private Professor professor;
}
