package subscription.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import subscription.service.CategoryService;

@Component
@AllArgsConstructor
public class CategoryUpdateScheduler {
    private final CategoryService categoryService;

    // @Scheduled(fixedRate = 86400000) // обновление категорий раз в день
    @Scheduled(fixedRate = 10000)
    public void scheduleCategoryUpdate() {
        categoryService.updateCategories();
    }
}
