package com.restaurant.pos.dto.table;

import com.restaurant.pos.entity.RestaurantTable;
import com.restaurant.pos.entity.TableStatus;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-09T14:43:35+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Ubuntu)"
)
@Component
public class TableMapperImpl implements TableMapper {

    @Override
    public TableResponse toResponse(RestaurantTable table) {
        if ( table == null ) {
            return null;
        }

        int seatingCapacity = 0;
        UUID id = null;
        String tableNumber = null;
        TableStatus status = null;

        seatingCapacity = table.getCapacity();
        id = table.getId();
        tableNumber = table.getTableNumber();
        status = table.getStatus();

        TableResponse tableResponse = new TableResponse( id, tableNumber, seatingCapacity, status );

        return tableResponse;
    }

    @Override
    public RestaurantTable toEntity(CreateTableRequest request) {
        if ( request == null ) {
            return null;
        }

        RestaurantTable restaurantTable = new RestaurantTable();

        restaurantTable.setCapacity( request.getSeatingCapacity() );
        restaurantTable.setTableNumber( request.getTableNumber() );
        restaurantTable.setStatus( request.getStatus() );

        return restaurantTable;
    }

    @Override
    public RestaurantTable toEntity(UpdateTableRequest request) {
        if ( request == null ) {
            return null;
        }

        RestaurantTable restaurantTable = new RestaurantTable();

        restaurantTable.setCapacity( request.getSeatingCapacity() );
        restaurantTable.setTableNumber( request.getTableNumber() );
        restaurantTable.setStatus( request.getStatus() );

        return restaurantTable;
    }
}
