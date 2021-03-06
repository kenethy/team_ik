package jogadores;

import java.awt.Rectangle;

import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

import simple_soccer_lib.PlayerCommander;

public class Goleiro extends PlayerBase {
	Vector2D ballPos0, ballPos1; //para calcular para onde a bola est� indo

	public Goleiro(PlayerCommander player) {
		super(player);
	}

	public void acaoGoleiro(long nextIteration) {
		int localState = -1;
		double xInit = -49;
		EFieldSide side = selfPerc.getSide();
		Vector2D initPos = new Vector2D(xInit * side.value(), 0);
		Vector2D enemyGoal = new Vector2D(50 * side.value(), 0);
		Vector2D ballPos;
		// PlayerPerception pTemp = null;
		Rectangle area = side == EFieldSide.LEFT ? new Rectangle(-52, -20, 16, 40) : new Rectangle(36, -20, 16, 40);

		while (true) {
			ballPos0 = fieldPerc.getBall().getPosition();
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();

			switch (matchPerc.getState()) {

			case BEFORE_KICK_OFF:
				// posicao inicial
				//if (selfPerc.getPosition() != initPos) {
				//System.out.println("GK" + selfPerc.getPosition());
				commander.doMoveBlocking(initPos.getX(), initPos.getY());
				commander.doMove(initPos.getX(), initPos.getY());
				dash(initPos, 100);
				//}
				break;

			case OFFSIDE_LEFT:
				break;
			case OFFSIDE_RIGHT:
				break;
			case KICK_OFF_LEFT:
				dash(new Vector2D(xInit * side.value(), ballPos.getY() / 5), 70);
				break;
			case KICK_OFF_RIGHT:
				dash(new Vector2D(xInit * side.value(), ballPos.getY() / 5), 70);
				break;
			case FREE_KICK_LEFT:
				if(isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
					turnToPoint(enemyGoal);
					kickToPoint(enemyGoal, 85);
				}
				break;
			case FREE_KICK_RIGHT:
				break;
			case KICK_IN_LEFT:
				break;
			case KICK_IN_RIGHT:
				break;
			case GOAL_KICK_LEFT:
				if (side == EFieldSide.LEFT) {
					dash(new Vector2D(ballPos.getX(), ballPos.getY()), 100);
					ballPos = fieldPerc.getBall().getPosition();
					dash(ballPos, 100);
					// TODO Colocar se a condi��o do goleiro for igual a da bola chutar
					if(isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
						//turnToPoint(enemyGoal);
						kickToPoint(enemyGoal, 85);
					}
					// Pode ser feito a verifica��o de um jogador proximo para receber a bola
				}
				break;
			case GOAL_KICK_RIGHT:
				if (side == EFieldSide.RIGHT) {
					dash(new Vector2D(ballPos.getX(), ballPos.getY()), 100);
					// TODO Colocar se a condi��o do goleiro for igual a da bola chutar

					turnToPoint(enemyGoal);
					//Thread.sleep(1);
					kickToPoint(enemyGoal, 85);
				}
				break;
			case PLAY_ON:
				// System.out.println(ballPos.angleFrom(selfPerc.getPosition()));

				switch (localState) {

				case -1: //ir para posi��o inicial
					if(isPointsAreClose(selfPerc.getPosition(), initPos, 2)){
						turnToPoint(ballPos);
						localState = 0;
					} else {
						dash(initPos, 100);
					}
					break;

				case 0: // posicionar para receber a bola
					//System.out.println("Estado goleiro: " + localState + selfPerc.getTeam());
					ballPos1 = ballPos;
					if (area.contains(ballPos.getX(), ballPos.getY()) && getClosestPlayerPoint(ballPos, side, 1, 0).getUniformNumber() == selfPerc.getUniformNumber()) 
						localState = 3;

					if ((side == EFieldSide.LEFT && ballPos.getX() < -15)
							|| (side == EFieldSide.RIGHT && ballPos.getX() > 15)){
						// calcular coeficiente angular da recta para poder formular a equa��o da reta
						double coefAng = (ballPos1.getY() - ballPos0.getY()) / (ballPos1.getX() - ballPos0.getX());
						//System.out.println("AngleFrom: goleiro "+selfPerc.getTeam() +" e ballPos :" +selfPerc.getPosition().angleFrom(ballPos));
						// Y do gol varia de -7 a 7
						// na equa��o da reta, quando X for -50, qnt ser� Y? y � y0 = m (x � x0)
						// -50 * sideValue para saber qual lado � meu gol
						double y = ballPos1.getY() + (coefAng * ((-51 * side.value()) - ballPos.getX()));
						if (Double.isNaN(y) || Double.isInfinite(y))
							y = 0;
						Vector2D bolaNoGol = new Vector2D(-51 * side.value(), y);
						if (bolaNoGol.getY() <= 7 && bolaNoGol.getY() >= -7) {
							System.out.println("Bola no gol: " + bolaNoGol);
							dash(bolaNoGol, 100);
						}
						// TODO verificar se chegou no dash
						if(isPointsAreClose(selfPerc.getPosition(), bolaNoGol, 1)){
							turnToPoint(ballPos);
							localState = 1;
						}
					}
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1.2)){ // se estiver perto da bola
						System.out.println(selfPerc.getPosition().distanceTo(ballPos));
						localState = 1; // ir para o estado 1
					}
					break;

				case 1: // agarrar a bola
					System.out.println("Estado goleiro: " + localState + selfPerc.getTeam());
					// TODO verificar se chegou no dash e se a bola est� na area do goleiro
					// Nesse caso seria o contrario, enquanto n�o estiver a 1 de margen da bola ir at� ela
					// verificar esse caso pois � o 3, o 3, n�o est� funcionando
					//while(!isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
					//if (!isPointsAreClose(selfPerc.getPosition(), ballPos, 2))
					//dash(ballPos); // aguardar a bola chegar at� ele
					//}
					//if(isPointsAreClose(selfPerc.getPosition(), ballPos, 10))
					//	dash(ballPos, 100);
					//if(isPointsAreClose(selfPerc.getPosition(), ballPos, 1))
					//commander.doCatchBlocking(selfPerc.getPosition().angleFrom(ballPos));
					if(isPointsAreClose(selfPerc.getPosition(), ballPos, 1.2)){
						System.out.println("Agarrou ");
						kickToPoint(enemyGoal, 100);
						//commander.doCatchBlocking(0);
						
					} //else kickToPoint(enemyGoal, 2);
					turnToPoint(enemyGoal);
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1.2))
						localState = 2;
					else
						localState = 0;
					break;

