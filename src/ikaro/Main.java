package ikaro;

import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) throws UnknownHostException {
		CommandTeam teamA = new CommandTeam();
		teamA.launchTeamAndServer();
		//teamA.launchTeam();
	}
}
