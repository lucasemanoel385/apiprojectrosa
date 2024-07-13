package br.com.rosa.domain.categoryItem.service;

import br.com.rosa.domain.categoryItem.Category;
import br.com.rosa.domain.categoryItem.RepositoryCategory;
import br.com.rosa.domain.categoryItem.dto.RegisterCategory;
import br.com.rosa.domain.categoryItem.dto.UpdateCategory;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.infra.exceptions.SqlConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private RepositoryCategory repository;

    @Autowired
    private RepositoryItem repositoryItem;


    public Category registerCategory(RegisterCategory data) {

        checkIfCategory(data.category());

        var category = new Category(data.category());

        repository.save(category);

        return category;
    }

    public Category updateCategory(UpdateCategory data) {

        checkIfCategory(data.category());

        var category = repository.getReferenceById(data.id());

        category.attCategory(data);

        repository.save(category);

        return category;

    }

    private void checkIfCategory(String category) {
        if (repository.existsByName(category)) {
            throw new SqlConstraintViolationException("Categoria já existe");
        }
    }

    public void deleteCategory(Long id) {

        var itensWithCategory = repositoryItem.existsByItemWithCategoryId(id);

        if (itensWithCategory > 0) {
            throw new SqlConstraintViolationException("Não é possível deletar a categoria, pois há produtos cadastrados nela");
        }

        repository.deleteById(id);

    }
}
