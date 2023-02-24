package uz.sh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
public interface BookRepo extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query(value = "select new uz.sh.BookDTO(b.id,b.title,b.description,b.price,b.authorId,a.name,a.age) from Book b inner join Author a on a.id = b.authorId")
    List<BookDTO> findAllBookDTO();

}
