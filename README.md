Messaging with Apache KAfka, zoo keeper in spring boot

create a db as per application.properties data source url

Run the below scripts 

CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    fname VARCHAR(32) NOT NULL,
    lname VARCHAR(32) NOT NULL,
    mobile VARCHAR(32) NOT NULL,
    created_at DATE NOT NULL
);

CREATE TABLE access_token (
    token_id BIGSERIAL PRIMARY KEY,    
    token VARCHAR(256) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(user_id),
    created_at DATE NOT NULL
);

CREATE TABLE contacts (
    contact_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(user_id),
    contact_user_id BIGINT NOT NULL REFERENCES users(user_id)
);

CREATE TABLE messages (
    message_id BIGSERIAL PRIMARY KEY,
    from_user_id BIGINT NOT NULL REFERENCES users(user_id),
    to_user_id BIGINT NOT NULL REFERENCES users(user_id),
    message VARCHAR(512) NOT NULL,
    sent_at DATE NOT NULL default CURRENT_TIMESTAMP
);
Insert needed users

insert into users(user_id,fname,lname,mobile,created_at) values(1001,'Suresh','CS','9895012345',current_timestamp)
insert into users(user_id,fname,lname,mobile,created_at) values(1002,'Shahnawas','DD','9895098950',current_timestamp)

Then

Zokeeper start
if winodws machine- go to winodws folder(cd D:\java\kafka_2.12-2.8.1\bin\windows) and try the below one
start  zookeeper-server-start.bat ../../config/zookeeper.properties
Kafka server start
start kafka-server-start.bat ../../config/server.properties
create the required topic(s)
start kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic SEND_MESSAGE
List avilable topics-if needed
start kafka-topics.bat --list --bootstrap-server localhost:9092

After completing this start the spring boot app-KafkaWebsocketMessagingApp

To test

1.First hit the /getcode endpoint
http://localhost:8080/api/auth/getcode
{"mobile":"9895012345"}
Resp->{
    "mobile": "9895012345",
    "activationCode": "274368"
}
2.hit the /login end point with mobile and activation code
{
    "mobile": "9895012345",
    "activationCode": "274368"
}
You will get an access token here
{
    "accessToken": "4f61727f-406d-4a51-90e4-17e3e9dd7b52"
}

3.Try websocket end points now with access token
Install the latest postman 
new->WebSocket request
try connect user 1 
ws://localhost:8080/messaging?accessToken=4f61727f-406d-4a51-90e4-17e3e9dd7b52 press Connect

try connect user 2
ws://localhost:8080/messaging?accessToken=2100ed2f-99a5-4d91-8aea-299ac2e21953 press Connect

Then send message in below format- Pls make sure the accesstoken is for correct user
User 2
{"accessToken":"2100ed2f-99a5-4d91-8aea-299ac2e21953","msg":"hello","sendTo":"1001","topic":"SEND_MESSAGE"
}

user 1
{"accessToken":"4f61727f-406d-4a51-90e4-17e3e9dd7b52","msg":"hi","sendTo":"1002","topic":"SEND_MESSAGE"
}

