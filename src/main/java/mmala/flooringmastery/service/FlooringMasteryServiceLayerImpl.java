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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mmala.flooringmastery.dao.FlooringMasteryOrderDao;
import mmala.flooringmastery.dao.FlooringMasteryProductDao;
import mmala.flooringmastery.dao.FlooringMasteryTaxDao;

/**
 *
 * @author 18437
 */
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {
    //Service layer is responsbie for the business logic of an application.
        
    private FlooringMasteryOrderDao orderDao;
    private FlooringMasteryProductDao productDao;
    private FlooringMasteryTaxDao taxDao;
    
    private final String customerNamePlaceHolder = "#*~";

    public FlooringMasteryServiceLayerImpl(FlooringMasteryOrderDao orderDao, FlooringMasteryProductDao productDao, FlooringMasteryTaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }
    
    //LIST ORDERS 
    
    @Override
    public String createOrderFileNameFromDate(LocalDate date){
        //date will be in the format YYYY-MM-DD
        //Need to convert this to the format: MMDDYYYY
        
        //Make a formatter with the required format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String dateFormatted = date.format(formatter);
        
        return "Orders_"+dateFormatted+".txt";
    }
    
    @Override
    public void checkOrderFileExists (String orderFileName)throws FlooringMasteryNoOrdersException{
       
        String [] orderFiles = orderDao.listAllOrderFiles();
        String orderfile = null;
       
        for (String orderFile : orderFiles){
            if (orderFileName.equals(orderFile)) {
                orderfile = orderFileName;
                if (orderfile!=null) {
                    break;
                }
            }
        }
        
        if (orderfile == null){
            throw new FlooringMasteryNoOrdersException (
            "ERROR: no orders exist for that date.");
        }
    }
        
    @Override
    public List <Order> getAllOrders (String fileWithDate) throws FlooringMasteryException {  
        
        return orderDao.getAllOrdersForADate(fileWithDate);
    }

    @Override
    public List<Order> getOrderList (LocalDate wantedOrderDate) throws FlooringMasteryNoOrdersException, FlooringMasteryException{
        String fileWithDate = createOrderFileNameFromDate(wantedOrderDate);
        checkOrderFileExists(fileWithDate);
        return getAllOrders(fileWithDate);
        
    }
    
    //ADD AN ORDER   
    // date
    @Override
    public LocalDate checkDateIsInFuture(LocalDate orderDate) throws FlooringMasteryDateErrorException{
        //Check if the date is in the future
        //Get the date for now
        LocalDate dateNow = LocalDate.now();
        //If the order date is before the date now
        if (orderDate.compareTo(dateNow)<0){
            throw new FlooringMasteryDateErrorException (
            "ERROR: Date must be in the future.");
        }
        return orderDate;
    }    
    
    //name
    
    @Override
    public void validateCustomerName(String customerNameInput)throws FlooringMasteryCustomerNameErrorException {
            if (customerNameInput.isBlank()  || customerNameInput.isEmpty()) {
            throw new FlooringMasteryCustomerNameErrorException (
                    "ERROR: customer name cannot be blank.");
                }
    }
    
    @Override 
    public String getCustomerNamePlaceHolder(String customerNameInput) {
        // If the name contains any ,,,,,, swap them out for #*~ 
        return customerNameInput.replace(",", customerNamePlaceHolder);
    }
    
    //State abbreviation
    @Override
    public void checkStateAgainstTaxFile(String stateAbbreviationInput)throws FlooringMasteryException, FlooringMasteryStateNotFoundException {
        List<Tax> taxesList = taxDao.getAllTaxes();
        String stateAbbreviation = null;
       for (Tax tax:taxesList) {
           if (tax.getStateAbbr().equalsIgnoreCase(stateAbbreviationInput)) {
               stateAbbreviation = tax.getStateAbbr();
               if (stateAbbreviation!=null){
                   break;
               }
           }
       }
       if (stateAbbreviation == null) {
           throw new FlooringMasteryStateNotFoundException (
           "ERROR: we cannot sell to " + stateAbbreviation + ".");
       }
    }
    //Product
    @Override
    public List <Product> getAllProducts()throws FlooringMasteryException {
        return productDao.getAllProducts();
    }
    
    @Override
    public void checkProductTypeAgainstProductsFile (String productTypeInput) throws FlooringMasteryException, FlooringMasteryProductTypeNotFoundException {
        List<Product> productList = productDao.getAllProducts();
        String productType = null;
        
        for (Product product:productList) {
            if (product.getProductType().equalsIgnoreCase(productTypeInput)) {
                productType = product.getProductType();
            }
        }
        if (productType == null) {
            throw new FlooringMasteryProductTypeNotFoundException (
                    "ERROR: " + productTypeInput + " is not in the product list.");
        }
    }
    
    @Override 
    public Product getProduct (String productType) throws FlooringMasteryException{
        return productDao.getProduct(productType);
    }
    
    //Calculations
    @Override
    public BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot) {
        return area.multiply(costPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFoot) {
        return area.multiply(laborCostPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate) {
        return (materialCost.add(laborCost)).multiply(taxRate.divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        return materialCost.add(laborCost).add(tax).setScale(2, RoundingMode.HALF_UP);
    }

    //Area
    @Override
    public void checkAreaOverMinOrder (BigDecimal areaInput) throws FlooringMasteryAreaBelowMinException {
        //if the area input is less than 100 sq ft, throw error
        if (areaInput.compareTo(new BigDecimal("100"))<0) {
            throw new FlooringMasteryAreaBelowMinException (
            "ERROR: the area is below the minimum order");
        }
     }
    
    //Tax
    
    @Override
    public Tax getTax(String stateAbbreviationInput)throws FlooringMasteryException {
        return taxDao.getTax(stateAbbreviationInput);
    }
    
    //create order and add it to memory
    @Override
    public int generateNewOrderNum()throws FlooringMasteryException {

        List<Integer> orderNums = orderDao.getAllOrderNums();
        int maxOrderNum = Collections.max(orderNums);
        return maxOrderNum + 1;
    }

    
   @Override 
    public Order createNewOrderIfRequired (String verifyOrder, LocalDate orderDateInput, int orderNumber, String customerNameInput, String stateAbbreviationInput, BigDecimal taxRate, String productTypeInput,
                    BigDecimal areaInput, BigDecimal CostPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total) 
    throws FlooringMasteryException {
      
        Order newOrder;
       if (verifyOrder.equalsIgnoreCase("Y")){
           newOrder = new Order(orderNumber);
           newOrder.setCustomerName(customerNameInput);
           newOrder.setStateAbbr(stateAbbreviationInput);
           newOrder.setTaxRate(taxRate);
           newOrder.setProductType(productTypeInput);
           newOrder.setArea(areaInput);
           newOrder.setCostPerSquareFoot(CostPerSquareFoot);
           newOrder.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
           newOrder.setMaterialCost(materialCost);
           newOrder.setLaborCost(laborCost);
           newOrder.setTax(tax);
           newOrder.setTotal(total);
          
           String newOrderFileName = createOrderFileNameFromDate(orderDateInput);
           String [] orderFiles = orderDao.listAllOrderFiles();
           String fileExists = null;
          
           for (String orderFile : orderFiles){
               if (newOrderFileName.equals(orderFile)) {
                fileExists = newOrderFileName;
                Order orderCreated = orderDao.addOrderToExistingFile(fileExists, orderNumber, newOrder);
                return orderCreated;          
                }
            }
        
        if (fileExists == null){
            Order orderCreated = orderDao.addOrderToNewFile(newOrderFileName, orderNumber, newOrder);
            return orderCreated;
        }
        }
        //If user doesn't want to save order
        return null;
       }
    
     
//Edit order   
    @Override
    public String checkForEdit (String updatedInfo) {
        
        if (updatedInfo == null 
                || updatedInfo.trim().length()==0
                || updatedInfo.isEmpty()
                || updatedInfo.isBlank()) {
            return null;
        } else {
           
            return updatedInfo;
        }
    }
    
    @Override
    public BigDecimal checkForEditBigDecimal (String updatedInfo) {
        
        if (updatedInfo == null 
                || updatedInfo.trim().length()==0
                || updatedInfo.isEmpty()
                || updatedInfo.isBlank()) {
            return null;
        } else {
            
            return new BigDecimal(updatedInfo);
        }
    }
    
    @Override
    public Order updateOrderCustomerNameIfRequired(String updatedCustomerName, Order orderToEdit){
       
        if (updatedCustomerName == null) {
            return orderToEdit;
        } else {
            
            orderToEdit.setCustomerName(updatedCustomerName);
            return orderToEdit;
        }
    }
    @Override
    public Order updateOrderStateIfRequired(String updatedState, Order orderToEdit){
       
        if (updatedState == null) {
            return orderToEdit;
        } else {
           
            orderToEdit.setStateAbbr(updatedState);
            return orderToEdit;
        }
    }

    @Override
    public Order updateOrderProductTypeIfRequired(String updatedProductType, Order orderToEdit){
        
        if (updatedProductType == null) {
            return orderToEdit;
        } else {
           
            orderToEdit.setProductType(updatedProductType);
            return orderToEdit;
        }
    }    
    
    @Override
    public Order updateOrderAreaIfRequired(BigDecimal updatedArea, Order orderToEdit){
        
        if (updatedArea == null) {
            return orderToEdit;
        } else {
            
            orderToEdit.setArea(updatedArea);
            return orderToEdit;
        }
    }
    
    @Override
    public Order updateOrderCalculations(Order editedOrder) throws FlooringMasteryException {
        //get the updated information and use it to obtain and update the other values.
        //if the values haven't actually been updated, they will just return the same old value.
        BigDecimal updatedTaxRate = null;
        BigDecimal updatedCostPerSquareFoot = null;
        BigDecimal updatedLaborCostPerSquareFoot = null;
        
        //if the state is not null, then get the new tax rate
        String updatedStateAbbreviation = editedOrder.getStateAbbr();
        if (updatedStateAbbreviation != null) {
            Tax updatedTaxObj = taxDao.getTax(updatedStateAbbreviation);
            updatedTaxRate = updatedTaxObj.getTaxRate();
        }
        
        String updatedProductType = editedOrder.getProductType();
        if (updatedProductType != null) {
            Product updatedProduct = productDao.getProduct(updatedProductType);
            updatedCostPerSquareFoot = updatedProduct.getCostPerSquareFoot();
            updatedLaborCostPerSquareFoot = updatedProduct.getLaborCostPerSquareFoot();
            BigDecimal updatedArea = editedOrder.getArea();
            //Update math
            BigDecimal updatedMaterialCost = this.calculateMaterialCost(updatedArea, updatedCostPerSquareFoot);
            BigDecimal updatedLaborCost = this.calculateLaborCost(updatedArea, updatedLaborCostPerSquareFoot);
            BigDecimal updatedTax = this.calculateTax(updatedMaterialCost, updatedLaborCost, updatedTaxRate);
            BigDecimal updatedTotal = this.calculateTotal(updatedMaterialCost, updatedLaborCost, updatedTax);
            editedOrder.setMaterialCost(updatedMaterialCost);
            editedOrder.setLaborCost(updatedLaborCost);
            editedOrder.setTax(updatedTax);
            editedOrder.setTotal(updatedTotal);
        }
       
       return editedOrder;
    }
    
    @Override
    public Order editOrderIfConfirmed(String toBeEdited, String orderFile, Order updatedOrder) throws FlooringMasteryException{
        //if edit confirmation is Y, edit the order and save
        if (toBeEdited.equalsIgnoreCase("Y")) {
            Order editedOrder = orderDao.editOrder(orderFile, updatedOrder);
            return editedOrder;
        } 
        //otherwise, return null
        return null;
    }

    //Remove order
    @Override
    public Order removeOrderIfConfirmed(String removeConfirmation, String orderFile, int orderNumber) throws FlooringMasteryException, FlooringMasteryOrderFileNotExistException {
        
        if (removeConfirmation.equalsIgnoreCase("Y")) {
            Order removedOrder = orderDao.removeOrder(orderFile, orderNumber);
            removeFileIfEmpty(orderFile);
            return removedOrder;
        }
        
        return null;
    }
    
    private void removeFileIfEmpty (String orderFile) throws FlooringMasteryException, FlooringMasteryOrderFileNotExistException {
        
        ArrayList<Order> orderList = (ArrayList<Order>) this.getAllOrders(orderFile);
        
        if (orderList.isEmpty()) {
            orderDao.deleteOrderFile(orderFile);
        }
    }
    
    @Override
    public int checkOrderNumExists(String orderFileName, int orderNumberInput) throws FlooringMasteryException, FlooringMasteryNoOrderNumException{
        List<Integer> orderNums = orderDao.getAllOrderNumsForADate(orderFileName);
        
        int orderNumFound = 0;
        
        for (Integer orderNum : orderNums){
            if (orderNumberInput == orderNum) {
                
                orderNumFound=orderNum;
                return orderNumFound;
            }
        }
        //if the order number not found, exception thrown.
        if (orderNumFound == 0) {
            throw new FlooringMasteryNoOrderNumException (
            "ERROR: no orders exist for that order number.");
    }
        return 0;
    }
    
    @Override
    public Order getOrder(String fileName, int orderNum) throws FlooringMasteryException {
        return orderDao.getOrder(fileName, orderNum);
    }
      
//Eport all data
    // exports all oeders to txt file


    @Override
    public void exportAllData() throws FlooringMasteryException {
        orderDao.exportAllData();
    }

        
    }
    
    

    

