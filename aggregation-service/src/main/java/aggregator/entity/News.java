package aggregator.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "news")
public class News {
    @Id
    private String id;
    private String title;
    private String content;
    private String url;
    private String publishedAt;
    //TODO: можно сохранять ссылки на изображения, чтобы в боте был не просто сухой текст
}
