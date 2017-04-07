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

public class TSPcompare
{
    ArrayList<City> cityList = new ArrayList<City>();
    ArrayList<City> cityPath = new ArrayList<City>();
    ArrayList<EdgeWeight> edgeWeights = new ArrayList<EdgeWeight>();
    ArrayList<EdgeWeight> mst = new ArrayList<EdgeWeight>();    
    
    public TSPcompare(int cityNum)
    {                
        for(int i = 0; i < cityNum; i++)
            cityList.add(new City(i));
        
        //for(City tp : cityList)
            //System.out.printf("%d x: %2.1f y: %2.1f\n", tp.ID, tp.x, tp.y);
        
        //Create GUI frame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                CityFrame frame = new CityFrame();
                frame.createMyGUI();
            }});
        
        //greedyPath();
        twiceAroundTree();
    }    
    public void greedyPath()
    {                
        City home = cityList.get(0);//Gets City 0 from cityList
        City currentCity = home;
        City nextCity = null;
        City bestCity = null;
        
        double totalDistance = 0;//Total Euclidean distance traveled
        double shortestPath;
        double pathCheck;
        
        int visitedCities = 0;        
        
        boolean quit = false;
        cityList.get(0).visited = true;
        cityPath.add(home);//Starts City path list with home
        
        System.out.printf("\nGreedy Tour:\n%d -> ", home.ID);
        
        while(quit != true)
        {
            shortestPath = Double.MAX_VALUE;
            
            for(int i = 0; i < cityList.size(); i++)
            {
                if(cityList.get(i).visited != true)
                {
                    //System.out.printf("1) City %d is not visited\n", i);
                    nextCity = cityList.get(i);                    
                    pathCheck = currentCity.distance(nextCity);
                    //System.out.printf("2) E-Distance is %s from %d & %d\n", pathCheck, currentCity.ID, nextCity.ID);
                    
                    if(pathCheck < shortestPath)
                    {
                        //System.out.printf("3) City %d has shortest path\n", i);
                        shortestPath = pathCheck;
                        bestCity = nextCity;                        
                    }
                }
                else
                {
                    visitedCities++;
                    if(visitedCities == cityList.size())
                    {
                        //All cities visited
                        //System.out.printf("All cities visited\n");
                        shortestPath = currentCity.distance(home);
                        quit = true;                        
                    }
                }
            }                                    
            cityPath.add(bestCity);
            
            cityList.get(bestCity.ID).visited = true;
            currentCity = bestCity;
            //System.out.printf("Current City: %d\n\n", currentCity.ID);
            if(quit != true)
                System.out.printf("%d -> ", currentCity.ID);    
            
            visitedCities = 0;
            totalDistance += shortestPath;
            //System.out.printf("Current Distance: %3.2f\n", totalDistance);
        }
        //Finalize TSP path; add final distance
        System.out.printf("%d\n", home.ID);
        cityPath.add(home);
        
        //Round Decimal to one place
        DecimalFormat newFormat = new DecimalFormat("#.#");
        double roundedTotal =  Double.valueOf(newFormat.format(totalDistance));
        
        System.out.printf("Total Cost: %3.2f\n", roundedTotal);
        System.out.printf("Time to find: %s sec\n", '?');
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
            this.x = randomNum();
            this.y = randomNum();
            this.visited = false;
            this.ID = num;
        }
        public double distance(City that)
        {
            double dx = Math.pow(this.x - that.x, 2);
            double dy = Math.pow(this.y - that.y, 2);
            return Math.sqrt((dx + dy));
        }
        //Creates random double between Min and Max
        private double randomNum()
        {
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
        double weight;
        
        public EdgeWeight(City to, City from)
        {
            this.to = to;
            this.from = from;
            
            weight = to.distance(from);
        }
        
        public double getWeight(){return weight;}
        
        public City getTo()     {return to;}
        public City getFrom()   {return from;}    
        
        public int compare(EdgeWeight that)
        {
            return Double.compare(this.weight, that.weight);
        }

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
        public class DrawingPanel extends JComponent
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
                    
                    x1 = (int)(cityPath.get(i-1).x * scaleX);
                    x2 = (int)(cityPath.get(i).x * scaleX);
                    y1 = (int)(cityPath.get(i-1).y * scaleY);
                    y2 = (int)(cityPath.get(i).y * scaleY);
                    
                    if(city.ID == 0)
                    {g.setColor(HOME_POINT);}
                    else
                    {g.setColor(POINT_COLOR);}
                                        
                    g.fillOval(x1, y1, dotSize, dotSize);
                    
                    g.setColor(LINE_COLOR);
                    g.drawString(cityID, x1, y1);                                                                                              
                    g.drawLine(x1, y1, x2, y2);                    
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
