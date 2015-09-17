/*******************************************************************************
 * Copyright 2014 Rafael Garcia Moreno.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bladecoder.engine.model;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.bladecoder.engine.assets.EngineAssetManager;

/**
 * A BaseActor is the foundation for all actors in Scenes
 * 
 * @author rgarcia
 */
abstract public class BaseActor implements Serializable {
	protected String id;
	protected Scene scene = null;
	protected boolean visible = true;
	protected final Polygon bbox = new Polygon();	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Polygon getBBox() {
		return bbox;
	}
	
	public boolean hit(float x, float y) {
		return getBBox().contains(x, y);
	}

	public boolean isVisible() {			
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setScene(Scene s) {
		scene = s;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	abstract public void update(float delta);

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("\nObject: ").append(id);
		sb.append("\n  Visible: ").append(visible);
		sb.append("\n  BBox: ").append(getBBox().toString());
		sb.append("\n");

		return sb.toString();
	}

	public float getX() {
		return bbox.getX();
	}
	
	public float getY() {
		return bbox.getY();
	}

	public void setPosition(float x, float y) {		
		bbox.setPosition(x, y);
	}
	
	@Override
	public void write(Json json) {
		json.writeValue("id", id);
		json.writeValue("visible", visible);

		float worldScale = EngineAssetManager.getInstance().getScale();
		Vector2 scaledPos = new Vector2(bbox.getX() / worldScale, bbox.getY() / worldScale);
		json.writeValue("pos", scaledPos);	
		json.writeValue("bbox", bbox.getVertices());
	}

	@Override
	public void read (Json json, JsonValue jsonData) {
		id = json.readValue("id", String.class, jsonData);
		visible = json.readValue("visible", Boolean.class, jsonData);

		Vector2 pos = json.readValue("pos", Vector2.class, jsonData);

		float worldScale = EngineAssetManager.getInstance().getScale();
		bbox.setPosition(pos.x * worldScale, pos.y * worldScale);
		
		float[] verts = json.readValue("bbox", float[].class, jsonData);
		
		if(verts.length > 0)
			bbox.setVertices(verts);
		
		bbox.setScale(worldScale, worldScale);		
	}

}
