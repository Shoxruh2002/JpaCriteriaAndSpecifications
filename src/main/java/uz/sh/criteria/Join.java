package uz.sh.criteria;

import jakarta.persistence.criteria.JoinType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Join {

    private Class clazz; //qaysi klass bn join buliwi

    private String clazzField;// join bulayotgan klass ning qaysi fieldi orqali join buliwi

    private Class joinClazz; //join qildiruvci klass

    private String joinField;//join qildiruvci klass ni qaysi feildi bn join buliwi

    private JoinType joinType;

}
