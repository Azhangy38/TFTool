# TFTool
A REST-based Java API, TFTool compiles match history data and outputs stats on units, traits, summoners on the NA server. Updated to support Set 5 units and match history.
# Planned Updates
* Will support data from Sets 1-3.5 and 4.5
* Analysis of most popular units and compositions in every tier
* SQL Database
# How to use TFTool
1) Obtain a temp Riot developer API-key and paste your key here: (\TFTool\src\main\java\com\example\demo\Service)
![image](https://user-images.githubusercontent.com/33067558/121982076-d6d68380-cd5c-11eb-9e8b-c3c52e642dfd.png)
2) Run the DemoApplication file by right-clicking it and hitting 'Run DemoApplication'
![image](https://user-images.githubusercontent.com/33067558/121983177-e35bdb80-cd5e-11eb-8946-624ea80c9d9b.png)
3) The local server is now running. To get HTTP GET and POST requests, create a new HTTP request in Postman and select which api call you want from the TFTController file
Ex)
![image](https://user-images.githubusercontent.com/33067558/121982775-2f5a5080-cd5e-11eb-91b5-b5808a3c2c86.png)
In this example, set key = summonerID and value => name of whatever player you are looking up (the URL will automatically change to match your inputs)
![image](https://user-images.githubusercontent.com/33067558/121982940-7e07ea80-cd5e-11eb-8c70-7a9c5fe03b04.png)
Ta-da! Now we have Soju's unit usage analysis

Disclaimer: Soju was in no way affiliated with this project
