package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Category;
import java.util.List;

/**
 * ICategoryService
 */
public interface ICategoryService {
  List<Category> findAll();

  Category save(Category category);

  Category findById(Long id);

  Category update(Long id, Category category);

  void deleteById(Long id);
}
