package jogadores;

import java.awt.Rectangle;

import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

import simple_soccer_lib.PlayerCommander;
//import simple_soccer_lib.perception.MatchPerception;
//import simple_soccer_lib.perception.PlayerPerception;

public class Goleiro extends PlayerBase {
	int localState = 0;
	Vector2D ballPos0, ballPos1; //para calcular para onde a bola está indo

	public Goleiro(PlayerCommander player) {
		super(player);
	}

	public void acaoGoleiro(long nextIteration) {
		double xInit = -49, yInit = 0;
		EFieldSide side = selfPerc.getSide();
		Vector2D initPos = new Vector2D(xInit * side.value(), yInit * side.value());
		Vector2D goalPos = new Vector2D(50 * side.value(), 0);
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
				commander.doMoveBlocking(xInit * side.value(), yInit * side.value());
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
					turnToPoint(goalPos);
					kickToPoint(goalPos, 85);
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
					commander.doMoveBlocking(ballPos.getX(), ballPos.getY());
					ballPos = fieldPerc.getBall().getPosition();
					dash(ballPos, 100);
					// TODO Colocar se a condição do goleiro for igual a da bola chutar
					if(isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
						turnToPoint(goalPos);
						kickToPoint(goalPos, 85);
					}
					// Pode ser feito a verificação de um jogador proximo para receber a bola
				}
				break;
			case GOAL_KICK_RIGHT:
				if (side == EFieldSide.RIGHT) {
					commander.doMoveBlocking(ballPos.getX(), ballPos.getY());
					// TODO Colocar se a condição do goleiro for igual a da bola chutar
					
					turnToPoint(goalPos);
					//Thread.sleep(1);
					kickToPoint(goalPos, 85);
				}
				break;
			case PLAY_ON:
				// System.out.println(ballPos.angleFrom(selfPerc.getPosition()));

				switch (localState) {

				case 0: // posicionar para receber a bola
					System.out.println("Estado goleiro: " + localState + selfPerc.getTeam());
					ballPos1 = ballPos;
					// Vector2D ballPos2 = ballPos1.sub(ballPos0);
					// calcular coeficiente angular da reta para poder formular a equação da reta
					
					if (area.contains(ballPos.getX(), ballPos.getY())) 
						localState = 3;

					if ((side == EFieldSide.LEFT && ballPos.getX() < 0)
							|| (side == EFieldSide.RIGHT && ballPos.getX() > 0)){
						double coefAng = (ballPos1.getY() - ballPos0.getY()) / (ballPos1.getX() - ballPos0.getX());

						// Y do gol varia de -7 a 7
						// na equação da reta, quando X for -50, qnt será Y? y – y0 = m (x – x0)
						double y;
						// -50 * sideValue para saber qual lado é meu gol
						y = ballPos1.getY() + (coefAng * ((-50 * side.value()) - ballPos.getX()));
						if (Double.isNaN(y) || Double.isInfinite(y))
							y = 0;
						Vector2D bolaNoGol = new Vector2D(-50 * side.value(), y);
						//System.out.println("Bola no Gol: " + bolaNoGol);
						//System.out.println("BallPos 0 e 1:" + ballPos0 + " " + ballPos1);
						//dash(new Vector2D(xInit * side.value(), ballPos.getY() / 5));
						if (bolaNoGol.getY() <= 7 && bolaNoGol.getY() >= -7) { // verificar se a bola esta na
							// minha metade do campo
							dash(bolaNoGol, 100);
						}
						// TODO verificar se chegou no dash
						if(isPointsAreClose(selfPerc.getPosition(), bolaNoGol, 1.2))
							localState = 1;
					} else if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1.2)) // se estiver perto da bola
						localState = 1; // ir para o estado 1
					break;

				case 1: // agarrar a bola
					System.out.println("Estado goleiro: " + localState + selfPerc.getTeam());
					// TODO verificar se chegou no dash e se a bola está na area do goleiro
					// Nesse caso seria o contrario, enquanto não estiver a 1 de margen da bola ir até ela
					// verificar esse caso pois é o 3, o 3, não está funcionando
					//while(!isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
					//if (!isPointsAreClose(selfPerc.getPosition(), ballPos, 2))
						//dash(ballPos); // aguardar a bola chegar até ele
					//}
					
					commander.doCatchBlocking(0);
					kickToPoint(goalPos, 1);
					//kickToPoint(goalPos, 2);
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1.2))
						localState = 2;
					else
						localState = 0;
					break;

				case 2: // chutar a bola
					System.out.println("Estado goleiro: " + localState);
					// Enquanto a bola estiver perto dele chutar
					//while(isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) { //tirei pois ele ficava parado no estado 2 e nao fazia mais nada
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 2))
						kickToPoint(goalPos, 85);
					//}
					else
						localState = 3;
					localState = 0;
					break;

				case 3: // perseguir a bola
					System.out.println("Estado goleiro: " + localState);
					if (area.contains(ballPos.getX(), ballPos.getY())) {
						dash(ballPos, 100);
						if (isPointsAreClose(selfPerc.getPosition(), ballPos, 0.5)) {
							kickToPoint(goalPos, 100);
						}
					} else {
						dash(initPos, 100);
						localState = 0;
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
					turnToPoint(goalPos);
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
					// Posicionar o goleiro em relação a bola
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