				case 2: // chutar a bola
					System.out.println("Estado goleiro: " + localState + selfPerc.getTeam());
					// Enquanto a bola estiver perto dele chutar
					//while(isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) { //tirei pois ele ficava parado no estado 2 e nao fazia mais nada
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1))
						kickToPoint(enemyGoal, 100);
					//}
					else
						localState = 3;
					localState = 0;
					break;

				case 3: // perseguir a bola na minha area
					System.out.println("3Estado goleiro: " + localState + selfPerc.getTeam());
					//dash(ballPos, 100);
					System.out.println("Distancia goleiro para bola " + selfPerc.getPosition().distanceTo(ballPos));
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1.6)) {
						//turnToPoint(enemyGoal);
						localState = 1;
						//kickToPoint(enemyGoal, 100);
					}
					if (area.contains(ballPos.getX(), ballPos.getY())) {
						//turnToPoint(ballPos);
						dash(ballPos, 100);

					} else {
						dash(initPos, 100);
						localState = -1;
					}
					break;

				}
				/*
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
					turnToPoint(enemyGoal);
					Vector2D vTemp = fieldPerc.getTeamPlayer(selfPerc.getSide(), uniform_mais_perto).getPosition();
					Vector2D vTempF = vTemp.sub(selfPerc.getPosition());
					double intensity = (vTempF.magnitude() * 100);
					kickToPoint(vTemp, intensity);
					setBallPossession(false);

				} else {
					pTemp = getClosestPlayerPoint(ballPos, side, 3);
				}
				if (area.contains(ballPos.getX(), ballPos.getY()))
					dash(ballPos);
				else if (pTemp != null && pTemp.getUniformNumber() == selfPerc.getUniformNumber()
						|| getPlayerRecebendo() == selfPerc.getUniformNumber()) {
					// defender
					dash(ballPos);
				} else {
					// Posicionar o goleiro em rela��o a bola
					// calculando a proporcao do largura do campo e do gol
					dash(new Vector2D(xInit * side.value(), ballPos.getY() / 5));
				}
				 */
				/* Todos os estados da partida */
			default:
				turnToPoint(ballPos);
				break;
			}
		}
	}
}
