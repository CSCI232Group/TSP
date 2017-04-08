import java.util.ArrayList;


public class Kruskal extends TSPcompare{
private ArrayList<City> cities=new ArrayList<City>();
private ArrayList<EdgeWeight> edges=new ArrayList<EdgeWeight>();
	public Kruskal() {
		super();
	}
	public void addEdge(EdgeWeight w){
		if(getCities().contains(w.to)&&!getCities().contains(w.from)){
    		this.edges.add(w);
    		this.cities.add(w.from);
    	}
    	else if(!getCities().contains(w.to)&&getCities().contains(w.from)){
    		this.edges.add(w);
    		this.cities.add(w.to);
    	}
    	else{
    		this.edges.add(w);
    		this.cities.add(w.to);
    		this.cities.add(w.from);
    	}
		

	}
	public int contains(EdgeWeight w){
		if(getCities().contains(w.to)&&!getCities().contains(w.from)){
			
    		return 1;
    	}
    	else if(!getCities().contains(w.to)&&getCities().contains(w.from)){
    		return -1;
    	}
		else if(getCities().contains(w.to)&&getCities().contains(w.from))
			return 0;
		else
			return 2;
	}
	public void join(Kruskal k){
		this.cities.addAll(k.getCities());
		this.edges.addAll(k.edges);
	}
	public ArrayList<City> getCities() {
		return cities;
	}
	public void setCities(ArrayList<City> cities) {
		this.cities = cities;
	}
	public ArrayList<EdgeWeight> list(){
		ArrayList<EdgeWeight> c=new ArrayList<EdgeWeight>();
		for (int i=0;i<this.edges.size();i++){
			if(!c.contains(this.edges.get(i).from)){
				c.add(this.edges.get(i));
			}
			if(!c.contains(this.edges.get(i).to)){
				c.add(this.edges.get(i));
			}
		}
		return c;
	}
}
