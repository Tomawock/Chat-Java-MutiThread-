package it.unibs.pajc.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MultiServer extends Thread {
	private Socket socket;
	private String clientName;
	private ArrayList<MultiServer> clients;
	private boolean isFirstTime=false;
	
	public MultiServer(Socket socket, int id,ArrayList<MultiServer> clients) {
		super("MS#" + id);
		this.socket = socket;
		this.clients=clients;
		isFirstTime=true;
	}
	
	public void run() {
		try (
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		) {
			String iLine;
			if(isFirstTime) { //non funge if
				out.println("INSERIRE USER");
				clientName = in.readLine();
				System.out.println("Joined " + clientName);
				out.println("Hello " + clientName);
				isFirstTime=false;
			}
			while(true) {
				if((iLine = in.readLine()) != null){
					stampa(iLine);
					System.out.printf("%s: %s \n", this.getName(), iLine);
					if("QUIT".equals(iLine))
						break;
				}
				
			}
			socket.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void stampa(String iLine) throws IOException {
		for(MultiServer ms:clients){
			if(!ms.getSocket().isClosed()){
				PrintWriter out =new PrintWriter(ms.getSocket().getOutputStream(), true);
				out.println(this.getClientName()+"::"+iLine);	
			}
		}		
	}
	
	public String getClientName() {
		return this.clientName;
	}

	public Socket getSocket() {
		return socket;
	}
	
}
