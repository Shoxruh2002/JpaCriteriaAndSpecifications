package uz.sh;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.web.bind.annotation.*;
import uz.sh.criteria.AuthorCriteria;
import uz.sh.criteria.CriteriaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
@RestController
@RequestMapping("/base")
public class BaseController {

    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final CriteriaService criteriaService;
    @Autowired
    private EntityManager entityManager;


    public BaseController( BookRepo bookRepo, AuthorRepo authorRepo, CriteriaService criteriaService ) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.criteriaService = criteriaService;
    }

    @PostMapping("/author/save")
    public Author saveAuthor( @RequestBody Author author ) {
        return authorRepo.save(author);
    }

    @PostMapping("/book/save")
    public Book saveBook( @RequestBody Book book ) {
        return bookRepo.save(book);
    }

    @GetMapping("/author/get-all1")
    public List<AuthorDTO> getAllAuthors1() {
        long l = System.currentTimeMillis();
        String query = criteriaService.createHQLQuery(Author.class, List.of("id", "name", "age"), AuthorDTO.class);
        List<AuthorDTO> resultList = entityManager.createQuery(query, AuthorDTO.class).getResultList();
        System.out.println(System.currentTimeMillis() - l);
        return resultList;

    }

    @GetMapping("/author/get-all2")
    public List<Author> getAllAuthors2() {
        long l = System.currentTimeMillis();
        List<Author> resultList = authorRepo.findAll();
        System.out.println(System.currentTimeMillis() - l);
        return resultList;
    }

    @GetMapping("/author/get-all3")
    public List<AuthorDTO> getAllAuthors3( @RequestParam("authorName") Optional<String> authorName,
                                           @RequestParam("authorAge") Optional<Integer> authorAge ) {
        long l = System.currentTimeMillis();
        AuthorCriteria authorCriteria = new AuthorCriteria(authorName, authorAge);
        String query = criteriaService.createHQLQueryWithFilter(Author.class, List.of("id", "name", "age"), AuthorDTO.class, authorCriteria.getFilters());
        List<AuthorDTO> resultList = entityManager.createQuery(query, AuthorDTO.class).getResultList();
        System.out.println(System.currentTimeMillis() - l);
        return resultList;
    }

    @GetMapping("/book/get-all")
    public List<BookDTO> getAllBooks() {
        return bookRepo.findAllBookDTO();
    }


    @GetMapping("/book/get-by-filter")
    public List<Book> findAllBooksFilter(
            @RequestParam("title") Optional<String> title,
            @RequestParam("description") Optional<String> description,
            @RequestParam("price") Optional<Double> price, Pageable pageable
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> book = criteriaQuery.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        if ( title.isPresent() ) {
            Predicate titlePredicate = criteriaBuilder.like(book.get("title"), "%" + title.get() + "%");
            predicates.add(titlePredicate);
        }
        if ( description.isPresent() ) {
            Predicate descriptionPredicate = criteriaBuilder.like(book.get("description"), "%" + description.get() + "%");
            predicates.add(descriptionPredicate);
        }
        if ( price.isPresent() ) {
            Predicate pricePredicate = criteriaBuilder.equal(book.get("price"), price.get());
            predicates.add(pricePredicate);
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), book, criteriaBuilder));
        TypedQuery<Book> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @GetMapping("/book-dto/get-by-filter")
    public List<BookDTO> findAllBookDTOsFilter(
            @RequestParam("title") Optional<String> title,
            @RequestParam("description") Optional<String> description,
            @RequestParam("price") Optional<Double> price,
            @RequestParam("authorName") Optional<String> authorName,
            @RequestParam("authorAge") Optional<Integer> authorAge,
            Pageable pageable
    ) {
        String query = """
                select b.id, b.title, b.description, b.price, b.author_id,a.name,a.age
                from book b
                         inner join author a on a.id = b.author_id
                         where
                        
                """;
        if ( title.isPresent() )
            query = query + "b.title like '%" + title.get() + "%'";
        if ( description.isPresent() )
            query = query + "b.description like '%" + description.get() + "%'";
        if ( authorName.isPresent() )
            query = query + "a.name like '%" + authorName.get() + "%'";
        if ( authorAge.isPresent() )
            query = query + "a.age = " + authorAge;
        return entityManager.createNamedQuery(query, BookDTO.class).getResultList();

//
//
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<BookDTO> criteriaQuery = criteriaBuilder.createQuery(BookDTO.class);
//        Metamodel metamodel = entityManager.getMetamodel();
//        EntityType<Book> Book_ = metamodel.entity(Book.class);
//        EntityType<Author> Author_ = metamodel.entity(Author.class);
//        Root<Book> book = criteriaQuery.from(Book.class);
//        List<Predicate> predicates = new ArrayList<>();
//
//        if ( title.isPresent() ) {
//            Predicate titlePredicate = criteriaBuilder.like(book.get("title"), "%" + title.get() + "%");
//            predicates.add(titlePredicate);
//        }
//        if ( description.isPresent() ) {
//            Predicate descriptionPredicate = criteriaBuilder.like(book.get("description"), "%" + description.get() + "%");
//            predicates.add(descriptionPredicate);
//        }
//        if ( price.isPresent() ) {
//            Predicate pricePredicate = criteriaBuilder.equal(book.get("price"), price.get());
//            predicates.add(pricePredicate);
//        }
//        criteriaQuery.where(predicates.toArray(new Predicate[0]));
//        criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), book, criteriaBuilder));
//        TypedQuery<Book> typedQuery = entityManager.createQuery(criteriaQuery);
//
//        return typedQuery.getResultList();
    }
}
