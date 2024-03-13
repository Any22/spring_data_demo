package org.persistance.spring_data.controller;

import java.time.LocalDate;
import java.util.List;

import org.persistance.spring_data.dto.BookDTO;
import org.persistance.spring_data.entity.Book;
import org.persistance.spring_data.exception.MyBookException;
import org.persistance.spring_data.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//things left :ResponseEntity and case sensitivity 

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@PropertySource(value= {"classpath:configuration.properties"})
public class BookController {
	@Autowired
	BookService bookService;

	@Value("${invalid.id}")
	private String Id_Not_Found;
	
	@Value("${book.not.found}")
	private String BOOK_NOT_FOUND;
	
	@Value("${book.not.found.low.price}")
	private String BOOK_NOT_FOUND_low_PRICE;
	
	@Value("${book.not.found.with.publishYear}")
	private String BOOK_NOT_FOUND_IN_PUBLISH_YEAR;
	
	@Value ("${author.publisher.names}")
	private String BOOK_PUBLISHER_AUTHOR_NOT_FOUND;
	
	@PostMapping("/create")
	public String addBookDetails(@RequestBody BookDTO book) throws MyBookException {
		String  titleOfBook = bookService.addBook(book);
		
		return "book successfully added";
		
	}
	
//	http://localhost:8083/book/get/3
	@GetMapping("/get/{bookId}")
	public BookDTO getBookDetails(@PathVariable Integer bookId) throws MyBookException {
		try {
				BookDTO	book = bookService.getBookDetails(bookId);
			
					if (null == book ) {
						log.info("no book found!"+ Id_Not_Found);
				    throw new MyBookException(Id_Not_Found);
			 }
			
			return book;	 
			
		} catch(Exception ex) {
			 throw ex;
		}
    }
	
	//http://localhost:8083/book/get?authorName=Yashwant Kanetkar
	@GetMapping("/get")
	public List<BookDTO> getBookByAuthorName(@RequestParam String authorName) throws MyBookException {
	    try {
	        List<BookDTO> books = bookService.getBookByAuthorName(authorName);
	        
	        if (books == null || books.isEmpty()) {
	            log.info("No books found for author: " + authorName);
	            throw new MyBookException(BOOK_NOT_FOUND);
	        }

	        return books;
	    } catch (Exception ex) {
	        throw ex;
	    }
	}

	//http://localhost:8083/book/get-all-books?page=0&size=2
	@GetMapping("/get-all-books")
    public Page<Book> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam int size ) {
        Pageable pageable = PageRequest.of(page, size);
        return bookService.findAll(pageable);
    }
	
	//http://localhost:8083/book/get-all-books-sorted?sortBy=authorName&sortOrder=asc
	@GetMapping("/get-all-books-sorted")
	public List<Book> getAllBooksSorted(
	        @RequestParam String sortBy,
	        @RequestParam  String sortOrder) {

	    Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
	    Sort sort = Sort.by(direction, sortBy); // Create a Sort object based on the sort parameters
	    return bookService.findAllSorted(sort);
	}

    
    
	
	//http://localhost:8083/book/get-by-price?price=35
	@GetMapping("/get-high-price")
	public List<BookDTO> getBookGreaterThanEqualToPrice(@RequestParam Integer price) throws Exception {
	    try {
	        List<BookDTO> books = bookService.getBookGreaterThanEqualToPrice(price);
	        
	        if (books == null || books.isEmpty()) {
	            log.info("No books found for high price: " + price);
	            throw new MyBookException(BOOK_NOT_FOUND);
	        }
	        log.info("The books are :{}" + books);
	        return books;
	    } catch (Exception ex) {
	        throw ex;
	    }
	}
    
	//http://localhost:8083/book/get-low-price?price=100
	@GetMapping("/get-low-price")
	public List<BookDTO> getBookLessThanPrice(@RequestParam Integer price) throws Exception {
	    try {
	        List<BookDTO> books = bookService.getBookLessThanPrice(price);
	        
	        if (books == null || books.isEmpty()) {
	            log.info("No books found for low price: " + price);
	            throw new MyBookException(BOOK_NOT_FOUND_low_PRICE);
	        }
	        log.info("The books are :{}" + books);
	        return books;
	    } catch (Exception ex) {
	        throw ex;
	    }
	}

	@GetMapping("/get-book-published-between")
	public List<BookDTO> getBookPublishedBetweenYear (@RequestParam  @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate, 
			@RequestParam  @DateTimeFormat(pattern = "dd-MM-yyyy")  LocalDate endDate) throws Exception  {
	    try {
	        List<BookDTO> books = bookService.getAllBooksPublishBetween(startDate, endDate);
	        
	        if (books == null || books.isEmpty()) {
	            log.info("No books found for these dates "+ startDate+"," + endDate );
	            throw new MyBookException(BOOK_NOT_FOUND_low_PRICE);
	        }
	        log.info("The books are :{}" + books);
	        return books;
	    } catch (Exception ex) {
	        throw ex;
	    }
	}
	
	@GetMapping("/get-book-published-after")
	public List<BookDTO> getBookPublishedAfterYear (@RequestParam  @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate yearPublished) throws Exception{
		
		try {
			 List<BookDTO> books = bookService.booksPublishedAfterYear(yearPublished);
			 log.info("The books are {}"+books);
			 if (books == null || books.isEmpty()) {
		            log.info("No books found for the date "+ yearPublished);
		            throw new MyBookException(BOOK_NOT_FOUND_IN_PUBLISH_YEAR);
		        }
			 
			return books;
		}catch(Exception ex) {
			throw ex;
		}
		
    }
	
	@GetMapping("/get-book-author-publisher")
	public List<BookDTO> getBookByAuthorNameAndPublisher (@RequestParam String authorName, 
			@RequestParam String publisher) throws Exception   {
				try {
					
					List<BookDTO> books = bookService.getbooksByAuthorNameAndPublisher(authorName, publisher);
					 if (books == null || books.isEmpty()) {
				            log.info("No books found for this publisher and author ");
				            throw new MyBookException(BOOK_PUBLISHER_AUTHOR_NOT_FOUND);
				        }
					return books;
				}catch(Exception ex) {
					throw ex;
				}
		
	}
	
	@PutMapping("/update/{bookId}")
	public void getBooksUpdatedByPrice (@PathVariable Integer bookId , @RequestParam Integer price) throws Exception {
		try {
			bookService.updateBookPrice(bookId, price);
			log.info("updated !!");
			
		}catch(Exception ex) {
			throw ex;
		}
		
	}
	
	@DeleteMapping("/delete/{bookId}")
	public void deleteBookById (@PathVariable Integer bookId) throws Exception {
		try {
			bookService.deleteBook(bookId);
			log.info("deleted  !!");
			
		}catch(MyBookException ex) {
			throw ex;
		}
		
	}
	
}
