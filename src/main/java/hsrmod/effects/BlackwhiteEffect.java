/*
package hsrmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BlackwhiteEffect extends AbstractGameEffect {
    FrameBuffer frameBuffer;
    ShaderProgram shader;
    
    public BlackwhiteEffect(float duration) {
        super();
        this.duration = duration;
        this.startingDuration = duration;
        this.color = new Color(0,0,0,1);
        ShaderProgram.pedantic = false; // Disable pedantic mode to avoid strict validation errors
        ShaderProgram shader = new ShaderProgram(Gdx.files.internal("HSRModResources/img/vertex.vert"), Gdx.files.internal("HSRModResources/img/blackwhite.frag")); 
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        
        if (!shader.isCompiled()) {
            System.err.println("Shader compilation failed: " + shader.getLog());
        }
    }

    @Override
    public void update() {
        super.update();
        if (isDone) {
            dispose();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        */
/*
        batch.begin();
        // Render your game world here
        batch.end();
        frameBuffer.end();*//*


        // Apply the shader effect
        if (duration == startingDuration) {
            batch.setShader(shader);
            frameBuffer.begin();
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
        batch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
        if (isDone) {
            batch.setShader(null);
        }
    }

    @Override
    public void dispose() {
        if (shader != null) shader.dispose();
        if (frameBuffer != null) frameBuffer.dispose();
    }
}
*/
