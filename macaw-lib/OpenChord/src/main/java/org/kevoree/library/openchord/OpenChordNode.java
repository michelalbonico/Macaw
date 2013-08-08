package org.kevoree.library.openchord;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Properties;

@Library(name="OpenChord")
@ComponentType
@DictionaryType({
	@DictionaryAttribute(name = "bootstrapPORT", optional = false, defaultValue= "8080"),
	@DictionaryAttribute(name = "bootstrapIP", optional = false, defaultValue= "127.0.0.1")
})
@Provides({
    @ProvidedPort(name = "receive", type = PortType.MESSAGE)
})

public class OpenChordNode extends AbstractComponentType {
	
	private Chord node;
	private boolean connected;
	
	@Start
	public void startNode() {
		System.out.println("Waiting for Bootstrap message!");
	}
	
	@Stop
	public void stopNode() {
		/*
		try {
			node.leave();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	@Update
	public void updateNode() {
		
	}
	
	// Example got in:
	// http://stackoverflow.com/questions/604424/java-convert-string-to-enum
	public enum Cmd {
		READY("Ready"),
		INSERT("Insert");    
	      
	    private final String vlr;  
	       
	    Cmd(String valor){  
	            this.vlr = valor;  
	    }  
	        
	    public String getValorEnum(){  
	          return vlr;  
	    }  
	}
	
	/**
	 * Bla, bla, bla...
	 *
	 * @param  Object  an String object
	 * @return	Nothing
	 * @see void
	 */
	@Port(name = "receive")
    public void receiveMessage(Object o) {
		String msg = (String) o.toString();
		System.out.println("Message: "+ msg);
		if (msg.trim().equals("READY".toString())) {
				System.out.println("Connecting to Bootstrap!");
				connectChord();
		} else {
				System.out.println("Message was not recognized!");
		}
	}

	/**
	 * Nothing to say
	 */
	public void connectChord() {
			if (connected == false) {
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
					ServerSocket s = new ServerSocket(0);
			        int port = s.getLocalPort();
			        s.close();
			        System.out.println("PORTA: "+port);
			        String address = InetAddress.getLocalHost().toString();
			        address = address.substring(address.indexOf("/") + 1, address.length());
			        URL localURL = new URL(protocol + "://" + address + ":" + port + "/");
			        URL bootstrapURL = new URL(protocol + "://127.0.0.1/8080");
			        System.out.println("Local: "+localURL.toString()+" / Bootstrap: "+bootstrapURL);
			        node = new ChordImpl();
			        node.join(localURL, bootstrapURL);
			        /* Erro no join - Leitura das propriedades do Log4J */
			        System.out.println("TESTANDOOOOOOOO");
			        connected = true;
				} catch (Exception e) {
					System.out.println("Exception: "+e.toString());
				}
			} else {
				System.out.println("Node already connected to Bootstrap!");
			}
    }
	
	public void insertSampleData() {
		System.out.println("Inserting sample data to Chord...");
		try {
			String data = "test";
			StringKey myKey = new StringKey(data);
			node.insert(myKey, data);
		} catch (Exception e) {
			System.out.println("Exception: "+e.toString());
		}
	}
}
