package jogadores;

import java.awt.Rectangle;

import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.PlayerPerception;

public class Goleiro extends PlayerBase {

	public Goleiro(PlayerCommander player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	public void acaoGoleiro(long nextIteration) {
		double xInit = -48, yInit = 0, ballX = 0, ballY = 0;
		EFieldSide side = selfPerc.getSide();
		Vector2D initPos = new Vector2D(xInit * side.value(), yInit * side.value());
		Vector2D goalPos = new Vector2D(50 * side.value(), 0);
		Vector2D ballPos;
		PlayerPerception pTemp = null;
		Rectangle area = side == EFieldSide.LEFT ? new Rectangle(-52, -20, 16, 40) : new Rectangle(36, -20, 16, 40);

		while (true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch (matchPerc.getState()) {
			case BEFORE_KICK_OFF:
				// posicao inicial
				commander.doMoveBlocking(xInit, yInit);
				break;
			case PLAY_ON:
				commander.doMoveBlocking(xInit, yInit);
				ballX = fieldPerc.getBall().getPosition().getX();
				ballY = fieldPerc.getBall().getPosition().getY();
				if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {

					// chutar pro player mais perto
					int uniform_mais_perto = 1;
					int incremento = 5;
					while (uniform_mais_perto == 1) {
						uniform_mais_perto = getClosestPlayerPoint(
								selfPerc.getPosition().sum(new Vector2D(incremento, 0)), selfPerc.getSide(), 3)
										.getUniformNumber();
						incremento += 5;
					}
					setPlayerRecebendo(uniform_mais_perto);
					kickToPoint(fieldPerc.getTeamPlayer(selfPerc.getSide(), uniform_mais_perto).getPosition(), 80);
					setBallPossession(false);

				} else
					pTemp = getClosestPlayerPoint(ballPos, side, 3);
				if (area.contains(ballX, ballY)
						|| (pTemp != null && pTemp.getUniformNumber() == selfPerc.getUniformNumber())
						|| getPlayerRecebendo() == selfPerc.getUniformNumber()) {
					// defender
					dash(ballPos);
					if(commander.doCatch(-45))
						commander.doCatchBlocking(45);
				} else if (!isPointsAreClose(selfPerc.getPosition(), initPos, 3)) {
					// recuar
					dash(initPos);
				} else {
					// olhar para a bola
					turnToPoint(ballPos);
				}
				/* Todos os estados da partida */
			default:
				break;
			}
		}
	}
}
