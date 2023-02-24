package uz.sh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Shoxruh Bekpulatov
 * Time : 24/02/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long id;

    private String title;

    private String description;

    private Double price;

    private Long authorId;

    private String authorName;

    private Integer authorAge;


}
