FROM python:3.11-slim

RUN apt-get update && apt-get install -y --fix-missing postgresql-client && rm -rf /var/lib/apt/lists/*

WORKDIR /scripts

COPY . /scripts

RUN pip install bcrypt

ENTRYPOINT ["sh", "/scripts/initialize.sh"]
