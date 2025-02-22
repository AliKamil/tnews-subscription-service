package aggregator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "news")
public class News {
    @Id
    private String id;
    private String title;
    private String content;
    private String url;
    private String publishedAt;
    @Indexed
    private String category;
    //TODO: можно сохранять ссылки на изображения, чтобы в боте был не просто сухой текст
}
