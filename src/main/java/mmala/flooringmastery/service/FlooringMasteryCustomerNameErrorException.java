/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.service;

/**
 *
 * @author 18437
 */
public class FlooringMasteryCustomerNameErrorException extends Exception {
    
    public FlooringMasteryCustomerNameErrorException(String message) {
        
        super(message);
    }
    public FlooringMasteryCustomerNameErrorException(String message, Throwable cause) {
        super(message, cause);
}
}