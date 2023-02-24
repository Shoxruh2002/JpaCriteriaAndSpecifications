package uz.sh.criteria;

import jakarta.validation.constraints.NotNull;
import uz.sh.Book;

import java.util.*;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
public class BookCriteria implements Criteria {


    private Filter titleFilter;

    private Filter descriptionFilter;

    private Filter priceFilter;

    private final Map<Filter, String> filters = new HashMap<>();

    private final Map<String, String> selectedFields;


    public BookCriteria( @NotNull ArrayList<String> selectedFields, Optional<String> titleFilter, Optional<String> descriptionFilter, Optional<Double> priceFilter ) {
        titleFilter.ifPresent(s -> {
            this.titleFilter = new Filter(Filter.FilterMode.LIKE_TRIM_PERCENT, "title", s);
            this.filters.put(this.titleFilter, Book.class.getSimpleName().toLowerCase());
        });
        descriptionFilter.ifPresent(s -> {
            this.descriptionFilter = new Filter(Filter.FilterMode.LIKE_TRIM_PERCENT, "description", s);
            this.filters.put(this.descriptionFilter, Book.class.getSimpleName().toLowerCase());
        });
        priceFilter.ifPresent(s -> {
            this.priceFilter = new Filter(Filter.FilterMode.EQUALS, "price", s);
            this.filters.put(this.priceFilter, Book.class.getSimpleName().toLowerCase());
        });
        HashMap<String, String> map = new LinkedHashMap<>();
        for ( String f : selectedFields ) {
            map.put(f, Book.class.getSimpleName().toLowerCase());
        }
        this.selectedFields = map;

    }


    @Override
    public Map<Filter, String> getFilters() {
        return this.filters;
    }

    @Override
    public Map<String, String> getSelectedFields() {
        return this.selectedFields;
    }

    public Filter getTitleFilter() {
        return titleFilter;
    }

    public Filter getDescriptionFilter() {
        return descriptionFilter;
    }

    public Filter getPriceFilter() {
        return priceFilter;
    }

}
