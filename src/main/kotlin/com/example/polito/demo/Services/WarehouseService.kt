package com.example.polito.demo.Services

import com.example.polito.demo.DTOs.ProductQuantityProjection
import com.example.polito.demo.DTOs.UpdateProductAvailabilityDTO
import java.util.*


interface WarehouseService {

    fun addProductToWarehouse(updateProductAvailabilityDTO: UpdateProductAvailabilityDTO, productId : Long)
    fun getAllProductAvailabilities(): Vector<ProductQuantityProjection>


}