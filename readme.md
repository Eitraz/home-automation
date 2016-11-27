## Build
```docker build -t home-automation git://github.com/Eitraz/home-automation```

## Run
```docker run --name home-automation --net=host -e ip=`ip route | awk '/ens160/ { print $9 }' | xargs --no-run-if-empty` -d home-automation```