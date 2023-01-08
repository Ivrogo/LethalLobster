package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cat.xtec.ioc.objects.BotonFire;
import cat.xtec.ioc.utils.Settings;

public class AssetManager {

    // Sprite Sheet
    public static Texture sheet;

    //fons
    public static TextureRegion background;

    //Llagosta
    public static TextureRegion[] lobster;

    // Submarine
    public static TextureRegion[] submarine;
    public static Animation lobsterAnim, submarineAnim;

    // Explosió
    public static TextureRegion[] explosion;
    public static Animation explosionAnim;

    //Monedes
    public static TextureRegion[] coins;
    public static Animation coinsAnim;

    public static TextureRegion[] bonusCoins;
    public static Animation bonusCoinsAnim;

    // Sons
    public static Sound explosionSound;
    public static Music music;
    public static Sound coinSound;

    //Fire
    public static TextureRegion botonFire;

    //Pause
    public static TextureRegion botonPause;

    //Laser
    public static TextureRegion laser;

    //Sound del laser
    public static Sound laserSound;

    // Font
    public static BitmapFont font;


    public static void load() {
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        sheet = new Texture(Gdx.files.internal("sheet.png"));
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Sprites de la de la llagosta
        lobster = new TextureRegion[6];
        for (int i = 0; i < lobster.length; i++) {
            lobster[i] = new TextureRegion(sheet, i * 41, 0, 41, 20);
            lobster[i].flip(false, true);
        }

        //Animació llangosta
        lobsterAnim = new Animation(0.05f, lobster);
        lobsterAnim.setPlayMode(Animation.PlayMode.LOOP);



        // Carreguem els 8 estats dels submarines
        submarine = new TextureRegion[8];
        for (int i = 0; i < submarine.length; i++) {

            submarine[i] = new TextureRegion(sheet, i * 50, 20, 50, 25);
            submarine[i].flip(false, true);

        }

        //animació submarines
        submarineAnim = new Animation(0.05f, submarine);
        submarineAnim.setPlayMode(Animation.PlayMode.LOOP);



        // Creem els 16 estats de l'explosió
        explosion = new TextureRegion[16];

        // Carreguem els 16 estats de l'explosió
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                explosion[index++] = new TextureRegion(sheet, j * 64,  i * 64 + 51, 64, 51);
                //explosion[index-1].flip(false, true);
            }
        }

        // Finalment creem l'animació
        explosionAnim = new Animation(0.04f, explosion);

        // Fons de pantalla
        background = new TextureRegion(sheet, 0, 177, 480, 135);
        background.flip(false, true);


        //Creem els 6 estats de les monedes
        coins = new TextureRegion[6];

        //Carreguem els estats de les monedes grogues
        for (int i = 0; i < coins.length; i++) {
            coins[i] = new TextureRegion(sheet, i * 20 + 400, 0, 18, 20 );
        }

        //Creem l'animació
        coinsAnim = new Animation(0.1f, coins);
        coinsAnim.setPlayMode(Animation.PlayMode.LOOP);


        //Creem els 6 estats de les monedes bonus
        bonusCoins = new TextureRegion[6];

        //Carreguem els estats de les monedes bonus
        for (int i = 0; i < bonusCoins.length; i++) {
            bonusCoins[i] = new TextureRegion(sheet, i * 20 + 400, 18, 20, 24);
        }

        //Creem l'animació de les monedes bonus
        bonusCoinsAnim = new Animation(0.1f, bonusCoins);
        bonusCoinsAnim.setPlayMode(Animation.PlayMode.LOOP);



        /******************************* Sounds *************************************/
        // Explosió
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        // Música del joc
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Theme Song 8-bit V1 _looping.wav"));
        music.setVolume(0.2f);
        music.setLooping(true);

        //Moneda
        coinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));



        /******************************* Text *************************************/
        // Font space
        FileHandle fontFile = Gdx.files.internal("fonts/Lethal.fnt");
        font = new BitmapFont(fontFile, true);
        font.getData().setScale(0.22f);


        //botó pause
        /*******************************Pause***************************************/
        botonPause = new TextureRegion(sheet, 249, 0, 22, 20);
        botonPause.flip(false, true);


        //botó fire
        /*******************************FIRE***************************************/
        botonFire = new TextureRegion(sheet, 281, 0, 22, 20);
        botonFire.flip(false, true);


        //Sprite del laser i so
        laser = new TextureRegion(sheet, 271, 10, Settings.LASER_WIDTH, Settings.LASER_HEIGHT);
        laser.flip(false,true);
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser-shot.wav"));


    }




    public static void dispose() {

        // Alliberem els recursos gràfics i de audio
        sheet.dispose();
        explosionSound.dispose();
        coinSound.dispose();
        music.dispose();


    }
}
