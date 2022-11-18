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
public class FlooringMasteryAreaBelowMinException extends Exception {
       
    public FlooringMasteryAreaBelowMinException(String message) {
        
        super(message);
    }
    public FlooringMasteryAreaBelowMinException(String message, Throwable cause) {
        super(message, cause);
}
}