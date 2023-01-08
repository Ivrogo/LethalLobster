package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.screens.GameScreen;
import cat.xtec.ioc.utils.Settings;

public class Lobster extends Actor {

    // Distintes posicions de la spacecraft, recta, pujant i baixant
    public static final int LOBSTER_STRAIGHT = 0;
    public static final int LOBSTER_UP = 1;
    public static final int LOBSTER_DOWN = 2;
    float runTime;

    // Paràmetres de la spacecraft
    private Vector2 position;
    private int width, height;
    private int direction;
    private GameScreen gameScreen;

    private boolean paused = false;
    private int lastDirection = LOBSTER_STRAIGHT;

    private Rectangle collisionRect;



    public Lobster(float x, float y, int width, int height, GameScreen gameScreen) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        this.gameScreen = gameScreen;

        // Inicialitzem la spacecraft a l'estat normal
        direction = LOBSTER_STRAIGHT;

        // Creem el rectangle de col·lisions
        collisionRect = new Rectangle();

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);


    }




    public void act(float delta) {
        super.act(delta);

        // Movem la spacecraft depenent de la direcció controlant que no surti de la pantalla
        switch (direction) {
            case LOBSTER_UP:
                if (this.position.y - Settings.LOBSTER_VELOCITY * delta >= 0) {
                    this.position.y -= Settings.LOBSTER_VELOCITY * delta;
                }
                break;
            case LOBSTER_DOWN:
                if (this.position.y + height + Settings.LOBSTER_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                    this.position.y += Settings.LOBSTER_VELOCITY * delta;
                }
                break;
            case LOBSTER_STRAIGHT:
                break;
        }

        collisionRect.set(position.x, position.y + 3, width, 10);
        setBounds(position.x, position.y, width, height);


    }

    // Getters dels atributs principals
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    // Canviem la direcció de la spacecraft: Puja
    public void goUp() {
        direction = LOBSTER_UP;
    }

    // Canviem la direcció de la spacecraft: Baixa
    public void goDown() {
        direction = LOBSTER_DOWN;
    }

    // Posem la spacecraft al seu estat original
    public void goStraight() {
        direction = LOBSTER_STRAIGHT;
    }

    // Obtenim el TextureRegion depenent de la posició de la spacecraft
   /* public TextureRegion getSpacecraftTexture() {

       switch (direction) {


            case LOBSTER_STRAIGHT:
                return AssetManager.spacecraft;
            case SPACECRAFT_UP:
                return AssetManager.spacecraftUp;
            case SPACECRAFT_DOWN:
                return AssetManager.spacecraftDown;
            default:
                return AssetManager.spacecraft;
        }
    } */

    public void reset() {

        // La posem a la posició inicial i a l'estat normal
        position.x = Settings.LOBSTER_STARTX;
        position.y = Settings.LOBSTER_STARTY;
        direction = LOBSTER_STRAIGHT;
        collisionRect = new Rectangle();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        runTime += Gdx.graphics.getDeltaTime();
        super.draw(batch, parentAlpha);
        batch.draw( (TextureRegion) AssetManager.lobsterAnim.getKeyFrame(runTime, true), position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    //TODO PREGUNTA 13 - Part 4 - Posem la llagosta en estat de pausa y la resumim
    public void pause() {
        this.paused = true;
    }

    public void resume(){
        this.paused = false;
    }

    // TODO PREGUNTA 11 - Part 2 - afegim l'objecte laser
    public void fire() {
        gameScreen.getScrollHandler().fireLaser(new Laser(position.x + width, position.y + height * 2/3, Settings.LASER_WIDTH, Settings.LASER_HEIGHT, Settings.LASER_SPEED));
    }
}
