package ikaro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) throws IOException {
		CommandTeam teamA = new CommandTeam();
		//se o processo do ervidor está rodando, lança só o time
		//se não, lança o time e o servidor
		if(isProcessRunning()) 
			teamA.launchTeam();
		else teamA.launchTeamAndServer();
		
	}
	
	//verificar se o servidor já está rodando (pra lançar apenas o time ou o time e o servidor)
	private static boolean isProcessRunning() throws IOException{
		String line;
		String pidInfo = "";
		Process p = Runtime.getRuntime().exec(System.getenv("windir")+"\\system32\\tasklist.exe");
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while((line = input.readLine())!= null){
			pidInfo+=line;
		}
		input.close();
		if(pidInfo.contains("rcssserver.exe"))
			return true;
		return false;
	}
}
