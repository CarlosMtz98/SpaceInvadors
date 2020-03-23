package com.cmer.spaceinvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.utils.Array;

import javax.xml.soap.Text;

class PantallaSpaceInvaders extends Pantalla {
    private final Juego juego;
    private Array<Alien> alienArray;
    private Texture textureAlien;
    private float TIEMPO_PASO = 0.5f;
    private int MAX_PASOS = 32;
    private int numeroPasos = MAX_PASOS/2;
    private Direccion direccion = Direccion.DERECHA;
    private float paso = ANCHO * 0.4f / MAX_PASOS;
    private float timerAlienMover = 0;
    private final int COLUMNAS = 11;
    private  final int RENGLONES = 5;

    private Nave nave;
    private Texture texturaNave;
    private Movimiento movimiento = Movimiento.QUIETO;

    // Bala
    private Bala bala;      // null
    private Texture texturaBala;

    //Marcador
    private Marcador marcador;


    public PantallaSpaceInvaders(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show()
    {
        cargarTexturas();
        crearAliens();
        crearNave();

        Gdx.input.setInputProcessor(new ProcessadorEntrada());
    }

    private void crearNave() {
        nave = new Nave(texturaNave, ANCHO/2, 45);
    }

    private void cargarTexturas()
    {
        texturaNave = new Texture("space/nave.png");
        textureAlien = new Texture("space/enemigoArriba.png");
        texturaBala = new Texture("space/bala.png");
    }

    private void crearAliens()
    {
        alienArray = new Array<>(RENGLONES * COLUMNAS);
        float dx = ANCHO * 0.8f / COLUMNAS;
        float dy = ALTO * 0.4f / RENGLONES;

        for (int x = 0; x < COLUMNAS; x++)
        {
            for (int y = 0; y < RENGLONES; y++)
            {
                Alien alien = new Alien(textureAlien, x*dx + ANCHO * 0.1f, y*dy + ALTO * 0.45f);
                alienArray.add(alien);
            }
        }
    }

    @Override
    public void render(float delta) {
        actualizar(delta);
        borrarPantalla(0,0,0);

        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        for (Alien alien : alienArray){alien.render(batch);}
        nave.render(batch);
        if(bala != null) { bala.render(batch); }

        batch.end();
    }

    private void actualizar(float delta) {
        moverNave();
        moverBala(delta);
        moverEnemigos(delta);
        probarColisiones();
    }

    private void moverEnemigos(float delta)
    {
        timerAlienMover += delta;
        if (timerAlienMover >= TIEMPO_PASO) {
            timerAlienMover = 0;
            float pasosDir = direccion == Direccion.DERECHA ? paso : -paso;
            if (direccion == Direccion.DERECHA) {
                for (Alien alien : alienArray) {
                    alien.mover(pasosDir);
                }
            }
        }
        numeroPasos ++;
        if (numeroPasos >= MAX_PASOS)
        {
            direccion =  direccion == Direccion.DERECHA ? Direccion.IZQUIERDA : Direccion.IZQUIERDA;
            numeroPasos = 0;

            // BAJAR
            float pasoAbajo = ALTO * 0.4f / RENGLONES;
            for (Alien alien: alienArray)
            {
                alien.bajar(pasoAbajo);
            }
        }
    }

    private void probarColisiones()
    {
        if (bala != null)
        {
            for (int i = alienArray.size -1; i >= 0; i--)
            {
                Alien alien = alienArray.get(i);
                Rectangle rectangleAlien = alien.sprite.getBoundingRectangle();
                Rectangle rectangleBala = bala.sprite.getBoundingRectangle();

                if (rectangleAlien.overlaps(rectangleBala))
                {
                    alienArray.removeIndex(i);
                    bala = null;
                    break;
                }
            }
        }
    }

    private void moverBala(float delta)
    {
        if (bala != null) {
            bala.mover(delta);
            if (bala.sprite.getY() > ALTO) {
                // Fuera de la pantalla
                bala = null;
            }
        }
    }

    private void moverNave() {
        switch (movimiento){
            case DERECHA:
                nave.mover(10);
                break;
            case IZQUIERDA:
                nave.mover(-10);
                break;
            default:
                break;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private class ProcessadorEntrada implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 v = new Vector3(screenX, screenY, 0);
            camara.unproject(v);
            if (v.y < ALTO/2){
                // Disparo
                if (bala == null) {
                    float xBala = nave.sprite.getX() + nave.sprite.getWidth() / 2f
                            - texturaBala.getWidth() / 2f;
                    float yBala = nave.sprite.getY() + nave.sprite.getHeight();
                    bala = new Bala(texturaBala, xBala, yBala);
                }

            }
            else {
                if (v.x >= ANCHO/2){
                    // Derecha
                    movimiento = Movimiento.DERECHA;
                } else {
                    // Izquierda
                    movimiento = Movimiento.IZQUIERDA;
                }
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            movimiento = Movimiento.QUIETO;
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }

    public enum Movimiento {
        DERECHA,
        IZQUIERDA,
        QUIETO
    }

}

