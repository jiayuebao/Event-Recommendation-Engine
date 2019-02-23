# FUNscover: The Personalized Event-Recommendation-Engine
A Java web service which can search and recommend the nearby events for users.
### Backend
- Server: Tomcat
- Data Source: Ticketmaster API
- Java Servlet(rpc):
  - servlet for login (/login)
  - servlet for logout (/logout)
  - servlet for register (/register)
  - servlet for recommend events(/recommendation)
  - servlet for browse nearby events(/search)
  - servlet for check favorites (/history)
- Database: MySQL, JDBC
  - events
  - users
  - history
  - categories
### Frontend
- HTML
- CSS
- JavaScript
- AJAX
### Recommendation Method
- content-based   
  - recommend according to the favorites events' categories 

### Functionality
- login/register
<img src="https://github.com/jiayuebao/Event-Recommendation-Engine/blob/master/pictures/register.png" height=70%, width=70%> 
<img src="https://github.com/jiayuebao/Event-Recommendation-Engine/blob/master/pictures/login.png" height=70%, width=70%> 

- browse nearby events and set favorite events
<img src="https://github.com/jiayuebao/Event-Recommendation-Engine/blob/master/pictures/browse.png" height=70%, width=70%> 
<img src="https://github.com/jiayuebao/Event-Recommendation-Engine/blob/master/pictures/setfavorite.png" height=70%, width=70%> 

- see his/her favorites
<img src="https://github.com/jiayuebao/Event-Recommendation-Engine/blob/master/pictures/favorites.png" height=70%, width=70%> 

- recommend events (have the same category of users' favorite events)
<img src="https://github.com/jiayuebao/Event-Recommendation-Engine/blob/master/pictures/recommend.png" height=70%, width=70%> 
