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
		Vector2D goalPos = new Vector2D(50 * side.value(), 0);
		Vector2D ballPos;
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
				ballX=fieldPerc.getBall().getPosition().getX();
				ballY=fieldPerc.getBall().getPosition().getY();
				if(isPointsAreClose(selfPerc.getPosition(),ballPos, 1)){
					
				// 	chutar
					getClosestPlayerPoint(goalPos, selfPerc.getSide(), 2).setReceiving(true); 
					kickToPoint(getClosestPlayerPoint(goalPos, selfPerc.getSide(), 2).getPosition(), 100);

					//getClosestPlayerPoint(selfPerc.getPosition(), selfPerc.getSide(), 2).setReceiving(true); 
					//kickToPoint(getClosestPlayerPoint(selfPerc.getPosition(), selfPerc.getSide(), 2).getPosition(), 80);
					setBallPossession(false);
					System.out.println("chutao1");
					
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
			/* Todos os estados da partida */
			default:
				break;
			}
		}
	}
}
