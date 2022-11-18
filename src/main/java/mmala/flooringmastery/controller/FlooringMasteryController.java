/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.controller;

import mmala.flooringmastery.dao.FlooringMasteryException;
import mmala.flooringmastery.dto.Order;
import mmala.flooringmastery.dto.Product;
import mmala.flooringmastery.dto.Tax;
import mmala.flooringmastery.service.FlooringMasteryAreaBelowMinException;
import mmala.flooringmastery.service.FlooringMasteryCustomerNameErrorException;
import mmala.flooringmastery.service.FlooringMasteryDateErrorException;
import mmala.flooringmastery.service.FlooringMasteryNoOrderNumException;
import mmala.flooringmastery.service.FlooringMasteryNoOrdersException;
import mmala.flooringmastery.service.FlooringMasteryOrderFileNotExistException;
import mmala.flooringmastery.service.FlooringMasteryProductTypeNotFoundException;
import mmala.flooringmastery.service.FlooringMasteryServiceLayer;
import mmala.flooringmastery.service.FlooringMasteryStateNotFoundException;
import mmala.flooringmastery.ui.FlooringMasteryView;
import mmala.flooringmastery.ui.UserIO;
import mmala.flooringmastery.ui.UserIOConsoleImpl;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author 18437
 */
public class FlooringMasteryController {
    private UserIO io = new UserIOConsoleImpl();
    private FlooringMasteryView view;
    private FlooringMasteryServiceLayer service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayer service) {
        this.view = view;
        this.service = service;
    }
    

public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        try{
            while (keepGoing) {
                menuSelection = getMenuSelection();
                
                switch (menuSelection) {
                        case 1:
                            listOrders();
                            break;
                        case 2:
                            createOrder();
                            break;
                        case 3:
                            editOrder();
                            break;
                        case 4:
                            removeOrder();
                            break;
                        case 5:
                            exportAllData();
                            break;
                        case 6:
                            keepGoing = false;
                            break;
                        default:
                            unknownCommand();
                }
            }
            exitMessage();
        } catch ( FlooringMasteryException | FlooringMasteryNoOrdersException 
                | FlooringMasteryDateErrorException 
                | FlooringMasteryCustomerNameErrorException 
                | FlooringMasteryStateNotFoundException 
                | FlooringMasteryProductTypeNotFoundException 
                | FlooringMasteryAreaBelowMinException 
                | FlooringMasteryNoOrderNumException 
                | FlooringMasteryOrderFileNotExistException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }


private int getMenuSelection() {
    return view.printMenuAndGetSelection();
}

