package it.unibs.pajc.network;

import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class Main {
	
	public static void main(String[] args) throws Exception {
		int port = args.length == 1 ? Integer.parseInt(args[0]) : 1234;
		System.out.println("\nStarting server on port: " + port);
		System.out.println("Waiting remote connections...");
		boolean listening = true;
		int id = 0;
		ArrayList<MultiServer> clients=new ArrayList<>();
		try ( 
			ServerSocket server = new ServerSocket(port);
				
		) {
			System.out.println(server.getLocalSocketAddress().toString());
			while(listening) {
				clients.add(new MultiServer(server.accept(), id,clients));
				clients.get(id).start();
				System.out.println("Joined new client #" + id);
				id++;
			}
		} catch(IOException e) {
			System.err.println("Errore durante la comunicazione! " + e);
		}
		
		System.out.println("Server stopped!");
	}
}

