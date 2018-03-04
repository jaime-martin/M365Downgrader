FROM node:carbon
# Create app directory
WORKDIR /usr/src/app

COPY package*.json ./
RUN npm install

COPY *.pem ./
COPY proxy2.js ./
COPY downgrader.jar ./
COPY fakedns.py ./
COPY downgrader.sh ./

RUN echo "deb http://http.debian.net/debian jessie-backports main" >> /etc/apt/sources.list

# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y -t jessie-backports openjdk-8-jdk && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME
RUN mkdir /dnsconf
VOLUME /dnsconf

EXPOSE 443:443
EXPOSE 53:53/udp
#CMD [ "npm", "start"]
#ENTRYPOINT [ "fakedns.py", "-c", "/dnsconf/dns.conf"]
CMD ["./downgrader.sh"]