// DISPLAY ORDERS
private void listOrders() throws FlooringMasteryNoOrdersException, FlooringMasteryException{
    view.displayListOrdersBanner();
    boolean hasErrors = false;
    List<Order> orderList = null;
    do {
        try {
            //prompt user for the date input
            LocalDate wantedOrderDate = view.getOrderDateListOrders();
            //check existing orders for that date  and return list
            orderList = service.getOrderList(wantedOrderDate);
            view.displayOrderListBanner(wantedOrderDate);
            hasErrors = false;
        } catch (DateTimeException | FlooringMasteryNoOrdersException | FlooringMasteryException e) {
            hasErrors = true;
            view.displayErrorMessage(e.getMessage());
        }
    } while (hasErrors);
    view.displayOrderList(orderList);
      

}
// CREATE ORDERS
private void createOrder() throws 
        FlooringMasteryDateErrorException, 
        FlooringMasteryCustomerNameErrorException, 
        FlooringMasteryException, 
        FlooringMasteryStateNotFoundException,
        FlooringMasteryProductTypeNotFoundException,
        FlooringMasteryAreaBelowMinException{
    view.displayCreateOrderBanner();
    boolean hasErrors = false;
    
    do {
        try {
            // prompt user for the date input and check format
            LocalDate orderDateInput = view.getOrderDateCreateOrder();
            //check if the date is in future
            service.checkDateIsInFuture(orderDateInput);

            //prompt user for the name input
            String customerNameInput = view.getCustomerName();  
            // name can't be empty
            service.validateCustomerName(customerNameInput);
            
            String customerNameInputPh = service.getCustomerNamePlaceHolder(customerNameInput);
            
            //prompt user for thestate abbreviation
            String stateAbbreviationInput = view.getStateAbbreviation();
            //check if that state exists in the taxes file
            service.checkStateAgainstTaxFile(stateAbbreviationInput);
            
            // prompt user to chose product
            List <Product> availableProductsAndPricing = service.getAllProducts();
         
            String productTypeInput = view.displayAvailableProductsAndGetSelection(availableProductsAndPricing);
            //check if the selection is valid
            service.checkProductTypeAgainstProductsFile(productTypeInput);
            Product productSelected = service.getProduct(productTypeInput);
            
            // prompt user to enter area needed
            BigDecimal areaInput = view.getArea();
            //check if the number is over 100 sqft
            service.checkAreaOverMinOrder(areaInput);
            
            //math - MaterialCost, LaborCost, Tax, Total
            BigDecimal materialCost = service.calculateMaterialCost(areaInput, productSelected.getCostPerSquareFoot());
            BigDecimal laborCost = service.calculateLaborCost(areaInput, productSelected.getLaborCostPerSquareFoot());
            
            //get tax rate depending on state abbreviation
            Tax taxObj = service.getTax(stateAbbreviationInput);
            BigDecimal tax = service.calculateTax(materialCost, laborCost, taxObj.getTaxRate());
            
            BigDecimal total = service.calculateTotal(materialCost, laborCost, tax);
            
            //new order number
            int orderNumber = service.generateNewOrderNum();
            
            //order summary view and ask id user wants to place it
            String verifyOrder = view.displayNewOrderSummary(orderDateInput, orderNumber, customerNameInput, stateAbbreviationInput,
                    productTypeInput, areaInput, materialCost, laborCost, tax, total);
            
            //if user selects "Y", order is placed and added to in-memory storage, if "N" -> return to main menu 
                  
            service.createNewOrderIfRequired(verifyOrder, orderDateInput, orderNumber, customerNameInputPh, stateAbbreviationInput, 
                    tax, productTypeInput, areaInput, materialCost, laborCost, materialCost, laborCost, tax, total);

            view.displayCreateSuccessBanner(verifyOrder);
            
            hasErrors = false;
        } catch (FlooringMasteryDateErrorException 
                | FlooringMasteryCustomerNameErrorException 
                | FlooringMasteryException | FlooringMasteryStateNotFoundException 
                | FlooringMasteryProductTypeNotFoundException 
                | FlooringMasteryAreaBelowMinException e) {
            hasErrors = true;
            view.displayErrorMessage(e.getMessage());
        }
    } while(hasErrors);
}

// Remove order
private void removeOrder() throws FlooringMasteryNoOrdersException, FlooringMasteryException, FlooringMasteryNoOrderNumException, FlooringMasteryOrderFileNotExistException{
    view.displayRemoveOrderBanner();
    boolean hasErrors = false;
    do {
        //prompt user to enter date and then order number
        LocalDate orderDateInput = view.getOrderDateRemoveOrder();
        int orderNumberInput = view.getOrderNumberRemoveOrder();
        try {
            
            String orderFileName = service.createOrderFileNameFromDate(orderDateInput);
            service.checkOrderFileExists(orderFileName);

            
            int orderNumToRemove = service.checkOrderNumExists(orderFileName, orderNumberInput);
            //if the order doesn't exist exeption will be thrown, if exists - delete order
            Order orderToRemove = service.getOrder(orderFileName, orderNumToRemove);

            //Display the information for the order we are removing
            view.displayOrderInformation(orderDateInput, orderToRemove);

            //conferm order deletion 
            String removeConfirmation = view.getRemoveConfirmation();

            ////if yes, remove the order, if no, return to menu
            Order removedOrder = service.removeOrderIfConfirmed(removeConfirmation, orderFileName, orderNumberInput);

            view.displayRemoveSuccessBanner(removedOrder);
            hasErrors = false;
        } catch (FlooringMasteryNoOrdersException | FlooringMasteryException | FlooringMasteryNoOrderNumException e) {
            hasErrors = true;
            view.displayErrorMessage(e.getMessage());
        }
    } while (hasErrors);
}


