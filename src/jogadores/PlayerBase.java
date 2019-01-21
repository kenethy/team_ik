package jogadores;

import java.util.ArrayList;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.FieldPerception;
import simple_soccer_lib.perception.MatchPerception;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class PlayerBase {
	protected PlayerCommander commander;
	public PlayerPerception selfPerc;
	protected FieldPerception fieldPerc;
	protected MatchPerception matchPerc;
	protected int playerRecebendo = -1;
	protected boolean ballPossession = false;
	
	//Mensagens
	protected String trueBallPossession = "temos a bola";
	protected String falseBallPossession = "perdemos a bola";
	protected String passeParaVoce = "passe pra vc";

	public PlayerBase(PlayerCommander player) {
		commander = player;
	}

	protected boolean isBallPossession() {
		return this.ballPossession;
	}

	protected void setBallPossession(boolean ballPossession) {
		this.ballPossession = ballPossession;
	}

	protected int getPlayerRecebendo() {
		return playerRecebendo;
	}

	protected void setPlayerRecebendo(int playerRecebendo) {
		this.playerRecebendo = playerRecebendo;
	}

	public void updatePerceptions() {
		PlayerPerception newSelf = commander.perceiveSelfBlocking();
		FieldPerception newField = commander.perceiveFieldBlocking();
		MatchPerception newMatch = commander.perceiveMatchBlocking();
		if (newSelf != null)
			this.selfPerc = newSelf;
		if (newField != null)
			this.fieldPerc = newField;
		if (newMatch != null)
			this.matchPerc = newMatch;
	}

	protected void turnToPoint(Vector2D point) {
		Vector2D newDirection = point.sub(selfPerc.getPosition());
		commander.doTurnToDirectionBlocking(newDirection);
	}

	protected void dash(Vector2D point, int speed) {
		if (selfPerc.getPosition().distanceTo(point) <= 1)
			return;
		if (!isAlignToPoint(point, 15))
			turnToPoint(point);
		//commander.doMove(x, y)
		commander.doDashBlocking(speed);
	}

	protected boolean isAlignToPoint(Vector2D point, double margin) {
		double angle = point.sub(selfPerc.getPosition()).angleFrom(selfPerc.getDirection());
		return angle < margin && angle > margin * (-1);
	}

	protected void kickToPoint(Vector2D point, double intensity) {
		Vector2D newDirection = point.sub(selfPerc.getPosition());
		double angle = newDirection.angleFrom(selfPerc.getDirection());
		if (angle > 90 || angle < -90) {
			commander.doTurnToDirectionBlocking(newDirection);
			angle = 0;
		}
		commander.doKickBlocking(intensity, angle);
	}

	protected boolean isPointsAreClose(Vector2D reference, Vector2D point, double margin) {
		return reference.distanceTo(point) <= margin;
	}

	//ignoreUniform para que a função ignore o uniforme passado como parametro. Se for 0, não irá ignorar nenhum
	protected PlayerPerception getClosestPlayerPoint(Vector2D point, EFieldSide side, double margin, int ignoreUniform) {
		ArrayList<PlayerPerception> lp = fieldPerc.getTeamPlayers(side);
		PlayerPerception np = null;
		if (lp != null && !lp.isEmpty()) {
			double dist=0, temp;
			dist = lp.get(0).getPosition().distanceTo(point);
			np = lp.get(0);

			if (isPointsAreClose(np.getPosition(), point, margin))
				return np;
			for (PlayerPerception p : lp) {
				if (p.getUniformNumber() != ignoreUniform){
					if (p.getPosition() == null)
						break;
					if (isPointsAreClose(p.getPosition(), point, margin))
						return p;
					temp = p.getPosition().distanceTo(point);
					if (temp < dist) {
						dist = temp;
						np = p;
					}
				}
			}
		}
		return np;
	}
	
	protected PlayerPerception getClosestPlayerBallTrajeto(EFieldSide side, double margin, int ignoreUniform) {
		Vector2D point1 = fieldPerc.getBall().getPosition();
		try {
			Thread.sleep(100); } catch (InterruptedException e) {
			e.printStackTrace();
		}
		Vector2D point2 = fieldPerc.getBall().getPosition();
		
		//double coefAng = Math.round(point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
		double x = point2.getX() + (point2.getX() - point1.getX());//point2.getX() + (coefAng * (point2.getX() - point1.getX()));
		double y = point2.getY() + (point2.getY() - point1.getY()); //point2.getY() + (coefAng * (point2.getX() - point1.getX()));
		Vector2D point = new Vector2D(x, y); // novo ponto para calcular trajetoria
		System.out.println("\n");
		System.out.println("Point 2" + point2);
		System.out.println("Point 1" + point1);
		System.out.println("Ponto trajetoria: " + point);
		System.out.println("\n");
		ArrayList<PlayerPerception> lp = fieldPerc.getTeamPlayers(side);
		PlayerPerception np = null;
		if (lp != null && !lp.isEmpty()) {
			double dist=0, temp;
			dist = lp.get(0).getPosition().distanceTo(point);
			np = lp.get(0);

			if (isPointsAreClose(np.getPosition(), point, margin))
				return np;
			for (PlayerPerception p : lp) {
				if (p.getUniformNumber() != ignoreUniform){
					if (p.getPosition() == null)
						break;
					if (isPointsAreClose(p.getPosition(), point, margin))
						return p;
					temp = p.getPosition().distanceTo(point);
					if (temp < dist) {
						dist = temp;
						np = p;
					}
				}
			}
		}
		return np;
	}

	/**
	 * Retorna se esta impedido Verifica se existe um jogador adversario a sua
	 * frente
	 * 
	 * @param playerReceive
	 * @param margin
	 * @param lp
	 * @param mySide
	 * @return
	 */
	protected boolean isOffside(Vector2D playerReceive, ArrayList<PlayerPerception> lp, EFieldSide mySide) {

		for (PlayerPerception p : lp) {
			if (p.getPosition() == null)
				break;
			// Se a posicao do jogador adversario esta entre o atacante e o goleiro
			if (mySide.equals(EFieldSide.LEFT)) {
				if (playerReceive.getX() < p.getPosition().getX() && !p.isGoalie())
					return false;
			} else {
				if (playerReceive.getX() > p.getPosition().getX() && !p.isGoalie())
					return false;
			}
		}
		return true;
	}
	
	
	/**
	 * TODO melhorar o posicionamento em relação a bola (pois ele esta indo para a
	 * posição da bola pra chutar na direção certa
	 * @param ballPos
	 * @param side
	 */
	protected void correrEChutar(Vector2D ballPos, EFieldSide side) {
		PlayerPerception playerMaisProximo = getClosestPlayerPoint(ballPos, side, 2, 0);
		PlayerPerception amigoMaisProximo = getClosestPlayerPoint(ballPos, side, 2, playerMaisProximo.getUniformNumber());
		if (amigoMaisProximo.getUniformNumber() == selfPerc.getUniformNumber())
			dash(ballPos, 60);
		if (playerMaisProximo.getUniformNumber() == selfPerc.getUniformNumber()) { // se o player mais proximo sou eu
			// corre ate a bola
			dash(ballPos, 90);
			// se estou na posicao da bola
			if (isPointsAreClose(selfPerc.getPosition(), ballPos, 2)) {
				dash(new Vector2D(selfPerc.getDirection().getX() + 2, selfPerc.getPosition().getY() + 2), 100);
				// chuta para o amigo mais perto
				turnToPoint(amigoMaisProximo.getPosition());
				double intensity = 100 * (selfPerc.getPosition().distanceTo(amigoMaisProximo.getPosition()) / 30);
				System.out.println("Distancia do passe: " + selfPerc.getPosition().distanceTo(amigoMaisProximo.getPosition()) + " Intensidade: "+ intensity);
				kickToPoint(amigoMaisProximo.getPosition(), intensity);
				setPlayerRecebendo(amigoMaisProximo.getUniformNumber());
				Mensagens.sendMessage(amigoMaisProximo.getUniformNumber(), passeParaVoce);
			}
		}
		
	}
}
