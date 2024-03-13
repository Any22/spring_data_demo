package org.persistance.spring_data.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.persistance.spring_data.dto.BookDTO;
import org.persistance.spring_data.entity.Book;
import org.persistance.spring_data.exception.MyBookException;
import org.persistance.spring_data.repository.BookRepository;
import org.persistance.spring_data.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Service(value="bookService")
@Slf4j
public class BookServiceImpl implements BookService {
	
	@Autowired 
	private BookRepository bookRepository;
	
	@Autowired
    private ModelMapper modelMapper;  
	
	@Autowired
	private Validator performValidation;
	
	@Value ("${book.already.present}")
	String bookPresent;
	
	@Value ("${book.not.found}")
	String bookNotFound;
	
	@Value ("${bookId.not.found}")
	String bookId_NOT_FOUND;

	@Value ("${book.not.found.high.price}")
	String bookNotFoundWithHighPrice;
	
	@Value ("${book.not.found.low.price}")
	String bookNotFoundWithLowPrice;

	
	@Override
	public BookDTO getBookDetails(Integer bookId) {
	Optional<Book> bookentityOptional =	bookRepository.findById(bookId);
	Book bookEntity = bookentityOptional.get();
	if (bookentityOptional.isPresent()) 
		
		return modelMapper.map(bookEntity, BookDTO.class);
		
	
	return BookDTO.builder().build();
	
		
	}


	@Override
	public String addBook(BookDTO bookDTO) throws MyBookException {
		
			
			try {
				 performValidation.validate(bookDTO);
			
				Optional<Book> optionalBook = bookRepository.findById(bookDTO.getBookId());
			
			  if (optionalBook.isPresent()) {
			    	//very important line otherwise we will get null
			    	Book book = optionalBook.get();
			    	log.info(" Book is alreadypresent "+ book.getTitle());
			    	throw new MyBookException(bookPresent);
			      
			  } else {
			    		 Book bookEntity = modelMapper.map(bookDTO, Book.class);
			    		 bookRepository.save(bookEntity);
			    		 BookDTO myBookDTO= modelMapper.map(bookEntity, BookDTO.class);
						
			    	return  myBookDTO.getTitle();
			    }
			
			} catch (Exception e) {
				
				throw e;
			}
		
	}

	
	@Override
	public Page<Book> findAll(Pageable page) {
		
		return bookRepository.findAll(page);
	}
    
	@Override
	public List<Book> findAllSorted (Sort sort) {
		List<Book> sortedBookEntities = new ArrayList<>();
		Iterable<Book> bookEntityIterable =bookRepository.findAll(sort);
		// Creating stream from bookEntityIterable and adding each entry to List
	    StreamSupport.stream(bookEntityIterable.spliterator(), false)
	            .forEach(sortedBookEntities::add);

	    return sortedBookEntities;
		
		
	}


	@Override
	public List<BookDTO> getBookByAuthorName(String authorName) throws MyBookException {
	
		
		       List<Book> books = bookRepository.findAllBooksByAuthorName(authorName);
		
		      if (null != books) {
		        return books.stream()
				.map(book-> modelMapper.map(book,BookDTO.class))
				.collect(Collectors.toList());
		      }
		      else {
		    	  throw new MyBookException(bookNotFound);
		      }
	
	}


	@Override
	public List<BookDTO> getBookGreaterThanEqualToPrice(Integer price) throws Exception{
		
		List<Book> listOfbookEntity = bookRepository.findByPriceGreaterThanEqual(price);
		
		if (null==listOfbookEntity || listOfbookEntity.isEmpty()) {
			log.info("The given price is "+ price);
			throw new Exception(bookNotFoundWithHighPrice);
		}
		List<BookDTO> response = listOfbookEntity.stream()
				.map(book -> modelMapper.map(book, BookDTO.class))
				.collect(Collectors.toList());
		return response;  
	}


	@Override
	public List<BookDTO> getBookLessThanPrice(Integer price) throws MyBookException {
      List<Book> books = bookRepository.findByPriceLessThan(price);
		
		if (null== books || books.isEmpty()) {
			log.info("The given price is "+ price);
			throw new MyBookException(bookNotFoundWithLowPrice);
		}
		 
		return books.stream()
				.map(book -> modelMapper.map(book, BookDTO.class))
				.collect(Collectors.toList());
		
	}


	@Override
	public List<BookDTO> getAllBooksPublishBetween(LocalDate startDate, LocalDate endDate) throws Exception {
		try { 
		
		List<Book> books = bookRepository.getAllBooksPublishBetween(startDate, endDate);
			
			if (null== books || books.isEmpty()) {
//				log.info("The given dates are "+ startDate);
				throw new MyBookException(bookNotFound);
			}
			 
			return books.stream()
					.map(book -> modelMapper.map(book, BookDTO.class))
					.collect(Collectors.toList());
			
		}catch(Exception ex) {
			throw ex;
		}
			
	}


	@Override
	public List<BookDTO> booksPublishedAfterYear(LocalDate yearPublished) {
		
		List<Book> books = bookRepository.getAllBooksAfterPublishedDate(yearPublished);
		
		return books
				.stream()
				.map(book->modelMapper.map(book,BookDTO.class ))
				.collect(Collectors.toList());
	}


	@Override
	public List<BookDTO> getbooksByAuthorNameAndPublisher(String authorName, String publisher) {
		
		List<Book> books = bookRepository.getAllBooksByAuthorAndPublisher(authorName, publisher);
		
		
		return books
				.stream()
				.map(book->modelMapper.map(book,BookDTO.class ))
				.collect(Collectors.toList());
	}


	@Override
	public void updateBookPrice(Integer bookId, Integer price) throws Exception {
		
		try {
		  Optional<Book> bookEntityOptional=	bookRepository.findById(bookId);
	
			if (bookEntityOptional.isPresent()) {
				Book bookEntity = bookEntityOptional.get();
				bookEntity.setPrice(price);
				bookRepository.save(bookEntity);
				log.info(bookEntity.toString());
			}  else {
		    	  throw new MyBookException(bookId_NOT_FOUND);
		       }
			
		} catch(Exception ex) {
			throw ex;
		}
	}


	@Override
	public void deleteBook(Integer bookId) throws Exception {
		Optional<Book> bookEntityOptional=	bookRepository.findById(bookId);
		if (bookEntityOptional.isPresent()) {
			Book bookEntity = bookEntityOptional.get();
			bookRepository.delete(bookEntity);
		}
		else {
			throw new MyBookException(bookId_NOT_FOUND);
		}
		
	}


	

}
