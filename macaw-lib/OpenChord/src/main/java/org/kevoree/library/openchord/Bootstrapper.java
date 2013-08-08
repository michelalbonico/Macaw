/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kevoree.library.openchord;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import de.uniba.wiai.lspi.chord.data.URL;

/**
 *
 * @author albonico
 */
@Library(name = "OpenChord")
@ComponentType
@DictionaryType({
    @DictionaryAttribute(name = "port", optional = false, defaultValue= "8080"),
    @DictionaryAttribute(name = "address", optional = false, defaultValue="127.0.0.1")
})
@Requires({
    @RequiredPort(name = "boot", type = PortType.MESSAGE, optional = true)
})
public class Bootstrapper extends AbstractComponentType {

    private static final int PORT = 8080;
    private static String HOST;
    private Chord chord;
    private boolean started = false;

    public Bootstrapper() {
        try {
            HOST = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
        }
    }
    
    @Start
    public void start() {
    	System.out.println("Bootstrap -> "+HOST+":"+PORT);
    	if (started == false) {
	    	try {
	    		// Reading OpenChord Properties
	    		// Errors loading propertie file using the default way.
	    		String file = "/chord.properties";
	    		Properties props = new ReadProperties().readChordProperties(file);
	    		System.setProperties(props);
	    		System.out.println("Properties successfully loaded!");
	        } catch (Exception e) {
	        	System.out.println("Error: " + e.toString());
	        }
	
	    	try {
		    	String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		    	URL localURL = new URL(protocol+"://"+HOST+":"+PORT+"/");
		    	chord = new ChordImpl();
		    	chord.create(localURL);
	    	} catch(Exception e) {
	    		System.out.println("Exception: "+e.toString()+"\n\n");
	    	}  	
	    	started = true;
	    	System.out.println("Bootstrap started!");
	    	/*
	    	System.out.println("Gossipping for client nodes!");
	        MessagePort mp = getPortByName("boot",MessagePort.class);
	        mp.process("test");
	        */
    	} else {
    		System.out.println("Bootstrap already started!");
    	}
    }

    @Stop
    public void stop() {
    	/*
    	try {
			chord.leave();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    }

    @Update
    public void update() {
    }
}
