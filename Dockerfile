FROM java:8

# Maven and GIT
RUN apt-get update && apt-get install -y maven git

COPY docker/build_and_run.sh build_and_run.sh
RUN chmod +x build_and_run.sh

CMD ["build_and_run.sh"]