package subscription.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import subscription.entity.Subscription;

import java.util.stream.Collectors;

interface CustomizedSave<T> {

  T save(T entity);
}

class CustomizedSaveImpl implements CustomizedSave<Subscription> {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Subscription save(Subscription entity) {
    Session session = entityManager.unwrap(Session.class);
    if (entity.getCategories() != null) {
        entity.setCategories(entity.getCategories().stream().map(session::merge).collect(Collectors.toSet()));
    }
    if (entity.getKeyWords() != null) {
       entity.setKeyWords(entity.getKeyWords().stream().map(session::merge).collect(Collectors.toSet()));
    }
    if (entity.getId() != null && session.find(Subscription.class, entity.getId()) != null) {
      return session.merge(entity);
    } else {
      session.persist(entity);
      return entity;
    }
  }
}
