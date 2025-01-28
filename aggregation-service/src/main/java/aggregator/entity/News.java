package aggregator.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "news")
public class News {
    @Id
    String id;
    String title;
    String content;
}
