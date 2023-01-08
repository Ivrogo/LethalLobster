package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import cat.xtec.ioc.objects.Lobster;
import cat.xtec.ioc.screens.GameScreen;

public class InputHandler implements InputProcessor {

    // Enter per a la gesitó del moviment d'arrastrar
    int previousY = 0;
    // Objectes necessaris
    private Lobster lobster;
    private GameScreen screen;
    private Vector2 stageCoord;

    private Stage stage;
    //TODO Pregunta 11 - Part 2 - enregistra el punter que mou la llagosta. Necessari per tal de poder
    // disparar a l'hora que movem la llagosta sense que el clic al botó de dispar ens influeixi en el
    // moviment de la mateixa.
    private int movementPointer;

    public InputHandler(GameScreen screen) {

        // Obtenim tots els elements necessaris
        this.screen = screen;
        lobster = screen.getLobster();
        stage = screen.getStage();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        switch (screen.getCurrentState()) {
            case READY:

                // Si fem clic comencem el joc
                screen.setCurrentState(GameScreen.GameState.RUNNING);
                break;
            case RUNNING:
                previousY = screenY;

                stageCoord = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
                Actor actorHit = stage.hit(stageCoord.x, stageCoord.y, true);
                if (actorHit != null) {
                    Gdx.app.log("HIT", actorHit.getName() + ", pointer: " + pointer);
                    //TODO PREGUNTA 13 - Part 4 - Si es fa clic sobre el botó pause pausem el joc
                    if (actorHit.getName().equals("pause")) {
                        screen.pauseGame();
                        //TODO PREGUNTA 11 - Part 2 - Si fem clic sobre el botó fire disparem un laser
                    } else if (actorHit.getName().equals("fire")) {
                        screen.getLobster().fire();
                        // Si es toca el botó fire no volem que es tracti com a movementPointer
                        if (movementPointer == pointer) {
                            movementPointer = -1;
                        }
                    }
                } else {
                    previousY = screenY;
                    Gdx.app.log("HIT ", "null, pointer: " + pointer);
                    movementPointer = pointer;
                }
                break;
            // Si l'estat és GameOver tornem a iniciar el joc
            case GAMEOVER:
                screen.reset();
                break;

            case PAUSE:
                screen.resumeGame();
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        // Quan deixem anar el dit acabem un moviment
        // i posem la nau en l'estat normal
        lobster.goStraight();
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Posem un umbral per evitar gestionar events quan el dit està quiet
        if (Math.abs(previousY - screenY) > 2)

            // Si la Y és major que la que tenim
            // guardada és que va cap avall
            if (previousY < screenY) {
                lobster.goDown();
            } else {
                // En cas contrari cap a dalt
                lobster.goUp();
            }
        // Guardem la posició de la Y
        previousY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
