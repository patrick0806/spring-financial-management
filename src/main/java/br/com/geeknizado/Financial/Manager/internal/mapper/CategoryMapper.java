package br.com.geeknizado.Financial.Manager.internal.mapper;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CategoryDTO;
import br.com.geeknizado.Financial.Manager.internal.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {
    public CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO map(Category category);
    List<CategoryDTO> map(List<Category> categories);
}
