package me.kinomoto.proteam;

import java.util.ArrayList;
import java.util.List;

public class Surroundings {
	List<AbstractOpticalElement> elements; 
	List<BeamSource> sources;
	List<Beam> beams; 
	
	public Surroundings(){
		elements = new ArrayList<AbstractOpticalElement>();
		sources = new ArrayList<BeamSource>();
		beams = new ArrayList<Beam>();
	}
	
	public void simulate()
	{
		beams.clear();
		for(BeamSource source : sources)
		{
			beams.add(source.getBeam());
			System.out.println("souce");
		}
		
		while(!simulated())
		{
			for (Beam beam : beams) {
				beam.simulate(this);
				System.out.println("beam");
			}
		}
		
		
	}
	
	public void add(AbstractOpticalElement e)
	{
		elements.add(e);
		simulate();
	}
	
	public void add(BeamSource s)
	{
		sources.add(s);
		simulate();
	}
	
	public void add(Beam b)
	{
		beams.add(b);
	}
	
	private boolean simulated()
	{
		for (Beam beam : beams) {
			if(!beam.getIfSimulated())
				return false;
		}
		return true;
	}
	
	
}
