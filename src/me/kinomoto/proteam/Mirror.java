package me.kinomoto.proteam;

public class Mirror extends AbstractOpticalElement {

	public Mirror(Point position) {
		super(position, AbstractOpticalElement.getMirror());
	}

	@Override
	void collisionSolution(Surroundings s) {
		// TODO Auto-generated method stub
		
	}

}
