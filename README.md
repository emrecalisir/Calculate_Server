# Calculate_Server
Face Detection on Server - Communicates with any client using Rest APIs


### Workspace environment requirements ###
1. Install apache tomcat and add related tomcat folder to the eclipse server
2. Install maven (optional)
 
### Project specific requirements ###
1. For the opencv configurations in eclipse, follow the tutorial below: 
http://docs.opencv.org/trunk/doc/tutorials/introduction/java_eclipse/java_eclipse.html
Moreover, to prevent unsatisfied link errors, check opencv from build path>order and export window. Secondly, add java build path entries to deployment assembly

2. Copy the xml file in resources to required path: haarcascade_frontalface_alt.xml to C:/opencv-2.4.9/sources/data/haarcascades/haarcascade_frontalface_alt.xmll
