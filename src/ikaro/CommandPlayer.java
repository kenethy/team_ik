package ikaro;

import jogadores.Armador;
import jogadores.Atacante;
import jogadores.Defensor;
import jogadores.Goleiro;
import simple_soccer_lib.PlayerCommander;

public class CommandPlayer extends Thread {

	private int LOOP_INTERVAL = 100; // 0.1s
	private Goleiro goleiro;
	private Defensor defensor1;
	private Defensor defensor2;
	private Armador armador1;
	private Armador armador2;
	private Atacante atacante1;
	private Atacante atacante2;

	public CommandPlayer(PlayerCommander player) {
		goleiro = new Goleiro(player);
		defensor1 = new Defensor(player);
		defensor2 = new Defensor(player);
		armador1 = new Armador(player);
		armador2 = new Armador(player);
		atacante1 = new Atacante(player);
		atacante2 = new Atacante(player);
		// commander = player;
	}

	@Override
	public void run() {
		System.out.println(">> Executando... ");
		long nextIteration = System.currentTimeMillis() + LOOP_INTERVAL;
		goleiro.updatePerceptions();
		defensor1.updatePerceptions();
		defensor2.updatePerceptions();
		armador1.updatePerceptions();
		armador2.updatePerceptions();
		atacante1.updatePerceptions();
		atacante2.updatePerceptions();

		/*
		 * goleiro.acaoGoleiro(nextIteration); defensor1.acaoDefensor(nextIteration);
		 * defensor2.acaoDefensor(nextIteration); armador1.acaoArmador(nextIteration,
		 * -15); armador2.acaoArmador(nextIteration, 15);
		 * atacante1.acaoAtacante(nextIteration, 15);
		 * atacante2.acaoAtacante(nextIteration, -15);
		 */
		//System.out.println(goleiro.selfPerc.getUniformNumber());
		if (goleiro.selfPerc.getUniformNumber() == 1)
			goleiro.acaoGoleiro(nextIteration);
		if (defensor1.selfPerc.getUniformNumber() == 2)
			defensor1.acaoDefensor(nextIteration, -15);
		if (defensor2.selfPerc.getUniformNumber() == 3)
			defensor2.acaoDefensor(nextIteration, 15);
		if (armador1.selfPerc.getUniformNumber() == 4)
			armador1.acaoArmador(nextIteration, 0);
		if (armador2.selfPerc.getUniformNumber() == 5)
			armador2.acaoArmador(nextIteration, 15);
		if (atacante1.selfPerc.getUniformNumber() == 6)
			atacante1.acaoAtacante(nextIteration, 15);
		if (atacante2.selfPerc.getUniformNumber() == 7)
			atacante2.acaoAtacante(nextIteration, -15);

		/*
		 * switch (goleiro.selfPerc.getUniformNumber()) { case 1:
		 * goleiro.acaoGoleiro(nextIteration); break; default: break; }
		 * switch(defensor1.selfPerc.getUniformNumber()){ case 2:
		 * defensor1.acaoDefensor(nextIteration, -15); break; default: break; }
		 * 
		 * case 2: defensor1.acaoDefensor(nextIteration, -15); break; //na pos (2o
		 * parametro), 0 é a linha horizontal com o meio do campo, nesse parametro, o
		 * -10 é somado ao 0 case 3: defensor2.acaoDefensor(nextIteration, 15); break;
		 * case 4: armador1.acaoArmador(nextIteration, 0); break; case 5:
		 * armador2.acaoArmador(nextIteration, 0); break; case 6:
		 * atacante1.acaoAtacante(nextIteration, 15); break; case 7:
		 * atacante2.acaoAtacante(nextIteration, -15); break; default : break;
		 */
	}

}
