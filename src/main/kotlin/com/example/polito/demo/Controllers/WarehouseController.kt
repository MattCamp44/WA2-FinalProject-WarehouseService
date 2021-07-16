package com.example.polito.demo.Controllers

import com.example.polito.demo.DTOs.ProductQuantityProjection
import com.example.polito.demo.DTOs.UpdateProductAvailabilityDTO
import com.example.polito.demo.DTOs.WarehouseDTO
import com.example.polito.demo.Services.WarehouseService
import com.sun.istack.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/warehouses")
class WarehouseController {

    @Autowired
    lateinit var warehouseService: WarehouseService

    @GetMapping("/warehouses")
    fun getAllWarehouses() : ResponseEntity<String> {





        println( "Ok" )

        return ResponseEntity<String>(HttpStatus.OK)




    }

    @GetMapping("/warehouses/{warehouseID}")
    fun getWarehouseById(

        @NotNull
        @PathVariable
        warehouseID: Long

    ) : ResponseEntity<String>  {

        println( "getWareHousesById with Id $warehouseID" )
        return ResponseEntity<String>(HttpStatus.OK)

    }

    @PostMapping("/warehouses")
    fun addWarehouse() : ResponseEntity<String> {

        println( "addWareHouse" )


        return ResponseEntity<String>(HttpStatus.OK)

    }

    //Full representation -> I send you the whole warehouse
    @PutMapping("/warehouses/{warehouseID}")
    fun updateOrCreateWarehouse(

        @NotNull
        @PathVariable
        warehouseID: Long

        ,

        warehouseDTO : WarehouseDTO

        ,

        bindingResult: BindingResult

    ) : ResponseEntity<String>  {

        println( "updateOrCreate with Id $warehouseID" )

        if(bindingResult.hasErrors())
            return ResponseEntity<String>(HttpStatus.BAD_REQUEST)

        return ResponseEntity<String>(HttpStatus.OK)


    }

    //Partial representation -> I only send you the Id of the warehouse and the field I want to update
    @PatchMapping("/warehouses/{warehouseID}")
    fun updateWarehouse(

        @NotNull
        @PathVariable
        warehouseID: Long

    ) : ResponseEntity<String>  {

        println( "update with Id $warehouseID" )

        return ResponseEntity<String>(HttpStatus.OK)

    }



    @DeleteMapping("/warehouses/{warehouseID}")
    fun deleteWarehouse(
        @NotNull
        @PathVariable
        warehouseID: Long
    ): ResponseEntity<String> {

        println( "delete with Id $warehouseID" )

        return ResponseEntity<String>(HttpStatus.OK)

    }



    //Internal API

    @GetMapping("/productavailability/{productID}")
    fun getProductAvailabilityById(
        @NotNull
        @PathVariable
        productID: Long
    ) : ResponseEntity<Long> {


        return ResponseEntity( warehouseService.getProductAvailability(productID) , HttpStatus.OK )



    }


    //Get all products availabilities
    @GetMapping("/productavailability")
    fun getProductAvailabilities() : ResponseEntity<Vector<ProductQuantityProjection>> {


        var itemVector : Vector<ProductQuantityProjection> = warehouseService.getAllProductAvailabilities()



        return ResponseEntity(itemVector,HttpStatus.OK)




    }





    //If quantity > 0
    //  If product in database -> add quantity
    //  Else -> add new product to database with quantity AND initialize alarm quantity with alarm quantity in DTO (if NULL alarm quantity = quantity/2)
    //Else subtract abs(quantity) to previous quantity
    @PutMapping("/productavailability/{productID}")
    fun updateProductAvailability(

        @NotNull
        @PathVariable
        productID: Long,

        @RequestBody
        updateProductAvailabilityDTO: UpdateProductAvailabilityDTO,

        bindingResult: BindingResult

    ): ResponseEntity<String> {

        if(bindingResult.hasErrors())
            return ResponseEntity(HttpStatus.BAD_REQUEST)


        try {
            warehouseService.addProductToWarehouse(updateProductAvailabilityDTO, productID)
        }catch (e : Exception){
            //TODO use constant strings...
            if(e.message == "Not enough items in warehouses"){

                //TODO send errors to internal network
                sendErrorToInternalNetwork(e)

                return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)
            }
            else if(e.message == "Bad updateProductAvailabilityDTO"){

                sendErrorToInternalNetwork(e)

                return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)

            }

        }

        return ResponseEntity(HttpStatus.OK)




    }

    //TODO send errors to internal network
    private fun sendErrorToInternalNetwork(e: Exception) {

        println("Sent report to internal network")

    }


}