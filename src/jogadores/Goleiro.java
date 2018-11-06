package jogadores;

import java.awt.Rectangle;

import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

import simple_soccer_lib.PlayerCommander;

public class Goleiro extends PlayerBase{
	
	public Goleiro(PlayerCommander player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	public void acaoGoleiro(long nextIteration) {
		double xInit = -48, yInit = 0, ballX = 0, ballY = 0;
		EFieldSide side = selfPerc.getSide();
		Vector2D initPos = new Vector2D(xInit * side.value(), yInit * side.value());
		Vector2D ballPos;
		Rectangle area = side == EFieldSide.LEFT ? new Rectangle(-52, -20, 16, 40) : new Rectangle(36, -20, 16, 40);
		while (true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch (matchPerc.getState()) {
			case BEFORE_KICK_OFF:
				// posiciona
				commander.doMoveBlocking(xInit, yInit);
				break;
			case PLAY_ON:

				ballX=fieldPerc.getBall().getPosition().getX();
				ballY=fieldPerc.getBall().getPosition().getY();
				if(isPointsAreClose(selfPerc.getPosition(),ballPos, 1)){
				// 	chutar
					kickToPoint(new Vector2D(0,0), 100);
				}else if(area.contains(ballX, ballY)){

				ballX = fieldPerc.getBall().getPosition().getX();
				ballY = fieldPerc.getBall().getPosition().getY();
				if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
					// chutar
					kickToPoint(new Vector2D(0, 0), 100);
				} else if (area.contains(ballX, ballY)) {

					// defender
					dash(ballPos);
				} else if (!isPointsAreClose(selfPerc.getPosition(), initPos, 3)) {
					// recuar
					dash(initPos);
				} else {
					// olhar para a bola
					turnToPoint(ballPos);
				}
				}
			/* Todos os estados da partida */
			default:
				break;
			}
		}
	}
}
