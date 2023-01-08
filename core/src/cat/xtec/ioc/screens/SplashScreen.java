package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import cat.xtec.ioc.LethalLobster;
import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;


public class SplashScreen implements Screen {

    private Stage stage;
    private LethalLobster game;
    private Batch batch;

    private Label.LabelStyle textStyle;
    private Label textLbl;
    private Label.LabelStyle scoreStyle;
    private Label scoreLbl;
    private float runTime = 0;
    private int positionX = 0 - Settings.LOBSTER_WIDTH;




    public SplashScreen(LethalLobster game) {

        this.game = game;

        //TODO Pregunta 14 - Part 5 - Afegim al label la informació de la taula high-score a prefs.
        Preferences pref = Gdx.app.getPreferences("high-score");
        int highestScore = pref.getInteger("high-score", 0);
        scoreStyle = new Label.LabelStyle(AssetManager.font, Color.RED);
        scoreLbl = new Label("Highest Score: " + highestScore, scoreStyle);


        // Creem la càmera de les dimensions del joc
        OrthographicCamera camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        // Posant el paràmetre a true configurem la càmera per a
        // que faci servir el sistema de coordenades Y-Down
        camera.setToOrtho(true);

        // Creem el viewport amb les mateixes dimensions que la càmera
        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

        // Creem l'stage i assginem el viewport
        stage = new Stage(viewport);
        batch = stage.getBatch();

        // Afegim el fons
        stage.addActor(new Image(AssetManager.background));

        // Creem l'estil de l'etiqueta i l'etiqueta
        textStyle = new Label.LabelStyle(AssetManager.font, null);
        textLbl = new Label("Lethal Lobster", textStyle);


        // Creem el contenidor necessari per aplicar-li les accions
        Container container = new Container(textLbl);
        container.setTransform(true);
        container.center();
        container.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2);

        //TODO PREGUNTA 14 - Part 5 - Creem el contenidor necessari per aplicar-li les accions
        Container containerScore = new Container(scoreLbl);
        containerScore.setTransform(true);
        containerScore.center();
        containerScore.setPosition(Settings.GAME_WIDTH / 2, scoreLbl.getHeight() - 5);

        // Afegim les accions de escalar: primer es fa gran i després torna a l'estat original ininterrompudament
        container.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.5f, 1.5f, 1), Actions.scaleTo(1, 1, 1))));
        containerScore.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.5f, 1.5f, 1), Actions.scaleTo(1, 1, 1))));
        stage.addActor(container);
        stage.addActor(containerScore);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        stage.draw();
        stage.act(delta);
        batch.begin();
        batch.draw((TextureRegion) AssetManager.lobsterAnim.getKeyFrame(runTime, true), positionX, Settings.GAME_HEIGHT / 2, Settings.LOBSTER_WIDTH, Settings.LOBSTER_HEIGHT);
        batch.end();
        if (positionX > Settings.GAME_WIDTH){
            positionX = 0 - Settings.LOBSTER_WIDTH;
        } else {
            positionX += 1;
        }
        runTime += delta;

        // Si es fa clic en la pantalla, canviem la pantalla
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport()));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
