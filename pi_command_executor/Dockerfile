FROM python:3.6

ADD . /app
COPY . ./app

WORKDIR /app
RUN pip3 install -r ./requirements.txt
WORKDIR /app/src/

CMD ["python3", "./command_executor.py"]