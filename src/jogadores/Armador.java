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
				System.out.println("recebendo: " + getPlayerRecebendo());
				//se o time esta com a bola, mas EU não estou com ela
				if(isBallPossession() && !isPointsAreClose(selfPerc.getPosition(), ballPos, 1)){ 
					//e não vou receber a bola
					if (selfPerc.getUniformNumber() == 4 && getPlayerRecebendo() != 4 && getPlayerRecebendo() != -1)  
						dash(new Vector2D(-10, 0)); //move para o meio de campo
					//se nao for camisa 4, é o camisa 5 armador
					else if (selfPerc.getUniformNumber() == 5 && getPlayerRecebendo() != 5 && getPlayerRecebendo() != -1) 
						dash(new Vector2D(10, 0)); //move para o meio de campo
				}
				//se estou perto da bola
				if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) { 
					setBallPossession(true); //setar que o nosso time esta com a bola
					setPlayerRecebendo(-1); //ninguém esta marcado para receber a bola
					// toca para o atacante mais perto
					double dist1 = Vector2D.distance(selfPerc.getPosition(), fieldPerc.getTeamPlayer(selfPerc.getSide(), 6).getPosition());
					double dist2 = Vector2D.distance(selfPerc.getPosition(), fieldPerc.getTeamPlayer(selfPerc.getSide(), 7).getPosition());
					vTemp = dist1>dist2 ? fieldPerc.getTeamPlayer(selfPerc.getSide(), 7).getPosition() : fieldPerc.getTeamPlayer(selfPerc.getSide(), 6).getPosition(); //pega o player mais perto
					if (dist1>dist2){
						setPlayerRecebendo(7);
						vTemp = fieldPerc.getTeamPlayer(selfPerc.getSide(),7).getPosition();
					}else{
						setPlayerRecebendo(6);
						vTemp = fieldPerc.getTeamPlayer(selfPerc.getSide(),6).getPosition();
					}
					Vector2D vTempF = vTemp.sub(selfPerc.getPosition());
					double intensity = (vTempF.magnitude() * 100) / 40;
					kickToPoint(vTemp, intensity);
					setBallPossession(false);
				}else{ //se não estou perto da bola, corre até ela
					pTemp = getClosestPlayerPoint(ballPos, side, 3);
					if ((pTemp != null && pTemp.getUniformNumber() == selfPerc.getUniformNumber()) || getPlayerRecebendo() == selfPerc.getUniformNumber()) {
						// pega a bola
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
