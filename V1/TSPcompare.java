package tsp;

/*
 * @author Ian Hecker
 * CSCI232, Lab3
 * Date: 4/7/17
 */

//Libraries for TSP
import java.util.ArrayList;
import java.util.Random;
import java.text.DecimalFormat;
//Libraries for GUI of cities
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TSPcompare
{
    ArrayList<City> cityList = new ArrayList<City>();
    
    public TSPcompare(int cityNum)
    {                
        for(int i = 0; i < cityNum; i++)
            cityList.add(new City(i));
        
        for(City tp : cityList)
            System.out.printf("%d x: %2.1f y: %2.1f\n", tp.ID, tp.x, tp.y);
        
        //Create GUI frame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                CityFrame frame = new CityFrame();
                frame.createMyGUI();
            }});
        
        /*int[] cityPath = */greedyPath(cityNum);
        /*
        System.out.println();
        for(int i=0; i < cityPath.length; i++)
        {
            System.out.print(cityPath[i]+" ");
        }*/
    }
    public void /*int[]*/ greedyPath(int cityNum)
    {        
        //int[] cityPath = new int[cityNum + 1];//hold indexes of next city from cityList
        //cityPath[0] = 0;
         
        City home = cityList.get(0);//Gets City 0 from cityList
        City currentCity = home;
        City nextCity = null;
        
        double totalDistance = 0;//Total Euclidean distance traveled
        double shortestPath;
        double pathCheck;
        
        int visitedCities = 0;
        //int cityPosition = 0;
        //int cityPathPos = 1;
        
        boolean quit = false;
        cityList.get(0).visited = true;
        
        while(quit != true)
        {
            shortestPath = Double.MAX_VALUE;
            
            for(int i = 0; i < cityList.size(); i++)
            {
                if(cityList.get(i).visited != true)
                {
                    System.out.printf("City %d is not visited\n", i);
                    nextCity = cityList.get(i);
                    pathCheck = currentCity.distance(nextCity);
                    System.out.printf("E-Distance is %s\n", pathCheck);
                    
                    if(pathCheck < shortestPath)
                    {
                        System.out.printf("City %d has shortest path\n", i);
                        shortestPath = pathCheck;
                        //cityPosition = i;
                    }
                }
                else
                {
                    visitedCities++;
                    if(visitedCities == cityList.size())
                    {
                        //All cities visited
                        System.out.printf("All cities visited\n");
                        shortestPath = currentCity.distance(home);
                        quit = true;
                    }
                }
            }
            //cityPath[cityPathPos] = cityPosition;
            //cityPathPos++;
            
            visitedCities = 0;
            totalDistance += shortestPath;
            
            currentCity = nextCity;
        }                        
        //return cityPath;              
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
            return Math.sqrt(dx + dy);
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
    public class CityFrame extends JFrame
    {
        private final int FRAME_WIDTH = 500;
        private final int FRAME_HEIGHT = 500;
        private final Color POINT_COLOR = Color.BLUE;
        private final Color GLINE_COLOR = Color.RED;
        
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
            ArrayList<City> graphPoints = new ArrayList<City>(); 
            double scaleX = (FRAME_WIDTH * 0.95) / 100;
            double scaleY = (FRAME_HEIGHT * 0.95) / 100;
            int dotSize = (FRAME_WIDTH + FRAME_HEIGHT) / 100;
            
            
            public DrawingPanel()
            {
                /*
                for(City c : cityList)
                {
                //Create new variable to replicate c; add to graph array
                    City graphPoint = c;
                    graphPoint.y = (int)(c.getX() * scaleX);
                    graphPoint.y = (int)(c.getY() * scaleY);
                    graphPoints.add(graphPoint);
                }*/
            }
            
            public void paint(Graphics g)
            {               
                g.setColor(POINT_COLOR);                                
                
                for(City c : cityList)
                {                
                    g.fillOval((int)(c.getX() * scaleX), (int)(c.getY() * scaleY), dotSize, dotSize);                                                            
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
