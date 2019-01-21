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
				//if (selfPerc.getPosition() != initPos) {
					//System.out.println("DF" + selfPerc.getPosition());
					commander.doMoveBlocking(initPos.getX(), initPos.getY());
				//}
				break;
			case KICK_OFF_LEFT:
				setPlayerRecebendo(-1);
				correrEChutar(ballPos,side);
				break;
			case KICK_OFF_RIGHT:
				setPlayerRecebendo(-1);
				correrEChutar(ballPos,side);
				break;
			case PLAY_ON:
				// POSSE DE BOLA
				if (Mensagens.receiveMessage(selfPerc.getUniformNumber()).equals(trueBallPossession))
					setBallPossession(true);
				else if (Mensagens.receiveMessage(selfPerc.getUniformNumber()).equals(falseBallPossession))
					setBallPossession(false);
				if (isBallPossession()) { 
					//System.out.println("posse de bola a favor");
					// Posse de bola e realização de toques
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
						setBallPossession(true);
						Mensagens.sendMessageAll(trueBallPossession);
						if (selfPerc.getUniformNumber() == 2) {
							// toca para o jogador 3
							vTemp = fieldPerc.getTeamPlayer(side, 3).getPosition();
						} else {
							// toca para o jogador 4
							vTemp = fieldPerc.getTeamPlayer(side, 4).getPosition();
						}
						double intensity = 100 * (selfPerc.getPosition().distanceTo(vTemp) / 30);
						kickToPoint(vTemp, intensity);
						setBallPossession(false);
						Mensagens.sendMessageAll(falseBallPossession);
						// Sem a bola, caso esteja próximo ir ate a bola
					} else {
						pTemp = getClosestPlayerPoint(ballPos, side, 3, 0);
						if (pTemp != null && pTemp.getUniformNumber() == selfPerc.getUniformNumber()) {
							// pega a bola
							dash(ballPos, 85);
						} else if (!isPointsAreClose(selfPerc.getPosition(), initPos, 3)) {
							// recuar - retornar a posição inicial (movimentação sem a bola
							dash(initPos, 70);
						} else {
							// olha para a bola
							turnToPoint(ballPos);
						}
					}
				} else {
					// SEM A POSSE DA BOLA
					//System.out.println("Sem a posse da bola");
					// Quando o ataque chegar perto do defensor
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 17)) {
						// ir ate a bola
						dash(ballPos, 100);
						// chutar em direcao ao gol
						if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1.5) )
							kickToPoint(new Vector2D(52 * side.value(), 0), 50);
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
