/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.ui;

import mmala.flooringmastery.dto.Order;
import mmala.flooringmastery.dto.Product;
import mmala.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author 18437
 */
public class FlooringMasteryView {
    UserIO io;
    
    private final String customerNamePlaceHolder = "#*~";
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }
            
    
    public int printMenuAndGetSelection() {
        io.print("   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("   * <<Flooring Program>>");
        io.print("   * 1. Display Orders");
        io.print("   * 2. Add an Order");
        io.print("   * 3. Edit an Order");
        io.print("   * 4. Remove an Order");
        io.print("   * 5. Export All Data");
        io.print("   * 6. Quit");
        io.print("   *");
        io.print("   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        return io.readInt("Please select from the choices above", 1, 6);
    }
    
    // LIST ORDER OPTION    
    
    public LocalDate getOrderDateListOrders(){
        io.print("What date would you like to display the orders for? ");
        return io.readDate("Please enter a date in the format YYYY-MM-DD");
    }
    
    public void displayOrderListBanner(LocalDate orderDate) {
        io.print("========= all Orders for " + orderDate + " =========");
    }
    
        public void displayListOrdersBanner() {
        io.print("=== List Orders ===");
}
    
    public String displayOrderList(List<Order> orderList) {
        String format =  "%16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s";
        String headings = String.format(format, 
                "Customer name",
                "State",
                "Tax rate",
                "Product",
                "Area",
                "Cost per square foot ($)",
                "Labor cost per square foot($)",
                "Material cost($)",
                "Labor cost($)",
                "Tax ($)",
                "Total ($)");
        io.print(headings);
        
        io.print("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Order currentOrder : orderList) {
                     
            String orderInfo = String.format(
                    "%16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s",
                    currentOrder.getCustomerName(),
                    currentOrder.getStateAbbr(),
                    currentOrder.getProductType(),
                    currentOrder.getTaxRate().toString(),
                    currentOrder.getArea().toString(),
                    currentOrder.getCostPerSquareFoot().toString(),
                    currentOrder.getLaborCostPerSquareFoot().toString(),
                    currentOrder.getMaterialCost().toString(),
                    currentOrder.getLaborCost().toString(),
                    currentOrder.getTax().toString(),
                    currentOrder.getTotal().toString());
            io.print(orderInfo);
        }
        io.print("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        return io.readString("Please hit enter to continue");
    }
    

    // CREATE ORDER OPTION
    public void displayCreateOrderBanner() {
        io.print("=== Create Order ===");
    }
    public LocalDate getOrderDateCreateOrder() {
        io.print("What date would you like to create an order for? ");
        return io.readDate("Please enter a date in the format YYYY-MM-DD");
    }
    
    public String getCustomerName() {
        return io.readString("What is your full name? ");
    }

    public String getStateAbbreviation() {
        return io.readString("What is the state abbreviation? ");
    }
    public String getProductType() {
        return io.readString("What product type would you like?");
    }
    
    public void displayCreateSuccessBanner(String verifyOrder){
        if (verifyOrder.equalsIgnoreCase("y")) {
            io.print("=== Order successfully created ===");
        } else {
            io.readString("Please press enter to continue.");
    } 
    }
    
    public String displayAvailableProductsAndGetSelection(List<Product> productList){
        io.print("Product type    CostPerSquareFoot   LaborCostPerSquareFoot ");
        productList.stream().map(currentProduct -> String.format("%s     :       %s      :        %s",
                currentProduct.getProductType(),
                currentProduct.getCostPerSquareFoot().toString(),
                currentProduct.getLaborCostPerSquareFoot().toString())).forEachOrdered(productInfo -> {
                    io.print(productInfo);
        });
        return io.readString("Which product type would you like? Please select from the list above");
    }
    
    public BigDecimal getArea() {
        return io.readBigDecimal("Please enter the area you required in sqft. There is a minimum order of 100sqft.");
    }
    
    
    public String displayNewOrderSummary(LocalDate orderDateInput, int orderNumber,String customerNameInput, String stateAbbreviationInput, String productTypeInput, BigDecimal areaInput, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total){
        io.print("========= Order Summary =========");
        io.print("Order date:         " + orderDateInput);
        io.print("Customer Name:      " + customerNameInput);
        io.print("State Abbreviation: " + stateAbbreviationInput);
        io.print("Product:            " + productTypeInput);
        io.print("Area required:      " + areaInput + " sqft");
        io.print("Material cost:      $"+ materialCost);
        io.print("Labor cost:         $"+ laborCost);
        io.print("Tax:                $" + tax);
        io.print("---------------------------------");
        io.print("Total:              $" + total);
        
        return io.readString("Do you want to place the order? (Y/N).");
    }

    public void displayCreateSuccessBanner(Order newOrder){
       
        if (newOrder==null){
            //return to main menu
            io.readString("Please hit enter to continue to main menu.");
        } else {
            io.print("=== Order Succesfully Created ===");
    }
    }
        
        
   //REMOVE ORDER OPTION 
    public void displayRemoveOrderBanner() {
        io.print("=== Remove Order ===");
    }
    
    public LocalDate getOrderDateRemoveOrder() {
        io.print("What is the date of the order you want to remove?");
        return io.readDate("Please enter a date in the format YYYY-MM-DD");
    }
    
    public int getOrderNumberRemoveOrder(){
        return io.readInt("What is the order number of the order you want to remove? ");
    }
   
    public void displayOrderInformation(LocalDate orderDateInput, Order orderToRemove) {
        io.print("=== Order Summary ===");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        io.print("Order date:         " + orderDateInput.format(formatter));
        io.print("Customer Name:      " + orderToRemove.getCustomerName());
        io.print("State Abbreviation: " + orderToRemove.getStateAbbr());
        io.print("Product:            " + orderToRemove.getProductType());
        io.print("Area required:      " +orderToRemove.getArea().toString() + " sqft");
        io.print("Material cost:      $"+ orderToRemove.getMaterialCost().toString());
        io.print("Labor cost:         $"+ orderToRemove.getLaborCost());
        io.print("Tax:                $" + orderToRemove.getTax().toString());
        io.print("---------------------------------");
        io.print("Total:              $" + orderToRemove.getTotal().toString());
    }
    
    public String getRemoveConfirmation() {
        return io.readString("Are you sure you want to remove the order? (Y/N)");
    }    
    
    public void displayRemoveSuccessBanner(Order removedOrder){
        if (removedOrder==null) {
           
            io.readString("Please hit enter to continue to main menu.");
        } else {
            io.print("=== Order Succesfully Removed ===");
    }
    }

//EDIT ORDER   
    public void displayEditOrderBanner(){
        io.print("=== Edit Order ===");
    }
    
    public LocalDate getOrderDateEditOrder() {
        io.print("What is the date of the order you want to edit?");
        return io.readDate("Please enter a date in the format YYYY-MM-DD");
    }
    
    public int getOrderNumberEditOrder(){
        return io.readInt("What is the order number of the order you want to edit? ");
    }
    
    public String displayAndGetEditCustomerName(Order orderToEdit){
        return io.readString("Enter customer name (" + orderToEdit.getCustomerName()+"):");
    }
    
    public String displayAndGetEditState (Order orderToEdit){
        return io.readString("Enter the state name (" + orderToEdit.getStateAbbr()+"):");
    }
    
    public String displayAndGetEditProductType(Order orderToEdit){
        return io.readString("Enter product type (" + orderToEdit.getProductType()+"):");
    }

    public String displayAndGetEditArea(Order orderToEdit){
        return io.readString("Enter the area (" + orderToEdit.getArea()+"):");
    }
    
    public void displayEditedOrderSummary(LocalDate orderDateInput,Order editedOrder) {
        io.print("=== Edited Order Summary ===");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        io.print("Order date:          " + orderDateInput.format(formatter));
        io.print("Customer Name:       " + editedOrder.getCustomerName());
        io.print("State abbreviation:  " + editedOrder.getStateAbbr());
        io.print("Product:             " + editedOrder.getProductType());
        io.print("Area required  :     " +editedOrder.getArea().toString() + " sqft");
        io.print("Material cost:       $"+ editedOrder.getMaterialCost().toString());
        io.print("Labor cost:          $"+ editedOrder.getLaborCost());
        io.print("Tax:                 $" + editedOrder.getTax().toString());
        io.print("---------------------------------");
        io.print("Total:               $" + editedOrder.getTotal().toString());
    }
    
    public String getSaveConfirmation(){
        return io.readString("Do you want the edit to be saved? (Y/N)");
    }
    
    public void displayEditSucessBanner(Order editedOrder){
        if (editedOrder==null) {
            io.readString("Please hit enter to continue to main menu.");
        } else {
            io.print("=== Order Succesfully Edited ===");
    } 
    }
    
    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }    
    public void displayExitBanner() {
        io.print("Good bye!");
    }
    
    public void displayUnknownCommandBanner() {
        io.print("Unknown command!");
    }
    
    public void displayExportAllDataBanner(){
        io.print("=== Export All Data ===");
    }
  
}
