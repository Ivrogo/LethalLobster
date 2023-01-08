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

public class Submarine extends Scrollable {

    private Circle collisionCircle;

    Random r;

    int assetSpaceship;
    float runTime;

    public Submarine(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        // Creem el cercle
        collisionCircle = new Circle();

        /* Accions */
        r = new Random();
        assetSpaceship = r.nextInt(8);

        setOrigin();


        // Equivalent:
        // this.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.rotateBy(-90f, 0.2f)));

    }

    public void setOrigin() {

        this.setOrigin(width/2 + 1, height/2);

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Actualitzem el cercle de col·lisions (punt central de l'asteroid i el radi.
        collisionCircle.set(position.x + width / 2.0f, position.y + width / 2.0f, width / 2.0f);


    }

    @Override
    public void reset(float newX) {
        super.reset(newX);
        // Obtenim un número al·leatori entre MIN i MAX
        float newSize = Methods.randomFloat(Settings.MIN_SUBMARINE, Settings.MAX_SUBMARINE);
        // Modificarem l'alçada i l'amplada segons l'al·leatori anterior
        width = height = 34 * newSize;
        // La posició serà un valor aleatòri entre 0 i l'alçada de l'aplicació menys l'alçada
        position.y =  new Random().nextInt(Settings.GAME_HEIGHT - (int) height);

        assetSpaceship = r.nextInt(8);
        setOrigin();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        runTime += Gdx.graphics.getDeltaTime();
        super.draw(batch, parentAlpha);
        //batch.draw(AssetManager.asteroid[assetAsteroid], position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
        batch.draw( (TextureRegion) AssetManager.submarineAnim.getKeyFrame(runTime, true), position.x, position.y, width, height);
    }

    // Retorna true si hi ha col·lisió
    public boolean collides(Lobster nau) {

        if (position.x <= nau.getX() + nau.getWidth()) {
            // Comprovem si han col·lisionat sempre i quan l'asteroid estigui a la mateixa alçada que la spacecraft
            return (Intersector.overlaps(collisionCircle, nau.getCollisionRect()));
        }
        return false;
    }

    // TODO PREGUNTA 11 - Part 2 - Metode de colisió entre el submari i el laser
    public boolean collides(Laser laser) {
        if (position.x <= laser.getX() + laser.getWidth()) {
            return (Intersector.overlaps(collisionCircle, laser.getCollisionRect()));
        }
        return false;
    }

}
