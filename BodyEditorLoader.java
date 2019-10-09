package com.libgdx.libro;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author Aurelien Ribon | http://www.aurelienribon.com
 * Este archivo es propiedad de Aurelien Ribon
 * I CLEARED THE COMMENTS FOR A BETTER PERFORMANCE
 */

public class BodyEditorLoader {

	// Model
	private final Model model;

	// Reusable stuff
	private final List<Vector2> vectorPool = new ArrayList<Vector2>();
	private final PolygonShape polygonShape = new PolygonShape();
	private final CircleShape circleShape = new CircleShape();
	private final Vector2 vec = new Vector2();

	public BodyEditorLoader(FileHandle file) {
		if (file == null) throw new NullPointerException("file is null");
		model = readJson(file.readString());
	}

	public BodyEditorLoader(String str) {
		if (str == null) throw new NullPointerException("str is null");
		model = readJson(str);
	}


	public void attachFixture(Body body, String name, FixtureDef fd, float scale) {
		RigidBodyModel rbModel = model.rigidBodies.get(name);
		if (rbModel == null) throw new RuntimeException("Name '" + name + "' was not found.");

		Vector2 origin = vec.set(rbModel.origin).scl(scale);

		for (int i=0, n=rbModel.polygons.size(); i<n; i++) {
			PolygonModel polygon = rbModel.polygons.get(i);
			Vector2[] vertices = polygon.buffer;

			for (int ii=0, nn=vertices.length; ii<nn; ii++) {
				vertices[ii] = newVec().set(polygon.vertices.get(ii)).scl(scale);
				vertices[ii].sub(origin);
			}

			polygonShape.set(vertices);
			fd.shape = polygonShape;
			body.createFixture(fd);

			for (Vector2 vertice : vertices) {
				free(vertice);
			}
		}

		for (int i=0, n=rbModel.circles.size(); i<n; i++) {
			CircleModel circle = rbModel.circles.get(i);
			Vector2 center = newVec().set(circle.center).scl(scale);
			float radius = circle.radius * scale;

			circleShape.setPosition(center);
			circleShape.setRadius(radius);
			fd.shape = circleShape;
			body.createFixture(fd);

			free(center);
		}
	}

	public String getImagePath(String name) {
		RigidBodyModel rbModel = model.rigidBodies.get(name);
		if (rbModel == null) throw new RuntimeException("Name '" + name + "' was not found.");

		return rbModel.imagePath;
	}

	public static class Model {
		public final Map<String, RigidBodyModel> rigidBodies = new HashMap<String, RigidBodyModel>();
	}

	public static class RigidBodyModel {
		public String name;
		public String imagePath;
		public final Vector2 origin = new Vector2();
		public final List<PolygonModel> polygons = new ArrayList<PolygonModel>();
		public final List<CircleModel> circles = new ArrayList<CircleModel>();
	}

	public static class PolygonModel {
		public final List<Vector2> vertices = new ArrayList<Vector2>();
		private Vector2[] buffer; // used to avoid allocation in attachFixture()
	}

	public static class CircleModel {
		public final Vector2 center = new Vector2();
		public float radius;
	}


	private Model readJson(String str) {
		Model m = new Model();

		JsonValue map = new JsonReader().parse(str);

		JsonValue bodyElem = map.getChild("rigidBodies");
		for (; bodyElem != null; bodyElem = bodyElem.next()) {
			RigidBodyModel rbModel = readRigidBody(bodyElem);
			m.rigidBodies.put(rbModel.name, rbModel);
		}

		return m;
	}

	private RigidBodyModel readRigidBody(JsonValue bodyElem) {
		RigidBodyModel rbModel = new RigidBodyModel();
		rbModel.name = bodyElem.getString("name");
		rbModel.imagePath = bodyElem.getString("imagePath");

		JsonValue originElem = bodyElem.get("origin");
		rbModel.origin.x = originElem.getFloat("x");
		rbModel.origin.y = originElem.getFloat("y");

		// polygons
		JsonValue polygonsElem = bodyElem.getChild("polygons");
		for (; polygonsElem != null ;polygonsElem = polygonsElem.next()){

			PolygonModel polygon = new PolygonModel();
			rbModel.polygons.add(polygon);

			JsonValue vertexElem = polygonsElem.child();
			for (; vertexElem != null; vertexElem = vertexElem.next()) {
				float x = vertexElem.getFloat("x");
				float y = vertexElem.getFloat("y");
				polygon.vertices.add(new Vector2(x, y));
			}

			polygon.buffer = new Vector2[polygon.vertices.size()];

		}

		// circles
		JsonValue circleElem = bodyElem.getChild("circles");

		for (; circleElem != null; circleElem = circleElem.next()) {
			CircleModel circle = new CircleModel();
			rbModel.circles.add(circle);

			circle.center.x = circleElem.getFloat("cx");
			circle.center.y = circleElem.getFloat("cy");
			circle.radius = circleElem.getFloat("r");
		}

		return rbModel;
	}


	private Vector2 newVec() {
		return vectorPool.isEmpty() ? new Vector2() : vectorPool.remove(0);
	}

	private void free(Vector2 v) {
		vectorPool.add(v);
	}

}
