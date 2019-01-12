package jogadores;

import java.awt.Rectangle;

import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.MatchPerception;
import simple_soccer_lib.perception.PlayerPerception;

public class Goleiro extends PlayerBase {

	public Goleiro(PlayerCommander player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	// Posicao da bola entre o goleiro
	// Proporcional a posicao entre 7 e -7

	public void acaoGoleiro(long nextIteration) {
		double xInit = -49, yInit = 0, ballX = 0, ballY = 0;
		EFieldSide side = selfPerc.getSide();
		Vector2D initPos = new Vector2D(xInit * side.value(), yInit * side.value());
		Vector2D goalPos = new Vector2D(50 * side.value(), 0);
		Vector2D ballPos;
		PlayerPerception pTemp = null;
		Rectangle area = side == EFieldSide.LEFT ? new Rectangle(-52, -20, 16, 40) : new Rectangle(36, -20, 16, 40);

		while (true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			int localState = 0;
			switch (matchPerc.getState()) {
			case BEFORE_KICK_OFF:
				// posicao inicial
				commander.doMoveBlocking(xInit, yInit);
				break;
			
			case OFFSIDE_LEFT: break;
			case OFFSIDE_RIGHT: break;
			case KICK_OFF_LEFT: break;
			case KICK_OFF_RIGHT: break;
			case FREE_KICK_LEFT: break;
			case FREE_KICK_RIGHT: break;
			case KICK_IN_LEFT: break;
			case KICK_IN_RIGHT: break;
			case GOAL_KICK_LEFT: break;
			case GOAL_KICK_RIGHT: break;
			case PLAY_ON:
				 
				switch (localState){
				
				case 0: //posicionar para receber a bola 
					
					localState = 1; //ir para o estado 1
					break;
				
				case 1: //agarrar a bola
					
					localState = 2;
					break;
				
				case 2: //chutar a bola 
					
					localState = 3;
					break;
				
				case 3: //perseguir a bola  
					
					localState = 1;
					break;
				
				}
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
					turnToPoint(goalPos);
					Vector2D vTemp = fieldPerc.getTeamPlayer(selfPerc.getSide(), uniform_mais_perto).getPosition();
					Vector2D vTempF = vTemp.sub(selfPerc.getPosition());
					double intensity = (vTempF.magnitude() * 100);
					kickToPoint(vTemp, intensity);
					setBallPossession(false);

				} else {
					pTemp = getClosestPlayerPoint(ballPos, side, 3);
				}
				if (area.contains(ballX, ballY))
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
				/* Todos os estados da partida */
			default:
				turnToPoint(ballPos);
				break;
			}
		}
	}
}
