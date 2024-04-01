package com.fdmgroup.todolist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.fdmgroup.todolist.model.Category;
import com.fdmgroup.todolist.repository.CategoryRepository;

public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepo;

	public Optional<Category> createCategory(Category category) {
		return Optional.ofNullable(categoryRepo.save(category));
	}
	
	public Optional<Category> findCategoryById(Long id) {
		return categoryRepo.findById(id);
	}
	
	public List<Category> findAllCategorys() {
		return categoryRepo.findAll();
	}
	
	public Optional<Category> updateCategory(Category category) {
		return Optional.ofNullable(categoryRepo.save(category));
	}
	
	public void deleteCategoryById(Long id) {
		categoryRepo.deleteById(id);
	}
}
