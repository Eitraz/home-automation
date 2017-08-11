## Build
```docker build -t home-automation git://github.com/Eitraz/home-automation```

## Run
``` docker run --name home-automation --net=host -e ip=`ip route | awk '/ens160/ { print $9 }' | xargs --no-run-if-empty` -v /etc/localtime:/etc/localtime:ro -v /home/docker/home_automation/application.properties:/application.properties -d home-automation```