# WA2-FinalProject-WarehouseService

##Introduction and general info

All routes names are very arbitrary, it is so because I guess they need to be configured properly
in the context of the whole architecture. This is up to you.
Moreover in this code, because of the @RequestMapping(/warehouses) everything starts with
/warehouses (for example the first endpoint is triggered with /warehouses/warehouses)

All /warehouse endpoints that are listed in the specifications are basically not implemented
It is really confusing because these functionalities have nothing to do with what actually the service
is supposed to do.
So do what you want with these endpoints, but know that they do not really make sense in
this context...

The main functionalities to be implemented for the service to work are :
- 1
POST/confirmorder/{orderId} : not implemented
POST/cancelorder/{orderID} : not implemented
that support the rollback of the placeOrder transactions
  - 2
  Fetching the admin emails from the service that owns the authentication service. Also putting the code 
    responsible for fetching and sending the emails inside a coroutine would be nice, because otherwise the 
    /placeOrder route takes very long to complete.
    -> implementing mailServiceimpl.retrieveAdminEmails()
    
- 3
the logic to send internal errors -> implementing WarehouseController.sendErrorToInternalNetwork




##Routes

-  GET/warehouses : getAllWarehouses
- GET/warehouses/{warehouseID} : not implemented
- POST/warehouses/{warehouseID} : not implemented
- PUT/warehouses/{warehouseID} : not implemented
- PATCH//warehouses/{warehouseID} : not implemented
- DELETE /warehouses/{warehouseID} : not implemented

- GET/productavailability/{productID} : getProductAvailability
- GET/productavailability : getAllProductAvailabilities
- PATCH/updateAlarmlevel : updateAlarmLevel
- PUT/placeOrder : placeOrderInWarehouse
- POST/productavailability/add : addQuantityTOProductInWarehouse
  -POST/product/add : addProductToWarehouse
- POST/confirmorder/{orderId} : not implemented
- POST/cancelorder/{orderID} : not implemented



-sendErrorToInternalNetwork


## Functions descrpitions

###GET/warehouses : getAllWarehouses OK
Gets all distinct warehouse IDs

###GET/warehouses/{warehouseID} : NOT IMPLEMENTED
Suggestion: maybe you should return all rows with warehouseId ?

###POST/warehouses/{warehouseID}  NOT IMPLEMENTED
Suggestion: maybe this let you manually add rows to the DB?

###PUT/warehouses/{warehouseID} : not implemented
Suggestion: I don't understand what the professor wants here..., at least in my implementation
this method does not really make sense

###PATCH//warehouses/{warehouseID} : not implemented
No idea

###DELETE /warehouses/{warehouseID} : not implemented
Suggestion: maybe deletes all rows with warehouseID?

###GET/productavailability/{productID} : getProductAvailability OK
Gets the availability of a product: THE SUM IN ALL WAREHOUSES

###GET/productavailability : getAllProductAvailabilities OK
Get all the availabilities for all products: THE SUM IN ALL WAREHOUSES
TO USE TO SHOW ALL AVAILABILITIES, SO THAT YOU DO NOT HAVE TO CALL THE PREVIOUS METHODS FOR EACH PRODUCT

###PATCH/updateAlarmlevel : updateAlarmLevel OK
Updates the alarm level for a row ( a row represents a product inside a warehouse )

###PUT/placeOrder : placeOrderInWarehouse OK
CHECKS if the given amount is available (SUM IN ALL WAREHOUSES)
Then it removes the quantity from the affected rows and if necessary sends the email to the admins
OBTAIN ADMIN EMAIL LIST FUNCTION: NOT IMPLEMENTED !!!!!!!
Then it inserts rows inside OrderInWarehouse Table, one for each row of ProductInWarehouse that has been affected
with the quantity that has been REMOVED from that row's quantity attribute in the previous step
These rows will be used to restore the quantities in productInWarehouse table in case the order is canceled

###POST/productavailability/add : addQuantityTOProductInWarehouse OK
Adds quantity to a product in a warehouse


###POST/product/add : addProductToWarehouse OK
Adds a product to a warehouse -> adds a row in ProductInWarehouseRepository

###POST/confirmorder/{orderId} : not implemented !!!!!!!!!!
Deletes row from OrderInWarehouse table with orderId

###POST/cancelorder/{orderID} : not implemented
RESTORES THE QUANTITY FOR PRODUCTS IN WAREHOUSES FOLLOWING A CANCEL OF AN ORDER
Basically it should fetch all rows with orderId from OrderInWarehouse table, and for
each of these rows it should add the quantity attribute value of the row to the row inside 
ProductInWarehouse table with the specified warehouseId and ProductId






