package me.kinomoto.proteam;

public class Prism extends AbstractOpticalElement {
	
	private double refractiveIndex = 1;

	public Prism(Point position) {
		super(position, AbstractOpticalElement.getSquare());
	}

	@Override
	void collisionSolution(Surroundings s) {
		// TODO Auto-generated method stub
		
	}

}
