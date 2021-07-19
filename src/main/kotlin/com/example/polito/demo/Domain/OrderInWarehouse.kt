package com.example.polito.demo.Domain

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id



@Entity
class OrderInWarehouse : Serializable {



    @Id
    @GeneratedValue
    var OrderInWarehouseId : Long? = null

    @Column
    var warehouseId : Long? = null

    @Column
    var productId : Long? = null

    @Column
    var orderId : Long? = null

    @Column
    var quantity : Long? = null





}