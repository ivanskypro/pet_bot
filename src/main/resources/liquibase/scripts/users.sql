-- liquibase formatted sql

-- changeSet ikalmykov:1
    CREATE TABLE users (
    id SERIAL,
    chatId SERIAL,
    name TEXT,
    phoneNumber SERIAL,
    location TEXT
    )
-- changeSet ikalmykov:2
    CREATE TABLE pets(
    id SERIAL,
    petName TEXT,
    age SERIAL
)
-- changeSet ikalmykov:3
    CREATE TABLE volunteers(
    id SERIAL,
    volunteerName TEXT,
    phoneNumber SERIAL
)

-- changeSet ikalmykov:4
    CREATE TABLE statusReports(
    id SERIAL,
    chatId SERIAL,
    reportText TEXT,
    phoneNumber SERIAL
)