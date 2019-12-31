FROM ubuntu:19.04

RUN apt-get update

RUN apt-get install -y python

RUN apt-get install -y python-django

RUN apt-get install -y golang-go

WORKDIR /root