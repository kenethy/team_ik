package jogadores;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class Armador extends PlayerBase {

	public Armador(PlayerCommander player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	public void acaoArmador(long nextIteration, int pos) {
		double xInit = -30 + pos, yInit = 0;
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
				selfPerc.setReceiving(false);
					if(isBallPossession() && !isPointsAreClose(selfPerc.getPosition(), ballPos, 1)){ //se o time esta com a bola, mas EU não estou com ela
						System.out.println("Armador" + selfPerc.getUniformNumber() + selfPerc.isReceiving());
						if (selfPerc.getUniformNumber() == 4 && !selfPerc.isReceiving()){ //-10
							dash(new Vector2D(-10, 0)); //move para o meio de campo
						}else if (selfPerc.getUniformNumber() == 5 && !selfPerc.isReceiving()){ //se nao for camisa 4, é o camisa 5 armador //10
							dash(new Vector2D(10, 0));
						} 
				} else if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
					setBallPossession(true);
					// toca para o jogador mais perto
					vTemp = getClosestPlayerPoint(selfPerc.getPosition(), selfPerc.getSide(), 1).getPosition();
					getClosestPlayerPoint(selfPerc.getPosition(), selfPerc.getSide(), 2).setReceiving(true);
						
					Vector2D vTempF = vTemp.sub(selfPerc.getPosition());
					double intensity = (vTempF.magnitude() * 100) / 40;
					kickToPoint(vTemp, intensity);
					//selfPerc.setReceiving(false);
					//--
				}else{
					pTemp = getClosestPlayerPoint(ballPos, side, 3);
					if (pTemp != null && pTemp.getUniformNumber() == selfPerc.getUniformNumber() && selfPerc.isReceiving()) {
						// pega a bola
						selfPerc.setReceiving(false);
						dash(ballPos);
					} else if (!isPointsAreClose(selfPerc.getPosition(), initPos, 3)) {
						// recua
						//dash(initPos);
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
