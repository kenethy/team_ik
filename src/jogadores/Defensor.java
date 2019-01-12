package jogadores;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class Defensor extends PlayerBase {

	public Defensor(PlayerCommander player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	public void acaoDefensor(long nextIteration, int pos) {
		double xInit = -30, yInit = 0 + pos;
		EFieldSide side = selfPerc.getSide();
		Vector2D initPos = new Vector2D(xInit * side.value(), yInit * side.value());
		Vector2D ballPos, vTemp;
		PlayerPerception pTemp;

		while (true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch (matchPerc.getState()) {
			
			case FREE_KICK_LEFT: break;
			case FREE_KICK_RIGHT: break;
			case KICK_IN_LEFT: break;
			case KICK_IN_RIGHT: break;
			case GOAL_KICK_LEFT: break;
			case GOAL_KICK_RIGHT: break;
			case BEFORE_KICK_OFF:
				commander.doMoveBlocking(xInit, yInit);
				break;
			case KICK_OFF_LEFT: setPlayerRecebendo(-1);
			case KICK_OFF_RIGHT: setPlayerRecebendo(-1);
			case PLAY_ON:
				// POSSE DE BOLA
				if (isBallPossession()) { 
					System.out.println("posse de bola a favor");
					// Posse de bola e realização de toques
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
						setBallPossession(true);
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
						setBallPossession(false);
						// Sem a bola, caso esteja próximo ir ate a bola
					} else {
						pTemp = getClosestPlayerPoint(ballPos, side, 3);
						if (pTemp != null && pTemp.getUniformNumber() == selfPerc.getUniformNumber()) {
							// pega a bola
							dash(ballPos);
						} else if (!isPointsAreClose(selfPerc.getPosition(), initPos, 3)) {
							// recuar - retornar a posição inicial (movimentação sem a bola
							dash(initPos);
						} else {
							// olha para a bola
							turnToPoint(ballPos);
						}
					}
				} else {
					// SEM A POSSE DA BOLA
					System.out.println("Sem a posse da bola");
					// Quando o ataque chegar perto do defensor
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 12)) {
						// ir ate a bola
						dash(ballPos);
						// chutar em direcao ao gol
						kickToPoint(new Vector2D(50 * side.value(), 0), 50);
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
