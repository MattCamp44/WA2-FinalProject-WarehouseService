package com.example.polito.demo.Repositories

import com.example.polito.demo.DTOs.ProductQuantityProjection
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
    override fun findAll(): Vector<ProductInWarehouse>

    fun findByProductIdAndWarehouseID(productId: Long, warehouseID: Long) : ProductInWarehouse?

//    @Query( " select productId, sum(quantity) from ProductInWarehouse group by productId" )
    @Query( " select new com.example.polito.demo.DTOs.ProductQuantityProjection(productId, sum(quantity)) from ProductInWarehouse group by productId" )
    fun getSums():Vector<ProductQuantityProjection>

    @Query( "select sum(quantity) from ProductInWarehouse where productId = :productId" )
    fun findTotalAvailabilityByProductId(@Param("productId") productId: Long): Long

    fun countProductInWarehouseByProductIdAndWarehouseID(warehouseID: Long, productId: Long): Int


    fun existsByProductIdAndWarehouseID(productId: Long, warehouseID: Long):Boolean

    @Query( "select distinct warehouseID from ProductInWarehouse " )
    fun getAllDistinctWarehouseIds() : Vector<Long>

}