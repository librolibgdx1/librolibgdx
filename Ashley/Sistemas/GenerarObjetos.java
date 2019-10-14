package com.libgdx.libro.Sistemas;

import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.libro.Entidades;

public class GenerarObjetos extends IntervalSystem {


    Entidades fabricaEntidades;
    World world;


    public GenerarObjetos(Entidades fabricaEntidades,World world) {
        super(1, 1);
        this.fabricaEntidades = fabricaEntidades;
        this.world = world;

    }

    @Override
    protected void updateInterval() {

      fabricaEntidades.addCuadrado(1.5f,6,-1,10);

    }

}
