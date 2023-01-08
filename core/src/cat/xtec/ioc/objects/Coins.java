package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;


import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class Coins extends Scrollable {

    private Circle collisionCircle;

    Random r;

    int assetCoins;
    float runTime;
    int randomNumber;
    boolean isBonus;

    public Coins(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        // Creem el cercle
        collisionCircle = new Circle();

        /* Accions */
        r = new Random();
        assetCoins = r.nextInt(6);

        isBonus = r.nextInt(100) > Settings.NORMAL_COIN_CHANCE ? true : false;
        this.velocity = isBonus ? Settings.BONUS_COIN_SPEED : Settings.COIN_SPEED;

        setOrigin();



        // Equivalent:
        // this.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.rotateBy(-90f, 0.2f)));

    }

    public void setOrigin() {

        this.setOrigin(width / 2 + 1, height / 2);

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Actualitzem el cercle de col·lisions (punt central de l'asteroid i el radi.
        collisionCircle.set(position.x + width / 2.0f, position.y + width / 2.0f, width / 2.0f);
        runTime += Gdx.graphics.getDeltaTime();


    }

    @Override
    public void reset(float newX) {
        super.reset(newX);
        // La posició serà un valor aleatòri entre 0 i l'alçada de l'aplicació menys l'alçada
        position.y = new Random().nextInt(Settings.GAME_HEIGHT - (int) height);

        setOrigin();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        runTime += Gdx.graphics.getDeltaTime();
        super.draw(batch, parentAlpha);
        //batch.draw(AssetManager.asteroid[assetAsteroid], position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
        if (isBonus) {
            batch.draw( (TextureRegion) AssetManager.bonusCoinsAnim.getKeyFrame(runTime, true), this.position.x, this.position.y, width, height);
        } else {
            batch.draw( (TextureRegion) AssetManager.coinsAnim.getKeyFrame(runTime, true), this.position.x, this.position.y, width, height);
        }
    }

    // Retorna true si hi ha col·lisió
    public boolean collides(Lobster llagosta) {

        if (position.x <= llagosta.getX() + llagosta.getWidth()) {
            // Comprovem si han col·lisionat sempre i quan l'asteroid estigui a la mateixa alçada que la spacecraft
            return (Intersector.overlaps(collisionCircle, llagosta.getCollisionRect()));
        }
        return false;
    }

    //Retorna si la moneda es bonus o no
    public  boolean isBonus() {
        return isBonus;
    }
}