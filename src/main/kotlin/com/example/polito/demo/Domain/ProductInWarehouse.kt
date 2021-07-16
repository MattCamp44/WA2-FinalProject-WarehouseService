package com.example.polito.demo.Domain

import java.io.Serializable
import javax.persistence.*

@Entity
class ProductInWarehouse : Serializable {

    @Id
    @GeneratedValue
    var productInWarehouseId : Long? = null

    @Column
    @GeneratedValue
    var warehouseID : Long? = null

    @Column
    var productId : Long? = null

    @Column
    var quantity : Long? = null

    @Column
    var alarmLevel : Long? = null



}

//@Embeddable
//class CompositeKey (
//
//    @Column
//    @GeneratedValue
//    var warehouseID : Long? = null
//    ,
//    @Column
//    var productId : Long? = null
//
//    ) : Serializable


