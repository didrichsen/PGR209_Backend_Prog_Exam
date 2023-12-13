# Backend Exam 2023 API Documentation

Welcome to our REST API documentation for our order system for managing orders. 
Our API allows users to create machines and customers, which can then be added to orders.

The order system is populated with a lot of data when running main, which can be queried 
with a tool like postman. If so, see endpoint documentation below. 

### Creating a machine 
A machine is composed of subassemblies, and each subassembly consists of various parts. An
already designed machine can be included in multiple orders through order lines.

However, for a new machine, the process involves creating parts, subassemblies, 
and finally, the machine – in that specific order. Each machine is aware of its subassemblies, 
and subassemblies are linked to their respective parts.

Worth noting, if a Machine is deleted, its underlying subassemblies and parts are deleted in that 
order.

Our Order system is designed to receive a list of ids when relying on **Request objects**. 

### MachineRequest Object

{
"machineName": "your_machine_name",
"price": 1000,
"subassemblyIds": [1, 2, 3]
}

### SubassemblyRequest Object

{
"subassemblyName": "your_subassembly_name",
"partIds": [1, 2, 3]
}

### Creating an order 
An order consists of order lines, each containing a single machine. 
An order has one customer, and it may have multiple order lines. 
A customer must have an address associated with them to be linked to an order. 
If an order is deleted, 
its underlying order lines will also be deleted. Customers, on the other hand, persist in the system even if 
they have no associated orders. A customer can have multiple orders, but an order line can 
only be part of one order. 

An Order is created by sending an OrderRequest Object.

#### OrderRequest Object
{
"customerId": 1,
"orderLineIds": [1,2,3,4]
}

##### Order Object
{
"orderId": 1,
"orderDate": "2023-12-13T12:34:56",
"totalPrice": 14999,
"customer": {
  Customer object properties go here
},
"orderLines": [{OrderLine Object, OrderLine Object}]
}

### Customer
Customers can be created without specifying an address, only email and name is required.
However, as previously mentioned, they must have a registered address before being associated with any orders.
It's worth noting that a customer can have multiple addresses associated with their profile.
The creation of a customer involves sending a Customer Object through the designated API endpoint.

#### Customer Object

{
"customerId": 0,
"customerName": "John Doe",
"email": "john.doe@example.com",
"orders": [],
"addresses": []
}

## Service Layer

Service layer is to a great extent making use records when sending result to controller. 

OrderResult is used when sending info when something is created or updated: 

{
"success": true,
"errorMessage": null,
"createdObject": { }
}

{
"success": true,
"errorMessage": "A machine needs to at least have one subassembly",
"createdObject": null
}

DeleteResultIds is used when deleting most of the objects. Returning a list of Ids if the object is in use and cant be deleted: 

{
"success": false,
"error": ""Can't delete machine. Machine placed in order lines.",
"related_ids": [1,2,3]
}

DeleteResultObject is used when deleting an order. We wanted to return deleted order lines instead of just ids. 

