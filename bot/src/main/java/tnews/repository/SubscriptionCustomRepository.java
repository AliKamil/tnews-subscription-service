package tnews.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.stream.Collectors;
import org.hibernate.Session;
import tnews.entity.Subscription;

interface CustomizedSave<T> {

  T save(T entity);
}

class CustomizedSaveImpl implements CustomizedSave<Subscription> {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Subscription save(Subscription entity) {
    Session session = entityManager.unwrap(Session.class);
    entity.setCategories(entity.getCategories().stream().map(session::merge).collect(Collectors.toSet()));
    entity.setKeyWords(entity.getKeyWords().stream().map(session::merge).collect(Collectors.toSet()));
    session.persist(entity);
    return session.merge(entity);
  }
}
