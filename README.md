# bank-system
- Designed a database which represents a bank model with users, accounts and transactions, using MySQL Workbench.
- Implemented three subsystems and a server for executing REST requests from clients in Java that comunicate via Java Messaging Service.
- Created a console application in Java for sending http request to the server using the Retrofit API

## Server
- Receives http requests from client application and forwards them to subsystems for executing
- Sends query results or requests responses back to client application

## Subsystem1
- Used for creating new users, places and branch offices and for updating users
- It can return all users, places and bracnch offices in the database

## Subsystem2
- Used for opening and shouting down user accounts and for recording transactions for one or two accounts
- It can return all accounts for a given user ID and all transactions for a given account ID

## Subsystem3
- Used for saving all data from first two subsystem in a backup database on every 2 minutes
- It can return all data from the backup database and the difference in data between regular and backup database

## Client
- A console application for interacting with users and sending http requests to the server
- Has options for creating users, places, branch offices, accouts and transactions, updating users and accounts and getting all data from regular and backup database
