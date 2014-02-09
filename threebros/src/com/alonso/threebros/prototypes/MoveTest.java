package com.alonso.threebros.prototypes;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.gl.Vertices;
import com.badlogic.androidgames.framework.impl.GLGame;
import com.badlogic.androidgames.framework.impl.GLGraphics;
import com.badlogic.androidgames.framework.math.Vector2;


public class MoveTest extends GLGame {
	enum State {
		START, BEGIN_VEC, MOVING
	};

    @Override
    public Screen getStartScreen() {
        return new CannonScreen(this);
    }

	class CannonScreen extends Screen {
		
	    float FRUSTUM_WIDTH = 4.8f;
	    float FRUSTUM_HEIGHT = 3.2f;
	    GLGraphics glGraphics;
	    Vertices vertices;
	    Vector2 cannonPos = new Vector2(2.4f, 0.5f);
	    float cannonAngle = 0;
	    Vector2 touchPos = new Vector2();
	    
	    
	    // Current move vector for main character
	    Vector2 startMovePos = new Vector2();
	    Vector2 moveVec = new Vector2(0f, 0f);
	    State moveState = State.START;
	
	    public CannonScreen(Game game) {
	        super(game);
	        glGraphics = ((GLGame) game).getGLGraphics();
	        vertices = new Vertices(glGraphics, 3, 0, false, false);
	        vertices.setVertices(new float[] { -0.5f, -0.5f, 
	                                            0.5f, 0.0f, 
	                                           -0.5f, 0.5f }, 0, 6);
	    }
	    
	    void transitionFromSTART(TouchEvent event) {
	    	if (event.type == TouchEvent.TOUCH_DOWN) {
	    		Log.i("MoveTest", "START -> BEGIN_VEC");
	    		startMovePos.x = event.x;
	    		startMovePos.y = event.y;
	    		moveState = State.BEGIN_VEC;
	    	}
	    	// TODO: Handle all cases (or document why skipping)
	    }
	    
	    void transitionFromBEGIN_VEC(TouchEvent event) {
	    	if (event.type == TouchEvent.TOUCH_DRAGGED) {
	    		Log.i("MoveTest", "BEGIN_VEC -> MOVING");
	    		moveVec.x = event.x - startMovePos.x;
	    		moveVec.y = event.y - startMovePos.y;
	    		Log.i("MoveTest", String.format("BEGIN_VEC -> MOVING, (%.2f, %.2f)", moveVec.x, moveVec.y));
	    		moveState = State.MOVING;
	    	}
	    	// TODO: Handle all cases (or document why skipping)
	    }
	    
	    void transitionFromMOVING(TouchEvent event) {
	    	switch(event.type) {
	    		case TouchEvent.TOUCH_DRAGGED:
		    		moveVec.x = event.x - startMovePos.x;
		    		moveVec.y = event.y - startMovePos.y;
		    		Log.i("MoveTest", String.format("MOVING-> MOVING, (%.2f, %.2f)", moveVec.x, moveVec.y));
		    		moveState = State.MOVING;
		    		
			    	// TODO: If we drag far enough, we should reset the startMovePos
	    			break;
	    			
	    		case TouchEvent.TOUCH_UP:
		    		Log.i("MoveTest", "MOVING -> START");
		    		moveVec.x = 0f;
	    			moveVec.y = 0f;
	    			moveState = State.START;
	    			break;
	    	}
	    }
	    
	    void handleTouchEvent(TouchEvent event) {
	    	switch (moveState) {
	    		case START:
	    			transitionFromSTART(event);
	    			break;
	    			
	    		case BEGIN_VEC:
	    			transitionFromBEGIN_VEC(event);
	    			break;
	    			
	    		case MOVING:
	    			transitionFromMOVING(event);
	    			break;
	    	}
	    }
	
	    @Override
	    public void update(float deltaTime) {
	        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	        game.getInput().getKeyEvents();
	
	        int len = touchEvents.size();
	        for (int i=0; i < len; i++) {
	        	handleTouchEvent(touchEvents.get(i));
	        }	        
	    }
	
	    @Override
	    public void present(float deltaTime) {
	
	        GL10 gl = glGraphics.getGL();
	        gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
	        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	        gl.glMatrixMode(GL10.GL_PROJECTION);
	        gl.glLoadIdentity();
	        gl.glOrthof(0, FRUSTUM_WIDTH, 0, FRUSTUM_HEIGHT, 1, -1);
	        gl.glMatrixMode(GL10.GL_MODELVIEW);
	        gl.glLoadIdentity();
	
	        gl.glTranslatef(cannonPos.x, cannonPos.y, 0);
	        gl.glRotatef(cannonAngle, 0, 0, 1);
	        vertices.bind();
	        vertices.draw(GL10.GL_TRIANGLES, 0, 3);
	        vertices.unbind();
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
	}
}
