package com.restaurant.pos.dto.menu;

import com.restaurant.pos.entity.Category;
import com.restaurant.pos.entity.MenuItem;
import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-09T14:43:35+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Ubuntu)"
)
@Component
public class MenuItemMapperImpl implements MenuItemMapper {

    @Override
    public MenuItemResponse toResponse(MenuItem item) {
        if ( item == null ) {
            return null;
        }

        String name = null;
        boolean isActive = false;
        UUID categoryId = null;
        String categoryName = null;
        UUID id = null;
        String description = null;
        BigDecimal price = null;

        name = item.getDishName();
        isActive = item.isAvailable();
        categoryId = itemCategoryId( item );
        categoryName = itemCategoryName( item );
        id = item.getId();
        description = item.getDescription();
        price = item.getPrice();

        MenuItemResponse menuItemResponse = new MenuItemResponse( id, name, description, price, categoryId, categoryName, isActive );

        return menuItemResponse;
    }

    @Override
    public MenuItem toEntity(CreateMenuItemRequest request) {
        if ( request == null ) {
            return null;
        }

        MenuItem menuItem = new MenuItem();

        menuItem.setDishName( request.getName() );
        menuItem.setAvailable( request.isActive() );
        menuItem.setPrice( request.getPrice() );
        menuItem.setDescription( request.getDescription() );

        return menuItem;
    }

    @Override
    public MenuItem toEntity(UpdateMenuItemRequest request) {
        if ( request == null ) {
            return null;
        }

        MenuItem menuItem = new MenuItem();

        menuItem.setDishName( request.getName() );
        menuItem.setAvailable( request.isActive() );
        menuItem.setPrice( request.getPrice() );
        menuItem.setDescription( request.getDescription() );

        return menuItem;
    }

    private UUID itemCategoryId(MenuItem menuItem) {
        Category category = menuItem.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }

    private String itemCategoryName(MenuItem menuItem) {
        Category category = menuItem.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }
}
