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
	
	public PlayerBase(PlayerCommander player) {
		commander = player;
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

	protected void dash(Vector2D point) {
		if (selfPerc.getPosition().distanceTo(point) <= 1)
			return;
		if (!isAlignToPoint(point, 15))
			turnToPoint(point);
		commander.doDashBlocking(70);
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

	protected PlayerPerception getClosestPlayerPoint(Vector2D point, EFieldSide side, double margin) {
		ArrayList<PlayerPerception> lp = fieldPerc.getTeamPlayers(side);
		PlayerPerception np = null;
		if (lp != null && !lp.isEmpty()) {
			double dist, temp;
			dist = lp.get(0).getPosition().distanceTo(point);
			np = lp.get(0);
			if (isPointsAreClose(np.getPosition(), point, margin))
				return np;
			for (PlayerPerception p : lp) {
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
		return np;
	}
}
