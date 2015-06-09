package me.kinomoto.proteam;

/**
 * The exception that is thrown during the collision detection when the segment is elongated too much and not only one intersection is detected.
 * It does nothing as it only causes the loop to break.
 *
 */
public class MultipleCollisionsException extends Exception {
	private static final long serialVersionUID = 459308947205344517L;

	public MultipleCollisionsException() {
	}

}
