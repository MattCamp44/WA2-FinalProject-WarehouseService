package com.example.polito.demo.Repositories

import com.example.polito.demo.Domain.ProductInWarehouse
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductInWarehouseRepository : CrudRepository<ProductInWarehouse, Long> {


    fun existsProductInWarehouseByWarehouseIDAndProductId(warehouseID: Long, productId: Long): Boolean
    fun findProductInWarehouseByWarehouseIDAndProductId(warehouseID: Long, productId: Long): ProductInWarehouse
    fun findAllByProductId(productId: Long) : Vector<ProductInWarehouse>
    fun findAllByProductIdOrderByQuantityDesc(productId: Long) : Vector<ProductInWarehouse>
    fun findFirstByProductIdOrderByQuantity(productId: Long) : ProductInWarehouse

    fun countProductInWarehouseByProductIdAndWarehouseID(warehouseID: Long, productId: Long): Int


    fun existsByProductIdAndWarehouseID(productId: Long, warehouseID: Long):Boolean



}