package com.example.polito.demo.Repositories

import com.example.polito.demo.Domain.OrderInWarehouse
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderInWarehouseRepository : CrudRepository<OrderInWarehouse, Long> {



}