/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery.dao;

import mmala.flooringmastery.dto.Tax;
import java.util.List;

/**
 *
 * @author 18437
 */
public interface FlooringMasteryTaxDao {
    List<Tax> getAllTaxes()throws FlooringMasteryException;
    
    Tax getTax(String stateAbbreviationInput) throws FlooringMasteryException;
}
