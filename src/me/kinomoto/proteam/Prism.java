package me.kinomoto.proteam;

public class Prism extends AbstractOpticalElement {
	
	private double refractiveIndex = 1;

	public Prism(Point position, String name, double ior) {
		super(position, AbstractOpticalElement.getSquare(), name);
		refractiveIndex = ior;
	}

	@Override
	void collisionSolution(Surroundings s, Beam b, Segment seg) {
		System.out.println("Collision with " + name);
		
	}
	
	public double getRefractiveIndex()
	{
		return refractiveIndex;
	}

	@Override
	public boolean isPointInside(Point p) {
		boolean out = false;
		for(int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++)
			if( ((get(i).y > p.y) != (get(j).y > p.y)) && 
					( p.x < (get(j).x - get(i).x) * (p.y - get(i).y) / (get(j).y - get(i).y) + get(i).x ) )
				out = !out;
		return out;
	}

}
