package org.persistance.spring_data.validator;

import java.time.LocalDate;

import org.persistance.spring_data.dto.BookDTO;
import org.persistance.spring_data.exception.MyBookException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class Validator {
	@Value("${invalid.year}")
	private String inValidDate;
	
	private LocalDate yearNow = LocalDate.now();
	

	private boolean validateYear(LocalDate publishingYear) {
		 log.info(yearNow.toString());
        if (publishingYear.isAfter(yearNow)) 
            return false;
        else 
        	return true;
    }
	

	public void validate(BookDTO bookDTO) throws MyBookException  {
		
		LocalDate publishingYear = bookDTO.getPublishedYear();
		if (! validateYear(publishingYear) )
	     throw new MyBookException(inValidDate);
	}
   
	
}
