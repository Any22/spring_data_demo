package org.persistance.spring_data.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name="book_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	    @Id
	    @Column(name = "book_id") 
		private Integer bookId;
		private String title;
		@Column(name = "author_name") 
		private String authorName;
		@Column(name = "published_year") 
		private LocalDate publishedYear;
		private String publisher;
		private Long isbn;
		private Integer price;
		
}
