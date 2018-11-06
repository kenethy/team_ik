package jogadores;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class Defensor extends PlayerBase{

	public Defensor(PlayerCommander player) {
		super(player);
		// TODO Auto-generated constructor stub
	}
	
	public void acaoDefensor(long nextIteration, int pos){
		double xInit = -30, yInit = 0 + pos;
		EFieldSide side = selfPerc.getSide();
		Vector2D initPos = new Vector2D(xInit * side.value(), yInit * side.value());
		Vector2D ballPos, vTemp;
		PlayerPerception pTemp;
		while (true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch (matchPerc.getState()) {
			case BEFORE_KICK_OFF:
				commander.doMoveBlocking(xInit, yInit);
				break;
			case PLAY_ON:
				if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
					if (selfPerc.getUniformNumber() == 2) {
						// toca para o jogador 3
						vTemp = fieldPerc.getTeamPlayer(side, 3).getPosition();
					} else {
						// toca para o jogador 4
						vTemp = fieldPerc.getTeamPlayer(side, 4).getPosition();
					}
					Vector2D vTempF = vTemp.sub(selfPerc.getPosition());
					double intensity = (vTempF.magnitude() * 100) / 40;
					kickToPoint(vTemp, intensity);
				} else {
					pTemp = getClosestPlayerPoint(ballPos, side, 3);
					if (pTemp != null && pTemp.getUniformNumber() == selfPerc.getUniformNumber()) {
						// pega a bola
						dash(ballPos);
					} else if (!isPointsAreClose(selfPerc.getPosition(), initPos, 3)) {
						// recua
						dash(initPos);
					} else {
						// olha para a bola
						turnToPoint(ballPos);
					}
				}
				break;
			/* Todos os estados da partida */
			default:
				break;
			}
	}

}
}
