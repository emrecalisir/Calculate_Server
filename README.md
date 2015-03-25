 ## EMRE CALISIR ## emrecalisir@gmail.com ## GALATASARAY UNIVERSITY COMPUTER SCIENCE M.SC. #
 
Calculate_Server is a project which responds to the computation requests sent by Android client application ClientGsu. Currently, the communication is established with JAVA Rest APIs and JAVA RMI. 

1. JAVA Rest APIs based communication
a. Install apache tomcat 

b.To simplify complex Web Service architecture, Jersey framework is used as RESTful communication method. Necessary Jersey jars are: 
b.1. jersey-client-1.18.jar
b.2. jersey-core-1.18.jar
b.3. jersey-server-1.18.jar
b.4. jersey-servlet-1.18.jar  

c.OpenCV framework is used as the face detection framework.OpenCV-2.4.9 version is used in the project.
c.1. For the opencv configurations in eclipse, follow the tutorial below: 
http://docs.opencv.org/trunk/doc/tutorials/introduction/java_eclipse/java_eclipse.html
Moreover, to prevent unsatisfied link errors, check opencv from build path>order and export window. Secondly, add java build path entries to deployment assembly
c.2. Copy the xml file in resources to required path: haarcascade_frontalface_alt.xml to C:/opencv-2.4.9/sources/data/haarcascades/haarcascade_frontalface_alt.xmll

2. JAVA RMI based communication
This is a newly developed feature. The package named as com.geag.rmi includes the necessary Java classes. On the other hand, lipermi-0.4.jar should be placed to the lib folder in the project. Moreover, this jar should be added to the build path of the project. 

