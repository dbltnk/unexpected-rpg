import java.util.ArrayList;
import java.util.List;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class JInputTest {
	
	public static void main(String[] args) {
		//outputControllers();
		getGamepadEvents();
    }
	
	public static void getGamepadEvents() {
		Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		List<Controller> gamepads = new ArrayList<Controller>();
		
	    for(int i =0;i<ca.length;i++){
	    	if (ca[i].getType() == Controller.Type.GAMEPAD) {
	    		System.out.println(ca[i].getName() + " - " + ca[i].getType().toString());
	    		gamepads.add(ca[i]);
	    	}
	    }
	    
	    while(true) {
	    	for (Controller c : gamepads) {
	    		c.poll();
	    		
	    		EventQueue queue = c.getEventQueue();
	    		Event event = new Event();
	    		
	    		W_EVENTS: while(queue.getNextEvent(event)) {
	                StringBuffer buffer = new StringBuffer(c.getName());
	                buffer.append(" at ");
	                buffer.append(event.getNanos()).append(", ");
	                Component comp = event.getComponent();
	                
	                if (comp.getIdentifier() != Component.Identifier.Button._0) {
	                	break W_EVENTS;
	                }
	                System.out.println(comp.getIdentifier().toString());
	                
	                buffer.append(comp.getName()).append(" changed to ");
	                float value = event.getValue(); 
	                if(comp.isAnalog()) {
	                   buffer.append(value);
	                } else {
	                   if(value==1.0f) {
	                      buffer.append("On");
	                   } else {
	                      buffer.append("Off");
	                   }
	                }
	                System.out.println(buffer.toString());
	             }
	    		
	    		try {
	                Thread.sleep(20);
	             } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	             }
	    	}
	    }
	}
	
	public static void outputControllers() {
		Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
		
	    for(int i =0;i<ca.length;i++){
	        /* Get the name of the controller */
	        System.out.println(ca[i].getName());
	        
	        System.out.println("Type: "+ca[i].getType().toString());

	        /* Get this controllers components (buttons and axis) */
	        Component[] components = ca[i].getComponents();
	        System.out.println("Component Count: "+components.length);
	        for(int j=0;j<components.length;j++){
	            
	            /* Get the components name */
	            System.out.println("Component "+j+": "+components[j].getName() + " / " + components[j].getIdentifier());
	        }
	    }
	}

}
