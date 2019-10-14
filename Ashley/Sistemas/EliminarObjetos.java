package com.libgdx.libro.Sistemas;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.libro.Componentes.BodyC;

public class EliminarObjetos extends IteratingSystem {

    World world;

    public EliminarObjetos(World world) {
        super(RenderSystem.FAMILYCUADRADO, 1);
        this.world = world;
    }

    public static final ComponentMapper<BodyC> bp =
            ComponentMapper.getFor(BodyC.class);


    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        BodyC bb = bp.get(entity);


        if(((int) bb.body.getPosition().y) == 1){

            entity.getComponent(BodyC.class).body.setActive(false);
            world.destroyBody(entity.getComponent(BodyC.class).body);
            entity.remove(BodyC.class);
            getEngine().removeEntity(entity);

        }


    }





}
