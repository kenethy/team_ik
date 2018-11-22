package jogadores;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class Atacante extends PlayerBase {

	public Atacante(PlayerCommander player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	public void acaoAtacante(long nextIteration, int pos) {
		double xInit = -15, yInit = 0 + pos;
		EFieldSide side = selfPerc.getSide();
		Vector2D initPos = new Vector2D(xInit * side.value(), yInit);
		Vector2D goalPos = new Vector2D(50 * side.value(), 0);
		Vector2D ballPos;
		PlayerPerception pTemp;
		while (true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch (matchPerc.getState()) {
			case BEFORE_KICK_OFF:
				commander.doMoveBlocking(xInit, yInit);
				break;
			case KICK_OFF_LEFT:
			case KICK_OFF_RIGHT:
			case PLAY_ON:
				// se o time esta com a bola, mas EU NÃO estou com ela
				if (isBallPossession() && !isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
					// e não vou receber a bola
					if (selfPerc.getUniformNumber() == 7 && getPlayerRecebendo() != 7 && getPlayerRecebendo() != -1)
						// acompanha a bola com companheiro
						dash(new Vector2D(ballPos.getX() + 10, -15));
					// se nao for camisa 7, é o camisa 6 atacante, vai para o gol adversario
					else if (selfPerc.getUniformNumber() == 6 && getPlayerRecebendo() != 6
							&& getPlayerRecebendo() != -1)
						dash(new Vector2D(goalPos.getX() - 20, 15));
					// Colocar para ele verificar a posicao do ultimo jogador
				}
				if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) { // se estou perto da bola
					setBallPossession(true); // seta que nosso time esta com a bola
					setPlayerRecebendo(-1); // ninguém está marcado para receber a bola
					if (isPointsAreClose(ballPos, goalPos, 30)) {
						kickToPoint(goalPos, 100); // chuta para o gol
						setBallPossession(false);
					} else {
						// se o player mais perto do gol não sou eu
						PlayerPerception p = getClosestPlayerPoint(goalPos, selfPerc.getSide(), 2);
						if (p.getUniformNumber() != selfPerc.getUniformNumber()) {
							Vector2D posTemp = p.getPosition();
							if (!isOffside(posTemp, fieldPerc.getTeamPlayers(EFieldSide.invert(side)), side)) {
								turnToPoint(posTemp);
								Vector2D vTempF = posTemp.sub(selfPerc.getPosition());
								double intensity = (vTempF.magnitude() * 100) / 40;
								setPlayerRecebendo(p.getUniformNumber());
								kickToPoint(posTemp, intensity);
							} else {
								setBallPossession(true);
								kickToPoint(goalPos, 20); // conduz para o gol
							}
						} else {
							setBallPossession(true);
							kickToPoint(goalPos, 20); // conduz para o gol
						}
					}
				} else {
					// se não estou perto da bola, corre até ela
					pTemp = getClosestPlayerPoint(ballPos, side, 3);
					System.out.println("else perto bola;  pTemp:" + pTemp.getUniformNumber());
					if ((pTemp != null && pTemp.getUniformNumber() == selfPerc.getUniformNumber()
							&& getPlayerRecebendo() == -1) || getPlayerRecebendo() == selfPerc.getUniformNumber()) {
						// pega a bola
						dash(ballPos);
					} else if (!isPointsAreClose(selfPerc.getPosition(), initPos, 3)) {
						// recua
						// dash(initPos);
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
