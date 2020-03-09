package com.cmer.spaceinvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class PantallaMenu extends Pantalla
{
    private final Juego juego;
    private Texture tesxturaFondo;
    private Stage escenaMenu;

    public PantallaMenu(Juego juego)
    {
        this.juego = juego;
    }

    @Override
    public void show()
    {
        tesxturaFondo = new Texture("bg.jpg");
        crearMenu();
    }

    private void crearMenu()
    {
        escenaMenu = new Stage(vista);

        Texture texturaBtnJugar = new Texture("btnJugar.png");
        TextureRegionDrawable trdJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
        Texture texturaBtnJugarPresionado = new Texture("btnJugarP.png");
        TextureRegionDrawable trdJugarPresionado = new TextureRegionDrawable(new TextureRegion(texturaBtnJugarPresionado));

        ImageButton btnJugar = new ImageButton(trdJugar, trdJugarPresionado);
        btnJugar.setPosition(ANCHO/2 - btnJugar.getWidth()/2, 2*ALTO/3);
        escenaMenu.addActor(btnJugar);

        //Listener
        btnJugar.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaSpaceInvaders(juego));
            }
        });
        Gdx.input.setInputProcessor(escenaMenu);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(tesxturaFondo, 0,0);
        batch.end();
        escenaMenu.draw();
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
        tesxturaFondo.dispose();
    }
}
