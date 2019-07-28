package com.dev.mertaksoy.flyingbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class FlyingBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture enemy1;
    Texture enemy2;
    Texture enemy3;
    float birdX;
    float birdY;
    float velocity=0; //Hız
    float gravity=0.15f; //Yerçekimi
    float enemyVelocity=2f;
    Random random;
    Circle birdCircle;
    ShapeRenderer shapeRenderer;
    int gameState=0; //Durum
    int numberOfEnemySet=4;
    float distance=0;
    float [] enemyX=new float[numberOfEnemySet];
    float [] enemyOffset=new float[numberOfEnemySet];
    float [] enemyOffset2=new float[numberOfEnemySet];
    float [] enemyOffset3=new float[numberOfEnemySet];

    Circle[] enemyCircle;
    Circle[] enemyCircle2;
    Circle[] enemyCircle3;

    int score=0;
    int scoredEnemy=0;
    BitmapFont fontScore;
    BitmapFont fontGameOver;

	@Override
	public void create () {
	batch = new SpriteBatch();
	background = new Texture("background.png");
	bird = new Texture("bird.png");
	enemy1 = new Texture("enemy.png");
	enemy2 = new Texture("enemy.png");
	enemy3 = new Texture("enemy.png");

	distance = Gdx.graphics.getWidth()/3; //arılar arasında ekran/3 kadar mesafe olsun
    random = new Random();

    fontScore = new BitmapFont();
    fontScore.setColor(Color.BLACK);
    fontScore.getData().setScale(4);

    fontGameOver = new BitmapFont();
    fontGameOver.setColor(Color.BLACK);
    fontGameOver.getData().setScale(4);


	birdX=Gdx.graphics.getWidth()/8;
	birdY=Gdx.graphics.getHeight()/3;

	shapeRenderer = new ShapeRenderer();

	birdCircle= new Circle();
	enemyCircle = new Circle[numberOfEnemySet];
	enemyCircle2 = new Circle[numberOfEnemySet];
	enemyCircle3 = new Circle[numberOfEnemySet];

	for(int i=0;i<numberOfEnemySet;i++){ //Düşmanların Y eksenleri için initialization

        enemyOffset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200); //nextFloat 0 ile 1 arasında bir yüzde veriyor
        enemyOffset2[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
        enemyOffset3[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);

	    enemyX[i]=Gdx.graphics.getWidth()-enemy1.getWidth()/2+i*distance;

        enemyCircle[i] = new Circle();
        enemyCircle2[i] = new Circle();
        enemyCircle3[i] = new Circle();
    }


	}

	@Override
	public void render () {
        batch.begin();
        batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        if(gameState==1){ //Oyun devam ederken

            if(enemyX[scoredEnemy]<birdX-bird.getWidth()/5){ //enemy kuşun gerisine geçmişse.. < birdX de denebilirdi
                score++;
                if(scoredEnemy<numberOfEnemySet-1){ //Her set için ayrı ayrı kontrol et. 3'ten küçükse 1 arttır
                    scoredEnemy++;
                }else{ //Büyükse 0'la
                    scoredEnemy=0;
                }
            }

            if(Gdx.input.justTouched()){ //Kullanıcı oyun başladıktan sonra ekrana basarsa
                velocity= -5; //Zıpla. getHeight * x gibi yazarak modifiye edilebilir

            }

            for(int i=0;i<numberOfEnemySet;i++){
                enemyX[i] = enemyX[i]-enemyVelocity; //Düşmanlar hareket etsin

                if(enemyX[i]<0){ //düşmanın yeri, 0'ın altına indiyse)
                    enemyX[i]=enemyX[i]+numberOfEnemySet*distance; //tekrar düşman setleri gönder

                    enemyOffset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200); //nextFloat 0 ile 1 arasında bir yüzde veriyor
                    enemyOffset2[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
                    enemyOffset3[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);

                } else {
                    enemyX[i]=enemyX[i]-enemyVelocity;
                }

                batch.draw(enemy1,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffset[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
                batch.draw(enemy2,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffset2[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
                batch.draw(enemy3,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffset3[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);

                enemyCircle[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);
                enemyCircle2[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset2[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);
                enemyCircle3[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset3[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);

            }



	        if(birdY>0&&birdY<Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/10){ //Kuşun Y ekseni 0'dan büyükse ve kuş yukarı çarpmışsa
                velocity=velocity+gravity;
                birdY=birdY-velocity;
            } else {
                gameState=2;
            }

        } else if(gameState==0){
            if(Gdx.input.justTouched()){ //Kullanıcı ekrana ilk basstığında
                gameState = 1; //Oyun başlasın
            }
        } else if (gameState==2){ //Oyun bittiğinde

            fontGameOver.draw(batch,"Game Over! \n  Try Again",Gdx.graphics.getWidth()/2.5f,Gdx.graphics.getHeight()/1.6f);

            if(Gdx.input.justTouched()){ //Kullanıcı ekrana ilk basstığında
                gameState = 1; //Oyun başlasın
                birdY=Gdx.graphics.getHeight()/3;

                for(int i=0;i<numberOfEnemySet;i++){

                    enemyOffset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200); //nextFloat 0 ile 1 arasında bir yüzde veriyor
                    enemyOffset2[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
                    enemyOffset3[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);

                    enemyX[i]=Gdx.graphics.getWidth()-enemy1.getWidth()/2+i*distance;

                    enemyCircle[i] = new Circle();
                    enemyCircle2[i] = new Circle();
                    enemyCircle3[i] = new Circle();
                }

                velocity=0;
                scoredEnemy=0;
                score=0;

            }

        }


        batch.draw(bird, birdX, birdY,Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10); //Kuşun boyutları vs

        fontScore.draw(batch,String.valueOf(score),100,200);
        batch.end();

        birdCircle.set(birdX+Gdx.graphics.getWidth()/30,birdY+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/40);

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.BLACK);
        //shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);


        for(int i =0;i<numberOfEnemySet;i++){
            //shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
            //shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset2[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
            //shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset3[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);

            if(Intersector.overlaps(birdCircle,enemyCircle[i])||Intersector.overlaps(birdCircle,enemyCircle2[i])||Intersector.overlaps(birdCircle,enemyCircle3[i])){ //çarpışmalar
                gameState=2;

            }
        }

        //shapeRenderer.end();
	}

	@Override
	public void dispose () {

	}
}
