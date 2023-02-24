package uz.sh;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.sh.criteria.AuthorCriteria;
import uz.sh.criteria.BookCriteria;
import uz.sh.criteria.CriteriaService;
import uz.sh.criteria.Join;

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

    @GetMapping("/author/get-all-by-custom-query")
    public List<AuthorDTO> getAllAuthorsByCustomQuery() {
        long l = System.currentTimeMillis();
        String query = criteriaService.createHQLQuery(Author.class, List.of("id", "name", "age"), AuthorDTO.class).toString();
        List<AuthorDTO> resultList = entityManager.createQuery(query, AuthorDTO.class).getResultList();
        System.out.println(System.currentTimeMillis() - l);
        return resultList;
    }

    @GetMapping("/author/get-all-by-find-all")
    public List<Author> getAllAuthorsByFindAll() {
        long l = System.currentTimeMillis();
        List<Author> resultList = authorRepo.findAll();
        System.out.println(System.currentTimeMillis() - l);
        return resultList;
    }

    @GetMapping("/author/get-all-by-filter")
    public List<AuthorDTO> getAllAuthorsByFilter( @RequestParam("authorName") Optional<String> authorName,
                                                  @RequestParam("authorAge") Optional<Integer> authorAge ) {
        long l = System.currentTimeMillis();
        AuthorCriteria authorCriteria = new AuthorCriteria(List.of("id", "name", "age"), authorName, authorAge);//criteria yasab olamz
        String query = criteriaService.createJoinedHQLQuery(Author.class, authorCriteria, AuthorDTO.class).toString();//tayyor query cani olamz
        List<AuthorDTO> resultList = entityManager.createQuery(query, AuthorDTO.class).getResultList();//tayyor natijani olamz
        System.out.println(System.currentTimeMillis() - l);
        return resultList;
    }

    @GetMapping("/author/get-all")
    public List<AuthorDTO> getAllAuthors() {
        long l = System.currentTimeMillis();
        String query = criteriaService.createHQLQuery(Author.class, List.of("id", "name", "age"), AuthorDTO.class).toString();//tayyor query cani olamz
        List<AuthorDTO> resultList = entityManager.createQuery(query, AuthorDTO.class).getResultList();//tayyor natijani olamz
        System.out.println(System.currentTimeMillis() - l);
        return resultList;
    }

    @GetMapping("/book-dto/get-all-by-find-all")
    public List<BookDTO> getAllBooksByFindAll(
            @RequestParam("title") Optional<String> title,
            @RequestParam("description") Optional<String> description,
            @RequestParam("price") Optional<Double> price,
            @RequestParam("authorName") Optional<String> authorName,
            @RequestParam("authorAge") Optional<Integer> authorAge
    ) {
        long l = System.currentTimeMillis();
        ArrayList<String> list = new ArrayList<>();
        list.add("id");
        list.add("title");
        list.add("description");
        list.add("price");
        list.add("authorId");
        AuthorCriteria authorCriteria = new AuthorCriteria(List.of("name", "age"), authorName, authorAge);//criteria yasab olamz
        BookCriteria bookCriteria = new BookCriteria(list, title, description, price);//criteria yasab olamz
        Join join = new Join(Author.class, "id", Book.class, "authorId", JoinType.INNER);
        String query = criteriaService.createJoinedHQLQuery(Book.class, bookCriteria, join, authorCriteria, BookDTO.class).toString();
        List<BookDTO> resultList = entityManager.createQuery(query, BookDTO.class).getResultList();//tayyor natijani olamz
        System.out.println(System.currentTimeMillis() - l);
        return resultList;
    }

//    @GetMapping("/book-dto/get-all-by-query")
//    public List<BookDTO> getAllBooksByQuery(
//            @RequestParam("title") Optional<String> title,
//            @RequestParam("description") Optional<String> description,
//            @RequestParam("price") Optional<Double> price,
//            @RequestParam("authorName") Optional<String> authorName,
//            @RequestParam("authorAge") Optional<Integer> authorAge,
//            ) {
//
//    }


//
//    @GetMapping("/book/get-by-filter")
//    public List<Book> findAllBooksFilter(
//            @RequestParam("title") Optional<String> title,
//            @RequestParam("description") Optional<String> description,
//            @RequestParam("price") Optional<Double> price, Pageable pageable
//    ) {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
//        Root<Book> book = criteriaQuery.from(Book.class);
//
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
//        return typedQuery.getResultList();
//    }
//
//    @GetMapping("/book-dto/get-by-filter")
//    public List<BookDTO> findAllBookDTOsFilter(
//            @RequestParam("title") Optional<String> title,
//            @RequestParam("description") Optional<String> description,
//            @RequestParam("price") Optional<Double> price,
//            @RequestParam("authorName") Optional<String> authorName,
//            @RequestParam("authorAge") Optional<Integer> authorAge,
//            Pageable pageable
//    ) {
//        String query = """
//                select b.id, b.title, b.description, b.price, b.author_id,a.name,a.age
//                from book b
//                         inner join author a on a.id = b.author_id
//                         where
//
//                """;
//        if ( title.isPresent() )
//            query = query + "b.title like '%" + title.get() + "%'";
//        if ( description.isPresent() )
//            query = query + "b.description like '%" + description.get() + "%'";
//        if ( authorName.isPresent() )
//            query = query + "a.name like '%" + authorName.get() + "%'";
//        if ( authorAge.isPresent() )
//            query = query + "a.age = " + authorAge;
//        return entityManager.createNamedQuery(query, BookDTO.class).getResultList();
//
////
////
////        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
////        CriteriaQuery<BookDTO> criteriaQuery = criteriaBuilder.createQuery(BookDTO.class);
////        Metamodel metamodel = entityManager.getMetamodel();
////        EntityType<Book> Book_ = metamodel.entity(Book.class);
////        EntityType<Author> Author_ = metamodel.entity(Author.class);
////        Root<Book> book = criteriaQuery.from(Book.class);
////        List<Predicate> predicates = new ArrayList<>();
////
////        if ( title.isPresent() ) {
////            Predicate titlePredicate = criteriaBuilder.like(book.get("title"), "%" + title.get() + "%");
////            predicates.add(titlePredicate);
////        }
////        if ( description.isPresent() ) {
////            Predicate descriptionPredicate = criteriaBuilder.like(book.get("description"), "%" + description.get() + "%");
////            predicates.add(descriptionPredicate);
////        }
////        if ( price.isPresent() ) {
////            Predicate pricePredicate = criteriaBuilder.equal(book.get("price"), price.get());
////            predicates.add(pricePredicate);
////        }
////        criteriaQuery.where(predicates.toArray(new Predicate[0]));
////        criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), book, criteriaBuilder));
////        TypedQuery<Book> typedQuery = entityManager.createQuery(criteriaQuery);
////
////        return typedQuery.getResultList();
//    }
}
