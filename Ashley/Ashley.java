package com.libgdx.libro;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.libro.Sistemas.EliminarObjetos;
import com.libgdx.libro.Sistemas.GenerarObjetos;
import com.libgdx.libro.Sistemas.RenderSystem;

public class Ashley implements Screen {


    private Viewport viewportFisicaTexturas, viewport;
    private Entidades fabricaEntidades;
    private Box2DDebugRenderer box2DDebugRenderer;
    private PooledEngine engine;
    private World world;
    private SpriteBatch batch;



    @Override
    public void show() {


        box2DDebugRenderer = new Box2DDebugRenderer();
        engine = new PooledEngine();
        batch = new SpriteBatch();


        viewportFisicaTexturas = new FitViewport(5, 7);
        viewport = new FitViewport(500, 700);


        world = new World(new Vector2(0, -10), true);
        engine = new PooledEngine();
        fabricaEntidades = new Entidades(engine,world);

        engine.addSystem(new GenerarObjetos(fabricaEntidades,world));
        engine.addSystem(new RenderSystem(batch,viewport));
        engine.addSystem(new EliminarObjetos(world));



    }


    @Override
    public void render(float delta) {
        ////////////////
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ///////////////


        box2DDebugRenderer.render(world,viewportFisicaTexturas.getCamera().combined);
        world.step(1/60f,6,2);

        viewport.getCamera().update();
        viewportFisicaTexturas.getCamera().update();

        engine.update(delta);


    }

    @Override
    public void resize(int width, int height) {
        viewportFisicaTexturas.update(width, height, true);
        viewport.update(width, height, true);
    }


    //////////////////////////
    //////////////////////////
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

        world.dispose();
        box2DDebugRenderer.dispose();

    }





}
