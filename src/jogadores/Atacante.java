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
		int localState = 0;
		double xInit = -15, yInit = 0 + pos;
		EFieldSide side = selfPerc.getSide();
		Vector2D initPos = new Vector2D(xInit * side.value(), yInit);
		Vector2D goalPos = new Vector2D(50 * side.value(), 0);
		Vector2D ballPos;
		//PlayerPerception pTemp;
		while (true) {
			updatePerceptions();
			ballPos = fieldPerc.getBall().getPosition();
			switch (matchPerc.getState()) {
			
			case OFFSIDE_LEFT:
				if (side == EFieldSide.LEFT){

				}
				break;
			case OFFSIDE_RIGHT:
				if (side == EFieldSide.RIGHT){

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
			case GOAL_KICK_LEFT:
				if (side == EFieldSide.LEFT){

				}
				break;
			case GOAL_KICK_RIGHT: 
				if (side == EFieldSide.RIGHT){

				}
				break;
			
			case BEFORE_KICK_OFF:
				//System.out.println(initPos);
				//if (selfPerc.getPosition() != initPos)
					commander.doMoveBlocking(initPos.getX(), initPos.getY());
					dash(initPos, 100);
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
				String playerRecebendo ;
				switch (localState){
				
				case -1: //ir para posição inicial
					if(isPointsAreClose(selfPerc.getPosition(), initPos, 2)){
						turnToPoint(ballPos);
						localState = 0;
					} else {
						dash(initPos, 100);
					}
					break;
				
				case 0:  //posicao inicial
					System.out.println("Atacante "+ selfPerc.getUniformNumber() + " " + selfPerc.getTeam() + " " + localState);
					dash(initPos, 80);
					// se estou na minha posição inicial ou sou o player mais perto
					if (isPointsAreClose(selfPerc.getPosition(), initPos, 1) 
							|| getClosestPlayerPoint(ballPos, side, 1, 0).getUniformNumber() == selfPerc.getUniformNumber()) 
						localState = 1;
					else
						localState = 0;
					break;
				case 1: //interceptar a bola

					System.out.println("Atacante "+ selfPerc.getUniformNumber() + " " + selfPerc.getTeam() + " " + localState);
					//playerRecebendo = Mensagens.receiveMessage(selfPerc.getUniformNumber());
					//String ballPossession = ;
					//if (playerRecebendo != null && playerRecebendo.equals(String.valueOf(selfPerc.getUniformNumber()))) //se o player recebendo sou eu
					//	localState = 2;
					//Vector2D point1 = fieldPerc.getBall().getPosition();
					//System.out.println("Point 1: " + ballPos + selfPerc.getTeam());
//					try {
//						Thread.sleep(150); } catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					Vector2D point2 = fieldPerc.getBall().getPosition();
					//System.out.println("Point 2: "+ point2 + selfPerc.getTeam());
					
					double x = point2.getX() + (point2.getX() - ballPos.getX());//point2.getX() + (coefAng * (point2.getX() - point1.getX()));
					double y = point2.getY() + (point2.getY() - ballPos.getY()); //point2.getY() + (coefAng * (point2.getX() - point1.getX()));
					Vector2D point = new Vector2D(x, y); // novo ponto para calcular trajetoria
					if (getClosestPlayerPoint(point, side, 1, 0).getUniformNumber() == selfPerc.getUniformNumber()){ // se eu sou o player mais perto da trajetoria da bola
						dash(point, 100);
//						try {
//							Thread.sleep(100);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
					} else localState = 0; // se nao sou o player mais perto, volto pro estado 0
					
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 2)) // se alcancei a bola 
						localState = 3;
					
					break;
					
				case 2: //receber a bola

					System.out.println("Atacante "+ selfPerc.getUniformNumber() + " " + selfPerc.getTeam() + " " + localState);
					playerRecebendo = Mensagens.receiveMessage(selfPerc.getUniformNumber());
					if (playerRecebendo.equals(String.valueOf(selfPerc.getUniformNumber()))) //se o player recebendo sou eu, corre atras da bola
						dash(ballPos, 85);
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1))
						localState = 3;
					break;
					
				case 3: //ir em direção ao gol adversario

					System.out.println("Atacante "+ selfPerc.getUniformNumber() + " " + selfPerc.getTeam() + " " + localState);
					if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)){
						setBallPossession(true);
						setPlayerRecebendo(-1);
						//turnToPoint(goalPos);
						kickToPoint(goalPos, 25);
					}else 
						dash(ballPos, 100);
					if (isPointsAreClose(ballPos, goalPos, 30)) 
						localState = 4;
					break;
					
				case 4: //chutar em direção ao gol
					System.out.println("Atacante "+ selfPerc.getUniformNumber() + " " + selfPerc.getTeam() + " " + localState);
					if (isPointsAreClose(ballPos, goalPos, 30) && isPointsAreClose(selfPerc.getPosition(), ballPos, 1)){ 
						kickToPoint(goalPos, 100);
						setBallPossession(false);
					}else
						localState = 1;
					break;
				}
