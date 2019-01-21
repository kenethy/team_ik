package jogadores;

public class Mensagens {

	static String msgs[] = {" ", " ", " ", " ", " ", " ", " "};//new String[7];
	//static String ballPossesion[] = new String[7];

	public static void sendMessage(int agent, String msg) {
		msgs[agent-1] = msg;
	}

	public static void sendMessageAll(String msg) {
		for (int i = 0; i < msgs.length; i++) {
			msgs[i] = msg;
		}
	}

	public static String receiveMessage(int agent) {
		return msgs[agent-1];
	}
}
