package com.example.polito.demo.Services

import com.example.polito.demo.DTOs.*
import java.util.*


interface WarehouseService {

    fun addProductToWarehouse(addProductDTO: AddProductDTO)
    fun getAllProductAvailabilities(): Vector<ProductQuantityProjection>
    fun getProductAvailability(productId: Long): Long
    fun getAllWarehouses(): Vector<Long>
    fun updateAlarmLevel(updateAlarmLevelDTO: UpdateAlarmLevelDTO)
    fun placeOrderInWarehouse(placeOrderDTO: PlaceOrderDTO)
    fun addQuantityTOProductInWarehouse(addProductQuantityDTO: AddProductQuantityDTO)

}