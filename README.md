# raul-nunez-sample-java-test
Assessment for java back end skills

# Description
The project was build intending to address the java skills test provided by clip. Please refer to:

https://github.com/cesaralcancio/simple-test  


for more details.

# Pre requisites 
Please have installed in the host machine the following:

    1. Java 1.8
    2. Maven 3.6
    3. Git 2.21
    
Once installed you will see something similar to the following when run mvn --version command:


    $ mvn --version
    Apache Maven 3.6.1 (d66c9c0b3152b2e69ee9bac180bb8fcc8e6af555; 2019-04-04T13:00:29-06:00)
    Maven home: /Users/developer/itexico/extract/apache-maven-3.6.1
    Java version: 1.8.0_211, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk1.8.0_211.jdk/Contents/Home/jre
    Default locale: en_MX, platform encoding: UTF-8
    OS name: "mac os x", version: "10.14.6", arch: "x86_64", family: "mac"
And for git:

    $ git --version
    git version 2.21.0
    
# Download
Please clone this repo by executing the following command:

    $ git clone https://github.com/rnunezdev/raul-nunez-sample-java-test.git
# Execute the application
For running the application just go inside the produced folder by running:

    $ cd raul-nunez-sample-java-test/
then run the following commands:
    
    1. $ mvn clean package spring-boot:repackage // this will download dependencies and executing the tests apart from building and package the application in a jar file
    2. $ java -jar target/backend-assesment-0.0.1-SNAPSHOT.jar // this will execute the generated jar file

Once the app is up and running you can open another Terminal and execute the following curl commands in order to execute the endpoints:

    1. $ curl -X GET http://localhost:9090/transaction/2/all -H 'cache-control: no-cache'
    
You'll see a large list of json object as a result (data is pre loaded in the DB for testing purposes)

    2. $ curl -X POST \
         http://localhost:9090/transaction/2 \
         -H 'cache-control: no-cache' \
         -H 'content-type: application/json' \
         -d '{
       	"amount": 56748.367,
       	"description": "My description",
       	"date": "2020-03-10"
       }'

You'll see a single json object as a result showing the data that has been just inserted, similar to:

    {"transactionId":"acc7e7cf-c640-44a5-a079-5dadbd54d4bb","userId":2,"description":"My description","amount":56748.37,"date":"2020-03-10"}
    
You can pick the transactionId from that object and then execute (inside brackets you need to put the generated Id for the transaction previously inserted):

    3. $ curl -X GET http://localhost:9090/transaction/2/[acc7e7cf-c640-44a5-a079-5dadbd54d4bb] -H 'cache-control: no-cache' 

You'll see exactly the same object than in the command executed before, we are getting the transaction object individually with the above.

    4. $ curl -X GET http://localhost:9090/transaction/2/sum -H 'cache-control: no-cache' 
 
The above command will produce an object similar to this:

    {"userId":2,"sum":126627.14}
    
it's making the addition of each amount for all the transactions associated to the userId provided in the url

And finally you can execute the report:

    5. $ curl -X GET http://localhost:9090/transaction/2/weekly -H 'cache-control: no-cache' 

Which will produce also a large list of objects according to the requirements listed in the test repo mentioned above

It's important to mention that negative scenarios described in test repo like error responses or empty lists are also covered by this project, you can test accordingly.

You can always import the curl commands described above into Postman for prettier and more legible results.

Enjoy!

P.D. A quick note about DB. I've used H2 in memory DB for persistence purposes. Data is inserted at starting the application and destroyed after application shuts down. In case you might want to take a look at pre loaded data please do the following:

    Edit src/main/resources/application.properties file and uncomment the following line:
    
    #spring.h2.console.enabled=true

This will allow you accessing the H2 console while th application is running by accessing:
    
    http://localhost:9090/h2-console/login.do

It will show a screen for entering connection parameters, please enter exactly the same then the ones declared in application.properties file, click connect and you'll have access to the SQL console for H2 DB. If you run:

    select * from TRANSACTIONS
    
You'll see all the pre inserted data for testing purposes.

Update: As a plus you can execute the last endpoint that will give you a random transaction, so please execute it more than once so you can see the transaction is different every time:

    curl -X GET http://localhost:9090/transaction/random -H 'cache-control: no-cache'