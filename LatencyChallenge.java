import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;

/*
 * This skeleton app creates a window frame with a chart panel. Extend
 * this class and add more classes to complete the challenge.
 */


class ConnectionHandler implements Runnable {
	private Socket client;
	private  BlockingQueue<Double> queue ;
	String hostname;
	int port;
	
	ConnectionHandler(BlockingQueue<Double> q,String host,int portNum) {
		this.hostname = new String(host);
		this.port = portNum;
		this.queue = q;
	}
	
	public void run() {
        while (LatencyChallenge.keepRunning) {
        	try {
        		long start = System.currentTimeMillis();
				client  = new Socket(hostname,port);
				long end = System.currentTimeMillis();
				double diff  = (end-start) ;
			    client.close();
				queue.put(diff);
			    Thread.sleep(2000);
			} catch (UnknownHostException e) {
				// TODO 
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}

public class LatencyChallenge {
    private final JFrame frame;
    public static  boolean keepRunning;
    private static Chart chart = new Chart("Latency Challenge","Connection Latency");
    HashMap<String,String> hosts = new HashMap<String,String> ();
    HashMap<String,BlockingQueue<Double>> queue = new HashMap<String,BlockingQueue<Double>>();
    LatencyChallenge() {
        keepRunning = true;
    	frame = new JFrame("LatencyChallenge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
    	frame.getContentPane().add(new JLabel("yahoo.com vs google.com latency"));        
    	frame.getContentPane().add(chart);
        frame.setVisible(true);
        frame.pack();

        queue.put("google",new LinkedBlockingQueue<Double>());
        queue.put("yahoo",new LinkedBlockingQueue<Double>());
        
        hosts.put("google", "www.google.com");
        hosts.put("yahoo", "www.yahoo.com");
        
    }

    public void run() throws InterruptedException {
        Double google_lat,yahoo_lat;
        
		new Thread(new ConnectionHandler(queue.get("google"),hosts.get("google"),80)).start();
		new Thread(new ConnectionHandler(queue.get("yahoo"),hosts.get("yahoo"),80)).start();
		
		while (true) {
    		google_lat = queue.get("google").take();
    		yahoo_lat = queue.get("yahoo").take();
    		
    		chart.addPoint(google_lat,yahoo_lat);
    		System.out.println(" Google: " + google_lat + " Yahoo : "+yahoo_lat);
    		
		}

    }

    public static void main(String args[]) {
        try {
			(new LatencyChallenge()).run();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
