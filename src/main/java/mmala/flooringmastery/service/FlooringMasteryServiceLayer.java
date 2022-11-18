/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.service;

import mmala.flooringmastery.dao.FlooringMasteryException;
import mmala.flooringmastery.dto.Order;
import mmala.flooringmastery.dto.Product;
import mmala.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author 18437
 */
public interface FlooringMasteryServiceLayer {
    
    BigDecimal calculateMaterialCost(BigDecimal area,BigDecimal costPerSquareFoot);
    
    BigDecimal calculateLaborCost(BigDecimal area,BigDecimal laborCostPerSquareFoot);
    
    BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost,BigDecimal taxRate);
    
    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);
    
    String createOrderFileNameFromDate(LocalDate date);
    
    void checkOrderFileExists (String orderfileName)throws FlooringMasteryNoOrdersException;
    
    List <Order> getAllOrders (String fileWithDate) throws FlooringMasteryException;
    
    List<Order> getOrderList (LocalDate wantedOrderDate) throws FlooringMasteryNoOrdersException, FlooringMasteryException;
    
    LocalDate checkDateIsInFuture(LocalDate orderDate) throws FlooringMasteryDateErrorException;
    
    void validateCustomerName(String customerNameInput)throws FlooringMasteryCustomerNameErrorException;
    
    void checkStateAgainstTaxFile(String stateAbbreviationInput)throws FlooringMasteryException, FlooringMasteryStateNotFoundException;
    
     List <Product> getAllProducts() throws FlooringMasteryException;
    
     void checkProductTypeAgainstProductsFile (String productTypeInput) throws FlooringMasteryException, FlooringMasteryProductTypeNotFoundException;
     
     void checkAreaOverMinOrder (BigDecimal areaInput) throws FlooringMasteryAreaBelowMinException;
     
     Product getProduct (String productType) throws FlooringMasteryException;
     
     Tax getTax(String stateAbbreviationInput)throws FlooringMasteryException;
     
     Order createNewOrderIfRequired (String verifyOrder, LocalDate orderDateInput, int orderNumber, String customerNameInput, String stateAbbreviationInput, BigDecimal taxRate, String productTypeInput,
                    BigDecimal areaInput, BigDecimal CostPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total)
             throws FlooringMasteryException ;
     
     int generateNewOrderNum()throws FlooringMasteryException;
     
     Order removeOrderIfConfirmed(String removeConfirmation, String orderFile, int orderNumber) throws FlooringMasteryException, FlooringMasteryOrderFileNotExistException;
     
     int checkOrderNumExists(String orderFileName, int orderNumberInput) throws FlooringMasteryException, FlooringMasteryNoOrderNumException;
     
     Order getOrder(String fileName, int orderNum) throws FlooringMasteryException ;
     
      String checkForEdit (String updatedInfo);
      
      Order updateOrderCustomerNameIfRequired(String updatedCustomerName, Order orderToEdit);
      
      Order updateOrderStateIfRequired(String updatedState, Order orderToEdit);
      
      Order updateOrderProductTypeIfRequired(String updatedProductType, Order orderToEdit);
      
      Order updateOrderAreaIfRequired(BigDecimal updatedArea, Order orderToEdit);
      
      BigDecimal checkForEditBigDecimal (String updatedInfo);
      
      Order updateOrderCalculations(Order editedOrder) throws FlooringMasteryException;
      
      Order editOrderIfConfirmed(String toBeEdited, String orderFile, Order updatedOrder) throws FlooringMasteryException;
      
      String getCustomerNamePlaceHolder(String customerNameInput);
      
      void exportAllData() throws FlooringMasteryException;
}


