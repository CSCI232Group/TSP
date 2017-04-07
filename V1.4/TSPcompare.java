
/*
 * @author Ian Hecker
 * CSCI232, Lab3
 * Date: 4/7/17
 */

//Libraries for TSP
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
//Libraries for GUI of cities
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TSPcompare                                                 //Traveling Sales Problem Comparing class
{
    ArrayList<City> cityList = new ArrayList<City>();                   //Setting up array list for each city
    ArrayList<City> cityPath = new ArrayList<City>();                   //Setting up array list for each path
    ArrayList<EdgeWeight> edgeWeights = new ArrayList<EdgeWeight>();    //Edge weights are given by the normal Euclidean distance between pairs of points.
    ArrayList<EdgeWeight> mst = new ArrayList<EdgeWeight>();    
    public  class Stopwatch 											//calculating runtime
    {
    	private final long start;
    	public Stopwatch()
    	{ start = System.currentTimeMillis(); }							//start time when called
    	public double elapsedTime()
    	{
    		long now = System.currentTimeMillis();						//how much time since start
    		return (now - start)/1000.0;
    	}
    }

    public TSPcompare(int cityNum)                                      // creating the next city for the path
    {                
        for(int i = 0; i < cityNum; i++)
            cityList.add(new City(i));									//add random city coordinates to citylist
        
        //for(City tp : cityList)
            //System.out.printf("%d x: %2.1f y: %2.1f\n", tp.ID, tp.x, tp.y);
        
        //Create GUI frame
        SwingUtilities.invokeLater(new Runnable() {                     // Creating the window for the program to run in
            @Override
            public void run(){											//create new frame to show path
                CityFrame frame = new CityFrame();
                frame.createMyGUI();
            }});
        
        greedyPath();
        
        
        twiceAroundTree();
    }    
    public void greedyPath()                                            //Greedy tour set up
    {                
    	Stopwatch stop=new Stopwatch();
        City home = cityList.get(0);									//Gets City 0 from cityList
        City currentCity = home;										//Starting location
        City nextCity = null;
        City bestCity = null;
        
        double totalDistance = 0;										//Total Euclidean distance traveled
        double shortestPath;
        double pathCheck;
        
        int visitedCities = 0;                              
        
        boolean quit = false;                                         
        cityList.get(0).visited = true;
        cityPath.add(home);												//Starts City path list with home
        
        System.out.printf("\nGreedy Tour:\n%d -> ", home.ID);          // Indicating that greedy tour has started
        
        while(quit != true)
        {
            shortestPath = Double.MAX_VALUE;                            // After set up we are now checking to see the next place to go to
            
            for(int i = 0; i < cityList.size(); i++)
            {
                if(cityList.get(i).visited != true)						//check city has not been used
                {
                    //System.out.printf("1) City %d is not visited\n", i);
                    nextCity = cityList.get(i);                    		//move to next city
                    pathCheck = currentCity.distance(nextCity);			//keep track of path length
                    //System.out.printf("2) E-Distance is %s from %d & %d\n", pathCheck, currentCity.ID, nextCity.ID);
                    
                    if(pathCheck < shortestPath)
                    {
                        //System.out.printf("3) City %d has shortest path\n", i);
                        shortestPath = pathCheck;						//make current path shortest if it is shorter than current shortest
                        bestCity = nextCity;                        
                    }
                }
                else
                {
                    visitedCities++;									//keep track of how many cities are visited
                    if(visitedCities == cityList.size())
                    {
                        //All cities visited
                        //System.out.printf("All cities visited\n");
                        shortestPath = currentCity.distance(home);		//path from current city back to first city
                        quit = true;                        
                    }
                }
            }                                    
            cityPath.add(bestCity);										//add city from shortest path
            
            cityList.get(bestCity.ID).visited = true;					//city is visited
            currentCity = bestCity;
            //System.out.printf("Current City: %d\n\n", currentCity.ID);
            if(quit != true)
                System.out.printf("%d -> ", currentCity.ID);    
            
            visitedCities = 0;
            totalDistance += shortestPath; 								//add path to total distance
            //System.out.printf("Current Distance: %3.2f\n", totalDistance);
        }
        																//Finalize TSP path; add final distance
        System.out.printf("%d\n", home.ID);
        cityPath.add(home);												//complete cycle
        
        																//Round Decimal to one place
        DecimalFormat newFormat = new DecimalFormat("#.#");
        double roundedTotal =  Double.valueOf(newFormat.format(totalDistance));
        																//print out cost and time
        System.out.printf("Total Cost: %3.2f\n", roundedTotal);                     
        System.out.printf("Time to find: %s sec\n", stop.elapsedTime());
    }
    public void twiceAroundTree()
    {
        int limit = cityList.size();
        int iter = 0;
        City to, from;
        double weight;
        
        for(int i = 0; i < limit; i++)
        {
            for(int j = i + 1; j < limit; j++)
            {                
                to = cityList.get(i);
                from = cityList.get(j);               
                
                edgeWeights.add(new EdgeWeight(to, from));                
                
                //Debugging Section
                weight = edgeWeights.get(iter).getWeight();
                iter++;
                System.out.printf("%d, %d: %f\n", i, j, weight);
                //End Debugging
            }
        }
        System.out.println();//DEBUGGING
        
        Collections.sort(edgeWeights);                
        
        //DEBUGGING
        for(int k = 0; k < edgeWeights.size(); k++)            
        {
            to = edgeWeights.get(k).getTo();
            from = edgeWeights.get(k).getFrom();
            weight = edgeWeights.get(k).getWeight();
            System.out.printf("%d, %d: %f\n", to.ID, from.ID, weight);
        }//END DEBUGGING
        
        /* MST Building Section */
        while(!edgeWeights.isEmpty())
        {        
            EdgeWeight current = edgeWeights.get(0);
            
            
            //Debugging
            System.out.printf("%f\n", current.getWeight());
                        
            edgeWeights.remove(0);
        }
        
        
        
        
        
        
        
        
        
        
        
        
        /* MST Building Section */        
    }
    //Point class created for cities on grid
    class City
    {
        private double x, y;
        public boolean visited;        
        public int ID;
        
        public City(int num)
        {
            this.x = randomNum();										//provide random coordinates for city
            this.y = randomNum();
            this.visited = false;
            this.ID = num;
        }
        public double distance(City that)
        {																//return distance between this city and another
            double dx = Math.pow(this.x - that.x, 2);
            double dy = Math.pow(this.y - that.y, 2);
            return Math.sqrt((dx + dy));
        }
        //Creates random double between Min and Max
        private double randomNum()
        {																//return a random number within 
            double min, max, random, num;
            
            min = 0; max = 100;                       
            random = new Random().nextDouble();
            num = min + (random * (max - min));                       
            
            //Round Decimal to one place
            DecimalFormat newFormat = new DecimalFormat("#.#");
            double roundedDecimal =  Double.valueOf(newFormat.format(num));                        
            
            return roundedDecimal;
        }
        public double getX()    {return x;}
        public double getY()    {return y;}        
    }
    //Holds edge weight of two cities and the respective cities
    public class EdgeWeight implements Comparable<EdgeWeight>
    {
        City to, from;
        double weight;													//distance between cities
        
        public EdgeWeight(City to, City from)
        {																
            this.to = to;
            this.from = from;
            
            weight = to.distance(from);
        }
        
        public double getWeight(){return weight;}
        
        public City getTo()     {return to;}
        public City getFrom()   {return from;}    
        	
        																//return which weight is larger
        @Override
        public int compareTo(EdgeWeight that)
        {
            return Double.compare(this.weight, that.weight);
        }
    }
    //Class that constructs and draws graph of city paths
    public class CityFrame extends JFrame
    {
        private final int FRAME_WIDTH = 500;
        private final int FRAME_HEIGHT = 500;
        private final Color POINT_COLOR = Color.BLUE;
        private final Color HOME_POINT = Color.GREEN;
        private final Color LINE_COLOR = Color.BLACK;
        
        public void createMyGUI()
        {                                    
            JFrame.setDefaultLookAndFeelDecorated(true);
            JFrame frame = new JFrame("CS232 TSP");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                       
            
            																//Add points to JFrame
            frame.getContentPane().add(new DrawingPanel());
            
            setBackground(Color.WHITE);
            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);        
            frame.setVisible(true);
        }
        public class DrawingPanel extends JComponent                        //This is our windows specitications 
        {                           
            double scaleX = (FRAME_WIDTH * 0.95) / 100;
            double scaleY = (FRAME_HEIGHT * 0.95) / 100;
            int dotSize = (FRAME_WIDTH + FRAME_HEIGHT) / 100;
                        
            public DrawingPanel(){}
            
            public void paint(Graphics g)
            {                                                               
                for(int i=1; i < cityPath.size(); i++)
                {
                    int x1, x2, y1, y2;
                    String cityID;
                    City city = cityPath.get(i-1);
                    cityID = String.valueOf(city.ID);
                    
                    x1 = (int)(cityPath.get(i-1).x * scaleX);				//get coordinates of neighbors
                    x2 = (int)(cityPath.get(i).x * scaleX);
                    y1 = (int)(cityPath.get(i-1).y * scaleY);
                    y2 = (int)(cityPath.get(i).y * scaleY);
                    
                    if(city.ID == 0)
                    {g.setColor(HOME_POINT);}
                    else
                    {g.setColor(POINT_COLOR);}
                                        									//draw points
                    g.fillOval(x1, y1, dotSize, dotSize);
                    
                    g.setColor(LINE_COLOR);
                    g.drawString(cityID, x1, y1);      						//write city ID                                                                                        
                    g.drawLine(x1, y1, x2, y2);        						//draw path between cities            
                }
            }            
        }        
    }
    public static void main(String[] key)
    {
        //Print out user-entered int
        int num = Integer.parseInt(key[0]);        
        
        TSPcompare TSP = new TSPcompare(num);
    }        
}
