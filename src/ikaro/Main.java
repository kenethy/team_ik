package ikaro;

import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) {
		try {
			CommandTeam teamA = new CommandTeam();
			teamA.launchTeamAndServer();
		} catch (UnknownHostException e) {
			System.out.println("Falha ao conectar");
		}
	}
}
