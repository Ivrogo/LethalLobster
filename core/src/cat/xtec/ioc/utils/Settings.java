package cat.xtec.ioc.utils;

public class Settings {

    // Mida del joc, s'escalarà segons la necessitat
    public static final int GAME_WIDTH = 480;
    public static final int GAME_HEIGHT = 140;

    //TODO PREGUNTA 10 - PART 1 - Propietats de la llagosta
    public static final float LOBSTER_VELOCITY = 75;
    public static final int LOBSTER_WIDTH = 36;
    public static final int LOBSTER_HEIGHT = 15;
    public static final float LOBSTER_STARTX = 20;
    public static final float LOBSTER_STARTY = GAME_HEIGHT/2 - LOBSTER_HEIGHT/2;

    //TODO PREGUNTA 10 - PART 1 - Propietats dels submarins
    public static final float MAX_SUBMARINE = 0.95f;
    public static final float MIN_SUBMARINE = 0.5f;

    //TODO EXERCICI 3 - Propietats de les monedes
    public static final float MAX_COINS = 0.5f;
    public static final float MIN_COINS = 0.25f;


    //TODO PREGUNTA 10 - PART 1 - Velocitat y gap dels submarins aixi com del fons
    public static final int SUBMARINE_SPEED = -150;
    public static final int SUBMARINE_GAP = 75;
    public static final int BG_SPEED = -100;

    //TODO PREGUNTA 13 - PART 4 - Propietats del botó de Pausa
    public static final int PAUSE_BUTTON_WIDTH = 25;
    public static final int PAUSE_BUTTON_HEIGHT = 25;
    public static final float PAUSE_BUTTON_X = GAME_WIDTH - PAUSE_BUTTON_WIDTH;
    public static final float PAUSE_BUTTON_Y = 0;

    //TODO PREGUNTA 11 - PART 2 - Propietats de disparar
    public static int LASER_SPEED = 200;
    public static int LASER_WIDTH = 10;
    public static int LASER_HEIGHT = 2;

    //TODO PREGUNTA 10 - PART 1 - Nombre maxim de submarins a la pantalla simultaneament y propietats dels intervals
    public static int MAX_SUBMARINE_NUMBER = 4;
    public static final float MAX_SUBMARINE_INTERVAL = 0.5f;
    public static final float MIN_SUBMARINE_INTERVAL = 0.25f;

    //TODO PREGUNTA 11 - PART 2 - Propietats del botó FIRE
    public static final int FIRE_BUTTON_WIDTH = 50;
    public static final int FIRE_BUTTON_HEIGHT = 50;
    public static final float FIRE_BUTTON_X = GAME_WIDTH - FIRE_BUTTON_WIDTH;
    public static final float FIRE_BUTTON_Y = GAME_HEIGHT - FIRE_BUTTON_HEIGHT;

    //Explosió
    public static float EXPLOSION_FRAME_DURATION = .05f;
    public static float EXPLOSION_FRAMES = 16;


    // TODO PREGUNTA 12 - PART 3 - Propietats per la moneda
    public static final int SCORE_INCREASE = 10; // s'incrementa en 100 cada cop que toca una moneda
    public static final int BONUS_SCORE_INCREASE = 30;
    public static int MAX_COIN_NUMBER = 4;
    public static final float MAX_COIN_INTERVAL = 1.75f;
    public static final float MIN_COIN_INTERVAL = 1.0f;
    public static final int COIN_SPEED = -100;
    public static final int BONUS_COIN_SPEED = -175;
    public static final int NORMAL_COIN_CHANCE = 70;

    //TODO PREGUNTA 11 y PREGUNTA 13 - PART 2 Y 4 - Propietats dels botons
    public static enum Status {
        SHOWN, HIDDEN
    }

}
