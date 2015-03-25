package me.kinomoto.proteam;

public class BeamSource {
	Segment segment;
	private double wavelenght;

	public BeamSource(Segment segment, double wavelenght) {
		super();
		this.segment = segment;
		this.wavelenght = wavelenght;
	}

	public Beam getBeam(double refractiveIndex) {
		return new Beam(segment, wavelenght, 1, refractiveIndex);
	}

}
