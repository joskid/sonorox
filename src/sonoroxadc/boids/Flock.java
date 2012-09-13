package sonoroxadc.boids;

import java.util.ArrayList;

public class Flock {
	  ArrayList boids; // An arraylist for all the boids

	  public Flock() {
	    boids = new ArrayList(); // Initialize the arraylist
	  }

	  Boid b ;
	  public void run() {
		 // System.out.println("flocking running");
	    for (int i = 0; i < boids.size(); i++) {
	      b = (Boid) boids.get(i);
	      b.run(boids);  // Passing the entire list of boids to each boid individually
	    }
	  }

	  public void addBoid(Boid b) {
	    boids.add(b);
	  }
	  public void removeBoid() {
	    if (boids.size()>0)
	       boids.remove(0);
	  }

	}
