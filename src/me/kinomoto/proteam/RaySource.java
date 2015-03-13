package me.kinomoto.proteam;

public class RaySource {
	private double waveLenght;
	private double x;
	private double y;
	private double angle;
	
	public RaySource(double waveLenght, double x, double y, double angle) {
		super();
		this.waveLenght = waveLenght;
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	
	public Ray getRay()
	{
		return new Ray();
	}

}
