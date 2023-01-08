package cat.xtec.ioc.screens;

import static cat.xtec.ioc.utils.Settings.EXPLOSION_FRAMES;
import static cat.xtec.ioc.utils.Settings.EXPLOSION_FRAME_DURATION;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.BotonFire;
import cat.xtec.ioc.objects.BotonPause;
import cat.xtec.ioc.objects.Coins;
import cat.xtec.ioc.objects.Submarine;
import cat.xtec.ioc.objects.Lobster;
import cat.xtec.ioc.objects.ScrollHandler;
import cat.xtec.ioc.utils.Settings;



public class GameScreen implements Screen {

    // Els estats del joc

    public enum GameState {

        READY, RUNNING, GAMEOVER, PAUSE

    }

    private GameState currentState;

    // Objectes necessaris
    private Stage stage;
    private Lobster lobster;
    private ScrollHandler scrollHandler;
    private Label.LabelStyle scoreStyle;
    private Label scoreLabel;
    private static int score;

    //Botó de Pause
    private BotonPause botonPause;

    //Botó de Fire
    private BotonFire botonFire;

    //Iniciem els arrays per les explosions de la colisio del submari amb el laser
    private DelayedRemovalArray<Submarine> explosiveSubmarine;
    private DelayedRemovalArray<Float> explosionsTimes;

    // Encarregats de dibuixar elements per pantalla
    private ShapeRenderer shapeRenderer;
    private Batch batch;

    // Per controlar l'animació de l'explosió
    private float explosionTime = 0;

    // Preparem el textLayout per escriure text
    private GlyphLayout textLayout;



    public GameScreen(Batch prevBatch, Viewport prevViewport) {

        // Iniciem la música
        AssetManager.music.play();

        // Creem el ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);

        batch = stage.getBatch();

        //Creem el label amb l'score
        scoreStyle = new Label.LabelStyle(AssetManager.font, Color.RED);
        scoreLabel = new Label("Score: " + score, scoreStyle);

        // Creem la nau i la resta d'objectes
        lobster = new Lobster(Settings.LOBSTER_STARTX, Settings.LOBSTER_STARTY, Settings.LOBSTER_WIDTH, Settings.LOBSTER_HEIGHT, this);
        scrollHandler = new ScrollHandler();

        //TODO PREGUNTA 13 - Part 4 - Creació del botó Pause
        botonPause = new BotonPause(Settings.PAUSE_BUTTON_X, Settings.PAUSE_BUTTON_Y, Settings.PAUSE_BUTTON_WIDTH, Settings.PAUSE_BUTTON_HEIGHT);

        //TODO PREGUNTA 11 - Part 2 - Creació del botó FIRE
        botonFire = new BotonFire(Settings.FIRE_BUTTON_X, Settings.FIRE_BUTTON_Y, Settings.FIRE_BUTTON_WIDTH, Settings.FIRE_BUTTON_HEIGHT);

        // Afegim els actors a l'stage
        stage.addActor(scrollHandler);
        stage.addActor(lobster);
        stage.addActor(scoreLabel);

        // Donem nom a l'Actor
        lobster.setName("lobster");

        //TODO PREGUNTA 13 - Part 4 - Afegim el boto pause a l'stage i li donem un nom
        stage.addActor(botonPause);
        botonPause.setName("pause");


        stage.addActor(botonFire);
        botonFire.setName("fire");


        explosiveSubmarine = new DelayedRemovalArray<Submarine>();
        explosionsTimes = new DelayedRemovalArray<Float>();

        // Iniciem el GlyphLayout
        textLayout = new GlyphLayout();
        textLayout.setText(AssetManager.font, "Are you\nready?");

        currentState = GameState.READY;

        // Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));

    }

