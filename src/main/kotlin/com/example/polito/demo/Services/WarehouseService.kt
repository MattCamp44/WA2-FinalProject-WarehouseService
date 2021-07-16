package com.example.polito.demo.Services

import com.example.polito.demo.DTOs.UpdateProductAvailabilityDTO


interface WarehouseService {

    fun addProductToWarehouse(updateProductAvailabilityDTO: UpdateProductAvailabilityDTO, productId : Long)
    fun checkQuantityOfProducts(productId: Long, quantity: Long): Boolean
    fun subtractQuantities( productId: Long ,quantity: Long)


}