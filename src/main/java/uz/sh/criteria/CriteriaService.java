package uz.sh.criteria;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
@Service
public class CriteriaService {

    public String createHQLQuery( Class clazz, List<String> selectFields, Class returnType ) {
        String as = clazz.getSimpleName().substring(0, 3);
        StringBuffer query = new StringBuffer("select new ").append(returnType.getName()).append("(");
        for ( String selectField : selectFields ) {
            query.append(as).append(".").append(selectField).append(", ");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(") from ").append(clazz.getSimpleName()).append(" ").append(as);
        return query.toString();
    }

    public String createHQLQueryWithFilter( Class clazz, List<String> selectFields, Class returnType, List<Filter> filters ) {
        String as = clazz.getSimpleName().substring(0, 3);
        StringBuffer query = new StringBuffer("select new ").append(returnType.getName()).append("(");
        for ( String selectField : selectFields ) {
            query.append(as).append(".").append(selectField).append(", ");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(") from ").append(clazz.getSimpleName()).append(" ").append(as);
        if ( filters.size() > 0 ) {
            query.append(" where ");
            for ( Filter filter : filters ) {
                if ( filter.getValue() != null ) {
                    query.append(as).append(".").append(filter.getField());
                    switch ( filter.getFilterMode() ) {
                        case LIKE -> query.append(" like '").append(filter.getValue()).append("' and ");
                        case LIKE_PREFIX_PERCENT -> query.append(" like '%").append(filter.getValue()).append("' and ");
                        case LIKE_SUFFIX_PERCENT -> query.append(" like '").append(filter.getValue()).append("%' and ");
                        case LIKE_TRIM_PERCENT -> query.append(" like '%").append(filter.getValue()).append("%' and ");
                        case EQUALS -> query.append(" = ").append(filter.getValue()).append(" and ");
                        case EQUALS_AS_STRING -> query.append(" = '").append(filter.getValue()).append("' and ");
                    }
                }
            }
            int length = query.length();
            query.delete(length - 4, length);
        }
        return query.toString();
    }

    public String createHQLQuery( Class clazz, Class returnType ) {
        return this.createHQLQuery(clazz, Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).toList(), returnType);
    }
}