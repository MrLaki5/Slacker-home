## Slacker home

### How to run:
* Create slacker.env file in root folder, with structure:
    **WAKE_MAC="MAC_ADDRESS_THAT_WILL_BE_WOKEN"**
* Note: First do set up on raspberry pi or on google console cloude, guide down.
* Connect to docker deamon with docker-machine with **docker-machine env machine_name**
* Copy last line of command's output that looks like **eval $(docker-machine env machine_name)**
* Now go to root folder of project with **cd**
* To start up containers on server docker deamon run **docker-compose -f docker-compose.yml up -d**
* If rebuild is needed use **docker-compose -f docker-compose.yml up -d --build**