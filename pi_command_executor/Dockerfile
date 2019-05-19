FROM python:3.6

ADD . /app
COPY . ./app

WORKDIR /app
RUN pip3 install -r ./requirements.txt
WORKDIR /app/src/

# Add wake on lan module to python path
ENV PYTHONPATH "${PYTHONPATH}:/app/src/Socket-simple-usage/wake_on_lan"

CMD ["python3", "./command_executor.py"]