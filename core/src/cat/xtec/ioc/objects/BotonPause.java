package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings.Status;

public class BotonPause extends Actor {

    // Paràmetres del botó pause
    private Vector2 position;
    private int width, height;
    private Status status;

    public BotonPause(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);

        //TODO PREGUNTA 13 - Part 4 -  D'entrada es mostará aquest botó
        status = Status.SHOWN;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        switch (status) {
            case SHOWN:
                batch.draw(AssetManager.botonPause, position.x, position.y, width, height);
                break;
            //TODO PREGUNTA 13 - Part 4 - Amaguem el botó
            case HIDDEN:
                break;
        }
    }


    public void setStatus(Status status) {
        this.status = status;
    }

}
