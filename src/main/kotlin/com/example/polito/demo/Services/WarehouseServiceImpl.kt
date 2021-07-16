package com.example.polito.demo.Services

import com.example.polito.demo.DTOs.ProductQuantityProjection
import com.example.polito.demo.DTOs.UpdateProductAvailabilityDTO
import com.example.polito.demo.Domain.ProductInWarehouse
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

    override fun addProductToWarehouse(updateProductAvailabilityDTO: UpdateProductAvailabilityDTO, productId : Long) {

        println(productInWarehouseRepository.existsByProductIdAndWarehouseID(productId , updateProductAvailabilityDTO.warehouseId!!))
        if(productInWarehouseRepository.existsByProductIdAndWarehouseID(productId,updateProductAvailabilityDTO.warehouseId!!)){

            println( "Found existing" )

            if(updateProductAvailabilityDTO.quantity < 0) {
                if (checkQuantityOfProducts(productId, Math.abs(updateProductAvailabilityDTO.quantity))) {
                    subtractQuantities(productId, Math.abs(updateProductAvailabilityDTO.quantity))
                }
                else{

                    throw Exception("Not enough items in warehouses")

                }
            }
            else
                addQuantities(productId, Math.abs(updateProductAvailabilityDTO.quantity))

            return

        }
        else{

            if(updateProductAvailabilityDTO.quantity == null || updateProductAvailabilityDTO.alarmQuantity == null ||
                    updateProductAvailabilityDTO.warehouseId == null)
                        throw Exception("Bad updateProductAvailabilityDTO")

            var productInWarehouse : ProductInWarehouse = ProductInWarehouse()

            productInWarehouse.alarmLevel = updateProductAvailabilityDTO.alarmQuantity
            productInWarehouse.productId = productId
            productInWarehouse.warehouseID = updateProductAvailabilityDTO.warehouseId
            productInWarehouse.quantity = updateProductAvailabilityDTO.quantity

            productInWarehouseRepository.save( productInWarehouse )

            return

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

    fun subtractQuantities(productId: Long , quantity: Long ) {

        var entriesVector : Vector<ProductInWarehouse> = productInWarehouseRepository.findAllByProductIdOrderByQuantityDesc(productId)

        var counter : Long = 0

        var tempQuantity = quantity

        for( item in entriesVector ){

            if( item.quantity?.minus(item.alarmLevel!!)!! >= tempQuantity ){

                item.quantity = item.quantity!! - tempQuantity

                counter++

                break

            }else{

                item.quantity = item.alarmLevel
                tempQuantity -= item.quantity?.minus(item.alarmLevel!!)!!

                counter++


            }

            if(tempQuantity != 0L) {

                for (item in entriesVector) {

                    if(item.quantity!! > tempQuantity){

                        item.quantity = item.quantity?.minus(tempQuantity)



                        productInWarehouseRepository.save(item)

                        sendEmailToAdmins( productId , item.warehouseID!! )

                        return


                    }
                    else{

                        tempQuantity -= item.quantity!!

                        item.quantity = 0

                        productInWarehouseRepository.save(item)

                        sendEmailToAdmins( productId , item.warehouseID!! )

                    }


                }

                return
            }

            var iteratorVar : Int = 0

            while( iteratorVar++ < counter )
                productInWarehouseRepository.save(entriesVector[ iteratorVar ])


            return


        }









    }

    fun addQuantities(productId: Long, quantity: Long) {

        var item : ProductInWarehouse = productInWarehouseRepository.findFirstByProductIdOrderByQuantity(productId)

        item.quantity = item.quantity!! + quantity

        productInWarehouseRepository.save(item)


    }

    //TODO send emails to admins
    fun sendEmailToAdmins(productId: Long, warehouseId: Long){

        println("Email sent to admins for product $productId in warehouse $warehouseId")

    }


}