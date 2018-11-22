package jogadores;

public class Mensagens {

	String msg[] = new String[7];

	public void sendMessage(int agent, String msg) {
		this.msg[agent] = msg;
	}

	public void sendMessageAll(int agent, String msg) {
		for (int i = 0; i < this.msg.length; i++) {
			this.msg[i] = msg;
		}
	}

	public String receiveMessage(int agent) {
		return msg[agent];
	}
}