//				// se o time esta com a bola, mas EU NÃO estou com ela
//				if (isBallPossession() && !isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) {
//					// e não vou receber a bola
//					if (selfPerc.getUniformNumber() == 7 && getPlayerRecebendo() != 7 && getPlayerRecebendo() != -1)
//						// acompanha a bola com companheiro
//						dash(new Vector2D(ballPos.getX() + 10 * side.value(), -15), 70);
//					// se nao for camisa 7, é o camisa 6 atacante, vai para o gol adversario
//					else if (selfPerc.getUniformNumber() == 6 && getPlayerRecebendo() != 6
//							&& getPlayerRecebendo() != -1)
//						dash(new Vector2D(goalPos.getX() - 20 * side.value(), 15), 70);
//					// Colocar para ele verificar a posicao do ultimo jogador
//				}
//				if (isPointsAreClose(selfPerc.getPosition(), ballPos, 1)) { // se estou perto da bola
//					setBallPossession(true); // seta que nosso time esta com a bola
//					setPlayerRecebendo(-1); // ninguém está marcado para receber a bola
//					if (isPointsAreClose(ballPos, goalPos, 25)) {
//						kickToPoint(goalPos, 100); // chuta para o gol
//						setBallPossession(false);
//					} else {
//						// se o player mais perto do gol não sou eu
//						PlayerPerception p = getClosestPlayerPoint(goalPos, selfPerc.getSide(), 2, 0);
//						if (p.getUniformNumber() != selfPerc.getUniformNumber()) {
//							Vector2D posTemp = p.getPosition();
//							double distance = selfPerc.getPosition().distanceTo(posTemp);
//							if (!isOffside(posTemp, fieldPerc.getTeamPlayers(EFieldSide.invert(side)), side) && distance <= 20) {
//								turnToPoint(posTemp);
//								//Vector2D vTempF = posTemp.sub(selfPerc.getPosition());
//								double intensity = (posTemp.magnitude() * 100) / 20;
//								//System.out.println(posTemp.magnitude() + "*100 / 40 = "+ posTemp.magnitude()*100 / 40);
//								setPlayerRecebendo(p.getUniformNumber());
//								kickToPoint(posTemp, intensity);
//							} else {
//								setBallPossession(true);
//								kickToPoint(goalPos, 15); // conduz para o gol
//								
//							}
//						} else {
//							setBallPossession(true);
//							kickToPoint(goalPos, 15); // conduz para o gol
//						}
//					}
//				} else {
//					// se não estou perto da bola, corre até ela
//					pTemp = getClosestPlayerPoint(ballPos, side, 3, 0);
//					//System.out.println("else perto bola;  pTemp:" + pTemp.getUniformNumber());
//					if ((pTemp != null && pTemp.getUniformNumber() == selfPerc.getUniformNumber()
//							&& getPlayerRecebendo() == -1) || getPlayerRecebendo() == selfPerc.getUniformNumber()) {
//						// pega a bola
//						dash(ballPos, 95);
//					} else if (!isPointsAreClose(selfPerc.getPosition(), initPos, 3)) {
//						// recua
//						// dash(initPos);
//					} else {
//						// olha para a bola
//						turnToPoint(ballPos);
//					}
//				}
//				break;
			/* Todos os estados da partida */
			default:
				break;
			}
		}
	}

}

