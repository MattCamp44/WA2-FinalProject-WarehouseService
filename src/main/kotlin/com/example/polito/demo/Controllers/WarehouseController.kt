package com.example.polito.demo.Controllers

import com.example.polito.demo.DTOs.*
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
    fun getAllWarehouses() : ResponseEntity<Vector<Long>> {


        var warehouseIdList : Vector<Long> =  warehouseService.getAllWarehouses()


        println( "Ok" )

        return ResponseEntity<Vector<Long>>(warehouseIdList,HttpStatus.OK)




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


    @PatchMapping("/updateAlarmlevel")
    fun updateAlarmLevel(

        @RequestBody
        updateAlarmLevelDTO: UpdateAlarmLevelDTO

    ,
        bindingResult: BindingResult

    ) : ResponseEntity<String> {

        if(bindingResult.hasErrors())
            return ResponseEntity(HttpStatus.BAD_REQUEST)

        try{

            warehouseService.updateAlarmLevel(updateAlarmLevelDTO)

        }catch (e : Exception){

            if(e.message == "Product not found in warehouse")
                return ResponseEntity(HttpStatus.NOT_FOUND)

            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

        }

        return ResponseEntity(HttpStatus.OK)

    }



    @PutMapping("/placeOrder")
    fun placeOrderInWarehouse(

        @RequestBody
        placeOrderDTO: PlaceOrderDTO,

        bindingResult: BindingResult

    ): ResponseEntity<String> {

        if(bindingResult.hasErrors())
            return ResponseEntity(HttpStatus.BAD_REQUEST)


        try {
            warehouseService.placeOrderInWarehouse( placeOrderDTO )
        }catch (e : Exception){


            sendErrorToInternalNetwork(e)

            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(HttpStatus.OK)




    }


    @PostMapping("/productavailability/add")
    fun addProductAvailability(

        @RequestBody
        addProductQuantityDTO: AddProductQuantityDTO,

        bindingResult: BindingResult


    ): ResponseEntity<String>{
        try {
            warehouseService.addQuantityTOProductInWarehouse(addProductQuantityDTO)
        }catch(e:Exception){

            sendErrorToInternalNetwork(e)
            return ResponseEntity(HttpStatus.NOT_FOUND)

        }

        return ResponseEntity(HttpStatus.OK)

    }



    @PostMapping("/product/add")
    fun addProductToWarehouse(



        @RequestBody
        addProductDTO: AddProductDTO,

        bindingResult: BindingResult



    ){


        try{
            warehouseService.addProductToWarehouse(addProductDTO)
        }
        catch(e:Exception){
            sendErrorToInternalNetwork(e)
        }



    }

    @PostMapping("confirmorder/{orderId}")
    fun confirmOrder( @PathVariable @NotNull orderId : Long ){

        //Removes entry from the backup of orders



    }




    @PostMapping("cancelorder/{orderId}")


    //TODO send errors to internal network
    private fun sendErrorToInternalNetwork(e: Exception) {

        println("Sent report to internal network")

    }





}

