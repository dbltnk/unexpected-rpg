import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
	
	/** 
	 * constructor: setup all gamepads
	 */
	public ControllerSupport() {
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
	 * run method of the thread:
	 * supervises the controllers, calls the actionPerformed() method
	 * of the ActionListeners in case of an event
	 */
	@Override
	public void run() {
		while(true) {
	    	for (Controller c : gamepads) {
	    		c.poll();
	    		
	    		EventQueue queue = c.getEventQueue();
	    		Event event = new Event();
	    		
	    		while (queue.getNextEvent(event)) {
	                Component comp = event.getComponent();
	                
	                String command = "";
	                if (comp.getIdentifier() == Component.Identifier.Button._0) {
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
	                	break;
	                }
	                
	                for (ActionListener a : getActionListeners()) {
                		a.actionPerformed(new ActionEvent(this,0,command));
                	}
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
