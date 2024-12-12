package tnews.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String categoryName;
    @ManyToMany(mappedBy = "categories")
    private Set<Subscription> subscriptions;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Category category = (Category) o;
//        return Objects.equals(id, category.id) &&
//                Objects.equals(categoryName, category.categoryName);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, categoryName);
//    }
}
