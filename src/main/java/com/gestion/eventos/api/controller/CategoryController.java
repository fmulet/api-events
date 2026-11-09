package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.dto.CategoryDto;
import com.gestion.eventos.api.mapper.CategoryMapper;
import com.gestion.eventos.api.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CategoryController
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<List<CategoryDto>> getAllCategories() {
    List<Category> categories = categoryService.findAll();

    return ResponseEntity.ok(
      categories.stream().map(categoryMapper::toDto).collect(Collectors.toList())
    );
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
    Category category = categoryService.findById(id);

    return ResponseEntity.ok(categoryMapper.toDto(category));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<CategoryDto> createCategory(
    @Valid @RequestBody CategoryDto categoryDto
  ) {
    Category category = categoryService.save(categoryMapper.toEntity(categoryDto));

    return new ResponseEntity<>(categoryMapper.toDto(category), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<CategoryDto> updateCategory(
    @PathVariable Long id,
    @Valid @RequestBody CategoryDto categoryDto
  ) {
    Category category = categoryService.update(id, categoryMapper.toEntity(categoryDto));

    return ResponseEntity.ok(categoryMapper.toDto(category));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
    categoryService.deleteById(id);

    return ResponseEntity.noContent().build();
  }
}
