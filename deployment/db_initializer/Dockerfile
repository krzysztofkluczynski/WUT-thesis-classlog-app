FROM python:3.11-slim

RUN apt-get update && apt-get install -y --fix-missing postgresql-client bash dos2unix && rm -rf /var/lib/apt/lists/*

WORKDIR /scripts

COPY . /scripts

# Convert all .sh scripts to Unix line endings
RUN find /scripts -type f -name "*.sh" -exec dos2unix {} +

RUN pip install bcrypt

ENTRYPOINT ["bash", "/scripts/initialize.sh"]
