package org.persistance.spring_data.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.persistance.spring_data.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book,Integer>,PagingAndSortingRepository<Book,Integer> {
	
   public List<Book> findAllBooksByAuthorName (String authorName);
   
   @Query("SELECT b FROM Book b WHERE b.price > :price OR b.price =:price")
   public List<Book> findByPriceGreaterThanEqual(Integer price);
   
   @Query("SELECT b FROM Book b WHERE b.price < :price")
   public List<Book> findByPriceLessThan(Integer price);
   
   @Query("SELECT b FROM Book b WHERE b.publishedYear BETWEEN :startDate AND :endDate")
   public List<Book> getAllBooksPublishBetween (LocalDate startDate, LocalDate endDate);
   
   @Query("SELECT b FROM Book b WHERE b.publishedYear > :publishedYear")
   public List<Book> getAllBooksAfterPublishedDate(LocalDate publishedYear);
   
   @Query("SELECT b FROM Book b WHERE b.authorName = :authorName AND b.publisher= :publisher")
   public List<Book> getAllBooksByAuthorAndPublisher(String authorName, String publisher);
   

}
