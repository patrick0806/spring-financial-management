package br.com.geeknizado.financial_management.adapters.rest.mapper;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CategoryDTO;
import br.com.geeknizado.financial_management.internal.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {
    public CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "isActive", target = "isActive")
    CategoryDTO map(Category category);

    List<CategoryDTO> map(List<Category> categories);
}
