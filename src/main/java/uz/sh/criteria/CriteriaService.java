package uz.sh.criteria;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

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
        String as = clazz.getSimpleName().substring(0, 3);
        StringBuffer query = new StringBuffer("select new ").append(returnType.getName()).append("(");
        for ( String selectField : selectFields ) {
            query.append(as).append(".").append(selectField).append(", ");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(") from ").append(clazz.getSimpleName()).append(" ").append(as);
        return query;
    }

    /**
     * Agar qaysidir tabledagi hamma field lar kerak bulsa , Shuni iwlatiw kk.
     * Bu automatic hamma fieldlarni aniqlab chiqib select qilib oladi
     *
     * @param clazz      -> bu qaysi Entity dan select qiliwni beradi.
     *                   Bunda table name emas EntityName beriliwi kk cunki hql query yasaladi
     * @param returnType -> bu doim DoimDTO buladi. @param selectFields da kelgan filedlarga mos konstruktori buliwi wart
     * @return hql query
     */

    public StringBuffer createHQLQuery( Class clazz, Class returnType ) {
        return this.createHQLQuery(clazz, Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).toList(), returnType);
    }


    /**
     * @param clazz        -> bu qaysi Entity dan select qiliwni beradi.
     *                     Bunda table name emas EntityName beriliwi kk cunki hql query yasaladi
     * @param selectFields -> select qiliniwi kerak bulgan field lar listni. Bu field lar yuqorida berilgan Entityda buliwi wart
     * @param returnType   -> bu doim DoimDTO buladi. @param selectFields da kelgan filedlarga mos konstruktori buliwi wart
     * @param criteria     -> icida Filterlari bor bulgan Criteria. Wunga kora where ni tuldirib ciqamz
     * @return hql query
     */

    public <T extends Criteria> StringBuffer createHQLQueryWithFilter( Class clazz, List<String> selectFields, Class returnType, T criteria ) {
        StringBuffer query = this.createHQLQuery(clazz, selectFields, returnType);//filtersiz queryni yasab oldik
        String as = clazz.getSimpleName().substring(0, 3);
        if ( criteria.getFilters().size() > 0 ) {//Agar rostan filter bor bulsa where orqali query ga quwib ciqamz
            query.append(" where ");
            for ( Filter filter : criteria.getFilters() ) {
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
            query.delete(length - 4, length);//oxirgi endni girs copamz
        }
        return query;
    }


}