package me.kinomoto.proteam;

public class BeamSource {
	private Segment segment;
	private double wavelenght;

	public BeamSource(Segment segment, double wavelenght) {
		super();
		this.segment = segment;
		this.wavelenght = wavelenght;
	}

	public Beam getBeam()
	{
		return new Beam(segment, wavelenght, 1);
	}

}
