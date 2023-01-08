package cat.xtec.ioc.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;

import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class ScrollHandler extends Group {

    // Fons de pantalla
    Background bg, bg_back;

    // Asteroides
    int numSubmarines;
    int numCoins;
    private ArrayList<Submarine> submarines;
    private static ArrayList<Coins> coins;


    //Arrays de monedes, laser y submarins
    private DelayedRemovalArray<Laser> llistaLaser;
    private DelayedRemovalArray<Submarine> submarineDown;
    private DelayedRemovalArray<Coins> llistaCoins;

    // Objecte Random
    Random r;

    //Temps y interval
    long startCoinTime = TimeUtils.nanoTime();
    long startSubTime = TimeUtils.nanoTime();
    float randomInterval;


    public ScrollHandler() {

        // Creem els dos fons
        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);

        // Afegim els fons al grup
        addActor(bg);
        addActor(bg_back);

        // Creem l'objecte random
        r = new Random();

        //Començem amb 3 monedes y submarins
        numSubmarines = 3;
        numCoins = 3;

        //Inicialitzem
        llistaLaser = new DelayedRemovalArray<Laser>();
        submarineDown = new DelayedRemovalArray<Submarine>();
        llistaCoins = new DelayedRemovalArray<Coins>();

        //Arraylist de monedes y submarins
        submarines = new ArrayList<Submarine>();
        coins = new ArrayList<Coins>();

        // Afegim un nou asteroide de mida i posició y aleatoria al array i a l'stage
        float newSize = Methods.randomFloat(Settings.MIN_SUBMARINE, Settings.MAX_SUBMARINE) * 34;
        Submarine sub = new Submarine(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.SUBMARINE_SPEED);
        submarines.add(sub);
        addActor(sub);

        for (int i = 1; i < numSubmarines; i++) {
            newSize = Methods.randomFloat(Settings.MIN_SUBMARINE, Settings.MAX_SUBMARINE) * 34;
            sub = new Submarine(submarines.get(submarines.size() - 1).getTailX() + Settings.SUBMARINE_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.SUBMARINE_SPEED);
            submarines.add(sub);
            addActor(sub);
        }

        //Afegim una nova moneda de mida y posició y aleatoria al array i a l'stage
        int coinSize = 15;
        Coins a = new Coins(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) coinSize), coinSize, coinSize, Settings.COIN_SPEED);
        coins.add(a);
        addActor(a);

        for (int i = 1; i < numCoins; i++) {
            a = new Coins(coins.get(coins.size() - 1).getTailX() + Settings.SUBMARINE_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) coinSize), coinSize, coinSize, Settings.COIN_SPEED);
            coins.add(a);
            addActor(a);
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Si algun element està fora de la pantalla, fem un reset de l'element.
        if (bg.isLeftOfScreen()) {
            bg.reset(bg_back.getTailX());

        } else if (bg_back.isLeftOfScreen()) {
            bg_back.reset(bg.getTailX());

        }

        // TODO PREGUNTA 10 - Part 1 - Creem un respawn de submarins per intervals de temps random
        float elapsedSubTime = MathUtils.nanoToSec * (TimeUtils.nanoTime() - startSubTime);
        if (elapsedSubTime >= randomInterval) {
            // Generam un nou interval de temps aleatori entre el mínim i el màxim
            this.randomInterval = MathUtils.random(Settings.MIN_SUBMARINE_INTERVAL, Settings.MAX_SUBMARINE_INTERVAL);
            // Tan sols si hi ha 4 o menys submarins en generem un de nou
            if (submarineDown.size < Settings.MAX_SUBMARINE_NUMBER) {
                // Quan es genera un nou submari es reseteja el startTime
                startSubTime = TimeUtils.nanoTime();
                // Afegim un nou submari de mida i posició y aleatoria al array i a l'stage
                float newSize = Methods.randomFloat(Settings.MIN_SUBMARINE, Settings.MAX_SUBMARINE) * 34;
                Submarine a = new Submarine(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.SUBMARINE_SPEED);
                submarines.add(a);
                addActor(a);
            }
        }
        // TODO PREGUNTA 12 - Part 3 - Creem un respawn de monedes per intervals de temps random
        float elapsedCoinTime = MathUtils.nanoToSec * (TimeUtils.nanoTime() - startCoinTime);
        if (elapsedCoinTime >= randomInterval) {
            // Generam un nou interval de temps aleatori entre el mínim i el màxim
            this.randomInterval = MathUtils.random(Settings.MIN_COIN_INTERVAL, Settings.MAX_COIN_INTERVAL);
            // Tan sols si hi ha 4 o menys submari en generem un de nou
            if (llistaCoins.size < Settings.MAX_COIN_NUMBER) {
                // Quan es genera un nou submari es reseteja el startTime
                startCoinTime = TimeUtils.nanoTime();
                // Afegim un nou submari de mida i posició y aleatoria al array i a l'stage
                int newSize = 15 ;
                Coins a = new Coins(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.COIN_SPEED);
                coins.add(a);
                addActor(a);
            }
        }



        //Controlem els lasers que surten per la part dreta
        for (Laser laser : llistaLaser) {
            if(laser.isRightOfScreen()) {
                removeActor(laser);
                llistaLaser.removeValue(laser, true);
            }
        }
    }

    public boolean collides(Lobster llagosta) {

        // Comprovem les col·lisions entre cada asteroid i la nau
        for (Submarine submarine : submarines) {
            if (submarine.collides(llagosta)) {
                return true;
            }
        }
        return false;
    }


    //TODO PREGUNTA 11 - Part 2 - Quan un laser colisiona amb un submari es borren els dos del stage,
    // es reprodueix el so d'explosió i retornem el submari per a que es faci la animació de explosió
    public Submarine submarineDestroyed() {
        for (Submarine submarine : submarines) {
            for (Laser laser : llistaLaser) {
                if (submarine.collides(laser)) {
                    removeActor(submarine);
                    removeActor(laser);
                    llistaLaser.removeValue(laser, true);
                    submarines.remove(submarine);
                    AssetManager.explosionSound.play();
                    return  submarine;
                }
            }
        }
        return null;
    }

    public Coins coinTaken(Lobster llagosta) {
        for (Coins coin : coins) {
                if (coin.collides(llagosta)) {
                    removeActor(coin);
                    coins.remove(coin);
                    AssetManager.coinSound.play();
                    return coin;
                }
            }
            return null;
        }

    public void reset() {

        for (Submarine submarine : submarines) {
            removeActor(submarine);
        }
        submarines.clear();

        if (coins.size() > 0) {
            coins.get(0).reset(Settings.GAME_WIDTH);
            for (int i = 1; i < coins.size(); i++) {
                coins.get(i).reset(coins.get(i - 1).getTailX() + Settings.SUBMARINE_GAP);
            }
        }
    }





    public ArrayList<Submarine> getSubmarines() {
        return submarines;
    }

    public static ArrayList<Coins> getCoins() {
        return coins;
    }

    // TODO PREGUNTA 11 - Part 2 - Afegim el laser a l'array y al stage
    public void fireLaser(Laser laser) {
        addActor(laser);
        llistaLaser.add(laser);
    }

}
