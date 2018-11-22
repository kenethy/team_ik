package ikaro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import team.legacy.A1.*;

public class Main {
	public static void main(String[] args) throws IOException {
		CommandTeam teamA = new CommandTeam();
		//para lançar o outro time, basta importar o pacote dele e chamar sua main antes ou depois do nosso launchTeam e/ou launchTeamAndServer
		//se o processo do servidor está rodando, lança só o time
		//se não, lança o time e o servidor
		if(isProcessRunning()) {
			//so inverter a ordem para trocar os lados
			teamA.launchTeam();
			MainA1.main(args);
		}
		else{
			//so inverter a ordem para trocar os lados
			teamA.launchTeamAndServer();
			MainA1.main(args);
			
		}
		
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
