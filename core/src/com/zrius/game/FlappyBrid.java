package com.zrius.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlappyBrid extends ApplicationAdapter {

    public static final String TAG = FlappyBrid.class.getSimpleName();
    public interface ListernerOnGameOver{
        void onGameOver();
    }

    public static ListernerOnGameOver listernerOnGameOver;

    public static void setListenerOnGameOver(ListernerOnGameOver listernerOnGameOver){
        FlappyBrid.listernerOnGameOver = listernerOnGameOver;
    }

    SpriteBatch batch;
    Texture background;
    Texture gameOver;
    //ShapeRenderer shapeRenderer;

    Texture[] birds;
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;
    Circle birdCircle;
    int score = 0;
    int scroringTube = 0;
    BitmapFont font;

    int gameState = 0;
    float gravity = 2;

    Texture topTube;
    Texture bottomTube;
    float gap = 450;
    float maxTubeOffset;
    Random randomGenerator;

    float tubeVelocity = 4;
    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTheTube;

    Rectangle[] topTubeRectangle;
    Rectangle[] bottomTubeRectangle;
    int pubShow = 0;

    //pub


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        gameOver = new Texture("gameover.png");
        // shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();

        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTheTube = Gdx.graphics.getWidth() * 3 / 4;
        topTubeRectangle = new Rectangle[numberOfTubes];
        bottomTubeRectangle = new Rectangle[numberOfTubes];
        startGame();

    }

    public void startGame(){
        birdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;
        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTheTube;

            topTubeRectangle[i] = new Rectangle();
            bottomTubeRectangle[i] = new Rectangle();
        }
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (tubeX[scroringTube] < Gdx.graphics.getWidth()/2) {
                Gdx.app.log("FlappyBird --- Score -------------- ", String.valueOf(score));
                score++;
                font.setColor(Color.WHITE);
                if (scroringTube < numberOfTubes-1) {
                    scroringTube++;
                } else {
                    scroringTube = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity = -25;

            }


            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] += numberOfTubes * distanceBetweenTheTube;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                } else {
                    tubeX[i] = tubeX[i] - tubeVelocity;
                }

                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                topTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], topTube.getWidth(), topTube.getHeight());

            }


            if (birdY > 0) {
                velocity = velocity + gravity;
                birdY -= velocity;
            }else {
                if(score >= 5){
                    score = score -5;
                    font.setColor(Color.RED);
                }else {
                    gameState = 2;// is the game over state
                } }

        } else if(gameState == 0){
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } if(gameState == 2){
            batch.draw(gameOver,Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight());
           if(pubShow == 0){
               if(listernerOnGameOver != null){
                   listernerOnGameOver.onGameOver();
                   pubShow = 1;
                   Gdx.app.log(TAG,"Game listener is init");
               }else {
                   pubShow = 0;
                   Gdx.app.log(TAG,"Game listener not init");
               }
           }

            if (Gdx.input.justTouched()) {
                gameState = 1;
                startGame();
                score =0;
                scroringTube = 0;
                velocity = 0;
                pubShow = 0;
            }
        }
        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }


        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        font.draw(batch,String.valueOf(score),100,200);
        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        // shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

        for (int i = 0; i < numberOfTubes; i++) {
            //  shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
            //  shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2 - gap/2 -bottomTube.getHeight() + tubeOffset[i],topTube.getWidth(),topTube.getHeight());

            if (Intersector.overlaps(birdCircle, topTubeRectangle[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangle[i])) {

                if(score >= 5){
                    score = score -5;
                    font.setColor(Color.RED);
                }else {
                    gameState = 2;
                }


            }
        }

        // shapeRenderer.end();

    }


}
