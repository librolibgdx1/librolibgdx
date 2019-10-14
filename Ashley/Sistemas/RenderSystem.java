package com.libgdx.libro.Sistemas;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.libro.Componentes.BodyC;
import com.libgdx.libro.Componentes.SpriteC;
import com.libgdx.libro.Componentes.TexturaC;

public class RenderSystem extends EntitySystem {

    ////FAMILIA PARA DIFERENCIAR ENTIDADES///////////////////
    public static final Family FAMILYCUADRADO = Family.all(
            BodyC.class, SpriteC.class,
            TexturaC.class
    ).get();
    ///////////////////////////////


    /////////////////OBTENER LOS COMPONENTES CON COMPONENTMAPPER//////////////////
    public static final ComponentMapper<SpriteC> sp =
            ComponentMapper.getFor(SpriteC.class);

    public static final ComponentMapper<BodyC> bp =
            ComponentMapper.getFor(BodyC.class);
    ////////////////////////////////////////////////////////


    SpriteBatch batch;
    Viewport viewport;
    /////////////////ARRAY DE CUADRADOS//////////////////////////
    private Array<Entity> arrayC = new Array<>();


    public RenderSystem(SpriteBatch batch, Viewport viewport){
        this.batch = batch;
        this.viewport = viewport;
    }


    @Override
    public void update(float deltaTime) {

        ///////////////////////ARRAY EN EL QUE METEMOS LAS ENTIDADES DE ESA FAMILIA///////////////////
        ImmutableArray<Entity> cuadrados = getEngine().getEntitiesFor(FAMILYCUADRADO);
        arrayC.addAll(cuadrados.toArray());

        ////////DIBUJAR SPRITE DEL CUADRADO/////////////////////
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        draw();

        batch.end();
        /////////////////////////////////

        arrayC.clear(); //////LIMPIAMOS EL ARRAY



    }


    public void draw(){

        ////RENDERIZAR CUADRADOS////
        for (Entity ent: arrayC){

            BodyC b = bp.get(ent);
            SpriteC s = sp.get(ent);

            s.sprite.setPosition(b.body.getPosition().x*100 - 50, b.body.getPosition().y*100 -40 );
            s.sprite.setRotation(b.body.getAngle() * MathUtils.radiansToDegrees);
            s.sprite.draw(batch);

        }
    }







}
