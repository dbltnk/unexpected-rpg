import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;


public class ControllerSupport extends Thread {

	/** list of gamepad controllers that could be detected */
	private List<Controller> gamepads;
	
	/** list of ActionListeners */
	private List<ActionListener> actionListeners = new ArrayList<ActionListener>();
	
	/** map that determines which button is currently pressed on which controller
	 *  (if there are multiple gamepads, the first one on which the button was
	 *  pressed "reserves" this button) */
	private HashMap<String,Controller> buttonPressed;
	
	/** 
	 * constructor: setup all gamepads
	 */
	public ControllerSupport() {
		// no buttons registered as pressed in the beginning
		this.buttonPressed = new HashMap<String,Controller>();
		this.buttonPressed.put("a", null);
		this.buttonPressed.put("b", null);
		this.buttonPressed.put("x", null);
		this.buttonPressed.put("y", null);
		
		Controller[] controllerArray = ControllerEnvironment.getDefaultEnvironment().getControllers();
		this.gamepads = new ArrayList<Controller>();
	    for (int i =0;i<controllerArray.length;i++) {
	    	// only consider gamepads
	    	if (controllerArray[i].getType() == Controller.Type.GAMEPAD) {
	    		System.out.println(controllerArray[i].getName() + " - " + controllerArray[i].getType().toString());
	    		gamepads.add(controllerArray[i]);
	    	}
	    }
	}

	/** 
	 * returns all action listeners
	 */
	public List<ActionListener> getActionListeners() {
		return this.actionListeners;
	}
	
	/**
	 *  adds an ActionListener that handles the component events;
	 *  needs to be called BEFORE the thread is started!
	 */
	public synchronized void addActionListener(ActionListener a) {
		this.actionListeners.add(a);
	}
	
	/**
	 * registers a controller that currently presses the given
	 * button if there is not already another controller that
	 * has reserved the button (this avoids strange behavior
	 * if the same button was pressed on multiple controllers)
	 */
	public void buttonPressed(Controller newController, String buttonStr) {
		Controller lastController = this.buttonPressed.get(buttonStr);
		if (lastController == null) {
			this.buttonPressed.put(buttonStr,newController);
			for (ActionListener a : getActionListeners()) {
        		a.actionPerformed(new ActionEvent(this,0,buttonStr));
        	}
		}
	}
	
	/**
	 * removes the registration of a controller for a given button
	 * if the button was released on this controller:
	 * the button can now be pressed again on any controller
	 */
	public void buttonReleased(Controller newController, String buttonStr) {
		Controller lastController = this.buttonPressed.get(buttonStr);
		if (lastController != null && lastController.equals(newController)) {
			this.buttonPressed.put(buttonStr, null);
		}
	}
		
	/**
	 * run method of the thread:
	 * supervises the controllers, calls the actionPerformed() method
	 * of the ActionListeners in case of an event
	 */
	@Override
	public void run() {
		while(true) {
			/* check for each gamepad and each of the 4 relevant buttons
			 * if the button was now pressed or is still pressed on the
			 * controller */
	    	for (Controller c : gamepads) {
	    		c.poll();
	    		
	    		if (c.getComponent(Component.Identifier.Button._0).getPollData() > 0) {
	    			buttonPressed(c,"a");
	    		} else {
	    			buttonReleased(c,"a");
	    		}
	    		if (c.getComponent(Component.Identifier.Button._1).getPollData() > 0) {
	    			buttonPressed(c,"b");
	    		} else {
	    			buttonReleased(c,"b");
	    		}
	    		if (c.getComponent(Component.Identifier.Button._2).getPollData() > 0) {
	    			buttonPressed(c,"x");
	    		} else {
	    			buttonReleased(c,"x");
	    		}
	    		if (c.getComponent(Component.Identifier.Button._3).getPollData() > 0) {
	    			buttonPressed(c,"y");
	    		} else {
	    			buttonReleased(c,"y");
	    		}
	    		
	    		/*
	    		EventQueue queue = c.getEventQueue();
	    		Event event = new Event();
	    		while (queue.getNextEvent(event)) {
	                Component comp = event.getComponent();
	                
	                String command = "";
	                if (comp.getIdentifier() == Component.Identifier.Button._0) {
	                	if (buttonAdown) {
	                		continue;
	                	}
	                	System.out.println("A");  	
	                	command = "a";
	                } else if (comp.getIdentifier() == Component.Identifier.Button._1) {
	                	System.out.println("B");
	                	command = "b";
	                } else if (comp.getIdentifier() == Component.Identifier.Button._2) {
	                	System.out.println("X");
	                	command = "x";
	                } else if (comp.getIdentifier() == Component.Identifier.Button._3) {
	                	System.out.println("Y");
	                	command = "y";
	                } else {
	                	continue;
	                }
	                
	                for (ActionListener a : getActionListeners()) {
                		a.actionPerformed(new ActionEvent(this,0,command));
                	}
	                
	                try {
		                Thread.sleep(500);
		             } catch (InterruptedException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		             }
	             }*/
	    		
	    		try {
	                Thread.sleep(20);
	            } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	    	}
	    }
	}
	
	/**
	 * Test program
	 */
	public static void main(String[] args) {
		ControllerSupport cs = new ControllerSupport();
		cs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("COMMAND: " + e.getActionCommand());
			}
			
			
		});
		cs.run();
    }
	
}
