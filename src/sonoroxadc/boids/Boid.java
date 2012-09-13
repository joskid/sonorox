package sonoroxadc.boids;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Boid {

	  private static Random randomizer = new Random();
	Vector3D loc;
	  Vector3D vel;
	  Vector3D acc;
	  float r;
	  float maxforce;    // Maximum steering force
	  float maxspeed;    // Maximum speed
	  Canvas canvas;
	  Paint paint;
	  int myColor;
	  public Boid(Vector3D l, float ms, float mf, Canvas canvas_, Paint paint_) {
	    acc = new Vector3D(0,0);
	    vel = new Vector3D(getRand(1,4),getRand(1,4));
	    myColor=getRand(1,4);
	    loc = l.copy();
	    r = 5.0f;
	    maxspeed = ms;
	    maxforce = mf;
	    canvas=canvas_;
	    paint=paint_;
	  }
	  public static final int getRand(int min, int max) {
	        int r = Math.abs(randomizer.nextInt());
	        return (r % ((max - min + 1))) + min;
	    }
	  public void run(ArrayList boids) {
	    flock(boids);
	    update();
	    borders();
	   /// if (spectrumVAR>9)
	    //{
	   //   flockShapeMode = (int)getRand(0,7);
	    //  //_("flockShapeMode:"+flockShapeMode+" sampleLEFT:"+sampleLEFT+" sampleRIGHT:"+sampleRIGHT+" spectrumVAR:"+spectrumVAR);
	   // }
	    render();
	  }

	  // We accumulate a new acceleration each time based on three rules
	  public void flock(ArrayList boids) {
	    Vector3D sep = separate(boids);   // Separation
	    Vector3D ali = align(boids);      // Alignment
	    Vector3D coh = cohesion(boids);   // Cohesion
	    // Arbitrarily weight these forces
	    sep.mult(2.0f);
	    ali.mult(1.0f);
	    coh.mult(1.0f);
	    // Add the force vectors to acceleration
	    acc.add(sep);
	    acc.add(ali);
	    acc.add(coh);
	  }

	  // Method to update location
	  public void update() {
	    // Update velocity
	    vel.add(acc);
	    // Limit speed
	    vel.limit(maxspeed);
	    loc.add(vel);
	    // Reset accelertion to 0 each cycle
	    acc.setXYZ(0,0,0);
	  }

	  public void seek(Vector3D target) {
	    acc.add(steer(target,false));
	  }

	  public void arrive(Vector3D target) {
	    acc.add(steer(target,true));
	  }

	  // A method that calculates a steering vector towards a target
	  // Takes a second argument, if true, it slows down as it approaches the target
	  public Vector3D steer(Vector3D target, boolean slowdown) {
	    Vector3D steer;  // The steering vector
	    Vector3D desired = target.sub(target,loc);  // A vector pointing from the location to the target
	    float d = desired.magnitude(); // Distance from the target is the magnitude of the vector
	    // If the distance is greater than 0, calc steering (otherwise return zero vector)
	    if (d > 0) {
	      // Normalize desired
	      desired.normalize();
	      // Two options for desired vector magnitude (1 -- based on distance, 2 -- maxspeed)
	      if ((slowdown) && (d < 100.0f)) desired.mult(maxspeed*(d/100.0f)); // This damping is somewhat arbitrary
	      else desired.mult(maxspeed);
	      // Steering = Desired minus Velocity
	      steer = target.sub(desired,vel);
	      steer.limit(maxforce);  // Limit to maximum steering force
	    } else {
	      steer = new Vector3D(0,0);
	    }
	    return steer;
	  }

	  public void render() {
		  switch (myColor)
		  {
		  case 0:
			  paint.setColor(0xFFff0000);
			  break;
		  case 1:
			  paint.setColor(0xFF00ff06);
			  break;
		  case 2:
			  paint.setColor(0xFFfff000);
			  break;
		  case 3:
			  paint.setColor(0xFF000000);
			  break;
		  case 4:
			  paint.setColor(0xFF00f6ff);
			  break;
		  }
		  
		  canvas.drawRect(loc.x,loc.y,loc.x+4,loc.y+4, paint);
		 // System.out.println("flocking rendering "+loc.x+","+loc.y);
	    // Draw a triangle rotated in the direction of velocity
	   // float theta = vel.heading2D() + radians(90);
	   // if (FILLON)
	   /// {
	    ///     //fill(255-spectrumVAR*15,255-spectrumVAR*12,255-spectrumVAR*8);
	   //      fill(sampleLEFT,255-sampleLEFT/2,255-spectrumVAR*3);
//
	   //// }
	    //else{
	    //    noFill();
	   // }
		//if (STROKE)
	    //        stroke(255,255-spectrumVAR*4,255-spectrumVAR*7);

	    //pushMatrix();
	   // translate(loc.x,loc.y,spectrumVAR*5);
	   // rotate(theta);


	   /* switch(flockShapeMode)
	    {
	      case 1:beginShape(QUADS); break;
	      case 2:beginShape(TRIANGLES); break;
	      case 3:beginShape(POINTS); break;
	      case 4:beginShape(LINES); break;
	      case 5:beginShape(TRIANGLE_STRIP); break;
	      case 6:beginShape(TRIANGLE_FAN); break;
	      case 7:beginShape(QUAD_STRIP); break;
	      default: flockShapeMode=1;
	    }*/

	    /* try {
	    vertex(0, -r*3f);//2);
	    vertex(-r, r*3f);//2);
	    vertex(r, r*3f);//2);
	     }catch(Exception e)
	     {
	       _W("exception vertex:"+e.getMessage());
	    //   vertex(0, r);
	   // vertex(r, r);
	   // vertex(r, r);
	     }
	    endShape(CLOSE);//close fixes it/?
	    popMatrix();*/
	  }
	  int flockShapeMode=2;//start on original triangles
	private float width=480;
	private float height=320;

	  // Wraparound
	  public void borders() {
	    if (loc.x < -r) loc.x = width+r;
	    if (loc.y < -r) loc.y = height+r;
	    if (loc.x > width+r) loc.x = -r;
	    if (loc.y > height+r) loc.y = -r;
	  }

	  // Separation
	  // Method checks for nearby boids and steers away
	  public Vector3D separate (ArrayList boids) {
	    float desiredseparation = 25.0f;
	    Vector3D sum = new Vector3D(0,0,0);
	    int count = 0;
	    // For every boid in the system, check if it's too close
	    for (int i = 0 ; i < boids.size(); i++) {
	      Boid other = (Boid) boids.get(i);
	      float d = loc.distance(loc,other.loc);
	      // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
	      if ((d > 0) && (d < desiredseparation)) {
	        // Calculate vector pointing away from neighbor
	        Vector3D diff = loc.sub(loc,other.loc);
	        diff.normalize();
	        diff.div(d);        // Weight by distance
	        sum.add(diff);
	        count++;            // Keep track of how many
	      }
	    }
	    // Average -- divide by how many
	    if (count > 0) {
	      sum.div((float)count);
	    }
	    return sum;
	  }

	  // Alignment
	  // For every nearby boid in the system, calculate the average velocity
	  public Vector3D align (ArrayList boids) {
	    float neighbordist = 50.0f;
	    Vector3D sum = new Vector3D(0,0,0);
	    int count = 0;
	    for (int i = 0 ; i < boids.size(); i++) {
	      Boid other = (Boid) boids.get(i);
	      float d = loc.distance(loc,other.loc);
	      if ((d > 0) && (d < neighbordist)) {
	        sum.add(other.vel);
	        count++;
	      }
	    }
	    if (count > 0) {
	      sum.div((float)count);
	      sum.limit(maxforce);
	    }
	    return sum;
	  }

	  // Cohesion
	  // For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
	  public Vector3D cohesion (ArrayList boids) {
	    float neighbordist = 50.0f;
	    Vector3D sum = new Vector3D(0,0,0);   // Start with empty vector to accumulate all locations
	    int count = 0;
	    for (int i = 0 ; i < boids.size(); i++) {
	      Boid other = (Boid) boids.get(i);
	      float d = loc.distance(loc,other.loc);
	      if ((d > 0) && (d < neighbordist)) {
	        sum.add(other.loc); // Add location
	        count++;
	      }
	    }
	    if (count > 0) {
	      sum.div((float)count);
	      return steer(sum,false);  // Steer towards the location
	    }
	    return sum;
	  }
	}
