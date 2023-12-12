# Backend Exam 2023 API Documentation

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

### MachineRequest

{
"subassemblyIds": [1, 2, 3],
"machineName": "Sample Machine",
"price": 1500
}