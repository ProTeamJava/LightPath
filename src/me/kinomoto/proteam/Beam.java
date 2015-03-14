package me.kinomoto.proteam;

public class Beam {
	private Segment segment;
	private boolean checkedCollision = false;
	private double wavelenght;

	public Beam(Segment segment, double wavelenght) {
		Point tmp = segment.end;
		tmp.x *= 1000;
		tmp.y *= 1000;
		this.segment = segment;
		this.wavelenght = wavelenght;
	}

	public boolean getIfSimulated()
	{
		return checkedCollision;
	}

	public void simulate(Surroundings s)
	{
		//check collision etc
		int collisionNum = 0;
		Point collisionPoint = null;
		double start = 0;
		double end = 1;
		for(;;)
		{
			double dx, dy;
			dx = segment.end.x - segment.begin.x;
			dy = segment.end.y - segment.begin.y;
			double nex = segment.begin.x + end * dx;
			double ney = segment.begin.y + end * dy;
			

			double nbx = segment.begin.x + start * dx;
			double nby = segment.begin.y + start * dy;
			
			Segment tmp = new Segment(new Point(nbx, nby), new Point(nex, ney));
			for(AbstractOpticalElement e : s.elements)
			{
				
				try {
					Point p = e.collision(tmp);
					if(p == null)
					{
						System.out.println("no collision");
					}
					else
					{
						collisionPoint = p;
						collisionNum++;
					}
				} catch (MultipleCollisionsException ex) {
					collisionNum += 2;
				}
			}
			
			if(collisionNum == 1)
			{
				segment.end = collisionPoint;
				System.out.println(collisionPoint);
				break;
			}
			else if(collisionNum == 0 && end == 1)
			{
				break;
			}
			else if(collisionNum == 0)
			{
				start = end;
				end = 1;				
			}
			else
			{
				end = (end - start) / 2.0;
			}
			collisionNum = 0;
		}

		checkedCollision = true;
	}



}
