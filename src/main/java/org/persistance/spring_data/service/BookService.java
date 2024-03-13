package org.persistance.spring_data.service;

import java.time.LocalDate;
import java.util.List;

import org.persistance.spring_data.dto.BookDTO;
import org.persistance.spring_data.entity.Book;
import org.persistance.spring_data.exception.MyBookException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/*****************************************************************************************************************************************
 * The Page interface provided by Spring Data works with entity classes rather than DTO (Data Transfer Object) classes.

* If you want to return a Page of DTO objects instead of entity objects from your service layer to your controller layer, you have 
* a couple of options:

 @Override
    public Page<BookDTO> findAll(Pageable page) {
        Page<Book> bookPage = bookRepository.findAll(page);
        return bookPage.map(this::convertToDto);
    }

    private BookDTO convertToDto(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }
    
    
 * @param bookId
 * @throws Exception
 ****************************************************************************************************************************************/
public interface BookService {
	
	public BookDTO getBookDetails(Integer bookId);
	public String addBook(BookDTO bookDTO) throws MyBookException;
	Page<Book> findAll(Pageable page);
	List<Book> findAllSorted (Sort sort);
	public List<BookDTO> getBookByAuthorName(String authorName) throws MyBookException;
	public List<BookDTO> getBookGreaterThanEqualToPrice(Integer price) throws Exception;
	public List<BookDTO> getBookLessThanPrice(Integer price) throws MyBookException;
	public List<BookDTO> getAllBooksPublishBetween (LocalDate startDate, LocalDate endDate) throws Exception;
	public List<BookDTO> booksPublishedAfterYear (LocalDate yearPublished);
	public List<BookDTO> getbooksByAuthorNameAndPublisher (String authorName, String publisher);
	public void updateBookPrice(Integer bookId, Integer price) throws Exception;
    public void deleteBook (Integer bookId) throws Exception;

}
