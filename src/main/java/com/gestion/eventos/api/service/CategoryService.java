package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.exception.ResourceNotFoundException;
import com.gestion.eventos.api.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CategoryService
 */
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  @Override
  @Transactional
  public Category save(Category event) {
    return categoryRepository.save(event);
  }

  @Override
  @Transactional(readOnly = true)
  public Category findById(Long id) {
    return categoryRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new ResourceNotFoundException("Category not found");
    }
    categoryRepository.deleteById(id);
  }

  @Override
  @Transactional
  public Category update(Long id, Category category) {
    Category existingCategory = categoryRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    existingCategory.setName(category.getName());
    existingCategory.setDescription(category.getDescription());
    return categoryRepository.save(existingCategory);
  }
}