private void exportAllData() throws FlooringMasteryException {
   // load data into export file, no messages, but file changes
    boolean hasErrors = false;
    view.displayExportAllDataBanner();
    try {
        service.exportAllData();
    } catch (FlooringMasteryException e) {
        hasErrors=true;
        view.displayErrorMessage(e.getMessage());
    } 
}
// if wrong menu selection
private void unknownCommand() {
    view.displayUnknownCommandBanner();
}

private void exitMessage() {
    view.displayExitBanner();
}


private void editOrder() throws 
        FlooringMasteryNoOrdersException, 
        FlooringMasteryNoOrderNumException, 
        FlooringMasteryStateNotFoundException,
        FlooringMasteryProductTypeNotFoundException,
        FlooringMasteryAreaBelowMinException{
    view.displayEditOrderBanner();
    boolean hasErrors = false;
    do {
        // Prompt user for date and than for order number
        LocalDate orderDateInput = view.getOrderDateEditOrder();
        int orderNumberInput = view.getOrderNumberEditOrder();
        
        try {
           // check if this order exists
            String orderFileName = service.createOrderFileNameFromDate(orderDateInput);
            service.checkOrderFileExists(orderFileName);

            //check if this order exists exists in the file
            int orderNumToEdit = service.checkOrderNumExists(orderFileName, orderNumberInput);
            //If doesn't exist, throw an exception, if yes -> get the order.
            Order orderToEdit = service.getOrder(orderFileName, orderNumToEdit);
            
           // Edit the name
            String updatedCustomerName = view.displayAndGetEditCustomerName(orderToEdit);
            updatedCustomerName = service.checkForEdit(updatedCustomerName);
            Order updatedOrder = service.updateOrderCustomerNameIfRequired(updatedCustomerName, orderToEdit);
                        
            //Edit the state
            String updatedStateAbbreviation = view.displayAndGetEditState(orderToEdit);
            updatedStateAbbreviation = service.checkForEdit(updatedStateAbbreviation);
            
            if (updatedStateAbbreviation!=null){
                service.checkStateAgainstTaxFile(updatedStateAbbreviation);
            }
            updatedOrder = service.updateOrderStateIfRequired(updatedStateAbbreviation, orderToEdit);
            
            //Edit product type
            String updatedProductType = view.displayAndGetEditProductType(orderToEdit);
            updatedProductType = service.checkForEdit(updatedProductType);
            
            if (updatedProductType!=null) {
                service.checkProductTypeAgainstProductsFile(updatedProductType);
            }
            updatedOrder = service.updateOrderProductTypeIfRequired(updatedProductType, orderToEdit);
            
            //Edit the area
            String updatedAreaString = view.displayAndGetEditArea(orderToEdit);
            BigDecimal updatedArea = service.checkForEditBigDecimal(updatedAreaString);
           // check if new number is 100 sqt or more
            if (updatedArea!=null) {
                service.checkAreaOverMinOrder(updatedArea);
            }
            updatedOrder = service.updateOrderAreaIfRequired(updatedArea, orderToEdit);
            
            //Update numbers
            updatedOrder = service.updateOrderCalculations(updatedOrder);
            
            //Display a summary of the new order info
            view.displayEditedOrderSummary(orderDateInput,orderToEdit);
            
            // Save new info or no
            String toBeEdited = view.getSaveConfirmation();
            
            //Save order with new info if confirmed
            Order editedOrder = service.editOrderIfConfirmed(toBeEdited,orderFileName,updatedOrder);
                        
            view.displayEditSucessBanner(editedOrder);
            
            hasErrors = false;
               } catch (FlooringMasteryNoOrdersException 
                       | FlooringMasteryNoOrderNumException 
                       | FlooringMasteryException | FlooringMasteryStateNotFoundException
                       | FlooringMasteryProductTypeNotFoundException
                       | FlooringMasteryAreaBelowMinException e) {
                   hasErrors = true;
                   view.displayErrorMessage(e.getMessage());
               }
           } while(hasErrors);
}

}
