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
					if(isBallPossession() && !isPointsAreClose(selfPerc.getPosition(), ballPos, 1)){ //se o time esta com a bola, mas EU não estou com ela
						if (getPlayerRecebendo() != 4 || getPlayerRecebendo() != 5){
							if (selfPerc.getUniformNumber() == 4) //-10
								dash(new Vector2D(-10, 0)); //move para o meio de campo
							else if (selfPerc.getUniformNumber() == 5) //se nao for camisa 4, é o camisa 5 armador //10
								dash(new Vector2D(10, 0));
						
				} else if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
					setBallPossession(true);
					setPlayerRecebendo(-1);
					// toca para o jogador mais perto
					vTemp = getClosestPlayerPoint(selfPerc.getPosition(), selfPerc.getSide(), 2).getPosition();
					setPlayerRecebendo(getClosestPlayerPoint(selfPerc.getPosition(), selfPerc.getSide(), 2).getUniformNumber());
						
					Vector2D vTempF = vTemp.sub(selfPerc.getPosition());
					double intensity = (vTempF.magnitude() * 100) / 40;
					kickToPoint(vTemp, intensity);
					//selfPerc.setReceiving(false);
					//--
				}else{
					pTemp = getClosestPlayerPoint(ballPos, side, 3);
					if (pTemp != null && pTemp.getUniformNumber() == selfPerc.getUniformNumber() && getPlayerRecebendo() == selfPerc.getUniformNumber()) {
						// pega a bola
						setPlayerRecebendo(-1);
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
