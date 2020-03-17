# TS3 Rainbow6 Bot
This project implements a bot for the program "TeamSpeak3". The main functionality of the bot 
is to assign ranks from the game "Rainbow6" to the users.

## Features
1. The main functionality is, as described above, to assign ranks from the game "Rainbow6" to the users
of a TeamSpeak server. 
    
    How does this work?
    * The rank will be assigned when a user connects to the TeamSpeak server.
    * The bot will look up the username on R6Tab
    * The bot will assign the corresponding rank for the user.
    
    Why is R6Tab used?
    * The bot can query the ranks without a username or password. Isn't this great?
    
    Why is there a time delay between rank updates?
    * R6Tab updates the rank of a player only once per day if someone visits the profile.
    * You can trigger an update once per hour if you click the refresh button on your profile.
    * The bot minimizes this time by querying every half and hour all profiles of the registered users.
    
2. The bot offers an option to assign a "Noob" rank for a user. The noob rank is temporary and last
with the default settings one week. You can change the value in the application.properties file.

3. The bot offers a small ChatBot. The ChatBot has currently two important commands:
    * !register \<Prename> \<Lastname> \<UplayName> : A user has to enter this command to register himself.
    When the user connects the next time to the server, the rainbow rank will be assigned.
        * Example: !register Max Mustermann headshotdude
    *  !noob : This displays all Noobs and the the remaining time.
    

## Setup and Deployment
1. Checkout this project. I recommend using IntelliJ IDEA. 
2. Prepare your TeamSpeak server:
    * Create ranks(server groups) for all Rainbow6 ranks. The ranks MUST be temporary. So if a user disconnects from he server
    the rank is removed automatically. 
    * Attention: Uplay changed the ranks with the last season but R6Tab has not. So you need the following (old) ranks:
        * No Rank
        * Copper 4, Copper 3, Copper 2, Copper 1
        * Bronze 4, Bronze 3, Bronze 2, Bronze 1
        * Silver 4, Silver 3, Silver 2, Silver 1
        * Gold 4, Gold 3, Gold 2, Gold 1
        * Platinum 3, Platinum 2, Platinum 1
        * Diamond
    * Next you have to configure the file application.properties.
    * Set the Server Query Admin username and password as well as the hostname, port and the virtual server id.
        ```
        ts3.adminUserName=YOUR_SERVER_ADMIN_USERNAME
        ts3.adminPassword=YOUR_SERVER_ADMIN_PASSWORD
        ts3.hostname=YOUR_SERVER_ADRESS
        ts3.queryPort=10011
        ts3.virtualServerID=1
        ```
   * Next you have to create a database with the name ts3_bot. I have used PostgrSQL. With small configuration tweaks you can also use MariaDB, other configurations were not tested.
   * Next you have to set the database values in the application.properties.
        ```
        #Database parameters
        spring.datasource.url=jdbc:postgresql://HOSTNAME:5432/DATABASE_NAME
        spring.datasource.username=DATABASE_USERNAME
        spring.datasource.password=DATAbASE_PASSWORD
        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.show-sql=false
        # disable warning on startup on postgreSQL
        spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false
        ```   
   * Next you have to set the platform(e.g. uplay, playstation, xbox) and the location in the application.properties.
        ```
        # Api config
        r6.region=EU
        r6.platform=uplay          
        ```
   * Last but not least you have to set for every rank the corresponding TeamSpeak id of the server group in 
   the application.properties. You can find the id, when you inspect your server groups on the right side of the name.
        ```
        ranks.norank=35 
        ranks.copper4=36
        ranks.copper3=37
        ranks.copper2=38
        ...
        ```
   * Now you are ready to go! Yeahh!!
3. Now you need to start the program. You can start it directly from IntelliJ and test if everything works
as expected. 
   * Remember: To get a rank you have to insert the register command in the (global) server chat. 
4. Now you can build to project to a .war file and deploy it on a tomcat server or something similar.
Then the program can run always and every user can get a rank.
   
 
     
