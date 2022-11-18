/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmala.flooringmastery;

import mmala.flooringmastery.controller.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 *
 * @author 18437
 */
public class App {
    public static void main(String[] args) {
        // if no Spring
//    UserIO io = new UserIOConsoleImpl();
//    FlooringMasteryView view = new FlooringMasteryView(io);
//    FlooringMasteryDao dao = new FlooringMasteryDaoFileImpl();
//    FlooringMasteryServiceLayer service = new FlooringMasteryServiceLayerImpl(dao);
//    
//    FlooringMasteryController controller = new FlooringMasteryController(view, service);
//    
//    controller.run();


    ApplicationContext ctx = 
            new ClassPathXmlApplicationContext("applicationContext.xml");
    FlooringMasteryController controller = 
            ctx.getBean("controller", FlooringMasteryController.class);
    controller.run();
    }
    
   
    
}
