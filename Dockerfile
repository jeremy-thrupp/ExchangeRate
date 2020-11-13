#We will use the alpine Linux image
FROM alpine
WORKDIR /root/exchange-rate
COPY ExchangeServer.java /root/exchange-rate

#Here we will install openjdk8
RUN apk add openjdk8
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:$JAVA_HOME/bin

#Compile the program
RUN javac ExchangeServer.java

ENTRYPOINT java ExchangeServer
