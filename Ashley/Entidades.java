package com.libgdx.libro;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.libro.Componentes.BodyC;
import com.libgdx.libro.Componentes.SpriteC;
import com.libgdx.libro.Componentes.TexturaC;

public class Entidades {

    PooledEngine engine;
    World world;

    public Entidades(PooledEngine engine, World world){
        this.engine = engine;
        this.world = world;


    }


    public void addCuadrado(float x , float y,float velY,float torque){

        SpriteC SPRITE = engine.createComponent(SpriteC.class);
        BodyC CUERPO = engine.createComponent(BodyC.class);
        TexturaC TEXTURA = engine.createComponent(TexturaC.class);

        TEXTURA.texture = new Texture(Gdx.files.internal("cuadrado.png"));
        SPRITE.sprite= new Sprite(TEXTURA.texture);

        //////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////

        BodyDef bdS = new BodyDef();
        bdS.type = BodyDef.BodyType.DynamicBody;
        bdS.position.set(x,y);
        bdS.bullet = true;
        CUERPO.body = world.createBody(bdS);


        SPRITE.sprite.setSize(100,100);
        SPRITE.sprite.setOrigin(50,50);
        CUERPO.body.setUserData(SPRITE.sprite);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.5f,0.5f);


        FixtureDef pol = new FixtureDef();
        pol.density = 1;
        pol.shape = polygonShape;

        CUERPO.body.createFixture(pol);
        polygonShape.dispose();

        CUERPO.body.setLinearVelocity(0,velY);
        CUERPO.body.applyTorque(torque,true);


        ////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////

        Entity cuadrado = engine.createEntity();

        cuadrado.add(SPRITE);
        cuadrado.add(CUERPO);
        cuadrado.add(TEXTURA);


        engine.addEntity(cuadrado);


    }




}
