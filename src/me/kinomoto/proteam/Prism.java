package me.kinomoto.proteam;

public class Prism extends AbstractOpticalElement {

	private double refractiveIndex = 1;

	public Prism(Point position, double ior) {
		super(position, AbstractOpticalElement.getSquare());
		refractiveIndex = ior;
	}

	@Override
	void findCollisionSolution(Surroundings s, Beam b, Segment seg) {
		/*
		 * wsp odbicia = ((n cos b - cos a)/(n cos b + cos a))^2
		 * a - kąt padania
		 * b - kąt załamania
		 * n - wzgledny wsp załamania ośrodka wzgleðem którego odbija się swiatło
		 */
			//System.out.println("Collision with " + name);
			
		    // wektor padający
			double sy = seg.end.x - seg.begin.x;
			double sx = seg.end.y - seg.begin.y;
			sy *= -1;

			//wektor prisma
			//obliczenie wektora normalnego do pow prisma 
			double nx = b.segment.end.x - b.segment.begin.x;
			double ny = b.segment.end.y - b.segment.begin.y;
			ny *= -1;
			
			//stusunek wspolczynników 
			//padający do odbitego
			double n12 = s.ior / refractiveIndex;
			//odbity do padającego
			double n21 = refractiveIndex / s.ior;

			//chcę 
			double rx;
			double ry;
			

			Point end = new Point(b.segment.end.x + nx, b.segment.end.y + ny);

			if (b.brightness > 0.01) {
				double bright = b.brightness * .99;
				Segment tmp = new Segment(b.segment.end, end);
				if(tmp.end.x - tmp.begin.x == 0 && tmp.end.y - tmp.begin.y == 0) {
					System.out.println("err");
				}
				s.add(new Beam(tmp, b.wavelenght, bright, b.refractiveIndex));
			}
			
			/*
			 *     vector* Refract_Unnormalized(vector N, vector I) 
    {
        vector* T = new vector();
        float a,two_ndoti,ndoti;
         // ------REFRACTION----------------------- //
         // New vector T = refraction of I through surface with normal N.
         // assumes r = ki/kr and invr = kr/ki are known constants.
         
         ndoti = N.x * I.x + N.y * I.y + N.z * I.z;    // 3 muls, 2 adds
         
         /// ---begin changes---------------=========
         g = gLUT[(int)(ndoti*scalefac)];  // 1 mul
         if (g != TIRflag) 
         {
            T->x = g*N.x - I.x;            // 3 muls, 3 adds
            T->y = g*N.y - I.y;            // --total------------
            T->z = g*N.z - I.z;            // total: 7 muls, 5 adds, 1 lookup
         /// ---end changes -----------------========            
         } else {
            // total internal reflection
            // this usually doesn't happen, so I don't count it.
            two_ndoti = ndoti + ndoti;     // +1 add
            T->x = two_ndoti * N.x - I.x;  // +3 adds, +3 muls
            T->y = two_ndoti * N.y - I.y;
            T->z = two_ndoti * N.z - I.z;
         }
         return T;
    }
			 */

	}

	public double getRefractiveIndex() {
		return refractiveIndex;
	}

	//TODO check if it is good
	@Override
	public boolean isPointInside(Point p) {
		boolean out = false;
		for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++)
			if (((get(i).y > p.y) != (get(j).y > p.y)) && (p.x < (get(j).x - get(i).x) * (p.y - get(i).y) / (get(j).y - get(i).y) + get(i).x))
				out = !out;
		return out;
	}

}