{
"Success": true,
"message": "Sample message",
"error": null"
"deletedObjects": [{} // List of order line objects that's been deleted]
}

## Endpoints documentation

All endpoints expect Json Request Body and all endpoints return Json objects. 

## AddressController

| Operation               | Endpoint                   | Description                                                   | Request Body                         | Success Response                                      | Error Response                                |
|-------------------------|----------------------------|---------------------------------------------------------------|--------------------------------------|-------------------------------------------------------|----------------------------------------------|
| Create Address          | POST /api/address           | Create a new address.                                         | Address object                       | 201 Created with the created address                  | 400 Bad Request with an error message        |
| Get Address by ID       | GET /api/address/{id}       | Retrieve an address by its ID.                                | Path Parameter: {id}                 | 200 OK with the retrieved address                     | 404 Not Found if the address does not exist  |
| Get Addresses by Page   | GET /api/address/page/{pageNumber} | Retrieve a list of addresses based on the page number.        | Path Parameters: {pageNumber}        | 200 OK with a list of addresses                       | None                                         |
| Delete Address          | DELETE /api/address/{id}    | Delete an address by its ID.                                  | Path Parameter: {id}                  | 204 No Content if the address is successfully deleted | 400 Bad Request with an error message        |
| Update Address          | PUT /api/address/{id}       | Update adress by its ID and with data from an Address object. | @Path Parameter: {id}, Address Object | 200 OK with updated object                            | 400 Bad Request with an error message. |
## CustomerController

| Operation               | Endpoint                   | Description                                     | Request Body            | Success Response                        | Error Response                                |
|-------------------------|----------------------------|-------------------------------------------------|-------------------------|------------------------------------------|----------------------------------------------|
| Create Customer         | POST /api/customer           | Create a new customer.                          | Customer object         | 201 Created with the created customer    | 400 Bad Request with an error message        |
| Get Customer by ID      | GET /api/customer/{id}       | Retrieve a customer by their ID.               | Path Parameters: {id}   | 200 OK with the retrieved customer      | 404 Not Found if the customer does not exist  |
| Get Customers by Page   | GET /api/customer/page/{pageNumber} | Retrieve a list of customers based on the page number. | Path Parameters: {pageNumber} | 200 OK with a list of customers     | None                                         |
| Add Address to Customer | POST /api/customer/{customerId}/add/{addressId} | Add an address to a customer.        | Path Parameters: {customerId}, {addressId} | 200 OK with the updated customer        | 400 Bad Request with an error message        |
| Delete Address from Customer | DELETE /api/customer/{customerId}/remove/{addressId} | Remove an address from a customer. | Path Parameters: {customerId}, {addressId} | 200 OK with the updated customer    | 400 Bad Request with an error message        |
| Delete Customer         | DELETE /api/customer/{id}    | Delete a customer by their ID.                | Path Parameters: {id}   | 204 No Content if the customer is successfully deleted | 400 Bad Request with an error message        |
| Update Customer         | PUT /api/customer/update/{customerId} | Update customer details.           | Path Parameters: {customerId} | 200 OK with the updated customer        | 400 Bad Request with an error message        |

## MachineController

| Operation               | Endpoint                   | Description                                     | Request Body            | Success Response                        | Error Response                                |
|-------------------------|----------------------------|-------------------------------------------------|-------------------------|------------------------------------------|----------------------------------------------|
| Create Machine          | POST /api/machine            | Create a new machine.                           | MachineRequest object   | 201 Created with the created machine    | 400 Bad Request with an error message        |
| Get Machine by ID       | GET /api/machine/{id}        | Retrieve a machine by its ID.                  | Path Parameters: {id}   | 200 OK with the retrieved machine      | 404 Not Found if the machine does not exist   |
| Get Machines by Page    | GET /api/machine/page/{pageNumber} | Retrieve a list of machines based on the page number. | Path Parameters: {pageNumber} | 200 OK with a list of machines     | None                                         |
| Delete Machine          | DELETE /api/machine/{id}     | Delete a machine by its ID.                    | Path Parameters: {id}   | 204 No Content if the machine is successfully deleted | 400 Bad Request with an error message        |
| Update Machine          | PUT /api/machine/update/{machineId} | Update machine details.              | Path Parameters: {machineId} | 200 OK with the updated machine        | 400 Bad Request with an error message        |


## OrderController

| Operation               | Endpoint                   | Description                                     | Request Body            | Success Response                        | Error Response                                |
|-------------------------|----------------------------|-------------------------------------------------|-------------------------|------------------------------------------|----------------------------------------------|
| Create Order            | POST /api/order              | Create a new order.                             | OrderRequest object     | 201 Created with the created order       | 400 Bad Request with an error message        |
| Get Order by ID         | GET /api/order/{id}          | Retrieve an order by its ID.                    | Path Parameters: {id}   | 200 OK with the retrieved order          | 404 Not Found if the order does not exist    |
| Get Orders by Page      | GET /api/order/page/{pageNumber} | Retrieve a list of orders based on the page number. | Path Parameters: {pageNumber} | 200 OK with a list of orders         | None                                         |
| Update Order            | PUT /api/order/update/{orderId} | Update order details.                         | Path Parameters: {orderId}, Order Object | 200 OK with updated object                            | 400 Bad Request with an error message. |
| Delete Order            | DELETE /api/order/{orderId}  | Delete an order by its ID.                      | Path Parameters: {orderId} | 204 No Content if the order is successfully deleted | 400 Bad Request with an error message        |


## OrderLineController

| Operation               | Endpoint                   | Description                                     | Request Body            | Success Response                        | Error Response                                |
|-------------------------|----------------------------|-------------------------------------------------|-------------------------|------------------------------------------|----------------------------------------------|
| Create OrderLine        | POST /api/order-line/{id}    | Create a new order line for a given order.     | Path Parameters: {id}   | 201 Created with the created order line | 400 Bad Request with an error message        |
| Delete OrderLine        | DELETE /api/order-line/{id}  | Delete an order line by its ID.                 | Path Parameters: {id}   | 204 No Content if the order line is successfully deleted | 400 Bad Request with an error message        |
| Get OrderLine by ID     | GET /api/order-line/{id}     | Retrieve an order line by its ID.              | Path Parameters: {id}   | 200 OK with the retrieved order line    | 404 Not Found if the order line does not exist |
| Get OrderLines by Page  | GET /api/order-line/page/{pageNumber} | Retrieve a list of order lines based on the page number. | Path Parameters: {pageNumber} | 200 OK with a list of order lines   | None                                         |
| Update OrderLine        | PUT /api/order-line/update/{orderLineId} | Update order line details.              | Path Parameters: {orderLineId}, OrderLine Object | 200 OK with updated object                            | 400 Bad Request with an error message        |

## PartController

| Operation               | Endpoint                   | Description                                     | Request Body            | Success Response                        | Error Response                                |
|-------------------------|----------------------------|-------------------------------------------------|-------------------------|------------------------------------------|----------------------------------------------|
| Create Part             | POST /api/part               | Create a new part.                             | Part object             | 201 Created with the created part        | 400 Bad Request with an error message        |
| Get Parts by Page       | GET /api/part/page/{pageNumber} | Retrieve a list of parts based on the page number. | Path Parameters: {pageNumber} | 200 OK with a list of parts           | None                                         |
| Get Part by ID          | GET /api/part/{id}           | Retrieve a part by its ID.                     | Path Parameters: {id}   | 200 OK with the retrieved part          | 404 Not Found if the part does not exist    |
| Delete Part             | DELETE /api/part/{id}        | Delete a part by its ID.                       | Path Parameters: {id}   | 204 No Content if the part is successfully deleted | 400 Bad Request with an error message        |
| Update Part             | PUT /api/part/update/{partId} | Update part details.                      | Path Parameters: {partId}, Part Object | 200 OK with updated object                            | 400 Bad Request with an error message        |

## SubassemblyController

| Operation               | Endpoint                   | Description                                     | Request Body            | Success Response                        | Error Response                                |
|-------------------------|----------------------------|-------------------------------------------------|-------------------------|------------------------------------------|----------------------------------------------|
| Create Subassembly      | POST /api/subassembly       | Create a new subassembly.                       | SubassemblyRequest      | 201 Created with the created subassembly | 400 Bad Request with an error message        |
| Get Subassembly by ID   | GET /api/subassembly/{id}  | Retrieve a subassembly by its ID.               | Path Parameters: {id}   | 200 OK with the retrieved subassembly   | 404 Not Found if the subassembly does not exist |
| Get Subassemblies by Page | GET /api/subassembly/page/{pageNumber} | Retrieve a list of subassemblies based on the page number. | Path Parameters: {pageNumber} | 200 OK with a list of subassemblies   | None                                         |
| Delete Subassembly      | DELETE /api/subassembly/{id} | Delete a subassembly by its ID.                 | Path Parameters: {id}   | 204 No Content if the subassembly is successfully deleted | 400 Bad Request with an error message        |
| Update Subassembly      | PUT /api/subassembly/{subassemblyId} | Update subassembly details.                | Path Parameters: {subassemblyId}, Subassembly Object | 200 OK with updated object                            | 400 Bad Request with an error message        |
