PickARestaurantApp Project consist of 2 parts   
1. BackEndAPI - back end Rest Api using Spring Boot 3.2.3, Java 17, in-memory H2 database   
2. FrontEnd - Angular 17   
 

---To clone repository to your local, use below command   
git clone https://github.com/wendyyktee/PickARestaurantApp.git


To start BackEndAPI on local, run BackEndApiApplication  
To start FrontEnd on local, execute command 'ng serve' on your terminal  


---BackEndAPI  
GET /session/initiateSession
GET /session/{sessionCode}
GET /session/endSession/{sessionCode}

GET /restaurant params = sessionId
PUT /restaurant body = Restaurant object

GET /result/{sessionCode}


---FrontEnd  
home - Home Page where user can start new session

session - Session Page where user can see details of the session,   
        - only initiator can see the End Session button,  
        - if user try to open a session that already ended less than 1 hour ago, system will redirect user to Result page,  
        - if user try to open a session that ended more than 1 hour ago, system will show pop up message indicate it's invalid session and will 
          redirect to home page

restaurant - List of restarant park under session page

result - Result Page to display picked restaurant

common-error-popup - customized pop up can be reused by others component and set the title and content accordingly, 
                    user stay on the page when pop up is closed

invalid-session-popup - customized pop up can be reused by others component and set the title and content accordingly,
                    user redirected to home page when pop up is closed