    private void drawElements() {

        // Recollim les propietats del Batch de l'Stage
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Pintem el fons de negre per evitar el "flickering"
        //Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Inicialitzem el shaperenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Definim el color (verd)
        shapeRenderer.setColor(new Color(0, 1, 0, 1));

        // Pintem la nau
        shapeRenderer.rect(lobster.getX(), lobster.getY(), lobster.getWidth(), lobster.getHeight());

        // Recollim tots els Submarine
        ArrayList<Submarine> submarines = scrollHandler.getSubmarines();
        Submarine submarine;

        for (int i = 0; i < submarines.size(); i++) {

            submarine = submarines.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.circle(submarine.getX() + submarine.getWidth() / 2, submarine.getY() + submarine.getWidth() / 2, submarine.getWidth() / 2);
        }
        shapeRenderer.end();

        //TODO Pregunta 12 - Part 3 - Recollim totes les monedes
        ArrayList<Coins> coins = ScrollHandler.getCoins();
        Coins coin;

        for (int i = 0; i < coins.size(); i++) {

            coin = coins.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.circle(coin.getX() + coin.getWidth() / 2, coin.getY() + coin.getWidth() / 2, coin.getWidth() / 2);
        }
        shapeRenderer.end();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // Dibuixem tots els actors de l'stage
        stage.draw();

        // Depenent de l'estat del joc farem unes accions o unes altres
        switch (currentState) {

            case GAMEOVER:
                updateGameOver(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case READY:
                updateReady();
                break;


        }

        //drawElements();

    }

    private void updateReady() {

        // Dibuixem el text al centre de la pantalla
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        //stage.addActor(textLbl);
        batch.end();

    }

    private void updateRunning(float delta) {
        stage.act(delta);
        if (scrollHandler.collides(lobster)) {
            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("lobster").remove();
            textLayout.setText(AssetManager.font, "Game Over :'(");
            //TODO PREGUNTA 14 - Part 5 - Segons l'score mostrarem un missatge diferent.
            if (score < 100) {
                textLayout.setText(AssetManager.font, "Game Over: \nTens que esforzarte mes!!");
            } else if (score >= 100 && score < 150) {
                textLayout.setText(AssetManager.font, "Game Over: \nHo estas fent molt be segueix aixi!");
            } else if (score >= 150) {
                textLayout.setText(AssetManager.font, "Game Over: \nGenial ets un maquina!");
            }
            //TODO PREGUNTA 14 - Part 5 - Guardem la puntuació mes alta dins de la taula high-score a les preferencies
            Preferences pref = Gdx.app.getPreferences("high-score");
            if (GameScreen.getScore() > pref.getInteger("high-score")) {
                pref.putInteger("high-score", GameScreen.getScore());
                pref.flush();
            }
            currentState = GameState.GAMEOVER;
        }


        //TODO Pregunta 11 - Part 2 - tal de reproduir-ne l'explosió a la posició on s'ha destruït.
        //Afegim també un zero a la llista explosionsTimes per tal de controlar el temps d'animació
        //d'aquesta explosió.
        Submarine submarine = scrollHandler.submarineDestroyed();
        if (submarine != null) {
            explosiveSubmarine.add(submarine);
            explosionsTimes.add(0f);
            submarine = null;
        }

        //TODO PREGUNTA 12 - PART 3 - Quan la llagosta toca la moneda incrementem l'score
        Coins coin = scrollHandler.coinTaken(lobster);

        if (coin != null) {
            if (coin.isBonus()) {
                score += Settings.BONUS_SCORE_INCREASE;
            } else {
                score += Settings.SCORE_INCREASE;
            }
            coin = null;
            scoreLabel.setText("Score: " + score);

        }


        //TODO PREGUNTA 11 - Part 2 - Treiem les explosións quan duren mes del necesari,
        //esborrem el submari i el temporitzador de les seves corresponents llistes.
        for (int i = 0; i < explosionsTimes.size; i++) {
            if (explosionsTimes.get(i) > EXPLOSION_FRAME_DURATION * EXPLOSION_FRAMES) {
                explosiveSubmarine.removeIndex(i);
                explosionsTimes.removeIndex(i);
            }
        }


        //l'anmimació d'explosió en la posició d'aquest.
        for(int i = 0; i <  explosiveSubmarine.size; i++) {
            Submarine explosive = explosiveSubmarine.get(i);
            batch.begin();
            batch.draw((TextureRegion) AssetManager.explosionAnim.getKeyFrame(explosionsTimes.get(i), false), (explosive.getX() + explosive.getWidth() / 2) - 32, explosive.getY() + explosive.getHeight() / 2 - 32, 64, 64);
            batch.end();
            explosionsTimes.set(i, explosionsTimes.get(i) + delta);
            if (explosionsTimes.get(i) > 0f) {
                Gdx.app.log("Explosion Time nº" + i + ": ", "" + explosionsTimes.get(i));
            }
        }
    }



    private void updateGameOver(float delta) {
        stage.act(delta);

        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH - textLayout.width) / 2, (Settings.GAME_HEIGHT - textLayout.height) / 2);
        // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
        batch.draw((TextureRegion) AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (lobster.getX() + lobster.getWidth() / 2) - 32, lobster.getY() + lobster.getHeight() / 2 - 32, 64, 64);
        batch.end();

        explosionTime += delta;

    }

    public void reset() {

        // Posem el text d'inici
        textLayout.setText(AssetManager.font, "Are you\nready?");
        // Cridem als restart dels elements.
        lobster.reset();
        scrollHandler.reset();

        // Posem l'estat a 'Ready'
        currentState = GameState.READY;

        // Afegim la nau a l'stage
        stage.addActor(lobster);

        //Reseteem l'score
        score = 0;
        scoreLabel.setText("Score: " + score);

        // Posem a 0 les variables per controlar el temps jugat i l'animació de l'explosió
        explosionTime = 0.0f;

    }

    //TODO PREGUNTA 13 - Part 4 - Metode per pausar el joc
    public void pauseGame() {
        //Es posa l'estat d'aquesta classe a PAUSE
        this.setCurrentState(GameScreen.GameState.PAUSE);
        //S'amaguen el botó Pause i Fire
        this.getPauseButton().setStatus(Settings.Status.HIDDEN);
        this.botonFire.setStatus(Settings.Status.HIDDEN);
        //Es truquen els mètodes pause dels actors que parpallejaràn
        this.getLobster().pause();
        //Ocultem el score
        this.scoreLabel.setVisible(false);
        //Disminuim el volum de la música
        AssetManager.music.setVolume(.05f);
    }

    //TODO PREGUNTA 13 - Part 4 - Metode per resumir el joc
    public void resumeGame() {
        //Es truquen els mètodes resume dels actors per que tornin a actuar
        this.lobster.resume();
        //Es posa l'estat d'aquesta classe a RUNNING
        this.setCurrentState(GameScreen.GameState.RUNNING);
        //Tornem a mostrem el botó pause mentre corre el joc
        this.getPauseButton().setStatus(Settings.Status.SHOWN);
        this.botonFire.setStatus(Settings.Status.SHOWN);
        //Mostrem el score
        this.scoreLabel.setVisible(true);
        //Tornem deixar el volum de la música com estaba
        AssetManager.music.setVolume(.2f);
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

    public Lobster getLobster() {
        return lobster;
    }

    public Stage getStage() {
        return stage;
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public BotonPause getPauseButton() {
        return botonPause;
    }
    public static int getScore() {
        return score;
    }
}
