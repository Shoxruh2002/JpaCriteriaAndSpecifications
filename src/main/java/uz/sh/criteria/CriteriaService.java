package uz.sh.criteria;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 * Definition : Icida reflections dan ham  foydalanailgan
 */
@Service
public class CriteriaService {


    /**
     * @param clazz        -> bu qaysi Entity dan select qiliwni beradi.
     *                     Bunda table name emas EntityName beriliwi kk cunki hql query yasaladi
     * @param selectFields -> select qiliniwi kerak bulgan field lar listni. Bu field lar yuqorida berilgan Entityda buliwi wart
     * @param returnType   -> bu doim DoimDTO buladi. @param selectFields da kelgan filedlarga mos konstruktori buliwi wart
     * @return hql query
     */

    public StringBuffer createHQLQuery( Class clazz, List<String> selectFields, Class returnType ) {
        String simpleName = clazz.getSimpleName();
        StringBuffer query = new StringBuffer("select new ").append(returnType.getName()).append("(");
        for ( String selectField : selectFields ) {
            query.append(simpleName).append(".").append(selectField).append(", ");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(") from ").append(simpleName).append(" ").append(simpleName);
        return query;
    }

    public <T extends Criteria> StringBuffer createJoinedHQLQuery( Class clazz, T criteria, Class returnType ) {

        //select

        StringBuffer query = selectPartCreator(clazz, criteria.getSelectedFields(), returnType);

        //filter

        return filterPartCreator(criteria.getFilters(), query);

    }


    public <T extends Criteria> StringBuffer createJoinedHQLQuery( Class clazz, T criteria1, Join join, T criteria2, Class returnType ) {
        Map<Filter, String> filters = criteria1.getFilters();
        filters.putAll(criteria2.getFilters());

        Map<String, String> selectedFields = criteria1.getSelectedFields();
        selectedFields.putAll(criteria2.getSelectedFields());

        StringBuffer query = this.selectPartCreator(clazz, selectedFields, returnType);

        query = joinPartCreator(join, query);

        return this.filterPartCreator(filters, query);

    }

    public <T extends Criteria> StringBuffer createJoinedHQLQuery( Class clazz, T criteria1, Join join1, T criteria2, Join join2, T criteria3, Class returnType ) {

        Map<Filter, String> filters = criteria1.getFilters();
        filters.putAll(criteria2.getFilters());
        filters.putAll(criteria3.getFilters());

        Map<String, String> selectedFields = criteria1.getSelectedFields();
        selectedFields.putAll(criteria2.getSelectedFields());
        selectedFields.putAll(criteria3.getSelectedFields());

        StringBuffer query = this.selectPartCreator(clazz, selectedFields, returnType);

        query = joinPartCreator(join1, query);
        query = joinPartCreator(join2, query);

        return this.filterPartCreator(filters, query);
    }


    private <T extends Criteria> StringBuffer filterPartCreator( Map<Filter, String> filterEntries, StringBuffer query ) {
        if ( filterEntries.size() > 0 ) {    //Agar rostan filter bor bulsa where orqali query ga quwib ciqamz
            query.append(" where ");

            for ( Map.Entry<Filter, String> filterEntry : filterEntries.entrySet() ) {
                if ( filterEntry.getKey().getValue() != null ) {
                    Filter filter = filterEntry.getKey();
                    query.append(filterEntry.getValue()).append(".").append(filter.getField());

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
            query.delete(length - 4, length);//oxirgi endni girs copamz
        }
        return query;
    }

    private <T extends Criteria> StringBuffer selectPartCreator( Class clazz, Map<String, String> selectedFields, Class returnType ) {
        String simpleName = clazz.getSimpleName();
        StringBuffer query = new StringBuffer("select new ").append(returnType.getName()).append("(");

        for ( Map.Entry<String, String> selectedField : selectedFields.entrySet() ) {
            query.append(selectedField.getValue()).append(".").append(selectedField.getKey()).append(", ");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(") from ").append(simpleName).append(" ").append(simpleName.toLowerCase());
        return query;
    }


    private StringBuffer joinPartCreator( Join join, StringBuffer query ) {

        switch ( join.getJoinType() ) {
            case INNER -> query.append(" inner join ");
            case LEFT -> query.append(" left join ");
            case RIGHT -> query.append(" right join ");
        }

        query.append(join.getClazz().getSimpleName())
                .append(" ")
                .append(join.getClazz().getSimpleName().toLowerCase())
                .append(" on ")
                .append(join.getJoinClazz().getSimpleName().toLowerCase())
                .append(".")
                .append(join.getJoinField())
                .append(" = ")
                .append(join.getClazz().getSimpleName().toLowerCase())
                .append(".")
                .append(join.getClazzField());
        return query;
    }
}