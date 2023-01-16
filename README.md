# TFTool
A REST-based Java API, TFTool compiles match history data and outputs stats on units, traits, and summoners on the NA server. Updated to support Set 8 units and match history.
# Planned Updates
* ~~Will support data from Sets 1-3.5 and 4.5~~ SHELVED
* Analysis of most popular units and compositions in every tier
* ~~SQL Database~~ COMPLETED, Support for MySQL database has been added
# How to use TFTool (using IntelliJ IDE)
1) Obtain a temp Riot developer API-key and paste your key in the Constants file: (\TFTool\src\main\java\com\example\demo\Service\Constants)
![constantstft](https://user-images.githubusercontent.com/33067558/212435324-e934584c-2199-458f-b3ce-b7d4cd7d036c.png)
2) Run the StartApplication file by right-clicking it and hitting 'Run StartApplication'
![startApplicationTFT](https://user-images.githubusercontent.com/33067558/212435523-45c70c3c-2c34-4ef9-bf29-53a669e7cddb.png)
3) Now that the local server is running, import my HTTP requests into Postman ("TFTools.postman_collection")
![image](https://user-images.githubusercontent.com/33067558/212436547-e454045b-a6f1-49fa-a258-c440ca8ef20a.png)

4) Run any of the available http requests. To get your (or the summoner you want to look-up) match history analysis run the "Match History Analysis" GET request on Postman
![image](https://user-images.githubusercontent.com/33067558/212435989-57692c76-707c-4630-9de8-73c1c9f8c27b.png)

