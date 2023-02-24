package uz.sh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
public interface AuthorRepo extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
}
