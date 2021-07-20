package com.example.polito.demo.Services

import com.example.polito.demo.DTOs.*
import com.example.polito.demo.Domain.OrderInWarehouse
import com.example.polito.demo.Domain.ProductInWarehouse
import com.example.polito.demo.Repositories.OrderInWarehouseRepository
import com.example.polito.demo.Repositories.ProductInWarehouseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class WarehouseServiceImpl : WarehouseService {

    @Autowired
    lateinit var productInWarehouseRepository: ProductInWarehouseRepository

    @Autowired
    lateinit var orderInWarehouseRepository: OrderInWarehouseRepository

    @Autowired
    lateinit var mailService: MailService

    override fun addProductToWarehouse(addProductDTO: AddProductDTO) {

        println(productInWarehouseRepository.existsByProductIdAndWarehouseID(addProductDTO.productId!! , addProductDTO.warehouseId!!))
        if(productInWarehouseRepository.existsByProductIdAndWarehouseID(addProductDTO.productId!!,addProductDTO.warehouseId!!)){



            throw Exception("Product already exists in warehouse")

//            if(addProductDTO.warehouseId == null)
//                addQuantities(addProductDTO.productId, Math.abs(addProductDTO.quantity))
//            else{
//
//                var productInWarehouse : ProductInWarehouse = productInWarehouseRepository.findProductInWarehouseByWarehouseIDAndProductId(addProductDTO.warehouseId, addProductDTO.productId!!)
//
//                productInWarehouse.quantity = productInWarehouse.quantity!! + addProductDTO.quantity
//
//                if(addProductDTO.alarmQuantity != null)
//                    productInWarehouse.alarmLevel = addProductDTO.alarmQuantity
//
//                productInWarehouseRepository.save(productInWarehouse)
//
//
//
//            }
//
//
//
//            return

        }
        else{

            if(addProductDTO.quantity == null || addProductDTO.alarmQuantity == null ||
                    addProductDTO.warehouseId == null)
                        throw Exception("Bad updateProductAvailabilityDTO")

            var productInWarehouse : ProductInWarehouse = ProductInWarehouse()

            productInWarehouse.alarmLevel = addProductDTO.alarmQuantity
            productInWarehouse.productId = addProductDTO.productId
            productInWarehouse.warehouseID = addProductDTO.warehouseId
            productInWarehouse.quantity = addProductDTO.quantity

            productInWarehouseRepository.save( productInWarehouse )

            return

        }



    }

    override fun placeOrderInWarehouse(placeOrderDTO: PlaceOrderDTO) {

        var orderRecords : Vector<OrderInWarehouse>

        var productAvailability = productInWarehouseRepository.findTotalAvailabilityByProductId(placeOrderDTO.productId!!)

        if( productAvailability < placeOrderDTO.quantity!!)
            throw Exception("Not enough availability")

        orderRecords = subtractQuantities(placeOrderDTO.productId!!, Math.abs(placeOrderDTO.quantity!!))

        for(order in orderRecords){

            order.orderId = placeOrderDTO.orderId

            orderInWarehouseRepository.save(order)

        }



    }



    override fun getAllProductAvailabilities(): Vector<ProductQuantityProjection> {

        var itemVector : Vector<ProductQuantityProjection>

        itemVector = productInWarehouseRepository.getSums()



        for( item in itemVector )
            if(item!=null)
                println(" ${item.productId}  ${item.quantity} ")


        return itemVector



    }


    override fun getProductAvailability(productId: Long): Long {

        return productInWarehouseRepository.findTotalAvailabilityByProductId(productId)


    }

    override fun getAllWarehouses(): Vector<Long> {

        return productInWarehouseRepository.getAllDistinctWarehouseIds()

    }

    override fun updateAlarmLevel(updateAlarmLevelDTO: UpdateAlarmLevelDTO) {

        var result : ProductInWarehouse? =  productInWarehouseRepository.findByProductIdAndWarehouseID( updateAlarmLevelDTO.productId!! , updateAlarmLevelDTO.warehouseId!! )

        if(result == null)
            throw Exception("Product not found in warehouse")
        else{

            result.alarmLevel = updateAlarmLevelDTO.alarmLevel

            productInWarehouseRepository.save(result)

        }

        return


    }


    fun checkQuantityOfProducts(productId: Long, quantity: Long): Boolean {

        var entriesVector : Vector<ProductInWarehouse> = productInWarehouseRepository.findAllByProductId( productId )

        var accumulator : Long = 0

        for(  item in entriesVector ){

            accumulator += item.quantity!!

            if(accumulator > quantity){

                println("Enough!")
                return true
            }

        }

        println("Not enough")

        return false




    }


    fun subtractQuantities(productId: Long , quantity: Long ) : Vector<OrderInWarehouse> {

        var orderRecords : Vector<OrderInWarehouse> = Vector<OrderInWarehouse>()


        var entriesVector : Vector<ProductInWarehouse> = productInWarehouseRepository.findAllByProductIdOrderByQuantityDesc(productId)



        var tempQuantity = quantity

        for( item in entriesVector ) {

            if (item.quantity!! >= tempQuantity) {

                var orderRecord: OrderInWarehouse = OrderInWarehouse()

                orderRecord.productId = item.productId
                orderRecord.warehouseId = item.warehouseID
                orderRecord.quantity = tempQuantity

                orderRecords.add(orderRecord)

                item.quantity = item.quantity!! - tempQuantity

                tempQuantity = 0L

                if(item.quantity!! < item.alarmLevel!!)
                    sendEmailToAdmins(item.productId!! , item.warehouseID!!)

                productInWarehouseRepository.save(item)

                println("Last step: taking away $tempQuantity")

                break

            } else {

                var orderRecord: OrderInWarehouse = OrderInWarehouse()


                orderRecord.productId = item.productId
                orderRecord.warehouseId = item.warehouseID
                orderRecord.quantity = item.quantity

                orderRecords.add(orderRecord)

                tempQuantity -= item.quantity!!

                item.quantity = 0

                sendEmailToAdmins(item.productId!! , item.warehouseID!!)



                productInWarehouseRepository.save(item)

                println("quantity to take out now is $tempQuantity")



            }
        }








        return orderRecords


    }



//    fun subtractQuantities(productId: Long , quantity: Long ) : Vector<OrderInWarehouse> {
//
//        var orderRecords : Vector<OrderInWarehouse> = Vector<OrderInWarehouse>()
//
//
//        var entriesVector : Vector<ProductInWarehouse> = productInWarehouseRepository.findAllByProductIdOrderByQuantityDesc(productId)
//
//
//
//        var tempQuantity = quantity
//
//        for( item in entriesVector ) {
//
//            if (item.quantity?.minus(item.alarmLevel!!)!! >= tempQuantity) {
//
//                var orderRecord: OrderInWarehouse = OrderInWarehouse()
//
//                orderRecord.productId = item.productId
//                orderRecord.warehouseId = item.warehouseID
//                orderRecord.quantity = tempQuantity
//
//                orderRecords.add(orderRecord)
//
//                item.quantity = item.quantity!! - tempQuantity
//
//                tempQuantity = 0L
//
//
//
//                break
//
//            } else {
//
//                var orderRecord: OrderInWarehouse = OrderInWarehouse()
//
//
//                orderRecord.productId = item.productId
//                orderRecord.warehouseId = item.warehouseID
//                orderRecord.quantity = item.quantity?.minus(item.alarmLevel!!)!!
//
//                orderRecords.add(orderRecord)
//
//                item.quantity = item.alarmLevel
//                tempQuantity -= item.quantity?.minus(item.alarmLevel!!)!!
//
//
//
//
//
//
//            }
//        }
//
//            if(tempQuantity != 0L) {
//
//                for (item in entriesVector) {
//
//                    if(item.quantity!! >= tempQuantity){
//
//                        var orderRecord : OrderInWarehouse = OrderInWarehouse()
//
//                        orderRecord.productId = item.productId
//                        orderRecord.warehouseId = item.warehouseID
//                        orderRecord.quantity = item.quantity!! - tempQuantity
//
//                        orderRecords.add(orderRecord)
//
//                        item.quantity = item.quantity?.minus(tempQuantity)
//
//
//
//                        productInWarehouseRepository.save(item)
//
//                        sendEmailToAdmins( productId , item.warehouseID!! )
//
//
//
//
//                    }
//                    else{
//
//                        var orderRecord : OrderInWarehouse = OrderInWarehouse()
//
//                        orderRecord.productId = item.productId
//                        orderRecord.warehouseId = item.warehouseID
//                        orderRecord.quantity = item.quantity!!
//
//                        orderRecords.add(orderRecord)
//
//                        tempQuantity -= item.quantity!!
//
//                        item.quantity = 0
//
//                        productInWarehouseRepository.save(item)
//
//                        sendEmailToAdmins( productId , item.warehouseID!! )
//
//                    }
//
//
//                }
//
//
//            }
//
//
//
//            for(item in entriesVector)
//                productInWarehouseRepository.save(item)
//
//
//            return orderRecords
//
//
//        }


    override fun addQuantityTOProductInWarehouse(addProductQuantityDTO: AddProductQuantityDTO) {

        var productInWarehouse = productInWarehouseRepository.findProductInWarehouseByWarehouseIDAndProductId( addProductQuantityDTO.warehouseId , addProductQuantityDTO.productId )

        if( productInWarehouse == null )
            throw Exception("Not found")

        productInWarehouse.quantity = productInWarehouse.quantity?.plus(addProductQuantityDTO.quantity)

    }



    fun addQuantities(productId: Long, quantity: Long) {

        var item : ProductInWarehouse = productInWarehouseRepository.findFirstByProductIdOrderByQuantity(productId)

        item.quantity = item.quantity!! + quantity

        productInWarehouseRepository.save(item)


    }

    //TODO send emails to admins
    fun sendEmailToAdmins(productId: Long, warehouseId: Long){

        println("Email sent to admins for product $productId in warehouse $warehouseId")

        mailService.sendMessage( productId , warehouseId )


    }



}