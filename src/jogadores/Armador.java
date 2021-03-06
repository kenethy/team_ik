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
		Vector2D initPos = new Vector2D(xInit * side.value(), yInit);
		Vector2D ballPos, vTemp;
		PlayerPerception pTemp;
		while (true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch (matchPerc.getState()) {
			
			case OFFSIDE_LEFT:
				if (side == EFieldSide.LEFT){
					correrEChutar(ballPos, side);
				}
				break;
			case OFFSIDE_RIGHT:
				if (side == EFieldSide.RIGHT){
					correrEChutar(ballPos, side);
				}
				break;
			case FREE_KICK_LEFT: 
				if (side == EFieldSide.LEFT){
					correrEChutar(ballPos, side);
				}
				break;
			case FREE_KICK_RIGHT:
				if (side == EFieldSide.RIGHT){
					correrEChutar(ballPos, side);
				}
				break;
			case KICK_IN_LEFT: 
				if (side == EFieldSide.LEFT){
					correrEChutar(ballPos, side);
				}
				break;
			case KICK_IN_RIGHT: 
				if (side == EFieldSide.RIGHT){
					correrEChutar(ballPos, side);
				}
				break;
			case GOAL_KICK_LEFT:
				if (side == EFieldSide.LEFT){

				}
				break;
			case GOAL_KICK_RIGHT: 
				if (side == EFieldSide.RIGHT){

				}
				break;
			case CORNER_KICK_LEFT:
				if (side == EFieldSide.LEFT){
					correrEChutar(ballPos, side);
				}
				break;
			case CORNER_KICK_RIGHT:
				if (side == EFieldSide.RIGHT){
					correrEChutar(ballPos, side);
				}
				break;
			case BEFORE_KICK_OFF:
				//System.out.println(initPos);
				//if (selfPerc.getPosition() != initPos){
					commander.doMoveBlocking(initPos.getX(), initPos.getY());
					dash(initPos, 100);
				//}
				break;
			case KICK_OFF_LEFT: 
				setPlayerRecebendo(-1);
				Mensagens.sendMessage(selfPerc.getUniformNumber(), "-1");
				correrEChutar(ballPos,side);
				break;
			case KICK_OFF_RIGHT: 
				setPlayerRecebendo(-1);
				Mensagens.sendMessage(selfPerc.getUniformNumber(), "-1");
				correrEChutar(ballPos,side);
				break;
			case PLAY_ON:
				//System.out.println("recebendo: " + getPlayerRecebendo());
				//Mensagens
				if (Mensagens.receiveMessage(selfPerc.getUniformNumber()).equals(trueBallPossession))
					setBallPossession(true);
				else if (Mensagens.receiveMessage(selfPerc.getUniformNumber()).equals(falseBallPossession))
					setBallPossession(false);
				
				if (Mensagens.receiveMessage(selfPerc.getUniformNumber()).equals(passeParaVoce))
					setPlayerRecebendo(selfPerc.getUniformNumber());
				// se o time esta com a bola, mas EU n�o estou com ela
				
				
				if (isBallPossession() && !isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
					// e n�o vou receber a bola
					if (selfPerc.getUniformNumber() == 4 && getPlayerRecebendo() != 4 && getPlayerRecebendo() != -1)
						dash(new Vector2D(-10 * side.value(), 0), 70); // move para o meio de campo
					// se nao for camisa 4, � o camisa 5 armador
					else if (selfPerc.getUniformNumber() == 5 && getPlayerRecebendo() != 5
							&& getPlayerRecebendo() != -1)
						dash(new Vector2D(10 * side.value(), 0), 70); // move para o meio de campo
				}
				// se estou perto da bola
				if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
					setBallPossession(true); // setar que o nosso time esta com a bola
					Mensagens.sendMessageAll(trueBallPossession);
					setPlayerRecebendo(-1); // ningu�m esta marcado para receber a bola			
					Mensagens.sendMessage(selfPerc.getUniformNumber(), "-1");

					// toca para o atacante mais perto
					double dist1 = Vector2D.distance(selfPerc.getPosition(),
							fieldPerc.getTeamPlayer(selfPerc.getSide(), 6).getPosition());
					double dist2 = Vector2D.distance(selfPerc.getPosition(),
							fieldPerc.getTeamPlayer(selfPerc.getSide(), 7).getPosition());

					// pega o atacante mais perto
					vTemp = dist1 > dist2 ? fieldPerc.getTeamPlayer(selfPerc.getSide(), 7).getPosition()
							: fieldPerc.getTeamPlayer(selfPerc.getSide(), 6).getPosition();
					if (dist1 > dist2) {
						setPlayerRecebendo(7);
						Mensagens.sendMessage(7, passeParaVoce);
						vTemp = fieldPerc.getTeamPlayer(selfPerc.getSide(), 7).getPosition();
					} else {
						setPlayerRecebendo(6);
						Mensagens.sendMessage(6, passeParaVoce);
						vTemp = fieldPerc.getTeamPlayer(selfPerc.getSide(), 6).getPosition();
					}
					//turnToPoint(vTemp);
					//Vector2D vTempF = vTemp.sub(selfPerc.getPosition());
					double intensity = 100 * (selfPerc.getPosition().distanceTo(vTemp) / 40);
					System.out.println("Distancia do passe: " + selfPerc.getPosition().distanceTo(vTemp) + " Intensidade: "+ intensity);
					//System.out.println(vTemp.magnitude() + "*100/40 = "+ vTemp.magnitude()*100/40);
					kickToPoint(vTemp, intensity);
					setBallPossession(false);
					Mensagens.sendMessageAll(falseBallPossession);

				} else { // se n�o estou perto da bola, corre at� ela
					pTemp = getClosestPlayerPoint(ballPos, side, 3, 0);
					if ((pTemp != null && (pTemp.getUniformNumber() == selfPerc.getUniformNumber())
							&& getPlayerRecebendo() == -1) || getPlayerRecebendo() == selfPerc.getUniformNumber()) {
						// pega a bola
						dash(ballPos, 100);
					} else if (!isPointsAreClose(selfPerc.getPosition(), initPos, 3)) {
						// recua
						 //dash(initPos, 100);
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
