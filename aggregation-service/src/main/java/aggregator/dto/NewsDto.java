package aggregator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private String title;
    private String content;
    private String url;

    @Override
    public String toString() {
        return "\t " + title + " \n\n " + content + " \n\n Источник: " + url; //нужно будет отформатировать текст
    }
}
