FROM ubuntu:16.04

# Maven and GIT
RUN apt-get update && apt-get install -y software-properties-common maven git

# Install Java
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

RUN git clone https://github.com/Eitraz/home-automation.git

COPY docker/build_and_run.sh build_and_run.sh
RUN chmod +x build_and_run.sh

COPY docker/supervisord.conf /etc/supervisor/conf.d/supervisord.conf
CMD ["/usr/bin/supervisord"]