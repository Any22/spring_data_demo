package org.persistance.spring_data.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
	private Integer bookId;
	private String title;
	private String authorName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate publishedYear;
	private String publisher;
	private Long isbn;
	private Integer price;
	
}
